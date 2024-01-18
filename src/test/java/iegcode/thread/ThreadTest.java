package iegcode.thread;

import org.junit.jupiter.api.Test;

public class ThreadTest {

    @Test
    void mainThread() {
        var name = Thread.currentThread().getName();
        System.out.println(name);
    }

    @Test
        // jalannya secara asynchrounus
    void createThread() {
        Runnable runnable = () -> {
            System.out.println("Hello from thread : " + Thread.currentThread().getName());
        };

        var thread = new Thread(runnable);
        thread.start();

        System.out.println("Program Selesai");
    }

    @Test
        // thread sleep
    void threadSleep() throws InterruptedException {
        Runnable runnable = () -> {
            try {
                Thread.sleep(2_000L);
                System.out.println("Hello from thread : " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        var thread = new Thread(runnable);
        thread.start();

        System.out.println("Program Selesai");

        Thread.sleep(3_000L);
    }

    @Test
        // join akan di tunggu lalu melanjutkan lagi
    void threadJoin() throws InterruptedException {
        Runnable runnable = () -> {
            try {
                Thread.sleep(2_000L);
                System.out.println("Hello from thread : " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        var thread = new Thread(runnable);
        thread.start();
        System.out.println("Menunggu Selesai");
        thread.join();
        System.out.println("Program Selesai");
    }

//    @Test // interrupt melakukan berhenti saat pekerjaan saat ini
//    void threadInterrupt() throws InterruptedException {
//        Runnable runnable = () -> {
//            for (int i = 0; i < 10; i++) {
//                System.out.println("Hello from thread : " + i);
//                try {
//                    Thread.sleep(1_000L);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        var thread = new Thread(runnable);
//        thread.start();
//        Thread.sleep(5_000);
//        System.out.println("Menunggu Selesai");
//        thread.join();
//        System.out.println("Program Selesai");
//    }

    @Test // yang benar
    void threadInterruptCorrect() throws InterruptedException {
        Runnable runnable = () -> {
            for (int i = 0; i < 10; i++) {
                // manual check interrupted
                if (Thread.interrupted()){
                    return;
                }
                System.out.println("Hello from thread : " + i);
                try {
                    Thread.sleep(1_000L);
                } catch (InterruptedException e) {
                    return; // break.
                }
            }
        };

        var thread = new Thread(runnable);
        thread.start();
        Thread.sleep(5_000);
        thread.interrupt();
        System.out.println("Menunggu Selesai");
        thread.join();
        System.out.println("Program Selesai");
    }

    @Test
    void threadName() {
        var thread = new Thread(() -> {
            System.out.println("Run in thread : " + Thread.currentThread().getName());
        });
        thread.setName("Gibran");
        thread.start();
    }

    @Test
    void threadState() throws InterruptedException{
        var thread = new Thread(() -> {
            System.out.println(Thread.currentThread().getState());
            System.out.println("Run in thread : " + Thread.currentThread().getName());
        });
        thread.setName("Gibran");
        System.out.println(thread.getState());
        thread.start();
        thread.join();
        System.out.println(thread.getState());
    }

}
