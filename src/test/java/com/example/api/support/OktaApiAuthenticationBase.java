package com.example.api.support;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OktaApiAuthenticationBase {

    private static final Pattern ACCESS_TOKEN_PATTERN = Pattern.compile("\\\"access_token\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"");
    private static final Pattern TOKEN_FROM_JSON_PATTERN = Pattern.compile("\\\"token\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"");

    private final EnvironmentConfig environmentConfig;
    private final TestLogger logger;
    private String accessToken;

    public OktaApiAuthenticationBase() {
        this(EnvironmentConfig.load(), new TestLogger(OktaApiAuthenticationBase.class));
    }

    public OktaApiAuthenticationBase(EnvironmentConfig environmentConfig, TestLogger logger) {
        this.environmentConfig = environmentConfig;
        this.logger = logger;
    }

    public Map<String, String> buildAuthorizationHeaders() {
        Map<String, String> headers = new LinkedHashMap<>();
        if (!environmentConfig.isOktaEnabled()) {
            return headers;
        }

        if (accessToken == null || accessToken.isBlank()) {
            accessToken = acquireAccessToken();
        }

        if (accessToken != null && !accessToken.isBlank()) {
            headers.put("Authorization", "Bearer " + accessToken);
        }
        return headers;
    }

    private String acquireAccessToken() {
        String tokenUrl = environmentConfig.getOktaTokenUrl();
        String clientId = environmentConfig.getOktaClientId();
        String clientSecret = environmentConfig.getOktaClientSecret();
        String scope = environmentConfig.getOktaScope();

        if (tokenUrl.isBlank() || clientId.isBlank() || clientSecret.isBlank()) {
            logger.warn("Okta authentication is enabled but credentials are not fully configured.");
            return null;
        }

        try {
            String body = String.format(
                    "grant_type=client_credentials&scope=%s&client_id=%s&client_secret=%s",
                    URLEncoder.encode(scope, StandardCharsets.UTF_8),
                    URLEncoder.encode(clientId, StandardCharsets.UTF_8),
                    URLEncoder.encode(clientSecret, StandardCharsets.UTF_8)
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(tokenUrl))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("Okta token request completed with status " + response.statusCode());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                Matcher matcher = ACCESS_TOKEN_PATTERN.matcher(response.body());
                if (matcher.find()) {
                    return matcher.group(1);
                }
                Matcher fallbackMatcher = TOKEN_FROM_JSON_PATTERN.matcher(response.body());
                if (fallbackMatcher.find()) {
                    return fallbackMatcher.group(1);
                }
            }
        } catch (Exception exception) {
            logger.warn("Unable to acquire Okta access token: " + exception.getMessage());
        }

        return null;
    }
}
