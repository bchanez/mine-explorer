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
        void should_destroy_wall_and_propel_player_through() {
            givenGameWithWall(new Position(0, 0), new Position(0, 1));

            var game = handler.handle(new ThrowGrenadeCommand(Direction.SOUTH));

            assertThat(game.playerPosition()).isEqualTo(new Position(0, 1));
            assertThat(game.walls()).isEmpty();
            assertThat(gameRepository.findCurrent().get().walls()).isEmpty();
        }

        @Test
        void should_decrement_grenade_count() {
            givenGameWithWall(new Position(0, 0), new Position(0, 1));

            var game = handler.handle(new ThrowGrenadeCommand(Direction.SOUTH));

            assertThat(game.grenadeCount()).isEqualTo(2);
        }

        @Test
        void should_reveal_target_position_in_fog_of_war() {
            givenGameWithWall(new Position(0, 0), new Position(0, 1));

            var game = handler.handle(new ThrowGrenadeCommand(Direction.SOUTH));

            assertThat(game.visibleCells()).contains(new Position(0, 1));
        }
    }

    @Nested
    class NoWall {

        @Test
        void should_waste_grenade_when_no_wall_in_direction() {
            givenGameAt(new Position(0, 0), 3);

            var game = handler.handle(new ThrowGrenadeCommand(Direction.SOUTH));

            assertThat(game.playerPosition()).isEqualTo(new Position(0, 0));
            assertThat(game.grenadeCount()).isEqualTo(2);
            assertThat(gameRepository.findCurrent().get().grenadeCount()).isEqualTo(2);
        }

        @Test
        void should_keep_other_walls_when_throwing_in_different_direction() {
            givenGameWithWallInOtherDirection();

            var game = handler.handle(new ThrowGrenadeCommand(Direction.SOUTH));

            assertThat(game.walls()).hasSize(1);
            assertThat(gameRepository.findCurrent().get().walls()).hasSize(1);
        }

        @Test
        void should_not_throw_when_no_grenades_left() {
            givenGameAt(new Position(0, 0), 0);

            var game = handler.handle(new ThrowGrenadeCommand(Direction.SOUTH));

            assertThat(game.playerPosition()).isEqualTo(new Position(0, 0));
            assertThat(game.grenadeCount()).isEqualTo(0);
        }
    }

    @Nested
    class Defeat {

        @Test
        void should_lose_when_propelled_into_mine() {
            givenGameWithWallAndMine(new Position(0, 0), new Position(0, 1));

            var game = handler.handle(new ThrowGrenadeCommand(Direction.SOUTH));

            assertThat(game.state()).isEqualTo(GameState.LOST);
            assertThat(game.playerPosition()).isEqualTo(new Position(0, 1));
        }
    }

    private void givenGameAt(Position position, int grenades) {
        var game = Game.create(new GameConfiguration(position, grenades));
        gameRepository.save(game);
    }

    private void givenGameWithWall(Position playerPos, Position wallTarget) {
        var wall = Wall.between(playerPos, wallTarget);
        var game = Game.create(new GameConfiguration(playerPos, 3, Set.of(wall)));
        gameRepository.save(game);
    }

    private void givenGameWithWallAndMine(Position playerPos, Position targetPos) {
        var wall = Wall.between(playerPos, targetPos);
        var game = Game.create(new GameConfiguration(playerPos, 3, Set.of(wall), null, Set.of(targetPos)));
        gameRepository.save(game);
    }

    private void givenGameWithWallInOtherDirection() {
        var wall = Wall.between(new Position(0, 0), new Position(1, 0));
        var game = Game.create(new GameConfiguration(new Position(0, 0), 3, Set.of(wall)));
        gameRepository.save(game);
    }
}
