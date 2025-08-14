package ru.otus.homework;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.service.NumberGeneratorImpl;

import java.io.IOException;

public class NumbersServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NumbersServer.class);
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(SERVER_PORT)
                .addService(new NumberGeneratorImpl())
                .build()
                .start();

        LOGGER.info("numbers server started, listening on '{}'", SERVER_PORT);
        server.awaitTermination();
    }


}
