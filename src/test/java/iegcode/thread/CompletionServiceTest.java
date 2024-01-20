package iegcode.thread;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.*;

public class CompletionServiceTest {

    private Random random = new Random(); // datanya random
    @Test
    void test() throws InterruptedException {

        // compeletionService - sebagai jembatan
        var executor = Executors.newFixedThreadPool(10);
        var completionService = new ExecutorCompletionService<String>(executor);

        // sumbit task - pengirim
        Executors.newSingleThreadExecutor().execute(() -> {
            for (int i = 0; i < 20; i++) {
                final var index = i;
                completionService.submit(() -> {
                   Thread.sleep(random.nextInt(2000));
                   return "Task-" + index;
                });
            }
        });

        // pool task - penerimanya
        Executors.newSingleThreadExecutor().execute(() -> {
            while (true){
                try {
                    Future<String> future = completionService.poll(5, TimeUnit.SECONDS);
                    if (future == null){
                        break;
                    } else {
                        System.out.println(future.get());
                    }
                }catch (InterruptedException | ExecutionException  e){
                    e.printStackTrace();
                    break;
                }
            }
        });
        // executor.shutdown
        executor.awaitTermination(1,TimeUnit.DAYS);
    }
}
