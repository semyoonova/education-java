package task1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        
        var logPath = "src/task1/run.log";

        try (var stream = Files.lines(Paths.get(logPath))) {
            
            var lines = stream.collect(Collectors.toList());
            getReport(lines);
        }
        
        catch (IOException e) {
            System.out.println("Ошибка чтения: " + e.getLocalizedMessage());
        }
    }

    static private void getReport(List<String> lines){
        var parser = new TestLogParser();
        parser.parse(lines);

        System.out.println("Результаты: ");
        var correctResults = parser.getCorrectResults();
        correctResults.stream()
            .map(result -> result.name() + " - " + result.status() + " (" + result.duration() + " ms)")
            .forEach(System.out::println);

        System.out.println("Битые строки: ");
        parser.getIncorrectResults().stream()
            .map(result -> result.result() + " - " + result.reason())
            .forEach(System.out::println);
        
        var message = "Первый упавший: " + parser.firstFailed(correctResults)
            .map(result -> result.name() + " - " + result.status() + " (" + result.duration() + " ms)")
            .orElse("Упадений нет");
        
        System.out.println(message);
    }
}