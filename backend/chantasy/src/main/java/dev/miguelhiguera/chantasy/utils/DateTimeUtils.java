package dev.miguelhiguera.chantasy.utils;

import java.time.LocalDateTime;

public class DateTimeUtils {

    public static LocalDateTime dateStringToLocalDateTime(String date) {
        return LocalDateTime.parse(date);
    }
}
