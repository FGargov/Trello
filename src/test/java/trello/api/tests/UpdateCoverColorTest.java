package trello.api.tests;

import base.BaseTestSetup;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.telerikacademy.api.tests.Constants.*;
import static com.telerikacademy.api.tests.Endpoints.BASE_URL;
import static com.telerikacademy.api.tests.Endpoints.CARDS_ENDPOINT;
import static com.telerikacademy.api.tests.Helper.isValid;
import static com.telerikacademy.api.tests.JSONRequests.UPDATE_CARD_COVER_COLOR;
import static io.restassured.RestAssured.baseURI;
import static java.lang.String.format;
import static org.apache.http.HttpStatus.SC_OK;

public class UpdateCoverColorTest extends BaseTestSetup {
    @Test
    public void coverColorTest() {
        baseURI = format("%s%s/%s", BASE_URL, CARDS_ENDPOINT, CARD_ID);

        String requestBody = format(UPDATE_CARD_COVER_COLOR, COVER_COLOR);
        Assert.assertTrue(isValid(requestBody), "Body is not a valid JSON");

        Response response = getApplicationAuthentication()
                .queryParam("key", KEY)
                .queryParam("token", TOKEN)
                .get();

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200");
        System.out.printf("A card with key %s and cover color %s was updated%n%n", CARD_ID, COVER_COLOR);

        Assert.assertEquals(response.jsonPath().getString("cover.color"), COVER_COLOR, "Cover color is not red");
    }
}
