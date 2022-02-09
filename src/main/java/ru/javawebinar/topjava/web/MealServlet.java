package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet implements CrudInterface {
    private static final Logger log = getLogger(MealServlet.class);
    private static List<Meal> mealList = Collections.synchronizedList(MealsUtil.mealsList);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("mealToList", setMealToList());
            request.getRequestDispatcher("meals.jsp").forward(request, response);
        } else {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            switch (action) {
                case ("delete"): {
                    mealList.remove(getListId(mealId));
                    request.setAttribute("mealToList", setMealToList());
                    response.sendRedirect("meals");
                    break;
                }
                case ("update"): {
                    log.debug("redirect to mealsCreUpd");
                    Meal oldMeal = mealList.get(getListId(mealId));
                    request.setAttribute("oldMeal", oldMeal);
                    request.getRequestDispatcher("mealsCreUpd.jsp").forward(request, response);
                    break;
                }
            }
        }
    }

    @Override
    public List<MealTo> setMealToList() {
        return MealsUtil.filteredByStreams(
                mealList,
                LocalTime.MIN,
                LocalTime.MAX,
                MealsUtil.CALORIES_PER_DAY);
    }

    @Override
    public int getListId(int mealId) {
        int count = -1;
        for (Meal meal : mealList) {
            count++;
            if (meal.getId() == mealId) {
                break;
            }
        }
        return count;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("redirect to POST");
        request.setCharacterEncoding("UTF-8");
        String cancel = request.getParameter("cancel");
        if (cancel != null && cancel.equalsIgnoreCase("true")) {
            response.sendRedirect("meals");
        } else {
            String save = request.getParameter("save");
            String dateTime = request.getParameter("datetime");
            String description = request.getParameter("description");
            String calories = request.getParameter("calories");
            if (save != null && !save.equals("")) {
                mealList.set(getListId(Integer.parseInt(save)), new Meal(LocalDateTime.parse(dateTime), description, Integer.parseInt(calories)));
            } else {
                mealList.add(new Meal(LocalDateTime.parse(dateTime), description, Integer.parseInt(calories)));
            }
            response.sendRedirect("meals");
        }
    }
}
