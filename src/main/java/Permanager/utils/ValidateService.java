package Permanager.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.TimeZone;

/**
 * Валидация: Проверка строки на необходимое значение
 */
public class ValidateService {

    /**
     * Валидация числа и конвертация строки в число (Integer)
     *
     * @param strInteger Строка с числом
     * @return Integer
     */
    public Optional<Integer> isValidInteger(String strInteger) {
        try {
            return Optional.of(Integer.parseInt(strInteger));
        } catch (Exception err) {
            return Optional.empty();
        }
    }

    /**
     * Валидация числа и конвертация строки в число (Long)
     *
     * @param strLong Строка с числом (Long)
     * @return Long
     */
    public Optional<Long> isValidLong(String strLong) {
        try {
            return Optional.of(Long.parseLong(strLong));
        } catch (Exception err) {
            return Optional.empty();
        }
    }


    /**
     * Валидация даты и конвертация строки в дату
     *
     * @param strLocalDate Строка с датой
     * @return LocalDate
     */
    public Optional<LocalDate> isValidDate(String strLocalDate) {

        String[] patterns = {
                "HH:mm dd.MM.yyyy",
                "HH:mm:ss dd.MM.yyyy",
                "HH:mm dd.MM.yy",
                "HH:mm:ss dd.MM.yy",
                "dd.MM.yyyy HH:mm",
                "dd.MM.yyyy HH:mm:ss",
                "dd.MM.yy HH:mm",
                "dd.MM.yy HH:mm:ss"
        };

        // Проходимся по каждому форматы дат
        for (String pattern : patterns) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                return Optional.of(LocalDate.parse(strLocalDate, formatter));
            } catch (Exception err) {
                // The exception is ignored for a specific reason
            }
        }
        return Optional.empty();
    }


    /**
     * Валидация времени и конвертация строки на время
     *
     * @param strLocalTime Строка со временем
     * @return LocalDate
     */
    public Optional<LocalDate> isValidTime(String strLocalTime) {
        String[] patterns = {
                "HH:mm dd.MM.yyyy",
                "HH:mm:ss dd.MM.yyyy",
                "HH:mm:ss dd.MM.yyyy",
                "HH:mm dd.MM.yy",
                "HH:mm:ss dd.MM.yy",
                "dd.MM.yyyy HH:mm",
                "dd.MM.yyyy HH:mm:ss",
                "dd.MM.yy HH:mm",
                "dd.MM.yy HH:mm:ss"
        };

        // Проходимся по каждому форматы дат
        for (String pattern : patterns) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                return Optional.of(LocalDate.parse(strLocalTime, formatter));
            } catch (Exception err) {
                break;
            }
        }
        return Optional.empty();
    }


    /**
     * Валидация часового пояса и конвертация строки в объект
     *
     * @param strTimeZone Строка с часовым поясом
     * @return TimeZone
     */
    public Optional<TimeZone> isValidTimeZone(String strTimeZone) {
        try {
            return Optional.of(TimeZone.getTimeZone(strTimeZone));
        } catch (Exception err) {
            return Optional.empty();
        }
    }

    private String formatterTimeZone(String timeZone) {
        if (!timeZone.contains("/")) {
            return timeZone;
        }

        String[] parts = timeZone.split("/");
        return parts[0].substring(0, 1).toUpperCase() + parts[0].substring(1).toLowerCase()
                + "/" + parts[1].substring(0, 1).toUpperCase() + parts[1].substring(1).toLowerCase();
    }
}
