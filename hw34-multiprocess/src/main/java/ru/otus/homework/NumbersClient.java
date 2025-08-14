package ru.otus.homework;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class NumbersClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(NumbersClient.class);
    private static final int SERVER_PORT = 8190;
    private static final String SERVER_HOST = "localhost";

    private static final AtomicInteger lastServerValue = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        LOGGER.info("numbers client is starting...");
        NumberGeneratorGrpc.NumberGeneratorStub stub = NumberGeneratorGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);
        int currentValue = 0;

        stub.generateNumbers(GenerateRequest.newBuilder().setFirstValue(1).setLastValue(30)
                        .build(),
                new StreamObserver<NumberResponse>() {
                    @Override
                    public void onNext(NumberResponse response) {
                        lastServerValue.set(response.getValue());  // Устанавливаем новое значение
                        LOGGER.info("new value: '{}'", lastServerValue.get());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        LOGGER.info("error: ", throwable);
                        latch.countDown();
                    }

                    @Override
                    public void onCompleted() {
                        LOGGER.info("request completed");
                        latch.countDown();
                    }
                });

        LOGGER.info("currentValue: '{}'", currentValue);
        for (int i = 0; i <= 50; i++) {
            currentValue += lastServerValue.getAndSet(0) + 1; // Получаем текущее значение
            LOGGER.info("currentValue: '{}'", currentValue);

            TimeUnit.MILLISECONDS.sleep(1000);
        }

        latch.await();
        channel.shutdown();
    }
}

