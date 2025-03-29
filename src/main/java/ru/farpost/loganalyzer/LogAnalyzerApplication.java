package ru.farpost.loganalyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static ru.farpost.loganalyzer.service.LogAnalyzerService.analyzeLog;

/**
 * Анализатор логов.
 */
public class LogAnalyzerApplication {

    /**
     * Точка входа в программу.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        double availabilityThreshold = 99.9;
        double maxResponseTime = 45.0;
        String logFilePath = null;

        for (int i = 0; i < args.length; i++) {
            if ("-u".equals(args[i]) && i + 1 < args.length) {
                availabilityThreshold = Double.parseDouble(args[i + 1]);
            } else if ("-t".equals(args[i]) && i + 1 < args.length) {
                maxResponseTime = Double.parseDouble(args[i + 1]);
            } else if ("-f".equals(args[i]) && i + 1 < args.length) {
                logFilePath = args[i + 1];
            }
        }

        try (BufferedReader reader = (logFilePath != null)
                ? new BufferedReader(new FileReader(logFilePath))
                : new BufferedReader(new InputStreamReader(System.in))) {
            analyzeLog(reader, availabilityThreshold, maxResponseTime);
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
    }

}