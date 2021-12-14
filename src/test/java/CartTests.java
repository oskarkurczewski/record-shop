import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CartTests {

    private final Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();

    private final String RENTS_URI = "http://localhost:8080/record-shop-1.0-SNAPSHOT/record-shop/users";
    private final String USERS_URI = "http://localhost:8080/record-shop-1.0-SNAPSHOT/record-shop/users";
    private final String RECORDS_URI = "http://localhost:8080/record-shop-1.0-SNAPSHOT/record-shop/records";
    private Map<String, Object> renter = new HashMap<>();
    private Map<String, Object> client = new HashMap<>();
    private Map<String, Object> firstRecord = new HashMap<>();
    private Map<String, Object> secondRecord = new HashMap<>();
    private String firstUserID = "";
    private String secondUserID = "";
    private String firstRecordID = "";
    private String secondRecordID = "";

    @BeforeEach
    public void initUsers() {
        renter.put("login", "Derek1234");
        renter.put("type", "RENTER");

        client.put("login", "Shawn1234");
        client.put("type", "CLIENT");

        Response firstUserCreateResponse = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gsonBuilder.toJson(renter))
                .when()
                .post(USERS_URI);

        Response secondUserCreateResponse = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gsonBuilder.toJson(client))
                .when()
                .post(USERS_URI);


        firstRecord.put("title", "Tender Buttons");
        firstRecord.put("artist", "Broadcast");
        firstRecord.put("releaseDate", "2020-07-10");
        firstRecord.put("rented", false);

        secondRecord.put("title", "Utopia");
        secondRecord.put("artist", "Bjork");
        secondRecord.put("releaseDate", "2017-05-12");
        secondRecord.put("rented", false);

        Response firstRecordCreateResponse = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gsonBuilder.toJson(firstRecord))
                .when()
                .post(RECORDS_URI);

        Response secondRecordCreateResponse = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gsonBuilder.toJson(secondRecord))
                .when()
                .post(RECORDS_URI);

        JSONParser jsonParser = new JSONParser();
        try {
            firstUserID = (String) (
                    (JSONObject) jsonParser.parse(firstUserCreateResponse.asString())
            ).get("userID");
            secondUserID = (String) (
                    (JSONObject) jsonParser.parse(secondUserCreateResponse.asString())
            ).get("userID");
            firstRecordID = (String) (
                    (JSONObject) jsonParser.parse(firstRecordCreateResponse.asString())
            ).get("recordID");
            secondRecordID = (String) (
                    (JSONObject) jsonParser.parse(secondRecordCreateResponse.asString())
            ).get("recordID");
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @AfterEach
    public void cleanUsers() {
        delete(USERS_URI + "/" + firstUserID);
        delete(USERS_URI + "/" + secondUserID);
        delete(RECORDS_URI + "/" + firstRecordID);
        delete(RECORDS_URI + "/" + secondRecordID);
    }

    @Test
    public void testAddSingleRentToCart() {
        Map<String, Object> rent = new HashMap<>();
        rent.put("userID", firstUserID);
        rent.put("recordID", firstRecordID);

        try {
            given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body("{\"recordID\": " + "\"" + rent.get("recordID") + "\"" + "}")
                    .when()
                    .post(RENTS_URI + "/" + firstUserID + "/cart")
                            .then().assertThat()
                            .body("recordID", hasItem(rent.get("recordID")));

            get(RENTS_URI + "/" + firstUserID + "/cart")
                    .then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .body("recordID", hasItem(rent.get("recordID")));
        } finally {
            delete(RENTS_URI + "/" + firstUserID + "/cart/" + rent.get("recordID"));
        }
    }

    @Test
    public void testAddMultipleRentToCart() {
        Map<String, Object> firstRent = new HashMap<>();
        firstRent.put("userID", firstUserID);
        firstRent.put("recordID", firstRecordID);

        Map<String, Object> secondRent = new HashMap<>();
        secondRent.put("userID", firstUserID);
        secondRent.put("recordID", secondRecordID);

        try {
            given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body("{\"recordID\": " + "\"" + firstRent.get("recordID") + "\"" + "}")
                    .when()
                    .post(RENTS_URI + "/" + firstUserID + "/cart")
                        .then().assertThat()
                            .body("recordID", hasItem(firstRent.get("recordID")));

            given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body("{\"recordID\": " + "\"" + secondRent.get("recordID") + "\"" + "}")
                    .when()
                    .post(RENTS_URI + "/" + firstUserID + "/cart")
                        .then().assertThat()
                            .body("recordID", hasItem(secondRent.get("recordID")));

            get(RENTS_URI + "/" + firstUserID + "/cart")
                    .then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .body("recordID", hasItems(firstRent.get("recordID"), secondRent.get("recordID")));
        } finally {
            delete(RENTS_URI + "/" + firstUserID + "/cart/" + firstRent.get("recordID"));
            delete(RENTS_URI + "/" + firstUserID + "/cart/" + secondRent.get("recordID"));
        }
    }

    @Test
    public void testDeleteRentFromCart() {
        Map<String, Object> rent = new HashMap<>();
        rent.put("userID", firstUserID);
        rent.put("recordID", firstRecordID);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\"recordID\": " + "\"" + firstRecordID + "\"" + "}")
                .when()
                .post(RENTS_URI + "/" + firstUserID + "/cart")
                .then().assertThat()
                .body("recordID", hasItem(firstRecordID));

        get(RENTS_URI + "/" + firstUserID + "/cart").then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("recordID", hasItem(firstRecordID));

        delete(RENTS_URI + "/" + rent.get("userID") + "/cart/" + rent.get("recordID")).then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("recordID", not(hasItem(rent.get("recordID"))));
    }

    @Test
    public void testDropCart() {
        Map<String, Object> firstRent = new HashMap<>();
        firstRent.put("userID", firstUserID);
        firstRent.put("recordID", firstRecordID);

        Map<String, Object> secondRent = new HashMap<>();
        secondRent.put("userID", firstUserID);
        secondRent.put("recordID", secondRecordID);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\"recordID\": " + "\"" + firstRent.get("recordID") + "\"" + "}")
                .when()
                .post(RENTS_URI + "/" + firstUserID + "/cart")
                    .then().assertThat()
                        .body("recordID", hasItem(firstRent.get("recordID")));

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\"recordID\": " + "\"" + secondRent.get("recordID") + "\"" + "}")
                .when()
                .post(RENTS_URI + "/" + firstUserID + "/cart")
                    .then().assertThat()
                        .body("recordID", hasItem(secondRent.get("recordID")));

        get(RENTS_URI + "/" + firstUserID + "/cart")
                .then().assertThat()
                    .statusCode(HttpStatus.SC_OK)
                    .body("recordID", hasItems(firstRent.get("recordID"), secondRent.get("recordID")));

        delete(RENTS_URI + "/" + firstUserID + "/cart")
                .then().assertThat()
                    .statusCode(HttpStatus.SC_OK)
                    .body("recordID", not(hasItems(firstRent.get("recordID"), secondRent.get("recordID"))));

        get(RENTS_URI + "/" + firstUserID + "/cart")
                .then().assertThat()
                    .statusCode(HttpStatus.SC_OK)
                    .body("recordID", not(hasItem(firstRent.get("recordID"))))
                    .body("recordID", not(hasItem(secondRent.get("recordID"))));
    }

}
