package com.mineexplorer.write.application.usecases;

import com.mineexplorer.write.application.domain.models.Game;
import com.mineexplorer.write.application.domain.models.GameConfiguration;
import com.mineexplorer.write.application.domain.models.Position;
import com.mineexplorer.write.application.domain.models.Wall;
import com.mineexplorer.write.application.domain.ports.GameRepository;

import java.util.HashSet;
import java.util.Set;

public class StartGameCommandHandler {

    private final GameRepository gameRepository;

    public StartGameCommandHandler(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public void handle(StartGameCommand command) {
        var walls = buildGridWalls();
        var config = new GameConfiguration(
                new Position(0, 0),
                5,
                walls,
                new Position(4, 4),
                Set.of()
        );
        var game = Game.create(config);
        gameRepository.save(game);
    }

    private Set<Wall> buildGridWalls() {
        var walls = new HashSet<Wall>();
        var openings = Set.of(
                Wall.between(new Position(0, 0), new Position(0, 1)),
                Wall.between(new Position(0, 1), new Position(0, 2)),
                Wall.between(new Position(1, 2), new Position(2, 2)),
                Wall.between(new Position(2, 2), new Position(2, 3)),
                Wall.between(new Position(3, 4), new Position(4, 4))
        );

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 4; x++) {
                var wall = Wall.between(new Position(x, y), new Position(x + 1, y));
                if (!openings.contains(wall)) {
                    walls.add(wall);
                }
            }
        }

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 5; x++) {
                var wall = Wall.between(new Position(x, y), new Position(x, y + 1));
                if (!openings.contains(wall)) {
                    walls.add(wall);
                }
            }
        }

        return walls;
    }
}
