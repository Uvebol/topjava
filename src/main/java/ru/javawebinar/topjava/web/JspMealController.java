package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    private MealService service;

    private final int userId = SecurityUtil.authUserId();

    @GetMapping("")
    public String getAll(Model model) {
        log.info("get Meals");
        model.addAttribute("meals", MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    @GetMapping(value = "", params = {"action", "id"})
    public String switchJsp(HttpServletRequest request, Model model) {
        log.info("switch JSP");
        String action = request.getParameter("action");
        if (action.equals("delete")){
            return delete(request);
        }else if (action.equals("update")){
            log.info("get Meal for update");
            model.addAttribute("meal", service.get(Integer.parseInt(request.getParameter("id")), userId));
            return "mealForm";
        }else if(action.equals("create")){
            log.info("create Meal");
            return "mealForm";
        }
        return "meals";
    }
    @GetMapping(value = "", params = {"action"})
    public String create(HttpServletRequest request, Model model){
        log.info("create Meal");
        if (request.getParameter("action").equals("create")){
            model.addAttribute(new Meal());
            return "mealForm";
        }
        return "";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String update(HttpServletRequest request) throws UnsupportedEncodingException {
        log.info("update Meal");
        Meal meal;
        String id = request.getParameter("id");
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        LocalDateTime localDateTime = LocalDateTime.parse(request.getParameter("dateTime"));

        if (id.equals("")){
            meal = new Meal(localDateTime, description, calories);
            service.create(meal, userId);
        }else {
            meal = service.get(Integer.parseInt(id), userId);
            meal.setDescription(description);
            meal.setCalories(calories);
            meal.setDateTime(localDateTime);
            service.update(meal, userId);
        }
        return "redirect:meals";
    }

    public String delete(HttpServletRequest request){
        log.info("delete Meal");
        service.delete(Integer.parseInt(request.getParameter("id")), userId);
        return "redirect:meals";
    }

    @GetMapping(value = "", params = {"action", "startDate", "endDate", "startTime", "endTime"})
    public String filter(HttpServletRequest request, Model model){
        log.info("filter Meal");
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        List<Meal> mealsList = service.getBetweenInclusive(startDate, endDate, userId);
        List<MealTo> result = MealsUtil.getFilteredTos(mealsList, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
        model.addAttribute("meals", result);
        return "meals";
    }
}
