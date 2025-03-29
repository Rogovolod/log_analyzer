package ru.farpost.loganalyzer;

import ru.farpost.loganalyzer.model.Interval;
import ru.farpost.loganalyzer.model.LogEntry;
import org.junit.jupiter.api.Test;
import ru.farpost.loganalyzer.util.AvailabilityCalculator;
import ru.farpost.loganalyzer.util.IntervalProcessor;
import ru.farpost.loganalyzer.service.LogAnalyzerService;
import ru.farpost.loganalyzer.util.LogParser;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class LogAnalyzerApplicationTest {

    @Test
    void testParseLogLine() {
        String logLine = "192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 44.510983 \"-\" \"@list-item-updater\" prio:0";
        LogEntry entry = LogParser.parse(logLine);

        assertNotNull(entry);
        assertEquals("16:47:02", entry.getTimestamp());
        assertEquals(200, entry.getStatusCode());
        assertEquals(44.510983, entry.getResponseTime(), 0.0001);
    }

    @Test
    void testCalculateAvailability() {
        int totalRequests = 100;
        int failedRequests = 5;

        double availability = AvailabilityCalculator.calculate(totalRequests, failedRequests);
        assertEquals(95.0, availability, 0.0001);
    }

    @Test
    void testProcessInterval() {
        List<Interval> intervals = new ArrayList<>();
        IntervalProcessor.process(intervals, "13:32:26", 94.5, 99.9);

        assertEquals(1, intervals.size());
        assertEquals("13:32:26", intervals.get(0).getStart());
        assertEquals("13:32:26", intervals.get(0).getEnd());
        assertEquals(94.5, intervals.get(0).getAvailability(), 0.0001);
    }

    @Test
    void testAnalyzeLog() {
        String logData = """
            192.168.32.181 - - [14/06/2017:13:32:26 +1000] "PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1" 500 2 44.510983 "-" "@list-item-updater" prio:0
            192.168.32.181 - - [14/06/2017:13:32:26 +1000] "PUT /rest/v1.4/documents?zone=default&_rid=7ae28555 HTTP/1.1" 200 2 23.251219 "-" "@list-item-updater" prio:0
            192.168.32.181 - - [14/06/2017:13:33:15 +1000] "PUT /rest/v1.4/documents?zone=default&_rid=e356713 HTTP/1.1" 503 2 30.164372 "-" "@list-item-updater" prio:0
            """;

        BufferedReader reader = new BufferedReader(new StringReader(logData));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        LogAnalyzerService.analyzeLog(reader, 99.0, 45.0);

        String output = outputStream.toString().trim();
        String[] lines = output.split("\n");

        assertEquals(2, lines.length);
        assertTrue(lines[0].contains("13:32:26 13:32:26 50.0%"));
        assertTrue(lines[1].contains("13:33:15 13:33:15 0.0%"));
    }
}