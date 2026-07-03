Feature: Sample CRUD API scenarios

  Scenario: Create a resource with POST
    Given a service consumer
    When I call the endpoint "/todos" with method "POST" and body "{\"title\":\"test task\",\"completed\":false}"
    Then the response status code should be 201

  Scenario: Update a resource with PUT
    Given a service consumer
    When I call the endpoint "/todos/1" with method "PUT" and body "{\"id\":1,\"title\":\"updated task\",\"completed\":true}"
    Then the response status code should be 200

  Scenario: Update a resource partially with PATCH
    Given a service consumer
    When I call the endpoint "/todos/1" with method "PATCH" and body "{\"completed\":true}"
    Then the response status code should be 200

  Scenario: Delete a resource with DELETE
    Given a service consumer
    When I call the endpoint "/todos/1" with method "DELETE"
    Then the response status code should be 200
