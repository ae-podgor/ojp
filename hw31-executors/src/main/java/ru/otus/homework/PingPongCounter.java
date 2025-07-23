package ru.otus.homework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPongCounter {
    private static final Logger logger = LoggerFactory.getLogger(PingPongCounter.class);
    private static final int MAX = 10;
    private static final int MIN = 1;

    private int counter = 1;
    private String last = "SECOND";
    private boolean upDirection = true;

    private synchronized void action(String message) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (last.equals(message)) {
                    this.wait();
                }

                logger.info(String.valueOf(counter));
                if (last.equals("FIRST")) {
                    if (upDirection) {
                        if (counter < MAX) {
                            counter++;
                        } else {
                            upDirection = false;
                            counter--;
                        }
                    } else {
                        if (counter > MIN) {
                            counter--;
                        } else {
                            upDirection = true;
                            counter++;
                        }
                    }
                }
                last = message;

                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        PingPongCounter pingPongCounter = new PingPongCounter();
        new Thread(() -> pingPongCounter.action("FIRST")).start();
        new Thread(() -> pingPongCounter.action("SECOND")).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
