package task3;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FlakyDetector {
    private static final String failedStatus = "FAILED";
    private static final String passedStatus = "PASSED";

    static Set<String> failed(List<String> run){
        return getResultsByStatus(run, failedStatus);
    }

    static Set<String> passed(List<String> run){
        return getResultsByStatus(run, passedStatus);
    }

    static List<String> flaky(List<String> run1, List<String> run2){
        var firstFailed = failed(run1)
            .stream()
            .map(test -> test.split("\\|")[0])
            .toList();
        return passed(run2)
            .stream()
            .map(test -> test.split("\\|")[0])
            .filter(test -> firstFailed.contains(test))
            .toList();
    }

    static Map<String, Long> statusSummary(List<String> run){
        return run.stream()
        .collect(Collectors.groupingBy(
            s -> s.split("\\|")[1],
            Collectors.counting()
        ));
    }

    private static Set<String> getResultsByStatus(List<String> run, String status){
        return run.stream()
            .filter(test -> test.split("\\|")[1].equals(status))
            .collect(Collectors.toSet());
    }
}