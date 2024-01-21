package iegcode.thread;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class ConcurrentMapTest {

    @Test
    void concurrentMap() throws InterruptedException {
        final var countDown = new CountDownLatch(20);
        final var map = new ConcurrentHashMap<Integer, String>();
        final var executor = Executors.newFixedThreadPool(20);

        for (int i = 0; i < 20; i++) {
            final var index = i;
            executor.execute(() -> {
                try {
                    Thread.sleep(2000);
                    map.putIfAbsent(index, "Data-" + index);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    countDown.countDown();
                }
            });
        }

        executor.execute(() -> {
            try {
                countDown.await();
                map.forEach((integer, s) -> System.out.println(integer + " : " + s));
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        });

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);
    }

    @Test
    void testCollection() {
        List<String> list = List.of("Ibrahim", "El", "Gibran");
        List<String> synchronzedList = Collections.synchronizedList(list);
    }
}
