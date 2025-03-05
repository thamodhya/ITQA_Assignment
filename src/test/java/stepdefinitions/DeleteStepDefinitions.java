package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.junit.Assert;
import utils.Config;

import static io.restassured.RestAssured.given;

public class DeleteStepDefinitions {

    private Response lastResponse;
    private String currentUsername;
    private String currentPassword;
    private final CommonSteps commonSteps;

    public DeleteStepDefinitions(CommonSteps commonSteps) {
        this.commonSteps = commonSteps;
    }

    @Given("Admin is authenticated for delete operations")
    public void admin_authenticate_as_with_password(String role, String password) {
        setCredentials(role);
    }

    @Given("User is authenticated for delete operations")
    public void user_authenticate_as_with_password(String role, String password) {
        setCredentials(role);
    }

    private void setCredentials(String role) {
        if ("admin".equalsIgnoreCase(role)) {
            currentUsername = Config.getInstance().getConfigData().credentials.admin.username;
            currentPassword = Config.getInstance().getConfigData().credentials.admin.password;
        } else if ("user".equalsIgnoreCase(role)) {
            currentUsername = Config.getInstance().getConfigData().credentials.user.username;
            currentPassword = Config.getInstance().getConfigData().credentials.user.password;
        } else {
            throw new IllegalArgumentException("Invalid user role: " + role);
        }
    }

    @When("User deletes a book with ID {int}")
    public void user_deletes_a_book_with_ID(int bookId) {
        performDelete(bookId);
    }

    @When("User deletes a non-existent book with ID {int}")
    public void user_deletes_a_non_existent_book_with_ID(int bookId) {
        performDelete(bookId);
    }

    @When("Admin deletes a book with ID {int} without proper authentication")
    public void admin_deletes_a_book_with_ID_without_proper_authentication(int bookId) {
        performDelete(bookId);
    }

    private void performDelete(int bookId) {
        lastResponse = given()
                .auth()
                .basic(currentUsername, currentPassword)
                .header("Content-Type", "application/json")
                .when()
                .delete("/api/books/" + bookId);

        System.out.println("Delete Response: " + lastResponse.getBody().asString());
        commonSteps.setLastResponse(lastResponse);
    }

    @Then("User should receive a successful response with status code {int}")
    public void user_should_receive_a_successful_response_with_status_code(int statusCode) {
        Assert.assertNotNull("No response received!", lastResponse);
        Assert.assertEquals(statusCode, lastResponse.getStatusCode());
    }
}
