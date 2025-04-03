package ru.farpost.loganalyzer.util;

import ru.farpost.loganalyzer.model.LogEntry;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Парсер строк лога.
 */
public class LogParser {

    private static final DateTimeFormatter logDateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy:HH:mm:ss");
    private static final DateTimeFormatter outputTimeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Парсит строку лога в объект LogEntry.
     *
     * @param line строка лога
     * @return объект LogEntry или null, если строка некорректна
     */
    public static LogEntry parse(String line) {
        String[] parts = line.split(" ");
        if (parts.length < 11) return null;

        try {
            String rawTimestamp = parts[3].substring(1);
            LocalDateTime dateTime = LocalDateTime.parse(rawTimestamp, logDateTimeFormat);
            String timestamp = dateTime.format(outputTimeFormat);

            int statusCode = Integer.parseInt(parts[8]);
            double responseTime = Double.parseDouble(parts[10]);

            return new LogEntry(timestamp, statusCode, responseTime);
        } catch (Exception e) {
            System.out.println("parse error");
            return null;
        }
    }
}
