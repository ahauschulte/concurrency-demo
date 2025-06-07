package com.github.ahauschulte.visibility.solution;

public class VisibilityDemo {

    static class Worker {
        volatile boolean stop = false;

        int someState = 0;

        Thread thread;

        Worker() {
            thread = new Thread(() -> {
                while (!stop) {
                    someState++;
                }
            });
        }

        void doSomeWorkInParallel() {
            thread.start();
        }

        void stopMe() {
            stop = true;
        }

        void waitForStop() {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i <= 10; ++i) {
            System.out.println("Iteration #" + i);
            
            Worker worker = new Worker();

            worker.doSomeWorkInParallel();

            Thread.sleep(1_000);

            worker.stopMe();
            worker.waitForStop();

            System.out.println("Iteration done.");
        }

    }
}