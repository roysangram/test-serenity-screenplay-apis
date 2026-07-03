package com.example.api.support;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;

public class TrendReportManagerTest {

    @Test
    public void shouldCreateSummaryAndHtmlReports() throws IOException {
        Path tempDir = Files.createTempDirectory("trend-report");

        try {
            TrendReportManager manager = new TrendReportManager(tempDir);
            manager.recordScenario("qa", "Sample scenario", "PASSED", 120L);

            Assert.assertTrue(Files.exists(tempDir.resolve("trend-summary.json")));
            Assert.assertTrue(Files.exists(tempDir.resolve("trend-report.html")));

            String html = Files.readString(tempDir.resolve("trend-report.html"));
            Assert.assertTrue(html.contains("Test Trend Report"));
        } finally {
            deleteRecursively(tempDir);
        }
    }

    private void deleteRecursively(Path path) throws IOException {
        if (path == null || !Files.exists(path)) {
            return;
        }
        if (Files.isDirectory(path)) {
            for (Path child : Files.list(path).toList()) {
                deleteRecursively(child);
            }
        }
        Files.deleteIfExists(path);
    }
}
