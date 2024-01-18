package iegcode.thread;


// lebih aman menghindari dari raseconditioon
public class SynchronizedCounter {
    private Long value = 0L;

    // synchronized untuk mengantri thread kalo selesai maka thread sselanjutnya dieksekusi
    public  void Increment() {
        synchronized (this){
            value++;
        }
    }

    public Long getValue() {
        return value;
    }
}