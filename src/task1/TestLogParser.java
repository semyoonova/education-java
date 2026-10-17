package task1;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

enum Status { PASSED, FAILED, SKIPPED }
record TestResult(String name, Status status, long duration) { }
record ErrorResult(String result, String reason) { }

class TestLogParser {

    private final List<TestResult> allCorrectResults = new ArrayList<>();
    private final List<ErrorResult> incorrectResults = new ArrayList<>();
    
    public List<TestResult> getCorrectResults() {
        Map<String, TestResult> lastByTestName = new LinkedHashMap<>();
        
        for (TestResult result : allCorrectResults) {
            lastByTestName.put(result.name(), result);
        }

        return new ArrayList<>(lastByTestName.values());
    }

    public List<ErrorResult> getIncorrectResults() {
        return new ArrayList<>(incorrectResults);
    }

    public Optional<TestResult> firstFailed(List<TestResult> results) {
        if (results == null || results.isEmpty()) {
            return Optional.empty();
        }

        return results.stream()
            .filter(r -> r.status() == Status.FAILED)
            .findFirst();
    }

    public void parse(List<String> lines) {
        allCorrectResults.clear();
        incorrectResults.clear();

        for (var line : lines) {
            if (line == null || line.isBlank()) continue;

            try {
                var parts = line.split("\\|");
                if (parts.length < 3){
                    incorrectResults.add(new ErrorResult(line, "Не хватает разделитетей или нет данных"));
                    continue;
                }

                var name = parts[0].trim();
                var status = Status.valueOf(parts[1].trim());
                var duration = Long.parseLong(parts[2].trim());

                allCorrectResults.add(new TestResult(name, status, duration));
            }
            
            catch (NumberFormatException e) {
                incorrectResults.add(new ErrorResult(line, "Длительность не является числом"));
            }

            catch (IllegalArgumentException e) {
                incorrectResults.add(new ErrorResult(line, "Некорректный статус"));
            }

            catch (Exception e) {
                incorrectResults.add(new ErrorResult(line, e.getMessage()));
            }
        }
    }
}