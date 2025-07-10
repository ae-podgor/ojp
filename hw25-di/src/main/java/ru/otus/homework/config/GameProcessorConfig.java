package ru.otus.homework.config;

import ru.otus.homework.appcontainer.api.AppComponent;
import ru.otus.homework.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.homework.services.EquationPreparer;
import ru.otus.homework.services.EquationPreparerImpl;
import ru.otus.homework.services.GameProcessor;
import ru.otus.homework.services.GameProcessorImpl;
import ru.otus.homework.services.IOService;
import ru.otus.homework.services.IOServiceStreams;
import ru.otus.homework.services.PlayerService;
import ru.otus.homework.services.PlayerServiceImpl;

@AppComponentsContainerConfig(order = 2)
public class GameProcessorConfig {

    @AppComponent(name = "gameProcessor")
    public GameProcessor gameProcessor(
            IOService ioService, PlayerService playerService, EquationPreparer equationPreparer) {
        return new GameProcessorImpl(ioService, equationPreparer, playerService);
    }

}
