package iegcode.thread;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class CyclicBarrierTest {

    @Test
    void test() throws InterruptedException {

        final var cyclicBarrier = new CyclicBarrier(5);
        final var executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 4; i++) {
            executor.execute(() -> {
                try {
                    System.out.println("Waiting");
                    cyclicBarrier.await();
                    System.out.println("Done Waiting");
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);
    }
}
