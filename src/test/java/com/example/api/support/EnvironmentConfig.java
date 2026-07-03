package com.example.api.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

public class EnvironmentConfig {

    private static final String DEFAULT_ENVIRONMENT = "default";

    private final String environment;
    private final Properties properties;

    private EnvironmentConfig(String environment, Properties properties) {
        this.environment = environment;
        this.properties = properties;
    }

    public static EnvironmentConfig load() {
        String environment = resolveEnvironment();
        Properties properties = new Properties();

        for (String resourcePath : new String[]{
                "/environments/" + environment + ".properties",
                "/environments/default.properties"
        }) {
            try (InputStream stream = EnvironmentConfig.class.getResourceAsStream(resourcePath)) {
                if (stream != null) {
                    properties.load(stream);
                    break;
                }
            } catch (IOException ignored) {
                // fall back to the next candidate
            }
        }

        return new EnvironmentConfig(environment, properties);
    }

    public String getEnvironment() {
        return environment;
    }

    public String getApiBaseUrl() {
        return getProperty("api.base.url", "https://jsonplaceholder.typicode.com");
    }

    public boolean isOktaEnabled() {
        return Boolean.parseBoolean(getProperty("okta.enabled", "false"));
    }

    public String getOktaTokenUrl() {
        return getProperty("okta.token.url", "");
    }

    public String getOktaClientId() {
        return getProperty("okta.client.id", "");
    }

    public String getOktaClientSecret() {
        return getProperty("okta.client.secret", "");
    }

    public String getOktaScope() {
        return getProperty("okta.scope", "api://default/.default");
    }

    private String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    private static String resolveEnvironment() {
        String environment = System.getProperty("test.environment");
        if (environment == null || environment.isBlank()) {
            environment = System.getProperty("serenity.env");
        }
        if (environment == null || environment.isBlank()) {
            environment = System.getenv("TEST_ENVIRONMENT");
        }
        if (environment == null || environment.isBlank()) {
            environment = DEFAULT_ENVIRONMENT;
        }
        return environment.toLowerCase(Locale.ROOT);
    }
}
