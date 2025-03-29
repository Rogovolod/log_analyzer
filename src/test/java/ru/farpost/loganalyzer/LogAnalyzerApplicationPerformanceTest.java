package ru.farpost.loganalyzer;

import org.junit.jupiter.api.Test;
import ru.farpost.loganalyzer.service.LogAnalyzerService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LogAnalyzerApplicationPerformanceTest {

    @Test
    void testPerformanceWithLargeLogFile() throws IOException {
        StringBuilder largeLog = new StringBuilder();
        for (int i = 0; i < 10_000_000; i++) {
            largeLog.append("192.168.32.181 - - [14/06/2017:13:32:26 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" ")
                    .append((i % 10 == 0 ? "500" : "200")).append(" 2 ")
                    .append((i % 10 == 0 ? "50.0" : "20.0")).append(" \"-\" \"@list-item-updater\" prio:0\n");
        }

        File tempFile = File.createTempFile("large-log", ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(largeLog.toString());
        }

        Instant start = Instant.now();

        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
            LogAnalyzerService.analyzeLog(reader, 99.0, 45.0);
        }

        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);

        System.out.println("Время обработки 10 миллионов записей: " + duration.toMillis() + " мс");

        assertTrue(duration.toSeconds() < 10, "Обработка заняла слишком много времени");
    }
}