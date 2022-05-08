package visibility.problem;

public class Visibility {

    static class Worker {
        private boolean stop = false;

        private int someState = 0;

        private Thread thread;

        public Worker() {
            thread = new Thread(() -> {
                while (!stop) {
                    someState++;
                }
            });
        }

        public void doSomeWorkInParallel() {
            thread.start();
        }

        public void stopMe() {
            stop = true;
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
