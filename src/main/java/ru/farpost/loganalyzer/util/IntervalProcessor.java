package ru.farpost.loganalyzer.util;

import ru.farpost.loganalyzer.model.Interval;

import java.util.List;

/**
 * Обработчик временных интервалов.
 */
public class IntervalProcessor {

    /**
     * Обрабатывает интервалы с учетом порога доступности.
     *
     * @param intervals список интервалов
     * @param currentTimestamp текущая временная метка
     * @param availability уровень доступности
     * @param threshold минимальный допустимый уровень доступности
     */
    public static void process(List<Interval> intervals, String currentTimestamp, double availability, double threshold) {
        if (availability < threshold) {
            if (!intervals.isEmpty()) {
                Interval lastInterval = intervals.get(intervals.size() - 1);
                if (lastInterval.getAvailability() == availability) {
                    lastInterval.setEnd(currentTimestamp);
                    return;
                }
            }
            intervals.add(new Interval(currentTimestamp, currentTimestamp, availability));
        }
    }

}
