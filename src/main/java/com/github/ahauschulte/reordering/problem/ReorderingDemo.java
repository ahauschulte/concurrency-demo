package com.github.ahauschulte.reordering.problem;

public class ReorderingDemo {
    int x = 0;
    int y = 0;

    int a = 0;
    int b = 0;

    Thread one = new Thread(() -> {
        a = 1;
        x = b;
    });

    Thread two = new Thread(() -> {
        b = 1;
        y = a;
    });

    void go(boolean shuffle) {
        if (shuffle) {
            one.start();
            two.start();
        } else {
            two.start();
            one.start();
        }
    }

    /*
     * Threads "one" and "two" will be executed concurrently. What are the possible outcomes for x and y?
     *   - The run method of "one" completes before the run method of "two" has started
     *     -> x = 0, y = 1
     *   - The run method of "two" completes before the run method of "one" has started
     *     -> x = 1, y = 0
     *   - The execution of the run methods of "one" and "two" overlap so that the statements "a = 1" and "b = 1"
     *     complete before the assignment statements for x and y are executed
     *     -> x = 1, y = 1
     *   - Is it possible to get the result x = 0, y = 0 after the run methods of "one" and "two" have been executed?
     */

    void waitForJoin() throws InterruptedException {
        one.join();
        two.join();
    }

    public static void main(String[] args) throws InterruptedException {

        while (hitCounterZeroZero < 2) {
            counter += 1;

            ReorderingDemo reorderingDemo = new ReorderingDemo();

            reorderingDemo.go(counter % 2 == 0);

            reorderingDemo.waitForJoin();

            updateStatistics(reorderingDemo.x, reorderingDemo.y);
        }

        printStatistics();
    }

    static void updateStatistics(int x, int y) {
        if (x == 0 && y == 0) {
            hitCounterZeroZero += 1;
        } else if (x == 1 && y == 1) {
            hitCounterOneOne += 1;
        } else if (x == 0 && y == 1) {
            hitCounterZeroOne += 1;
        } else if (x == 1 && y == 0) {
            hitCounterOneZero += 1;
        }
    }

    static void printStatistics() {
        System.out.printf("Statistics:%n" +
                        "Iterations overall %d%n" +
                        "Hits ( 0, 1 ): %d%n" +
                        "Hits ( 1, 0 ): %d%n" +
                        "Hits ( 1, 1 ): %d%n" +
                        "Hits ( 0, 0 ): %d%n",
                counter, hitCounterZeroOne, hitCounterOneZero, hitCounterOneOne, hitCounterZeroZero);
    }

    static long counter = 0;
    static long hitCounterZeroZero = 0;
    static long hitCounterOneOne = 0;
    static long hitCounterZeroOne = 0;
    static long hitCounterOneZero = 0;
}
