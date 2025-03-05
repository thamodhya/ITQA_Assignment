package additional;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import utils.Config;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class DeleteNonexistent {
    private Response response;
    private String adminUsername;
    private String adminPassword;
    private String userUsername;
    private String userPassword;

    public DeleteNonexistent() {
        // Load credentials from the config
        Config.ConfigData.Credentials credentials = Config.getInstance().getConfigData().credentials;
        this.adminUsername = credentials.admin.username;
        this.adminPassword = credentials.admin.password;
        this.userUsername = credentials.user.username;
        this.userPassword = credentials.user.password;
    }

    @When("I delete the book with ID {int} as admin")
    public void iDeleteTheBook(int id) {
        response = RestAssured.given()
                .auth()
                .basic("admin", "password")
                .when()
                .delete("/api/books/" + id);
    }

    @When("I delete the book with ID {int} as user")
    public void iDeleteTheBookAsUser(int id) {
        response = RestAssured.given()
                .auth()
                .basic("user", "password")
                .when()
                .delete("/api/books/" + id);
    }

    @Then("I should get a status code {int} for invalid delete operation")
    public void iShouldGetADeleteStatusCode(int statusCode) {
        // Assert that the status code matches the expected value
        Assert.assertEquals(response.getStatusCode(), statusCode, "Status code does not match");
    }

    @Then("I should get an error message {string} for invalid delete operation")
    public void iShouldGetAnDelErrorMessage(String expectedMessage) {
        // Get the response body as a string (plain text)
        String actualMessage = response.getBody().asString();

        // Assert that the actual message matches the expected error message
        Assert.assertEquals(actualMessage, expectedMessage, "Error message does not match");
    }
}
