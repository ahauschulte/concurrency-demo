package com.github.ahauschulte.atomicity.problem;

public class AtomicityDemo1 {

    static class Counter {
        private int i;

        Counter(int initialValue) {
            i = initialValue;
        }

        void increment() {
            ++i;
        }

        int getCounterValue() {
            return i;
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
