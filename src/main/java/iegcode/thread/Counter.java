package iegcode.thread;

public class Counter {

    private Long value = 0L;

    public void Increment(){
        value++;
    }

    public Long getValue(){
        return value;
    }
}
