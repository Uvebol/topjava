package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, SecurityUtil.authUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            Map<Integer, Meal> value = new ConcurrentHashMap<>();
            value.put(userId, meal);
            repository.put(meal.getId(), value);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.get(meal.getId()).computeIfPresent(userId, (key, value) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        if (repository.get(id).containsKey(userId)) {
            return repository.remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        //return repository.get(SecurityUtil.authUserId()).get(id);
        return repository.get(id).get(SecurityUtil.authUserId());
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        Collection<Meal> value = Collections.synchronizedList(new ArrayList<>());
        synchronized (value) {
            for (Map<Integer, Meal> map : repository.values()) {
                if (map.containsKey(userId)) {
                    value.add(map.get(userId));
                }
            }
            if (value.size() != 0) {
                return value.stream()
                        .sorted((meal_1, meal_2) -> meal_2.getDate().compareTo(meal_1.getDate()))
                        .collect(Collectors.toList());
            }
            return value;
        }
    }
}

