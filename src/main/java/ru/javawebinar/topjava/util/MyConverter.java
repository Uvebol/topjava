package ru.javawebinar.topjava.util;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalTime;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

public class MyConverter {

    public static class StringToLocalDate implements Converter<String, LocalDate> {
        @Override
        public LocalDate convert(String source) {
            return parseLocalDate(source);
        }
    }

    public static class StringToLocalTime implements Converter<String, LocalTime>{
        @Override
        public LocalTime convert(String source) {
            return parseLocalTime(source);
        }
    }
}
