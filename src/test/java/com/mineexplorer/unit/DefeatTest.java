package com.mineexplorer.unit;

import com.mineexplorer.domain.Direction;
import com.mineexplorer.domain.Game;
import com.mineexplorer.domain.GameConfiguration;
import com.mineexplorer.domain.GameState;
import com.mineexplorer.domain.Position;
import com.mineexplorer.domain.Wall;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DefeatTest {

    @Test
    void should_lose_when_player_steps_on_mine() {
        // Given
        var playerPosition = new Position(1, 1);
        var minePosition = new Position(2, 1);
        var config = new GameConfiguration(
                playerPosition,
                0,
                Set.of(),
                null,
                Set.of(minePosition)
        );
        var game = Game.create(config);

        // When
        var newGame = game.move(Direction.EAST);

        // Then
        assertThat(newGame.playerPosition()).isEqualTo(minePosition);
        assertThat(newGame.visibleCells()).contains(minePosition);
        assertThat(newGame.state()).isEqualTo(GameState.LOST);
    }

    @Test
    void should_lose_from_chain_reaction_when_mine_hides_behind_wall() {
        // Given
        var playerPosition = new Position(1, 1);
        var minePosition = new Position(2, 1);
        var wallBetweenPlayerAndMine = Wall.between(playerPosition, minePosition);
        var config = new GameConfiguration(
                playerPosition,
                2,
                Set.of(wallBetweenPlayerAndMine),
                null,
                Set.of(minePosition)
        );
        var game = Game.create(config);

        // When
        var newGame = game.throwGrenade(Direction.EAST);

        // Then
        assertThat(newGame.walls()).doesNotContain(wallBetweenPlayerAndMine);
        assertThat(newGame.playerPosition()).isEqualTo(minePosition);
        assertThat(newGame.grenadeCount()).isEqualTo(1);
        assertThat(newGame.visibleCells()).contains(minePosition);
        assertThat(newGame.state()).isEqualTo(GameState.LOST);
    }

    @Test
    void should_block_player_actions_after_losing() {
        // Given
        var minePosition = new Position(2, 2);
        var config = new GameConfiguration(minePosition, 0, Set.of(), null, Set.of(minePosition));
        var lostGame = Game.create(config);

        // When
        var gameAfterMove = lostGame.move(Direction.NORTH);

        // Then
        assertThat(gameAfterMove.playerPosition()).isEqualTo(minePosition);
        assertThat(gameAfterMove.state()).isEqualTo(GameState.LOST);
    }
}
