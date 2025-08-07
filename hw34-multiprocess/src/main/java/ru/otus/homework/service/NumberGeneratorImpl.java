package ru.otus.homework.service;

import io.grpc.stub.StreamObserver;
import ru.otus.homework.GenerateRequest;
import ru.otus.homework.NumberGeneratorGrpc;
import ru.otus.homework.NumberResponse;

import java.util.concurrent.TimeUnit;

public class NumberGeneratorImpl extends NumberGeneratorGrpc.NumberGeneratorImplBase {

    @Override
    public void generateNumbers(GenerateRequest request, StreamObserver<NumberResponse> responseObserver) {
        int firstValue = request.getFirstValue();
        int lastValue = request.getLastValue();
        int current = firstValue;

        while (current <= lastValue) {
            NumberResponse response = NumberResponse.newBuilder().setValue(current)
                    .build();
            responseObserver.onNext(response);
            current++;

            try {
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        responseObserver.onCompleted();
    }
}
