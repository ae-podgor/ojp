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

@AppComponentsContainerConfig(order = 0)
public class EquationPreparerConfig {

    @AppComponent(name = "equationPreparer")
    public EquationPreparer equationPreparer() {
        return new EquationPreparerImpl();
    }

}
