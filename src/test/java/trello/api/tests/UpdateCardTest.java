package trello.api.tests;

import base.BaseTestSetup;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.telerikacademy.api.tests.Constants.*;
import static com.telerikacademy.api.tests.Constants.TOKEN;
import static com.telerikacademy.api.tests.Endpoints.BASE_URL;
import static com.telerikacademy.api.tests.Endpoints.CARDS_ENDPOINT;
import static com.telerikacademy.api.tests.Helper.isValid;
import static com.telerikacademy.api.tests.JSONRequests.UPDATE_CARD;
import static io.restassured.RestAssured.baseURI;
import static java.lang.String.format;
import static org.apache.http.HttpStatus.SC_OK;

public class UpdateCardTest extends BaseTestSetup {
    @Test
    public void correctDataReturned_when_createCardTest() {
        baseURI = format("%s%s/%s", BASE_URL, CARDS_ENDPOINT, CARD_ID);

        String requestBody = format(UPDATE_CARD, NEW_CARD_NAME, NEW_CARD_DESCRIPTION, KEY, TOKEN);
        Assert.assertTrue(isValid(requestBody), "Body is not a valid JSON");

        Response response = getApplicationAuthentication()
                .body(requestBody)
                .put();

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200");
        System.out.printf("A card with key %s was updated%n%n", CARD_ID);

        Assert.assertEquals(response.jsonPath().getString("name"), "Updated card title name.", "Card name is not updated.");
        Assert.assertEquals(response.jsonPath().getString("desc"), "Updated card description", "Card description is not updated.");
    }
}
