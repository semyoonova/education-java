package task3;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        var run1 = List.of(
            "LoginTest|PASSED",
            "CreateTaskTest|FAILED",
            "CommentTest|FAILED",
            "LogoutTest|PASSED"
        );
        var run2 = List.of(
            "LoginTest|PASSED",
            "CreateTaskTest|PASSED",
            "CommentTest|FAILED",
            "StatsTest|PASSED"
        );

        System.out.println("Flaky-кандидаты: " + FlakyDetector.flaky(run1, run2));
        System.out.println("Сводка: " + FlakyDetector.statusSummary(run2));
        System.out.println(FlakyDetector.failed(run2));
        System.out.println(FlakyDetector.passed(run1));
    }
}