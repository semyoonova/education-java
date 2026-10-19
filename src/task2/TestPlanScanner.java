package task2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

record TestStep(String id, String methodName, int order) {
    public int getOrder() {return order;}
    public String getId() {return id;}
    public String getName() {return methodName;}
};

public class TestPlanScanner { 
    static void buildPlan(Class<?> clazz){
        var methods = Arrays.stream(clazz.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(TestId.class));

        var steps = methods
            .map(method -> {
                var annotation = method.getAnnotation(TestId.class);
                var id = annotation.value();
                var name = method.getName();
                var order = annotation.order();
                return new TestStep(id, name, order);
            })
            .sorted(Comparator.comparingInt(TestStep::getOrder));
        
        var formatedPlan = formatPlan(steps);
        printPlan(clazz.getName(), formatedPlan);
    }

    private static Stream<String> formatPlan(Stream<TestStep> steps){
        return steps
            .map(step -> String.format("%d. %s -> %s", step.getOrder(), step.getId(), step.getName()));
    }

    private static void printPlan(String clazzName, Stream<String> steps){
        System.out.println(String.format("План прогона для %s:", clazzName));
        var stepsList = steps.toList();
        if (stepsList.isEmpty()){
            System.out.println("Кейсов нет");
        }
        else{
            stepsList.forEach(System.out::println);
        };
    }
}