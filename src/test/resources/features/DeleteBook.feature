Feature: User Delete Book

  Scenario: User successfully deletes a book
    Given The API server is running
    And User is authenticated for delete operations
    When User deletes a book with ID 1
    Then User should receive a successful response with status code 200

  Scenario: User tries to delete a book that does not exist
    Given The API server is running
    And User is authenticated for delete operations
    When User deletes a non-existent book with ID 999
    Then User should receive a failed response with status code 404 and error message "Book not found" for operation

  Scenario: Admin tries to delete a book without proper authentication
    Given The API server is running
    And Admin is authenticated for delete operations
    When Admin deletes a book with ID 1 without proper authentication
    Then Admin should receive a failed response with status code 403 and error message "User not permitted" for operation
