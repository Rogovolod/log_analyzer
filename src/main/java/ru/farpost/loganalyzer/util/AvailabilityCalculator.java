package ru.farpost.loganalyzer.util;

/**
 * Класс для расчета уровня доступности.
 */
public class AvailabilityCalculator {

    /**
     * Рассчитывает уровень доступности.
     *
     * @param totalRequests общее количество запросов
     * @param failedRequests количество неудачных запросов
     * @return уровень доступности в процентах
     */
    public static double calculate(int totalRequests, int failedRequests) {
        return 100.0 * (double) (totalRequests - failedRequests) / totalRequests;
    }

}
