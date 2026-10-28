package app;

import java.io.InputStream;
import java.util.Properties;

public class App {
    public String greet(String name){
        if (name == "" || name == null){
            return "Привет!";
        }
        return String.format("Привет, %s!", name);
    }

    public static void main(String[] args) {
        App app = new App();

        try (InputStream input = App.class.getResourceAsStream("/app.properties")) {
            if (input == null) {
                throw new IllegalStateException("app.properties не найден как ресурс");
            }
            Properties properties = new Properties();
            properties.load(input);
            String name = properties.getProperty("student.name", "неизвестный");
            System.out.println(app.greet(name));
        } catch (Exception e) {
            // Для учебного проекта достаточно вывести ошибку
            e.printStackTrace();
        }
    }
}