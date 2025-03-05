package additional;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import utils.Config;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class UpdateSuccess {
    private Response response;
    private String adminUsername;
    private String adminPassword;
    private String userUsername;
    private String userPassword;

    public UpdateSuccess() {
        // Load credentials from the config
        Config.ConfigData.Credentials credentials = Config.getInstance().getConfigData().credentials;
        this.adminUsername = credentials.admin.username;
        this.adminPassword = credentials.admin.password;
        this.userUsername = credentials.user.username;
        this.userPassword = credentials.user.password;
    }

    @When("I update the book with ID {int} to title {string} and author {string} as admin - valid id")
    public void UpdateTheBook(int id, String title, String author) {
        response = RestAssured.given()
                .auth()
                .basic("admin", "password")
                .header("Content-Type", "application/json")
                .body("{\"id\": " + id + ", \"title\": \"" + title + "\", \"author\": \"" + author + "\"}")
                .when()
                .put("/api/books/" + id);
    }

    @Then("I should get a status code {int} for valid update operation")
    public void ShouldGetAUpdateStatusCode(int statusCode) {
        // Assert that the status code matches the expected value
        Assert.assertEquals(response.getStatusCode(), statusCode, "Status code does not match");
    }

    @Then("I should get an error message {string} for valid update operation")
    public void ShouldGetAnUpErrorMessage(String expectedMessage) {
        // Get the response body as a string (plain text)
        String actualMessage = response.getBody().asString();

        // Assert that the actual message matches the expected error message
        Assert.assertEquals(actualMessage, expectedMessage, "Error message does not match");
    }

}
