package additional;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import utils.Config;

public class DeleteSuccess {
    private Response response;
    private String adminUsername;
    private String adminPassword;
    private String userUsername;
    private String userPassword;

    public DeleteSuccess() {
        // Load credentials from the config
        Config.ConfigData.Credentials credentials = Config.getInstance().getConfigData().credentials;
        this.adminUsername = credentials.admin.username;
        this.adminPassword = credentials.admin.password;
        this.userUsername = credentials.user.username;
        this.userPassword = credentials.user.password;
    }

    @When("I delete the book with ID {int} as user - valid id")
    public void DeleteTheBookAsUser(int id) {
        response = RestAssured.given()
                .auth()
                .basic("user", "password")
                .when()
                .delete("/api/books/" + id);
    }

    @Then("I should get a status code {int} for valid delete operation")
    public void ShouldGetADeleteStatusCode(int statusCode) {
        // Assert that the status code matches the expected value
        Assert.assertEquals(response.getStatusCode(), statusCode, "Status code does not match");
    }

    @Then("I should get an error message {string} for valid delete operation")
    public void ShouldGetAnDelErrorMessage(String expectedMessage) {
        // Get the response body as a string (plain text)
        String actualMessage = response.getBody().asString();

        // Assert that the actual message matches the expected error message
        Assert.assertEquals(actualMessage, expectedMessage, "Error message does not match");
    }


}
