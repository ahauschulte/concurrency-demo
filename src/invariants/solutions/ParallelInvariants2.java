package invariants.solutions;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ParallelInvariants2 {

    static class Circle {
        private static final AtomicLong invariantViolationCounter = new AtomicLong(0);

        private double radius;
        private double perimeter;
        private double area;

        public Circle(double radius) {
            this.radius = radius;
            perimeter = calculatePerimeter(radius);
            area = calculateArea(radius);
        }

        public void updateCircle(double radius) {
            checkCircleConsistency(this, invariantViolationCounter);

            this.radius = radius;
            perimeter = calculatePerimeter(radius);
            area = calculateArea(radius);
        }

        public double getRadius() {
            return radius;
        }

        public double getPerimeter() {
            return perimeter;
        }

        public double getArea() {
            return area;
        }

        private static double calculatePerimeter(double radius) {
            return 2. * Math.PI * radius;
        }

        private static double calculateArea(double radius) {
            return Math.PI * radius * radius;
        }

        private static long getInvariantViolationCount() {
            return invariantViolationCounter.get();
        }
    }

    private static void checkCircleConsistency(Circle circle, AtomicLong inconsistencyCounter) {
        double localRadius = circle.getRadius();
        double localPerimeter = circle.getPerimeter();
        double localArea = circle.getArea();

        double controlValuePerimeter = Circle.calculatePerimeter(localRadius);
        double controlValueArea = Circle.calculateArea(localArea);

        if ((localPerimeter - controlValuePerimeter > EPS) || (localArea - controlValueArea > EPS)) {
            inconsistencyCounter.incrementAndGet();
        }
    }

    private static final double EPS = 0.0001;

    private static final AtomicLong inconsistentObservationCounter = new AtomicLong(0);
    private static volatile boolean stop = false;

    private static final ReadWriteLock lock = new ReentrantReadWriteLock();

    public static void main(String[] args) throws InterruptedException {
        Instant starts = Instant.now();

        Circle circle = new Circle(1.);

        Runnable r = () -> {
            for (long i = 1; i < 10_000_000; ++i) {
                lock.writeLock().lock();
                try {
                    circle.updateCircle(i);
                } finally {
                    lock.writeLock().unlock();
                }
            }
        };

        Runnable c = new Thread(() -> {
            while (!stop) {
                double localRadius;
                double localPerimeter;
                double localArea;

                lock.readLock().lock();
                try {
                    localRadius = circle.getRadius();
                    localPerimeter = circle.getPerimeter();
                    localArea = circle.getArea();
                } finally {
                    lock.readLock().unlock();
                }

                double controlValuePerimeter = Circle.calculatePerimeter(localRadius);
                double controlValueArea = Circle.calculateArea(localArea);

                if ((localPerimeter - controlValuePerimeter > EPS) || (localArea - controlValueArea > EPS)) {
                    inconsistentObservationCounter.incrementAndGet();
                }
            }
        });

        Thread thread0 = new Thread(c);
        Thread thread1 = new Thread(r);
        Thread thread2 = new Thread(r);

        thread0.start();
        thread1.start();
        thread2.start();

        thread1.join(); thread2.join();
        stop = true;
        thread0.join();

        Instant ends = Instant.now();
        Duration duration = Duration.between(starts, ends);
        double durationInSeconds = duration.getSeconds() + duration.getNano() / 1_000_000_000d;

        System.out.println("Number of invariant violations: " + Circle.getInvariantViolationCount());
        System.out.println("Number of inconsistent observations: " + inconsistentObservationCounter.get());

        System.out.printf("Execution time %.3f second(s)%n", durationInSeconds);
    }
}
