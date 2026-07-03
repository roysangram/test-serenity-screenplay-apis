Feature: Sample API testing with Serenity Screenplay

  Scenario Outline: Retrieve a resource successfully
    Given a service consumer
    When I call the endpoint "<endpoint>" with method "GET"
    Then the response status code should be 200
    And the response body should contain "title"

    Examples:
      | endpoint |
      | /todos/1 |
      | /todos/2 |
      | /todos/3 |

