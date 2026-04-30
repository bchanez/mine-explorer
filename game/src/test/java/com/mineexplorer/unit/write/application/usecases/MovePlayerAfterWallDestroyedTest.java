package com.mineexplorer.unit.write.application.usecases;

import com.mineexplorer.write.adapters.secondary.persistence.InMemoryGameRepository;
import com.mineexplorer.write.application.domain.models.Direction;
import com.mineexplorer.write.application.domain.models.Game;
import com.mineexplorer.write.application.domain.models.GameConfiguration;
import com.mineexplorer.write.application.domain.models.Position;
import com.mineexplorer.write.application.domain.models.Wall;
import com.mineexplorer.write.application.usecases.MovePlayerCommand;
import com.mineexplorer.write.application.usecases.MovePlayerCommandHandler;
import com.mineexplorer.write.application.usecases.ThrowGrenadeCommand;
import com.mineexplorer.write.application.usecases.ThrowGrenadeCommandHandler;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MovePlayerAfterWallDestroyedTest {

    private final InMemoryGameRepository gameRepository = new InMemoryGameRepository();
    private final MovePlayerCommandHandler moveHandler = new MovePlayerCommandHandler(gameRepository);
    private final ThrowGrenadeCommandHandler grenadeHandler = new ThrowGrenadeCommandHandler(gameRepository);

    @Test
    void should_allow_passage_when_wall_was_destroyed() {
        // Given: a game in progress with a wall between (1, 1) and (2, 1)
        var wall = Wall.between(new Position(1, 1), new Position(2, 1));
        var game = Game.create(new GameConfiguration(new Position(1, 1), 3, Set.of(wall), null, Set.of()));
        gameRepository.save(game);

        // And: the wall is destroyed by throwing a grenade east, propelling player to (2, 1)
        grenadeHandler.handle(new ThrowGrenadeCommand(Direction.EAST));
        assertThat(currentGame().playerPosition()).isEqualTo(new Position(2, 1));
        assertThat(currentGame().walls()).isEmpty();

        // When: the player moves west (Q) through the destroyed wall
        moveHandler.handle(new MovePlayerCommand(Direction.WEST));

        // Then: the player is at position (1, 1)
        assertThat(currentGame().playerPosition()).isEqualTo(new Position(1, 1));
    }

    private Game currentGame() {
        return gameRepository.findCurrent().orElseThrow();
    }
}
