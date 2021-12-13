import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.json.simple.parser.JSONParser;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

public class UserTests {

    final static String ROOT_URI = "http://localhost:8080/record-shop-1.0-SNAPSHOT/record-shop/users";

    @Test
    public void testCreateAndGetByID() {
        Response postResponse = given().
                contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\"login\": \"janet12345\", \"active\": true, \"type\": \"CLIENT\"}")
                .when()
                .post(ROOT_URI);
        try {
            JSONObject postJSON = (JSONObject) new JSONParser().parse(postResponse.asString());
            String userID = (String) postJSON.get("userID");

            postResponse.then().assertThat()
                    .statusCode(HttpStatus.SC_OK)
                    .body("login", is("janet12345"))
                    .body("type", is("CLIENT"))
                    .body("active", is(false));

            try {
                Response getResponse = get(ROOT_URI + "/" + userID);

                getResponse.then()
                        .body("login", is("janet12345"))
                        .body("type", is("CLIENT"))
                        .body("userID", is(userID))
                        .body("active", is(true));
            } finally {
                delete(ROOT_URI + "/" + userID);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetUserByLogin() {
        Response firstPOSTResponse = given().
                contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\"login\": \"janet1234\", \"active\": true, \"type\": \"CLIENT\"}")
                .when()
                .post(ROOT_URI);

        Response secondPOSTResponse = given().
                contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\"login\": \"janet5678\", \"active\": true, \"type\": \"RENTER\"}")
                .when()
                .post(ROOT_URI);

        try {
            JSONObject firstPOSTJSON = (JSONObject) new JSONParser().parse(firstPOSTResponse.asString());
            JSONObject secondPOSTJSON = (JSONObject) new JSONParser().parse(secondPOSTResponse.asString());

            String firstUserID = (String) firstPOSTJSON.get("userID");
            String secondUserID = (String) secondPOSTJSON.get("userID");

            try {
                firstPOSTResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .body("login", is("janet1234"))
                        .body("type", is("CLIENT"))
                        .body("active", is(true));

                secondPOSTResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .body("login", is("janet5678"))
                        .body("type", is("RENTER"))
                        .body("active", is(true));

                Response getResponse = get(ROOT_URI + "?login=janet");
                getResponse.then()
                        .body("login", hasItems("janet1234", "janet5678"))
                        .body("type", hasItems("RENTER", "CLIENT"))
                        .body("userID", hasItems(firstUserID, secondUserID))
                        .body("active", hasItems(true, true));

            } finally {
                delete(ROOT_URI + "/" + firstUserID);
                delete(ROOT_URI + "/" + secondUserID);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateLogin() {
        Response CreateResponse = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\"login\": \"janet1234\", \"active\": true, \"type\": \"CLIENT\"}")
                .when()
                .post(ROOT_URI);

        try {
            JSONObject POSTJson = (JSONObject) new JSONParser().parse(CreateResponse.asString());
            String userID = (String) POSTJson.get("userID");

            try {
                CreateResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .body("login", is("janet1234"))
                        .body("type", is("CLIENT"))
                        .body("active", is(true));

                Response UpdateResponse = given()
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .body("{\"login\": \"janet5678\"}")
                        .when()
                        .post(ROOT_URI + "/" + userID + "/changeLogin");

                UpdateResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .body("login", is("janet5678"))
                        .body("type", is("CLIENT"))
                        .body("active", is(true));

            } finally {
                delete(ROOT_URI + "/" + userID);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}