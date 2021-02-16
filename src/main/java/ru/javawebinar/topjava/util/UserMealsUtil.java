package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> mapCaloriesByDay = new HashMap<>();

        for (UserMeal um : meals) {
            mapCaloriesByDay.merge(um.getDate(), um.getCalories(), Integer::sum);
        }

        List<UserMealWithExcess> umExcessList = new ArrayList<>();
        for (UserMeal um : meals) {
            if (TimeUtil.isBetweenHalfOpen(um.getTime(), startTime, endTime)) {
                umExcessList.add(new UserMealWithExcess(um, mapCaloriesByDay.get(um.getDate()) > caloriesPerDay));
            }
        }
        return umExcessList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> mapCaloriesByDay = meals.stream().collect(Collectors.toMap(UserMeal::getDate, UserMeal::getCalories, Integer::sum));

        return meals.stream()
       .filter(um -> TimeUtil.isBetweenHalfOpen(um.getTime(), startTime, endTime))
       .map(um -> new UserMealWithExcess(um, mapCaloriesByDay.get(um.getDate()) > caloriesPerDay))
       .collect(Collectors.toList());
    }
}
