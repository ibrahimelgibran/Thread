package iegcode.thread;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FutureTest {
    @Test
    void testFuture()throws ExecutionException, InterruptedException {

        var executor = Executors.newSingleThreadExecutor();

        Callable<String> callable = () -> {
          Thread.sleep(5000);
          return "hi";
        };

       Future<String> future =  executor.submit(callable);
       System.out.println("Selesai Future");

       while (!future.isDone()){
           System.out.println("Waiting future");
           Thread.sleep(1000);
       }

       String value = future.get();
       System.out.println(value);
    }

    @Test
    void testFutureCancel()throws ExecutionException, InterruptedException {

        var executor = Executors.newSingleThreadExecutor();

        Callable<String> callable = () -> {
            Thread.sleep(5000);
            return "hi";
        };

        Future<String> future =  executor.submit(callable);
        System.out.println("Selesai  Future");

        Thread.sleep(2000);
        future.cancel(true);

        System.out.println(future.isCancelled());
        String value = future.get();
        System.out.println(value);
    }

    @Test // nunggu selesai langsung kita ambil
    void invokeAll()throws ExecutionException, InterruptedException {

        var execute = Executors.newFixedThreadPool(10);

        List<Callable<String>> callables = IntStream.range(1,11).mapToObj(value -> (Callable<String>)() -> {
            Thread.sleep(value * 500L);
            return String.valueOf(value);
        }).collect(Collectors.toList());

        var futures = execute.invokeAll(callables);

        for (Future<String> stringFuture : futures){
            System.out.println(stringFuture.get());
        }
    }

    @Test // nunggu yang cepat, ngambil yang paling cepat
    void invokeAny()throws ExecutionException, InterruptedException {

        var execute = Executors.newFixedThreadPool(10);

        List<Callable<String>> callables = IntStream.range(1,11).mapToObj(value -> (Callable<String>)() -> {
            Thread.sleep(value * 500L);
            return String.valueOf(value);
        }).collect(Collectors.toList());

        var value = execute.invokeAny(callables);
        System.out.println(value);
    }
}
