# Serenity Screenplay Cucumber API Test Framework

This project provides a sample enterprise-style API automation framework using:
- Java 17
- Maven
- Serenity BDD
- Screenplay pattern
- Cucumber
- RestAssured

## Run the tests

```bash
mvn test
```

## Sample scenarios

The framework includes:
- a basic GET sample scenario
- CRUD scenarios for POST, PUT, PATCH, and DELETE
- an Okta-authentication support scaffold for token-based API access
- public endpoint samples that demonstrate synchronous and delayed/async-style requests using httpbin.org

You can switch environments by updating the properties in:
- src/test/resources/environments/default.properties
- src/test/resources/environments/qa.properties

## Report location

After the run, Serenity reports are available under:

```bash
target/site/serenity/index.html
```

The generated Cucumber HTML report is also available at:

```bash
target/cucumber-report.html
```
