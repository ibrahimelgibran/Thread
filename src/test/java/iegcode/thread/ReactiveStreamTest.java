package iegcode.thread;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

public class ReactiveStreamTest {


    @Test
    void publish() throws InterruptedException {
        var publisher = new SubmissionPublisher<String>();
        var subscriber1 = new PrintSubscriber("A" , 1000L);
        var subscriber2 = new PrintSubscriber("B" , 500L);
        publisher.subscribe(subscriber1);
        publisher.subscribe(subscriber2);

        var executor = Executors.newFixedThreadPool(10);
        executor.execute(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(2000);
                    publisher.submit("Gibran-" + i);
                    System.out.println(Thread.currentThread().getName() + " Send Gibran-" + i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);

        Thread.sleep(30 * 1000);
    }

    @Test // buffer mirip seperti antrian tapi menampung datanya.
    void buffer() throws InterruptedException {
        var publisher = new SubmissionPublisher<String>(Executors.newWorkStealingPool(), 10);
        var subscriber1 = new PrintSubscriber("A" , 1000L);
        var subscriber2 = new PrintSubscriber("B" , 500L);
        publisher.subscribe(subscriber1);
        publisher.subscribe(subscriber2);

        var executor = Executors.newFixedThreadPool(10);
        executor.execute(() -> {
            for (int i = 0; i < 100; i++) {
                publisher.submit("Gibran-" + i);
                System.out.println(Thread.currentThread().getName() + " Send Gibran-" + i);
            }
        });

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);

        Thread.sleep(30 * 1000);
    }

    @Test
    void processor() throws InterruptedException {

        var publisher = new SubmissionPublisher<String>();

        var processor = new HelloProcessor();
        publisher.subscribe(processor);

        var subscriber = new PrintSubscriber("A", 1000L);
        publisher.subscribe(subscriber);

        var executor = Executors.newFixedThreadPool(10);
        executor.execute(() -> {
            for (int i = 0; i < 10; i++) {
                publisher.submit("Hello Gibran-" + i);
                System.out.println(Thread.currentThread().getName() + " Send Gibran-" + i);
            }
        });

        Thread.sleep(100 * 1000);

    }

    public static class PrintSubscriber implements Flow.Subscriber<String>{
        private Flow.Subscription subscription;
        private String name;
        private Long sleep;

        public PrintSubscriber(String name, Long sleep) {
            this.name = name;
            this.sleep = sleep;
        }


        @Override // memulai on subscribe data
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            this.subscription.request(1); // menarik data
        }

        @Override // diekesekusi untuk menerima data / menarik data
        public void onNext(String item) { // nanti data yang di tarik akan masuk ke onNext sampe data selesai
            try {
                Thread.sleep(sleep * 2);
                System.out.println(Thread.currentThread().getName() + " : " + name + " : " + item);
                this.subscription.request(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override // jika terjadi error
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
        }

        @Override // ketika selesai menerima data
        public void onComplete() {
            System.out.println(Thread.currentThread().getName() + " : DONE" );
        }
    }

    public static class HelloProcessor extends SubmissionPublisher<String> implements Flow.Processor<String, String>{

        private Flow.Subscription subscription;

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            this.subscription.request(1);
        }

        @Override
        public void onNext(String item) {
            var value = "Hello " + item;
            submit(value);
            this.subscription.request(1);
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
        }

        @Override
        public void onComplete() {
            close();
        }
    }
}
