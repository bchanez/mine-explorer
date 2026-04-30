package com.mineexplorer.configuration;

import com.mineexplorer.console.GameRenderer;
import com.mineexplorer.write.adapters.secondary.persistence.InMemoryGameRepository;
import com.mineexplorer.write.application.domain.ports.GameRepository;
import com.mineexplorer.write.application.usecases.MovePlayerCommandHandler;
import com.mineexplorer.write.application.usecases.StartGameCommandHandler;
import com.mineexplorer.write.application.usecases.ThrowGrenadeCommandHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameConfiguration {

    @Bean
    GameRepository gameRepository() {
        return new InMemoryGameRepository();
    }

    @Bean
    StartGameCommandHandler startGameCommandHandler(GameRepository gameRepository) {
        return new StartGameCommandHandler(gameRepository);
    }

    @Bean
    MovePlayerCommandHandler movePlayerCommandHandler(GameRepository gameRepository) {
        return new MovePlayerCommandHandler(gameRepository);
    }

    @Bean
    ThrowGrenadeCommandHandler throwGrenadeCommandHandler(GameRepository gameRepository) {
        return new ThrowGrenadeCommandHandler(gameRepository);
    }

    @Bean
    GameRenderer gameRenderer() {
        return new GameRenderer();
    }
}
