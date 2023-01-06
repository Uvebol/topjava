package ru.javawebinar.topjava.web.meal;

import org.apache.taglibs.standard.tag.el.core.IfTag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static ru.javawebinar.topjava.util.DateTimeUtil.*;

@RestController
@RequestMapping(value = MealRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {

    static final String REST_URL = "/rest/meals";

    @Override
    @RequestMapping
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping("/{id}")
    public Meal get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createWithLocation(@RequestBody Meal meal) {
        Meal created = super.create(meal);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal, @PathVariable int id) {
        super.update(meal, id);
    }

    /*
    @GetMapping(value = "/filter")
    public List<MealTo> getBetweenLocDateTime
    (@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {
        LocalDate localStartDate = startDateTime.toLocalDate();
        LocalTime localStartTime = startDateTime.toLocalTime();
        LocalDate localEndDate = endDateTime.toLocalDate();
        LocalTime localEndTime = endDateTime.toLocalTime();
        return super.getBetween(localStartDate, localStartTime, localEndDate, localEndTime);
    }
    */

    @GetMapping(value = "/filter")
    @ResponseStatus(HttpStatus.OK)
    public List<MealTo> getBetweenLocDateTime(@RequestParam(required = false) LocalDate startDate,
                                              @RequestParam(required = false) LocalTime startTime,
                                              @RequestParam(required = false) LocalDate endDate,
                                              @RequestParam(required = false) LocalTime endTime) {

        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}