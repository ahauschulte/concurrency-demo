package datarace.problem;

public class SequentialCounter {

    static class Counter {
        private int i;

        public Counter(int initialValue) {
            i = initialValue;
        }

        public void increment() {
            ++i;
        }

        public int getCounterValue() {
            return i;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter(0);

        for (int i = 0; i < 10_000; ++i) {
            counter.increment();
        }

        for (int i = 0; i < 10_000; ++i) {
            counter.increment();
        }

        System.out.println("Counter value: " + counter.getCounterValue());
    }
}
