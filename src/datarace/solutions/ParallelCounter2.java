package datarace.solutions;

import java.util.concurrent.atomic.AtomicInteger;

public class ParallelCounter2 {

    static class Counter {
        private final AtomicInteger i;

        public Counter(int initialValue) {
            i = new AtomicInteger(initialValue);
        }

        public void increment() {
            i.incrementAndGet();
        }

        public int getCounterValue() {
            return i.get();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter(0);

        Runnable r = () -> {
            for (int i = 0; i < 10_000; ++i) {
                counter.increment();
            }
        };

        Thread thread1 = new Thread(r);
        Thread thread2 = new Thread(r);

        thread1.start();
        thread2.start();

        thread1.join(); thread2.join();

        System.out.println("Counter value: " + counter.getCounterValue());
    }
}
