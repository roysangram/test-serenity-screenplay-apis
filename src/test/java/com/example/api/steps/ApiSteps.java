package com.example.api.steps;

import org.junit.Assert;

import com.example.api.support.EnvironmentConfig;
import com.example.api.support.TestLogger;
import com.example.api.support.TrendReportManager;
import com.example.api.tasks.CallApi;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.questions.LastResponse;
import net.serenitybdd.screenplay.rest.questions.TheResponse;

public class ApiSteps {

    private final EnvironmentConfig environmentConfig = EnvironmentConfig.load();
    private final TestLogger logger = new TestLogger(ApiSteps.class);
    private final TrendReportManager trendReportManager = new TrendReportManager();
    private Actor actor;

    @Before
    public void setTheStage() {
        OnStage.setTheStage(new OnlineCast());
        logger.info("Running against environment: " + environmentConfig.getEnvironment());
        logger.info("API base URL: " + environmentConfig.getApiBaseUrl());
    }

    @Given("a service consumer")
    public void aServiceConsumer() {
        actor = OnStage.theActorCalled("API Consumer");
    }

    @When("I call the endpoint {string} with method {string}")
    public void iCallTheEndpointWithMethod(String path, String method) {
        actor.attemptsTo(CallApi.with(method, path, "{}"));
    }

    @When("I call the endpoint {string} with method {string} using base URL {string}")
    public void iCallTheEndpointWithMethodAndBaseUrl(String path, String method, String baseUrl) {
        actor.attemptsTo(CallApi.with(method, path, "{}", baseUrl));
    }

    @When("I call the endpoint {string} with method {string} and body {string}")
    public void iCallTheEndpointWithMethodAndBody(String path, String method, String body) {
        actor.attemptsTo(CallApi.with(method, path, body));
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatus) {
        int actualStatus = actor.asksFor(TheResponse.statusCode());
        logger.logResponse(actualStatus, actor.asksFor(LastResponse.received()).getBody().asString());
        Assert.assertEquals(expectedStatus, actualStatus);
        trendReportManager.recordScenario(environmentConfig.getEnvironment(), "API status validation", actualStatus == expectedStatus ? "PASSED" : "FAILED", 0L);
    }

    @Then("the response body should contain {string}")
    public void theResponseBodyShouldContain(String expectedText) {
        String body = actor.asksFor(LastResponse.received()).getBody().asString();
        logger.info("Response body contains expected text: " + body.contains(expectedText));
        Assert.assertTrue(body.contains(expectedText));
        trendReportManager.recordScenario(environmentConfig.getEnvironment(), "API body validation", body.contains(expectedText) ? "PASSED" : "FAILED", 0L);
    }
}
