package iegcode.thread;

public class Balance {

    private Long value;

    public Balance(Long value){
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public static void tranferDeadlock(Balance from, Balance to, Long value) throws InterruptedException{
        synchronized (from){
            Thread.sleep(1000L);
            synchronized (to){
                Thread.sleep(1000L);
                from.setValue(from.getValue() - value);
                to.setValue(to.getValue() + value);
            }
        }
    }

    public static void tranfer(Balance from, Balance to, Long value) throws InterruptedException{
        synchronized (from){
            Thread.sleep(1000L);
            from.setValue(from.getValue() - value);
        }
        synchronized (to){
            Thread.sleep(1000L);
            to.setValue(to.getValue() + value);
        }
    }
}
