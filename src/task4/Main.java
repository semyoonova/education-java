package task4;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

enum Status { PASSED, FAILED, SKIPPED }
record TestResult(String name, Status status, long durationMs) {}
record StatusGroup(Status status, long count, List<String> names) {}

public class Main {

    public static void main(String[] args) {
        List<TestResult> run = List.of(
            new TestResult("LoginTest",        Status.PASSED, 120),
            new TestResult("CreateTaskTest",   Status.PASSED, 940),
            new TestResult("UpdateStatusTest", Status.FAILED, 2100),
            new TestResult("CommentTest",      Status.PASSED, 380),
            new TestResult("DeleteTaskTest",   Status.FAILED, 760),
            new TestResult("StatsTest",        Status.SKIPPED, 0),
            new TestResult("LogoutTest",       Status.PASSED, 90)
        );

        printSummary(run);
    }

    private static void printSummary(List<TestResult> run){
        System.out.println("=== Сводка прогона ===");
        printGroupedByStatusTests(run);
        printTopLongestTests(run, 3);
        printAveragePassedDuration(run);
    }

    private static void printGroupedByStatusTests(List<TestResult> run){
        var byStatus = run.stream()
            .collect(Collectors.groupingBy(
                TestResult::status,
                Collectors.mapping(TestResult::name, Collectors.toList())
            ))
            .entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> new StatusGroup(e.getKey(), e.getValue().size(), e.getValue())
            ));

        byStatus.forEach((status, group) ->
            System.out.println(String.format("%s (%d): %s", group.status(), group.count(), String.join(", ", group.names())))
        );
    }

    private static void printTopLongestTests(List<TestResult> run, int top){
        var byTime = run.stream()
            .sorted(Comparator.comparing(TestResult::durationMs).reversed())
            .limit(top)
            .map(TestResult::name)
            .toList();

        System.out.println(String.format("Топ-%d долгих: %s", top, String.join(", ", byTime)));
    }

    private static void printAveragePassedDuration(List<TestResult> run){
        var average = run.stream()
            .filter(res -> res.status() == Status.PASSED)
            .collect(Collectors.averagingLong(TestResult::durationMs))
            .doubleValue();
        
        System.out.println(String.format("Среднее время прошедших: %f ms", average));
    }
}