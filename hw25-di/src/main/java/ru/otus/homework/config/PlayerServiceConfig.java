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

@AppComponentsContainerConfig(order = 1)
public class PlayerServiceConfig {

    @AppComponent(name = "playerService")
    public PlayerService playerService(IOService ioService) {
        return new PlayerServiceImpl(ioService);
    }

}
