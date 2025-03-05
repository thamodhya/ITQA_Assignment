package additional;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UpdateAndDeleteBookSteps {

    private Response lastResponse;
    private String currentUsername;
    private String currentPassword;
    private final CommonValidationSteps commonValidationSteps;

    public UpdateAndDeleteBookSteps(CommonValidationSteps commonValidationSteps) {
        this.commonValidationSteps = commonValidationSteps;
    }

    // Common Steps
    @Given("Admin has the base URI set to {string}")
    public void adminHasTheBaseURISetTo(String baseUri) {
        RestAssured.baseURI = baseUri;
    }

    @Given("Admin authenticate as {string} with password {string}")
    public void adminAuthenticatesAs(String username, String password) {
        currentUsername = username;
        currentPassword = password;
    }

    // Update Book Test Cases

    @When("Admin updates a book with ID {int}, title {string}, and author {string}")
    public void adminUpdatesABook(int id, String title, String author) {
        String bookData = String.format("{ \"title\": \"%s\", \"author\": \"%s\" }", title, author);
        lastResponse = given()
                .auth().basic(currentUsername, currentPassword)
                .header("Content-Type", "application/json")
                .body(bookData)
                .when()
                .put("/api/books/" + id);

        commonValidationSteps.setLastResponse(lastResponse);
    }

    @When("Admin tries to update a book with mismatched ID {int}, title {string}, and author {string}")
    public void adminUpdatesBookWithMismatchedID(int id, String title, String author) {
        adminUpdatesABook(id, title, author);
    }

    @When("Admin updates a non-existent book with ID {int}, title {string}, and author {string}")
    public void adminUpdatesNonExistentBook(int id, String title, String author) {
        adminUpdatesABook(id, title, author);
    }

    @When("Admin updates a book with ID {int}, missing title, and author {string}")
    public void adminUpdatesBookWithMissingTitle(int id, String author) {
        String bookData = String.format("{ \"title\": \"\", \"author\": \"%s\" }", author);
        lastResponse = given()
                .auth().basic(currentUsername, currentPassword)
                .header("Content-Type", "application/json")
                .body(bookData)
                .when()
                .put("/api/books/" + id);

        commonValidationSteps.setLastResponse(lastResponse);
    }

    @When("Admin updates a book with ID {int}, title {string}, and missing author")
    public void adminUpdatesBookWithMissingAuthor(int id, String title) {
        String bookData = String.format("{ \"title\": \"%s\", \"author\": \"\" }", title);
        lastResponse = given()
                .auth().basic(currentUsername, currentPassword)
                .header("Content-Type", "application/json")
                .body(bookData)
                .when()
                .put("/api/books/" + id);

        commonValidationSteps.setLastResponse(lastResponse);
    }

    @When("Admin updates a book with ID {int}, duplicate title {string}, and author {string}")
    public void adminUpdatesBookWithDuplicateTitle(int id, String title, String author) {
        adminUpdatesABook(id, title, author);
    }

    @When("User updates a book with ID {int}, title {string}, and author {string}")
    public void userUpdatesBook(int id, String title, String author) {
        String bookData = String.format("{ \"title\": \"%s\", \"author\": \"%s\" }", title, author);
        lastResponse = given()
                .auth().basic(currentUsername, currentPassword)
                .header("Content-Type", "application/json")
                .body(bookData)
                .when()
                .put("/api/books/" + id);

        commonValidationSteps.setLastResponse(lastResponse);
    }

    // Delete Book Test Cases

    @When("Admin deletes a book with ID {int}")
    public void adminDeletesABook(int id) {
        lastResponse = given()
                .auth().basic(currentUsername, currentPassword)
                .when()
                .delete("/api/books/" + id);

        commonValidationSteps.setLastResponse(lastResponse);
    }

    @When("Admin deletes a non-existent book with ID {int}")
    public void adminDeletesNonExistentBook(int id) {
        adminDeletesABook(id);
    }

    @When("Admin deletes a book with ID {int} without proper authentication")
    public void adminDeletesBookWithoutAuthentication(int id) {
        lastResponse = given()
                .when()
                .delete("/api/books/" + id);

        commonValidationSteps.setLastResponse(lastResponse);
    }

    @When("User deletes a book with ID {int}")
    public void userDeletesABook(int id) {
        lastResponse = given()
                .auth().basic(currentUsername, currentPassword)
                .when()
                .delete("/api/books/" + id);

        commonValidationSteps.setLastResponse(lastResponse);
    }

    @Then("{string} should receive a response with status code {int} and error message {string}")
    public void validateResponse(String role, int statusCode, String message) {
        int actualStatusCode = lastResponse.getStatusCode();
        String actualMessage = lastResponse.jsonPath().getString("message");

        assert actualStatusCode == statusCode : role + " expected " + statusCode + " but got " + actualStatusCode;
        assert actualMessage.contains(message) : role + " expected message to contain: " + message + ", but got: " + actualMessage;
    }

    @Then("{string} should receive a successful response with status code {int}")
    public void validateSuccessfulResponse(String role, int statusCode) {
        int actualStatusCode = lastResponse.getStatusCode();

        assert actualStatusCode == statusCode : role + " expected " + statusCode + " but got " + actualStatusCode;
    }
}
