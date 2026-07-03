package com.example.api.support;

import org.junit.Assert;
import org.junit.Test;

public class EnvironmentConfigTest {

    @Test
    public void shouldResolveEnvironmentSpecificProperties() {
        System.setProperty("test.environment", "qa");

        EnvironmentConfig config = EnvironmentConfig.load();

        Assert.assertEquals("qa", config.getEnvironment());
        Assert.assertEquals("https://qa.example.com", config.getApiBaseUrl());
        Assert.assertTrue(config.isOktaEnabled());

        System.clearProperty("test.environment");
    }
}
