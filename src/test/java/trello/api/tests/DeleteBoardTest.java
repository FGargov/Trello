package trello.api.tests;

import base.BaseTestSetup;
import io.restassured.response.Response;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static com.telerikacademy.api.tests.Endpoints.BOARD_ENDPOINT;
import static io.restassured.RestAssured.basePath;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;

public class DeleteBoardTest extends BaseTestSetup {
    @BeforeSuite
    public void boardTestSetup() {
        if (isNull(boardId)) {
            BoardTest boardTest = new BoardTest();
            boardTest.createBoardTest();
        }
    }

    @Test
    public void deleteBoardTest() {
        basePath = BOARD_ENDPOINT;

        Response response = getApplicationAuthentication()
                .pathParam("id", boardId)
                .when()
                .delete();

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, SC_OK, format("Incorrect status code. Expected %s.", SC_OK));
        assertEquals(response.getBody().jsonPath().getString("idBoard"), boardId, "Board ids don't match");


        System.out.printf("Board with id:\"%s\" was deleted.",boardId);
    }
}
