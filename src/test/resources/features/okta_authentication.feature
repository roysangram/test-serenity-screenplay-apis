Feature: Okta API authentication support

  Scenario: Authenticate with Okta and call a protected endpoint
    Given a service consumer
    When I call the endpoint "/todos/1" with method "GET"
    Then the response status code should be 200
    And the response body should contain "title"
