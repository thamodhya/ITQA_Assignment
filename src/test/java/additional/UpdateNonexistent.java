package additional;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import utils.Config;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class UpdateNonexistent {
    private Response response;
    private String adminUsername;
    private String adminPassword;
    private String userUsername;
    private String userPassword;

    public UpdateNonexistent() {
        // Load credentials from the config
        Config.ConfigData.Credentials credentials = Config.getInstance().getConfigData().credentials;
        this.adminUsername = credentials.admin.username;
        this.adminPassword = credentials.admin.password;
        this.userUsername = credentials.user.username;
        this.userPassword = credentials.user.password;
    }

//    @Given("the API server is running")
//    public void theApiServerIsRunning() {
//        RestAssured.baseURI = "http://localhost:7080";
//    }

    @When("I update the book with ID {int} to title {string} and author {string} as admin")
    public void iUpdateTheBook(int id, String title, String author) {
        response = RestAssured.given()
                .auth()
                .basic("admin", "password")
                .header("Content-Type", "application/json")
                .body("{\"id\": " + id + ", \"title\": \"" + title + "\", \"author\": \"" + author + "\"}")
                .when()
                .put("/api/books/" + id);
    }

    @When("I update the book with ID {int} to title {string} and author {string} as user")
    public void iUpdateTheBookAsUser(int id, String title, String author) {
        response = RestAssured.given()
                .auth()
                .basic("user", "password")
                .header("Content-Type", "application/json")
                .body("{\"id\": " + id + ", \"title\": \"" + title + "\", \"author\": \"" + author + "\"}")
                .when()
                .put("/api/books/" + id);
    }

    @Then("I should get a status code {int} for invalid update operation")
    public void iShouldGetAUpdateStatusCode(int statusCode) {
        // Assert that the status code matches the expected value
        Assert.assertEquals(response.getStatusCode(), statusCode, "Status code does not match");
    }

    @Then("I should get an error message {string} for invalid update operation")
    public void iShouldGetAnUpErrorMessage(String expectedMessage) {
        // Get the response body as a string (plain text)
        String actualMessage = response.getBody().asString();

        // Assert that the actual message matches the expected error message
        Assert.assertEquals(actualMessage, expectedMessage, "Error message does not match");
    }

}
