package visibility.solutions;

public class Visibility3 {

    static class Worker {
        private final Object lock = new Object();

        private boolean stop = false;

        private int someState = 0;

        private Thread thread;

        public Worker() {
            thread = new Thread(() -> {
                while (!isStopped()) {
                    someState++;
                }
            });
        }

        private boolean isStopped() {
            synchronized (lock) {
                return stop;
            }
        }

        public void doSomeWorkInParallel() {
            thread.start();
        }

        public void stopMe() {
            synchronized (lock) {
                stop = true;
            }
        }

        public void waitForStop() {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; ++i) {
            Worker worker = new Worker();

            worker.doSomeWorkInParallel();

            Thread.sleep(1_000);

            worker.stopMe();
            worker.waitForStop();

            System.out.print(".");
        }

    }
}
