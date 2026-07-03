Feature: Public async API sample

  Scenario: Call a public synchronous endpoint and validate a response
    Given a service consumer
    When I call the endpoint "/anything" with method "GET" using base URL "https://httpbin.org"
    Then the response status code should be 200
    And the response body should contain "https://httpbin.org/anything"

  Scenario: Call a public delayed endpoint and validate a response
    Given a service consumer
    When I call the endpoint "/delay/2" with method "GET" using base URL "https://httpbin.org"
    Then the response status code should be 200
    And the response body should contain "delay"
