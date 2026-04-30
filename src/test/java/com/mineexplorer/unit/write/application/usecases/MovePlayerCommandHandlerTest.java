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

        @ParameterizedTest(name = "should move {0} from (2,2)")
        @CsvSource({
                "NORTH, 2, 1",
                "SOUTH, 2, 3",
                "EAST, 3, 2",
                "WEST, 1, 2"
        })
        void should_move_in_all_directions(Direction direction, int expectedX, int expectedY) {
            givenGameAt(new Position(2, 2));

            handler.handle(new MovePlayerCommand(direction));

            assertThat(currentGame().playerPosition()).isEqualTo(new Position(expectedX, expectedY));
        }

        @ParameterizedTest(name = "should not move {2} when at boundary ({0},{1})")
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
        void should_not_move_outside_grid_boundaries(int x, int y, Direction direction) {
            givenGameAt(new Position(x, y));

            handler.handle(new MovePlayerCommand(direction));

            assertThat(currentGame().playerPosition()).isEqualTo(new Position(x, y));
        }

        @ParameterizedTest(name = "should move from edge ({0},{1}) towards {2}")
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
        void should_move_from_edge_towards_center(int startX, int startY, Direction direction, int endX, int endY) {
            givenGameAt(new Position(startX, startY));

            handler.handle(new MovePlayerCommand(direction));

            assertThat(currentGame().playerPosition()).isEqualTo(new Position(endX, endY));
        }

        @Test
        void should_not_move_when_blocked_by_wall() {
            givenGameWithWall(new Position(0, 0), new Position(0, 1));

            handler.handle(new MovePlayerCommand(Direction.SOUTH));

            assertThat(currentGame().playerPosition()).isEqualTo(new Position(0, 0));
        }

        @Test
        void should_reveal_new_position_in_fog_of_war() {
            givenGameAt(new Position(0, 0));

            handler.handle(new MovePlayerCommand(Direction.SOUTH));

            assertThat(currentGame().visibleCells()).contains(new Position(0, 0), new Position(0, 1));
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
        void should_not_allow_moves_after_winning() {
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
        void should_not_allow_moves_after_losing() {
            givenGameWithMine(new Position(0, 0), new Position(0, 1));
            handler.handle(new MovePlayerCommand(Direction.SOUTH));

            handler.handle(new MovePlayerCommand(Direction.NORTH));

            assertThat(currentGame().playerPosition()).isEqualTo(new Position(0, 1));
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
