package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import org.springframework.dao.DuplicateKeyException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MEAL_ID, USER_ID);
        assertThat(meal).usingRecursiveComparison().isEqualTo(MEALS.get(0));
    }

    @Test
    public void getNotFoundException() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, UserTestData.NOT_FOUND));
    }

    @Test
    public void delete() {
        service.delete(MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, USER_ID));
    }

    @Test
    public void deleteNotFoundException() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID, UserTestData.NOT_FOUND));
    }

    @Test
    public void getBetweenInclusive() {
        LocalDate start = LocalDate.of(2020, 1, 30);
        LocalDate end = LocalDate.of(2020, 1, 30);
        List<Meal> defaultsValue = Arrays.asList(MEALS.get(4), MEALS.get(5), MEALS.get(6));
        List<Meal> filteredMeals = service.getBetweenInclusive(start, end, USER_ID);
        assertThat(defaultsValue).usingRecursiveFieldByFieldElementComparator().isEqualTo(filteredMeals);
    }

    @Test
    public void getAll() {
        List<Meal> list = service.getAll(USER_ID);
        assertThat(list).usingRecursiveFieldByFieldElementComparator().isEqualTo(MEALS);
    }

    @Test
    public void update() {
        Meal mealUpd = new Meal(MEALS.get(0));
        mealUpd.setDateTime(LocalDateTime.of(3000, Calendar.OCTOBER, 5, 21, 22));
        mealUpd.setDescription("Update Test Meal");
        mealUpd.setCalories(777);

        service.update(mealUpd, USER_ID);
        assertThat(mealUpd).usingRecursiveComparison().isEqualTo(service.get(mealUpd.getId(), USER_ID));
    }

    @Test
    public void updateNotFoundException() {
        assertThrows(NotFoundException.class, () -> service.update(MEALS.get(0), UserTestData.ADMIN_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(MealTestData.getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = MealTestData.getNew();
        newMeal.setId(newId);
        assertThat(created).usingRecursiveComparison().isEqualTo(newMeal);
        assertThat(created).usingRecursiveComparison().isEqualTo(service.get(newId, USER_ID));
    }

    @Test
    public void duplicateDateTimeCreate() {
        Meal meal = new Meal(MEALS.get(0).getDateTime(), "Duplicate DateTime create, Meal", 15);
        assertThrows(DuplicateKeyException.class, () -> service.create(meal, USER_ID));
    }
}