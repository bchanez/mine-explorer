package com.mineexplorer.unit.write.application.usecases;

import com.mineexplorer.write.adapters.secondary.persistence.InMemoryGameRepository;
import com.mineexplorer.write.application.domain.models.Direction;
import com.mineexplorer.write.application.domain.models.Game;
import com.mineexplorer.write.application.domain.models.GameConfiguration;
import com.mineexplorer.write.application.domain.models.GameState;
import com.mineexplorer.write.application.domain.models.Position;
import com.mineexplorer.write.application.domain.models.Wall;
import com.mineexplorer.write.application.usecases.MovePlayerCommand;
import com.mineexplorer.write.application.usecases.MovePlayerCommandHandler;
import com.mineexplorer.write.application.usecases.NoGameInProgressException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MovePlayerCommandHandlerTest {

    private final InMemoryGameRepository gameRepository = new InMemoryGameRepository();
    private final MovePlayerCommandHandler handler = new MovePlayerCommandHandler(gameRepository);

    @Test
    void should_fail_when_no_game_in_progress() {
        assertThatThrownBy(() -> handler.handle(new MovePlayerCommand(Direction.SOUTH)))
                .isInstanceOf(NoGameInProgressException.class);
    }

    @Nested
    class Movement {

        @ParameterizedTest(name = "should move {0} when path is clear")
        @CsvSource({
                "NORTH, 2, 1",
                "SOUTH, 2, 3",
                "EAST, 3, 2",
                "WEST, 1, 2"
        })
        void should_move_when_path_is_clear(Direction direction, int expectedX, int expectedY) {
            givenGameAt(new Position(2, 2));

            handler.handle(new MovePlayerCommand(direction));

            assertThat(currentGame().playerPosition()).isEqualTo(new Position(expectedX, expectedY));
        }

        @ParameterizedTest(name = "should stay when moving {2} at border ({0},{1})")
        @CsvSource({
                "0, 0, NORTH",
                "0, 0, WEST",
                "4, 0, NORTH",
                "4, 0, EAST",
                "0, 4, SOUTH",
                "0, 4, WEST",
                "4, 4, SOUTH",
                "4, 4, EAST"
        })
        void should_stay_when_moving_outside_grid(int x, int y, Direction direction) {
            givenGameAt(new Position(x, y));

            handler.handle(new MovePlayerCommand(direction));

            assertThat(currentGame().playerPosition()).isEqualTo(new Position(x, y));
        }

        @ParameterizedTest(name = "should move {2} when leaving border ({0},{1})")
        @CsvSource({
                "0, 0, SOUTH, 0, 1",
                "0, 0, EAST, 1, 0",
                "4, 4, NORTH, 4, 3",
                "4, 4, WEST, 3, 4",
                "0, 4, NORTH, 0, 3",
                "0, 4, EAST, 1, 4",
                "4, 0, SOUTH, 4, 1",
                "4, 0, WEST, 3, 0"
        })
        void should_move_when_leaving_border(int startX, int startY, Direction direction, int endX, int endY) {
            givenGameAt(new Position(startX, startY));

            handler.handle(new MovePlayerCommand(direction));

            assertThat(currentGame().playerPosition()).isEqualTo(new Position(endX, endY));
        }

        @Test
        void should_stay_when_blocked_by_wall() {
            givenGameWithWall(new Position(0, 0), new Position(0, 1));

            handler.handle(new MovePlayerCommand(Direction.SOUTH));

            assertThat(currentGame().playerPosition()).isEqualTo(new Position(0, 0));
        }

        @Test
        void should_reveal_cell_when_moving_into_it() {
            givenGameAt(new Position(0, 0));

            handler.handle(new MovePlayerCommand(Direction.SOUTH));

            assertThat(currentGame().visibleCells()).contains(new Position(0, 0), new Position(0, 1));
        }

        @Test
        void should_keep_cells_visible_when_leaving_them() {
            // Given: player at (0,0), no walls between (0,0)-(1,0) and (1,0)-(2,0)
            givenGameAt(new Position(0, 0));

            // When: player moves east twice
            handler.handle(new MovePlayerCommand(Direction.EAST));
            handler.handle(new MovePlayerCommand(Direction.EAST));

            // Then: player is at (2,0) and all visited cells remain visible
            assertThat(currentGame().playerPosition()).isEqualTo(new Position(2, 0));
            assertThat(currentGame().visibleCells()).containsExactlyInAnyOrder(
                    new Position(0, 0),
                    new Position(1, 0),
                    new Position(2, 0)
            );
        }
    }

    @Nested
    class Victory {

        @Test
        void should_win_when_reaching_exit() {
            givenGameWithExit(new Position(0, 0), new Position(0, 1));

            handler.handle(new MovePlayerCommand(Direction.SOUTH));

            assertThat(currentGame().state()).isEqualTo(GameState.WON);
        }

        @Test
        void should_reveal_exit_and_win_when_stepping_on_it() {
            // Given: Scenario 6.3 - player at (1,1), exit at (2,1), no wall between them
            // Cell (2,1) is not visible initially (fog of war)
            var playerPosition = new Position(1, 1);
            var exitPosition = new Position(2, 1);
            var game = Game.create(new GameConfiguration(playerPosition, 3, Set.of(), exitPosition, Set.of()));
            gameRepository.save(game);

            // Verify precondition: exit cell is not visible
            assertThat(currentGame().visibleCells()).doesNotContain(exitPosition);

            // When: player moves east (D)
            handler.handle(new MovePlayerCommand(Direction.EAST));

            // Then: cell (2,1) becomes visible
            assertThat(currentGame().visibleCells()).contains(exitPosition);
            // And: the player sees that cell (2,1) contains the exit (implicit: cell is visible + exit is there)
            assertThat(currentGame().playerPosition()).isEqualTo(exitPosition);
            // And: the game is won (WON)
            assertThat(currentGame().state()).isEqualTo(GameState.WON);
        }

        @Test
        void should_block_movement_when_game_is_won() {
            givenGameWithExit(new Position(0, 0), new Position(0, 1));
            handler.handle(new MovePlayerCommand(Direction.SOUTH));

            handler.handle(new MovePlayerCommand(Direction.SOUTH));

            assertThat(currentGame().playerPosition()).isEqualTo(new Position(0, 1));
        }
    }

    @Nested
    class Defeat {

        @Test
        void should_lose_when_stepping_on_mine() {
            givenGameWithMine(new Position(0, 0), new Position(0, 1));

            handler.handle(new MovePlayerCommand(Direction.SOUTH));

            assertThat(currentGame().state()).isEqualTo(GameState.LOST);
        }

        @Test
        void should_block_movement_when_game_is_lost() {
            givenGameWithMine(new Position(0, 0), new Position(0, 1));
            handler.handle(new MovePlayerCommand(Direction.SOUTH));

            handler.handle(new MovePlayerCommand(Direction.NORTH));

            assertThat(currentGame().playerPosition()).isEqualTo(new Position(0, 1));
        }

        @Test
        void should_continue_playing_when_mine_is_nearby_but_unreached() {
            // Given: player at (1,1), mine at (2,1), wall between them
            var wall = Wall.between(new Position(1, 1), new Position(2, 1));
            var game = Game.create(new GameConfiguration(
                    new Position(1, 1),
                    3,
                    Set.of(wall),
                    null,
                    Set.of(new Position(2, 1))
            ));
            gameRepository.save(game);

            // When: player moves south (not towards the mine)
            handler.handle(new MovePlayerCommand(Direction.SOUTH));

            // Then: player is at (1,2) and game is still PLAYING
            assertThat(currentGame().playerPosition()).isEqualTo(new Position(1, 2));
            assertThat(currentGame().state()).isEqualTo(GameState.PLAYING);
        }
    }

    @Nested
    class EdgeCases {

        @Test
        void should_move_when_path_is_clear_and_no_grenades() {
            // Given: Scenario 7.1 - player at (1,1) with 0 grenades
            // No wall between (1,1) and (1,2), wall between (1,1) and (2,1)
            var playerPos = new Position(1, 1);
            var wallEast = Wall.between(new Position(1, 1), new Position(2, 1));
            var game = Game.create(new GameConfiguration(playerPos, 0, Set.of(wallEast)));
            gameRepository.save(game);

            // When: player moves south
            handler.handle(new MovePlayerCommand(Direction.SOUTH));

            // Then: player is at (1,2) and game is PLAYING
            assertThat(currentGame().playerPosition()).isEqualTo(new Position(1, 2));
            assertThat(currentGame().state()).isEqualTo(GameState.PLAYING);
        }

        @Test
        void should_stay_when_surrounded_by_walls_without_grenades() {
            // Given: Scenario 7.2 - player at (2,2) with 0 grenades
            // Walls in all 4 directions
            var playerPos = new Position(2, 2);
            var wallWest = Wall.between(new Position(2, 2), new Position(1, 2));
            var wallEast = Wall.between(new Position(2, 2), new Position(3, 2));
            var wallNorth = Wall.between(new Position(2, 2), new Position(2, 1));
            var wallSouth = Wall.between(new Position(2, 2), new Position(2, 3));
            var game = Game.create(new GameConfiguration(playerPos, 0, Set.of(wallWest, wallEast, wallNorth, wallSouth)));
            gameRepository.save(game);

            // When: player tries to move east
            handler.handle(new MovePlayerCommand(Direction.EAST));

            // Then: player stays at (2,2) and game is PLAYING
            assertThat(currentGame().playerPosition()).isEqualTo(new Position(2, 2));
            assertThat(currentGame().state()).isEqualTo(GameState.PLAYING);
        }
    }

    private Game currentGame() {
        return gameRepository.findCurrent().orElseThrow();
    }

    private void givenGameAt(Position position) {
        var game = Game.create(new GameConfiguration(position, 3));
        gameRepository.save(game);
    }

    private void givenGameWithWall(Position playerPos, Position wallTarget) {
        var wall = Wall.between(playerPos, wallTarget);
        var game = Game.create(new GameConfiguration(playerPos, 3, Set.of(wall)));
        gameRepository.save(game);
    }

    private void givenGameWithExit(Position playerPos, Position exitPos) {
        var game = Game.create(new GameConfiguration(playerPos, 3, Set.of(), exitPos, Set.of()));
        gameRepository.save(game);
    }

    private void givenGameWithMine(Position playerPos, Position minePos) {
        var game = Game.create(new GameConfiguration(playerPos, 3, Set.of(), null, Set.of(minePos)));
        gameRepository.save(game);
    }
}
