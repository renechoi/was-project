package org.example.counter;

public class RaceConditionDemo {
    public static void main(String[] args) {
        // 싱글톤 객체에서 멀티 스레드 환경에서 하나의 객체를 공유하게 되면 (state상태를 유지하게 되면)
        // 원하는 결과값이 나오지 않음
        // => thread safety 하지 않다
        Counter counter = new Counter();
        Thread t1 = new Thread(counter, "Thread-1");
        Thread t2 = new Thread(counter, "Thread-2");
        Thread t3 = new Thread(counter, "Thread-3");

        t1.start();
        t2.start();
        t3.start();

    }
}
