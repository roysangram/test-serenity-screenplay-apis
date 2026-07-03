package com.example.api.tasks;

import java.util.Map;

import com.example.api.support.EnvironmentConfig;
import com.example.api.support.OktaApiAuthenticationBase;
import com.example.api.support.TestLogger;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Delete;
import net.serenitybdd.screenplay.rest.interactions.Get;
import net.serenitybdd.screenplay.rest.interactions.Patch;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.serenitybdd.screenplay.rest.interactions.Put;

public class CallApi implements Task {
    private final String method;
    private final String path;
    private final String body;
    private final String baseUrl;
    private final EnvironmentConfig environmentConfig;
    private final TestLogger logger;
    private final OktaApiAuthenticationBase authBase;

    public CallApi(String method, String path, String body) {
        this(method, path, body, null, EnvironmentConfig.load(), new TestLogger(CallApi.class), new OktaApiAuthenticationBase());
    }

    public CallApi(String method, String path, String body, String baseUrl) {
        this(method, path, body, baseUrl, EnvironmentConfig.load(), new TestLogger(CallApi.class), new OktaApiAuthenticationBase());
    }

    public CallApi(String method, String path, String body, String baseUrl, EnvironmentConfig environmentConfig, TestLogger logger, OktaApiAuthenticationBase authBase) {
        this.method = method;
        this.path = path;
        this.body = body;
        this.baseUrl = baseUrl;
        this.environmentConfig = environmentConfig;
        this.logger = logger;
        this.authBase = authBase;
    }

    public static CallApi with(String method, String path, String body) {
        return instrumented(CallApi.class, method, path, body);
    }

    public static CallApi with(String method, String path, String body, String baseUrl) {
        return instrumented(CallApi.class, method, path, body, baseUrl);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String resolvedBaseUrl = (baseUrl != null && !baseUrl.isBlank()) ? baseUrl : environmentConfig.getApiBaseUrl();
        logger.logRequest(method, resolvedBaseUrl + path);
        actor.can(CallAnApi.at(resolvedBaseUrl));

        Map<String, String> authHeaders = authBase.buildAuthorizationHeaders();
        if ("GET".equalsIgnoreCase(method)) {
            actor.attemptsTo(Get.resource(path).with(request -> {
                for (Map.Entry<String, String> entry : authHeaders.entrySet()) {
                    request.header(entry.getKey(), entry.getValue());
                }
                return request;
            }));
        } else if ("POST".equalsIgnoreCase(method)) {
            actor.attemptsTo(Post.to(path).with(request -> {
                request.header("Content-Type", "application/json");
                for (Map.Entry<String, String> entry : authHeaders.entrySet()) {
                    request.header(entry.getKey(), entry.getValue());
                }
                request.body(body);
                return request;
            }));
        } else if ("PUT".equalsIgnoreCase(method)) {
            actor.attemptsTo(Put.to(path).with(request -> {
                request.header("Content-Type", "application/json");
                for (Map.Entry<String, String> entry : authHeaders.entrySet()) {
                    request.header(entry.getKey(), entry.getValue());
                }
                request.body(body);
                return request;
            }));
        } else if ("PATCH".equalsIgnoreCase(method)) {
            actor.attemptsTo(Patch.to(path).with(request -> {
                request.header("Content-Type", "application/json");
                for (Map.Entry<String, String> entry : authHeaders.entrySet()) {
                    request.header(entry.getKey(), entry.getValue());
                }
                request.body(body);
                return request;
            }));
        } else if ("DELETE".equalsIgnoreCase(method)) {
            actor.attemptsTo(Delete.from(path).with(request -> {
                for (Map.Entry<String, String> entry : authHeaders.entrySet()) {
                    request.header(entry.getKey(), entry.getValue());
                }
                return request;
            }));
        }
    }
}
