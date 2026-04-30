package com.mineexplorer.console;

import com.mineexplorer.write.application.domain.models.Direction;
import com.mineexplorer.write.application.domain.models.Game;
import com.mineexplorer.write.application.domain.models.GameState;
import com.mineexplorer.write.application.domain.models.Position;
import com.mineexplorer.write.application.domain.models.Wall;
import com.mineexplorer.write.application.domain.ports.GameRepository;
import com.mineexplorer.write.application.usecases.MovePlayerCommand;
import com.mineexplorer.write.application.usecases.MovePlayerCommandHandler;
import com.mineexplorer.write.application.usecases.StartGameCommand;
import com.mineexplorer.write.application.usecases.StartGameCommandHandler;
import com.mineexplorer.write.application.usecases.ThrowGrenadeCommand;
import com.mineexplorer.write.application.usecases.ThrowGrenadeCommandHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleGameRunner implements CommandLineRunner {


    private final StartGameCommandHandler startGameHandler;
    private final MovePlayerCommandHandler movePlayerHandler;
    private final ThrowGrenadeCommandHandler throwGrenadeHandler;
    private final GameRepository gameRepository;

    public ConsoleGameRunner(
            StartGameCommandHandler startGameHandler,
            MovePlayerCommandHandler movePlayerHandler,
            ThrowGrenadeCommandHandler throwGrenadeHandler,
            GameRepository gameRepository) {
        this.startGameHandler = startGameHandler;
        this.movePlayerHandler = movePlayerHandler;
        this.throwGrenadeHandler = throwGrenadeHandler;
        this.gameRepository = gameRepository;
    }

    @Override
    public void run(String... args) {
        var scanner = new Scanner(System.in);

        printWelcome();
        startGameHandler.handle(new StartGameCommand());

        while (true) {
            var game = gameRepository.findCurrent().orElseThrow();
            printGame(game);

            if (game.state() == GameState.WON) {
                System.out.println("🎉 VICTORY! You reached the exit!");
                break;
            }
            if (game.state() == GameState.LOST) {
                System.out.println("💥 DEFEAT! You stepped on a mine...");
                break;
            }

            System.out.print("\n> ");
            var input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("q") || input.equals("quit")) {
                System.out.println("Goodbye!");
                break;
            }

            processInput(input);
        }
    }

    private void printWelcome() {
        System.out.println("""

            ╔═══════════════════════════════════════╗
            ║         MINE EXPLORER                 ║
            ╠═══════════════════════════════════════╣
            ║  Reach the exit [E] without exploding ║
            ║                                       ║
            ║  Commands:                            ║
            ║    z/w = up      s = down             ║
            ║    q/a = left    d = right            ║
            ║                                       ║
            ║    gz/gw = grenade up                 ║
            ║    gs = grenade down                  ║
            ║    gq/ga = grenade left               ║
            ║    gd = grenade right                 ║
            ║                                       ║
            ║    quit/q = quit                      ║
            ╚═══════════════════════════════════════╝
            """);
    }

    private void printGame(Game game) {
        System.out.println();
        System.out.println("Grenades: " + "💣".repeat(game.grenadeCount()));
        System.out.println();

        var visibleCells = game.visibleCells();
        int minX = visibleCells.stream().mapToInt(Position::x).min().orElse(0);
        int maxX = visibleCells.stream().mapToInt(Position::x).max().orElse(0);
        int minY = visibleCells.stream().mapToInt(Position::y).min().orElse(0);
        int maxY = visibleCells.stream().mapToInt(Position::y).max().orElse(0);

        for (int y = minY; y <= maxY; y++) {
            var row = new StringBuilder();
            for (int x = minX; x <= maxX; x++) {
                var pos = new Position(x, y);
                row.append(cellToString(game, pos));
                row.append(hasVisibleWallToRight(game, pos) ? "│" : " ");
            }
            System.out.println(row);

            var wallRow = new StringBuilder();
            for (int x = minX; x <= maxX; x++) {
                var pos = new Position(x, y);
                wallRow.append(hasVisibleWallBelow(game, pos) ? "──" : "  ");
                wallRow.append(" ");
            }
            if (wallRow.toString().contains("─")) {
                System.out.println(wallRow);
            }
        }
    }

    private boolean hasVisibleWallToRight(Game game, Position pos) {
        var rightNeighbor = new Position(pos.x() + 1, pos.y());
        var wallExists = game.walls().contains(Wall.between(pos, rightNeighbor));
        var isVisible = game.visibleCells().contains(pos) || game.visibleCells().contains(rightNeighbor);
        return wallExists && isVisible;
    }

    private boolean hasVisibleWallBelow(Game game, Position pos) {
        var belowNeighbor = new Position(pos.x(), pos.y() + 1);
        var wallExists = game.walls().contains(Wall.between(pos, belowNeighbor));
        var isVisible = game.visibleCells().contains(pos) || game.visibleCells().contains(belowNeighbor);
        return wallExists && isVisible;
    }

    private String cellToString(Game game, Position pos) {
        if (pos.equals(game.playerPosition())) {
            return "🧑";
        }
        if (!game.visibleCells().contains(pos)) {
            return "  ";
        }
        if (game.state() == GameState.WON && pos.equals(game.playerPosition())) {
            return "🚪";
        }
        return "··";
    }

    private void processInput(String input) {
        switch (input) {
            case "z", "w" -> move(Direction.NORTH);
            case "s" -> move(Direction.SOUTH);
            case "q", "a" -> move(Direction.WEST);
            case "d" -> move(Direction.EAST);
            case "gz", "gw" -> throwGrenade(Direction.NORTH);
            case "gs" -> throwGrenade(Direction.SOUTH);
            case "gq", "ga" -> throwGrenade(Direction.WEST);
            case "gd" -> throwGrenade(Direction.EAST);
            default -> System.out.println("Unknown command: " + input);
        }
    }

    private void move(Direction direction) {
        movePlayerHandler.handle(new MovePlayerCommand(direction));
    }

    private void throwGrenade(Direction direction) {
        throwGrenadeHandler.handle(new ThrowGrenadeCommand(direction));
    }
}
