package ru.otus.homework;


/*
В классе AppComponentsContainerImpl реализовать обработку, полученной в конструкторе конфигурации,
основываясь на разметке аннотациями из пакета appcontainer. Так же необходимо реализовать методы getAppComponent.
В итоге должно получиться работающее приложение. Менять можно только класс AppComponentsContainerImpl.
Можно добавлять свои исключения.

Раскоментируйте тест:
@Disabled //надо удалить
Тест и демо должны проходить для всех реализованных вариантов
Не называйте свой проект ДЗ "homework-template", это имя заготовки)

PS Приложение представляет собой тренажер таблицы умножения
*/

import ru.otus.homework.appcontainer.AppComponentsContainerImpl;
import ru.otus.homework.appcontainer.api.AppComponentsContainer;
import ru.otus.homework.config.EquationPreparerConfig;
import ru.otus.homework.config.GameProcessorConfig;
import ru.otus.homework.config.IOServiceConfig;
import ru.otus.homework.config.PlayerServiceConfig;
import ru.otus.homework.services.GameProcessor;
import ru.otus.homework.services.GameProcessorImpl;

@SuppressWarnings({"squid:S125", "squid:S106"})
public class App {

    public static void main(String[] args) {
        // Опциональные варианты
        // AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig1.class, AppConfig2.class);

        // Тут можно использовать библиотеку Reflections (см. зависимости)
         AppComponentsContainer container = new AppComponentsContainerImpl("ru.otus.homework.config");

        // Обязательный вариант
//        AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig.class);
//        AppComponentsContainer container = new AppComponentsContainerImpl(
//                EquationPreparerConfig.class, GameProcessorConfig.class, IOServiceConfig.class, PlayerServiceConfig.class);

        // Приложение должно работать в каждом из указанных ниже вариантов
//        GameProcessor gameProcessor = container.getAppComponent(GameProcessor.class);
         GameProcessor gameProcessor = container.getAppComponent(GameProcessorImpl.class);
//         GameProcessor gameProcessor = container.getAppComponent("gameProcessor");

        gameProcessor.startGame();
    }
}
