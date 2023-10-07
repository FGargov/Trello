package trello.api.tests;

import base.BaseTestSetup;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.time.Instant;
import java.util.Map;

import static com.telerikacademy.api.tests.Constants.*;
import static com.telerikacademy.api.tests.Endpoints.*;
import static com.telerikacademy.api.tests.Helper.isValid;
import static com.telerikacademy.api.tests.JSONRequests.CARD;
import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.baseURI;
import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

public class CardTest extends BaseTestSetup {
    @BeforeSuite
    public void cardTestSetup() {
       if (isNull(boardId)) {
           BoardTest boardTest = new BoardTest();
           boardTest.createBoardTest();
       }

       if (isNull(toDoListId)) {
           ListTest listTest = new ListTest();
           listTest.createListTest();
       }
    }

    @Test
    public void createCardTest() {
        basePath = CARDS_ENDPOINT;

        String cardNameUnique = String.format("%s %s", CARD_NAME, uniqueName);
        String cardDescriptionUnique = String.format("%s %s", CARD_DESCRIPTION, uniqueName);

        Response response = getApplicationAuthentication()
                .queryParam("idList", toDoListId)
                .queryParam("name", cardNameUnique)
                .queryParam("desc", cardDescriptionUnique)
                .when()
                .post();

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, format("Incorrect status code. Expected %s.", SC_OK));
        assertEquals(response.getBody().jsonPath().getString("name"), cardNameUnique, "Card name don't match");
        assertEquals(response.getBody().jsonPath().getString("desc"), cardDescriptionUnique, "Card description don't match");
        assertEquals(response.getBody().jsonPath().getString("idList"), toDoListId, "List ids don't match");

        String dataLastActivity = response.getBody().jsonPath().getString("dateLastActivity");
        Instant actualDate = Instant.parse(dataLastActivity);
        Instant expectedDate = Instant.parse(timestamp);
        Assert.assertTrue((actualDate.isAfter(expectedDate)), "Actual date is not after expected date.");
        //Assert.assertTrue(actualDate.isAfter(expectedDate), "Actual date should be after expected date.");// може и с това да стане.

        cardId = response.getBody().jsonPath().getString("id");
        System.out.printf("Card with name '%s' and id '%s' was successfully created.%n", cardNameUnique, cardId);
    }


    @Test
    public void updateCardNameAndDescriptionQuearyParamTest() {
        if (isNull(cardId)) {
            createCardTest();
        }

        basePath = CARD_ENDPOINT;

        Response response = getApplicationAuthentication()
                .pathParam("id", cardId)
                .queryParam("name", NEW_CARD_NAME)
                .queryParam("desc", NEW_CARD_DESCRIPTION)
                .when()
                .put();

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, format("Incorrect status code. Expected %s.", SC_OK));
        assertEquals(response.getBody().jsonPath().getString("name"), NEW_CARD_NAME, "Card names don't match.");
        assertEquals(response.getBody().jsonPath().getString("desc"), NEW_CARD_DESCRIPTION, "Card description don't match.");
        assertEquals(response.getBody().jsonPath().getString("id"), cardId, "Card ids don't match.");

        System.out.println("Card name and description was successfully updated.");
    }

    @Test
    public void updateCardCoverColorTest() {
        if (isNull(cardId)) {
            createCardTest();
        }

        basePath = CARD_ENDPOINT;
        String color = "lime";

        String coverColorBody = format("{\n" +
                "    \"cover\": {\n" +
                "        \"color\": \"%s\"\n" +
                "    }\n" +
                "}", color);

        Response response = getApplicationAuthentication()
                .pathParam("id", cardId)
                .contentType("application/json")
                .body(coverColorBody)
                .when()
                .put();

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, format("Incorrect status code. Expected %s.", SC_OK));

        //Правим проверка дали цвета съвпада
        Map<String, String> cover = response.getBody().jsonPath().getMap("cover");
        String actualColor = cover.get("color");
        assertEquals(actualColor, color, "Card cover colors don't match");

        System.out.printf("Card cover color was successfully changed to '%s'.\n", color);
    }

    @Test
    public void moveCardToInTestListTest() {
        if (isNull(cardId)) {
            createCardTest();
        }

        basePath = CARD_ENDPOINT;

        Response response = getApplicationAuthentication()
                .pathParam("id", cardId)
                .queryParam("idList", toDoListId)
                .when()
                .put();

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, format("Incorrect status code. Expected %s.", SC_OK));
        assertEquals(response.getBody().jsonPath().getString("idList"), toDoListId, "Card list names don't match.");

        System.out.println("Card was successfully move to 'In Test' list.");
    }

    @Test
    public void correctDataReturned_when_createCardTest() {
        baseURI = format("%s%s", BASE_URL, CARDS_ENDPOINT);

        String requestBody = format(CARD, CARD_NAME, CARD_DESCRIPTION, LIST_ID, KEY, TOKEN);
        Assert.assertTrue(isValid(requestBody), "Body is not a valid JSON");

        Response response = getApplicationAuthentication()
                .body(requestBody)
                .post();

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, SC_OK, "Incorrect status code. Expected 200");
        System.out.printf("A card with key %s was created%n%n", CARD_ID);

        Assert.assertFalse(response.jsonPath().getString("id").isEmpty(), "Card ID is empty in the response");
        Assert.assertEquals(response.jsonPath().getString("name"), "My first card", "Card name is not correct");
    }
}
