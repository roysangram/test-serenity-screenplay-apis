package com.example.api.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLogger {

    private final Logger logger;

    public TestLogger(Class<?> type) {
        this.logger = LoggerFactory.getLogger(type);
    }

    public void info(String message) {
        logger.info("[TEST] {}", message);
    }

    public void warn(String message) {
        logger.warn("[TEST] {}", message);
    }

    public void error(String message, Throwable throwable) {
        logger.error("[TEST] {}", message, throwable);
    }

    public void logRequest(String method, String url) {
        info("Request: " + method + " " + url);
    }

    public void logResponse(int statusCode, String body) {
        info("Response status: " + statusCode + " body: " + body);
    }
}
