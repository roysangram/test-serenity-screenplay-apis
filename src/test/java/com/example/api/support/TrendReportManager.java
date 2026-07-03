package com.example.api.support;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TrendReportManager {

    private final Path reportDirectory;

    public TrendReportManager() {
        this(Paths.get("target", "reports"));
    }

    public TrendReportManager(Path reportDirectory) {
        this.reportDirectory = reportDirectory;
    }

    public void recordScenario(String environment, String scenarioName, String status, long durationMillis) {
        try {
            Files.createDirectories(reportDirectory);
            Path historyFile = reportDirectory.resolve("trend-history.jsonl");
            Path summaryFile = reportDirectory.resolve("trend-summary.json");
            Path htmlFile = reportDirectory.resolve("trend-report.html");

            String entry = String.format(
                    "{\"timestamp\":\"%s\",\"environment\":\"%s\",\"scenario\":\"%s\",\"status\":\"%s\",\"durationMs\":%d}\n",
                    Instant.now(),
                    environment,
                    scenarioName.replace('"', '\''),
                    status,
                    durationMillis
            );
            Files.writeString(historyFile, entry, StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND);

            List<String> lines = Files.exists(historyFile) ? Files.readAllLines(historyFile) : new ArrayList<>();
            long passed = lines.stream().filter(line -> line.contains("\"status\":\"PASSED\"")).count();
            long failed = lines.stream().filter(line -> line.contains("\"status\":\"FAILED\"")).count();
            long skipped = lines.stream().filter(line -> line.contains("\"status\":\"SKIPPED\"")).count();

            Map<String, Object> summary = new LinkedHashMap<>();
            summary.put("environment", environment);
            summary.put("totalRuns", lines.size());
            summary.put("passed", passed);
            summary.put("failed", failed);
            summary.put("skipped", skipped);
            summary.put("lastUpdated", Instant.now().toString());

            Files.writeString(summaryFile, summary.toString().replace("=", ":") + System.lineSeparator(), StandardCharsets.UTF_8);

            String html = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                      <meta charset=\"UTF-8\"/>
                      <title>Test Trend Report</title>
                    </head>
                    <body>
                      <h1>Test Trend Report</h1>
                      <p>Environment: %s</p>
                      <p>Total runs: %d</p>
                      <p>Passed: %d</p>
                      <p>Failed: %d</p>
                      <p>Skipped: %d</p>
                    </body>
                    </html>
                    """.formatted(environment, lines.size(), passed, failed, skipped);
            Files.writeString(htmlFile, html, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to write trend report", exception);
        }
    }
}
