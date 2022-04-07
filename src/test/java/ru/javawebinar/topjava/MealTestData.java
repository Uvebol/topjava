package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class MealTestData {
    public static final int MEAL_ID = 200_006;
    public static final int MEAL_NOT_FOUND = 500_000;

    public static final List<Meal> MEALS = Arrays.asList(
        new Meal(200_006, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410),
        new Meal(200_005, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
        new Meal(200_004, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
        new Meal(200_003, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
        new Meal(200_002, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
        new Meal(200_001, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
        new Meal(200_000, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500)
    );

    public static Meal getNew() {
        return new Meal(null,
                LocalDateTime.of(1999, Month.DECEMBER, 1, 15, 15),
                "Завтрак NEW",
                500);
    }
}
