package com.mineexplorer.console;

import com.mineexplorer.write.application.domain.models.Direction;
import com.mineexplorer.write.application.domain.models.Game;
import com.mineexplorer.write.application.domain.models.GameState;
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
    private final GameRenderer gameRenderer;

    public ConsoleGameRunner(
            StartGameCommandHandler startGameHandler,
            MovePlayerCommandHandler movePlayerHandler,
            ThrowGrenadeCommandHandler throwGrenadeHandler,
            GameRepository gameRepository,
            GameRenderer gameRenderer) {
        this.startGameHandler = startGameHandler;
        this.movePlayerHandler = movePlayerHandler;
        this.throwGrenadeHandler = throwGrenadeHandler;
        this.gameRepository = gameRepository;
        this.gameRenderer = gameRenderer;
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
        System.out.print(gameRenderer.render(game));
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
