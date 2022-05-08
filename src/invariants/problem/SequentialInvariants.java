package invariants.problem;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

public class SequentialInvariants {

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

    public static void main(String[] args) throws InterruptedException {
        Instant starts = Instant.now();

        Circle circle = new Circle(1.);

        for (long i = 1; i < 10_000_000; ++i) {
            circle.updateCircle(i);
        }

        for (long i = 1; i < 10_000_000; ++i) {
            circle.updateCircle(i);
        }

        Instant ends = Instant.now();
        Duration duration = Duration.between(starts, ends);
        double durationInSeconds = duration.getSeconds() + duration.getNano() / 1_000_000_000d;

        System.out.println("Number of invariant violations: " + Circle.getInvariantViolationCount());

        System.out.printf("Execution time %.3f second(s)%n", durationInSeconds);
    }
}
