package app;

import org.junit.jupiter.api.Test;

import app.App;

import static org.junit.jupiter.api.Assertions.*;

public class Apptest {
    private final App app = new App();

    @Test
    void greetWithNormalName() {
        assertEquals("Привет, Анна!", app.greet("Анна"));
    }

    @Test
    void greetWithEmptyString() {
        assertEquals("Привет!", app.greet(""));
    }

    @Test
    void greetWithNull() {
        assertEquals("Привет!", app.greet(null));
    }
}
