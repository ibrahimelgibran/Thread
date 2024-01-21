package iegcode.thread;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Exchanger;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExchangerTest {

    @Test
    void test() throws InterruptedException {

        final var exchanger = new Exchanger<String>();
        final var executor = Executors.newFixedThreadPool(10);

        executor.execute(() -> {
            try {
                System.out.println("Thread 1 : send : First");
                Thread.sleep(1000);
                var result = exchanger.exchange("First");
                System.out.println("Thread 1 : Receive : First " + result);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        executor.execute(() -> {
            try {
                System.out.println("Thread 2 : send : Second");
                Thread.sleep(2000);
                var result = exchanger.exchange("First");
                System.out.println("Thread 2 : Receive : Second " + result);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);
    }
}

// kalo 1 pihak tidak ada menerima maka akan muter terus sampe ada yang harus menerima baru bisa