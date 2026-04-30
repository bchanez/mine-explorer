package com.mineexplorer.unit.write.application.usecases;

import com.mineexplorer.write.adapters.secondary.persistence.InMemoryGameRepository;
import com.mineexplorer.write.application.domain.models.Direction;
import com.mineexplorer.write.application.domain.models.Game;
import com.mineexplorer.write.application.domain.models.GameConfiguration;
import com.mineexplorer.write.application.domain.models.GameState;
import com.mineexplorer.write.application.domain.models.Position;
import com.mineexplorer.write.application.domain.models.Wall;
import com.mineexplorer.write.application.usecases.NoGameInProgressException;
import com.mineexplorer.write.application.usecases.ThrowGrenadeCommand;
import com.mineexplorer.write.application.usecases.ThrowGrenadeCommandHandler;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThrowGrenadeCommandHandlerTest {

    private final InMemoryGameRepository gameRepository = new InMemoryGameRepository();
    private final ThrowGrenadeCommandHandler handler = new ThrowGrenadeCommandHandler(gameRepository);

    @Test
    void should_fail_when_no_game_in_progress() {
        assertThatThrownBy(() -> handler.handle(new ThrowGrenadeCommand(Direction.SOUTH)))
                .isInstanceOf(NoGameInProgressException.class);
    }

    @Nested
    class DestroyWall {

        @Test
        void should_destroy_wall_and_propel_player_when_throwing_grenade() {
            givenGameWithWall(new Position(0, 0), new Position(0, 1));

            handler.handle(new ThrowGrenadeCommand(Direction.SOUTH));

            assertThat(currentGame().playerPosition()).isEqualTo(new Position(0, 1));
            assertThat(currentGame().walls()).isEmpty();
        }

        @Test
        void should_consume_grenade_when_throwing() {
            givenGameWithWall(new Position(0, 0), new Position(0, 1));

            handler.handle(new ThrowGrenadeCommand(Direction.SOUTH));

            assertThat(currentGame().grenadeCount()).isEqualTo(2);
        }

        @Test
        void should_reveal_destination_when_blast_propels_player() {
            givenGameWithWall(new Position(0, 0), new Position(0, 1));

            handler.handle(new ThrowGrenadeCommand(Direction.SOUTH));

            assertThat(currentGame().visibleCells()).contains(new Position(0, 1));
        }

        @Test
        void should_allow_grenade_throws_in_multiple_directions_when_surrounded_by_walls() {
            // Given: player at (0,0) with 2 grenades, surrounded by walls east and south
            var playerPosition = new Position(0, 0);
            var wallEast = Wall.between(playerPosition, new Position(1, 0));
            var wallSouth = Wall.between(playerPosition, new Position(0, 1));
            var config = new GameConfiguration(playerPosition, 2, Set.of(wallEast, wallSouth), new Position(4, 4), Set.of());
            gameRepository.save(Game.create(config));

            // When: throw grenade east
            handler.handle(new ThrowGrenadeCommand(Direction.EAST));

            // Then: player propelled east, wall destroyed, grenade consumed
            assertThat(currentGame().playerPosition()).isEqualTo(new Position(1, 0));
            assertThat(currentGame().grenadeCount()).isEqualTo(1);
            assertThat(currentGame().walls()).doesNotContain(wallEast);
        }
    }

    @Nested
    class BorderWall {

        @Test
        void should_waste_grenade_when_throwing_at_border() {
            // Given: player at west border (0, 2) with 2 grenades
            givenGameAt(new Position(0, 2), 2);

            // When: throw grenade west (toward indestructible border)
            handler.handle(new ThrowGrenadeCommand(Direction.WEST));

            // Then: grenade consumed, no movement, game continues
            assertThat(currentGame().playerPosition()).isEqualTo(new Position(0, 2));
            assertThat(currentGame().grenadeCount()).isEqualTo(1);
            assertThat(currentGame().state()).isEqualTo(GameState.PLAYING);
        }

        @Test
        void should_keep_player_inside_grid_when_destroying_border_wall() {
            // Given: player at east edge of the grid with a wall blocking the outside
            var borderWall = Wall.between(new Position(4, 2), new Position(5, 2));
            var game = Game.create(new GameConfiguration(new Position(4, 2), 2, Set.of(borderWall), null, Set.of()));
            gameRepository.save(game);

            // When: player throws grenade toward the border wall
            handler.handle(new ThrowGrenadeCommand(Direction.EAST));

            // Then: grenade is consumed but player cannot leave the grid
            assertThat(currentGame().playerPosition()).isEqualTo(new Position(4, 2));
            assertThat(currentGame().grenadeCount()).isEqualTo(1);
            assertThat(currentGame().state()).isEqualTo(GameState.PLAYING);
        }
    }

    @Nested
    class DestroyedWall {

        @Test
        void should_waste_grenade_when_wall_already_destroyed() {
            // Given: game with wall between (1,1) and (2,1), player destroys it first
            givenGameWithPreviouslyDestroyedWall(new Position(1, 1), new Position(2, 1));

            // When: throw grenade east (toward already destroyed wall)
            handler.handle(new ThrowGrenadeCommand(Direction.EAST));

            // Then: grenade wasted, player stays, no movement
            assertThat(currentGame().playerPosition()).isEqualTo(new Position(1, 1));
            assertThat(currentGame().grenadeCount()).isEqualTo(1);
        }
    }

    @Nested
    class NoWall {

        @Test
        void should_waste_grenade_when_no_wall_in_direction() {
            givenGameAt(new Position(0, 0), 3);

            handler.handle(new ThrowGrenadeCommand(Direction.SOUTH));

            assertThat(currentGame().playerPosition()).isEqualTo(new Position(0, 0));
            assertThat(currentGame().grenadeCount()).isEqualTo(2);
        }

        @Test
        void should_keep_other_walls_when_throwing_in_different_direction() {
            givenGameWithWallInOtherDirection();

            handler.handle(new ThrowGrenadeCommand(Direction.SOUTH));

            assertThat(currentGame().walls()).hasSize(1);
        }

        @Test
        void should_refuse_throw_when_no_grenades_left() {
            givenGameAt(new Position(0, 0), 0);

            handler.handle(new ThrowGrenadeCommand(Direction.SOUTH));

            assertThat(currentGame().playerPosition()).isEqualTo(new Position(0, 0));
            assertThat(currentGame().grenadeCount()).isEqualTo(0);
        }
    }

    @Nested
    class Defeat {

        @Test
        void should_lose_when_blast_propels_onto_mine() {
            givenGameWithWallAndMine(new Position(0, 0), new Position(0, 1));

            handler.handle(new ThrowGrenadeCommand(Direction.SOUTH));

            assertThat(currentGame().state()).isEqualTo(GameState.LOST);
            assertThat(currentGame().playerPosition()).isEqualTo(new Position(0, 1));
        }
    }

    @Nested
    class Victory {

        @Test
        void should_win_when_blast_propels_onto_exit() {
            givenGameWithWallAndExit(new Position(3, 4), new Position(4, 4));

            handler.handle(new ThrowGrenadeCommand(Direction.EAST));

            assertThat(currentGame().state()).isEqualTo(GameState.WON);
            assertThat(currentGame().playerPosition()).isEqualTo(new Position(4, 4));
            assertThat(currentGame().grenadeCount()).isEqualTo(0);
            assertThat(currentGame().walls()).isEmpty();
        }

        @Test
        void should_block_grenade_when_game_is_won() {
            // Given: a won game where player has reached the exit with 2 grenades remaining
            givenWonGameWithGrenades(new Position(4, 4), 2);

            // When: the player attempts to throw a grenade north
            handler.handle(new ThrowGrenadeCommand(Direction.NORTH));

            // Then: grenade count unchanged, game still won
            assertThat(currentGame().grenadeCount()).isEqualTo(2);
            assertThat(currentGame().state()).isEqualTo(GameState.WON);
        }
    }

    private Game currentGame() {
        return gameRepository.findCurrent().orElseThrow();
    }

    private void givenGameAt(Position position, int grenades) {
        var game = Game.create(new GameConfiguration(position, grenades, Set.of(), null, Set.of()));
        gameRepository.save(game);
    }

    private void givenGameWithWall(Position playerPos, Position wallTarget) {
        var wall = Wall.between(playerPos, wallTarget);
        var game = Game.create(new GameConfiguration(playerPos, 3, Set.of(wall), null, Set.of()));
        gameRepository.save(game);
    }

    private void givenGameWithWallAndMine(Position playerPos, Position targetPos) {
        var wall = Wall.between(playerPos, targetPos);
        var game = Game.create(new GameConfiguration(playerPos, 3, Set.of(wall), null, Set.of(targetPos)));
        gameRepository.save(game);
    }

    private void givenGameWithWallAndExit(Position playerPos, Position exitPos) {
        var wall = Wall.between(playerPos, exitPos);
        var game = Game.create(new GameConfiguration(playerPos, 1, Set.of(wall), exitPos, Set.of()));
        gameRepository.save(game);
    }

    private void givenGameWithWallInOtherDirection() {
        var wall = Wall.between(new Position(0, 0), new Position(1, 0));
        var game = Game.create(new GameConfiguration(new Position(0, 0), 3, Set.of(wall), null, Set.of()));
        gameRepository.save(game);
    }

    private void givenGameWithPreviouslyDestroyedWall(Position playerPos, Position destroyedWallTarget) {
        // First create game with the wall
        var wall = Wall.between(playerPos, destroyedWallTarget);
        var game = Game.create(new GameConfiguration(playerPos, 3, Set.of(wall), null, Set.of()));
        // Destroy the wall by throwing a grenade (simulating "previously destroyed")
        var direction = directionBetween(playerPos, destroyedWallTarget);
        var gameAfterDestruction = game.throwGrenade(direction);
        // Now player is at destroyedWallTarget with 2 grenades
        // Move player back to original position by creating a new game state
        // that represents: player at playerPos, 2 grenades, no wall
        gameRepository.save(Game.create(new GameConfiguration(playerPos, 2, Set.of(), null, Set.of())));
    }

    private Direction directionBetween(Position from, Position to) {
        if (to.x() > from.x()) return Direction.EAST;
        if (to.x() < from.x()) return Direction.WEST;
        if (to.y() > from.y()) return Direction.SOUTH;
        return Direction.NORTH;
    }

    private void givenWonGameWithGrenades(Position exitPosition, int grenades) {
        // Create a game where the player reaches the exit by moving (WON state) with grenades
        // Start adjacent to exit, move to exit to trigger WON state
        var startPosition = new Position(exitPosition.x() - 1, exitPosition.y());
        var game = Game.create(new GameConfiguration(startPosition, grenades, Set.of(), exitPosition, Set.of()));
        var wonGame = game.move(Direction.EAST);
        gameRepository.save(wonGame);
    }
}
