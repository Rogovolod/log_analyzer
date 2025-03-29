package ru.farpost.loganalyzer.service;

import ru.farpost.loganalyzer.model.Interval;
import ru.farpost.loganalyzer.model.LogEntry;
import ru.farpost.loganalyzer.util.AvailabilityCalculator;
import ru.farpost.loganalyzer.util.IntervalProcessor;
import ru.farpost.loganalyzer.util.LogParser;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Сервис для анализа лога.
 */
public class LogAnalyzerService {

    /**
     * Выполняет анализ лога.
     *
     * @param reader поток для чтения лога
     * @param availabilityThreshold минимальный допустимый уровень доступности
     * @param maxResponseTime максимальное допустимое время ответа
     */
    public static void analyzeLog(BufferedReader reader, double availabilityThreshold, double maxResponseTime) {
        try {
            String line;
            String currentTimestamp = null;
            int totalRequests = 0;
            int failedRequests = 0;
            List<Interval> intervals = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                LogEntry entry = LogParser.parse(line);
                if (entry == null) continue;

                boolean isFailure = (entry.getStatusCode() >= 500 && entry.getStatusCode() < 600) || entry.getResponseTime() > maxResponseTime;

                if (!entry.getTimestamp().equals(currentTimestamp)) {
                    if (currentTimestamp != null && totalRequests > 0) {
                        double availability = AvailabilityCalculator.calculate(totalRequests, failedRequests);
                        IntervalProcessor.process(intervals, currentTimestamp, availability, availabilityThreshold);
                    }
                    currentTimestamp = entry.getTimestamp();
                    totalRequests = 0;
                    failedRequests = 0;
                }

                totalRequests++;
                if (isFailure) failedRequests++;
            }

            if (currentTimestamp != null && totalRequests > 0) {
                double availability = AvailabilityCalculator.calculate(totalRequests, failedRequests);
                IntervalProcessor.process(intervals, currentTimestamp, availability, availabilityThreshold);
            }

            intervals.forEach(interval -> System.out.printf(Locale.US, "%s %s %.1f%%\n", interval.getStart(), interval.getEnd(), interval.getAvailability()));
        } catch (Exception e) {
            System.err.println("Ошибка обработки лога: " + e.getMessage());
        }
    }

}
