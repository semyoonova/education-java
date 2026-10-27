package task5;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

enum Status { PASSED, FAILED }

record TestResult(String name, Status status, long durationMs) { }

public class ParallelRunner {

    public static void main(String[] args) throws InterruptedException {
        var tests = generateTests(8);
        var startTime = System.nanoTime();
        try (ExecutorService executor = Executors.newFixedThreadPool(4)) {
            
            var futures = executor.invokeAll(tests);

            var results = futures.stream()
                    .map(f -> {
                        try {
                            return f.get(); 
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();

            var endTime = System.nanoTime();
            var realDurationMs = (endTime - startTime) / 1000000; 

            var passedCount = results.stream()
                    .filter(r -> r.status() == Status.PASSED)
                    .count();
            var failedCount = results.size() - passedCount;

            var totalSequentialTimeMs = results.stream()
                    .mapToLong(TestResult::durationMs)
                    .sum();

            var savedMs = totalSequentialTimeMs - realDurationMs;

            System.out.printf("Прошло: %d, упало: %d%n", passedCount, failedCount);
            System.out.printf("Суммарное время тестов: %d ms%n", totalSequentialTimeMs);
            System.out.printf("Реальное время прогона: %d ms%n", realDurationMs);
            System.out.printf("Параллельность сэкономила: %d ms%n", savedMs);
        }

    }

    private static Callable<TestResult> fakeTest(String name, long durationMs, Status status) {
        return () -> {
            Thread.sleep(durationMs);
            return new TestResult(name, status, durationMs);
        };
    }

    private static List<Callable<TestResult>> generateTests(int count) {
        Random rand = new Random(42);
        return IntStream.range(0, count)
            .mapToObj(i -> {
                long durationMs = 300 + rand.nextInt(201); 
                Status status = (i % 2 == 0) ? Status.PASSED : Status.FAILED;
                return fakeTest("Test-" + i, durationMs, status);
            })
            .toList();
    }
}