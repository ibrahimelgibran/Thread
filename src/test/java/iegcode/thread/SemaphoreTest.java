package iegcode.thread;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreTest {

    @Test
    void test() throws InterruptedException{

        final var semaphore = new Semaphore(5);
        final var executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 20; i++) {
         executor.execute(() -> {
             try {
                 semaphore.acquire();// acuire =  boleh menaikkan counternya atau tidak
                 Thread.sleep(2000);
                 System.out.println("Finish");
             }catch (InterruptedException e){
                 e.printStackTrace();
             } finally {
                 semaphore.release();
             }
         });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);
    }
}
