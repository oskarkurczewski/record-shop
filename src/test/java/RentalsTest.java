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

public class RentalsTest {

    private final Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();

    private final String RENTS_URI = "http://localhost:8080/record-shop-1.0-SNAPSHOT/record-shop/users";
    private final String USERS_URI = "http://localhost:8080/record-shop-1.0-SNAPSHOT/record-shop/users";
    private final String RECORDS_URI = "http://localhost:8080/record-shop-1.0-SNAPSHOT/record-shop/records";
    private Map<String, Object> renter = new HashMap<>();
    private Map<String, Object> client = new HashMap<>();
    private Map<String, Object> firstRecord = new HashMap<>();
    private Map<String, Object> secondRecord = new HashMap<>();
    private Map<String, Object> rent = new HashMap<>();
    private String renterID = "";
    private String clientID = "";
    private String firstRecordID = "";
    private String secondRecordID = "";

    @BeforeEach
    public void initUsers() {
        renter.put("login", "Derek1234");
        renter.put("type", "RENTER");

        client.put("login", "Shawn1234");
        client.put("type", "CLIENT");

        Response renterCreateResponse = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gsonBuilder.toJson(renter))
                .when()
                .post(USERS_URI);

        Response clientCreateResponse = given()
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
            renterID = (String) (
                    (JSONObject) jsonParser.parse(renterCreateResponse.asString())
            ).get("userID");
            clientID = (String) (
                    (JSONObject) jsonParser.parse(clientCreateResponse.asString())
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

        rent.put("userID", clientID);
        rent.put("recordID", firstRecordID);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{" + "\"recordID\": " + "\"" + rent.get("recordID") + "\"" + "}")
                .when()
                .post(RENTS_URI + "/" + rent.get("userID") + "/cart");

    }

    @AfterEach
    public void cleanUsers() {
        delete(USERS_URI + "/" + renterID);
        delete(USERS_URI + "/" + clientID);
        delete(RECORDS_URI + "/" + firstRecordID);
        delete(RECORDS_URI + "/" + secondRecordID);
        delete(RENTS_URI + "/" + clientID + "/cart");
    }

    @Test
    public void rentOneFromCart() {
        try {
            given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body("{" + "\"renterID\": " + "\"" + renterID + "\"" + "}")
                    .when()
                    .post(RENTS_URI + "/" + rent.get("userID") + "/rentals")
                        .then().assertThat()
                            .statusCode(HttpStatus.SC_OK)
                            .body("recordID", hasItem(rent.get("recordID")));

            get(RENTS_URI + "/" + rent.get("userID") + "/rentals")
                    .then().assertThat()
                        .body("recordID", hasItem(rent.get("recordID")));
        } finally {
            given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body("{" + "\"renterID\": " + "\"" + renterID + "\"" + "}")
                    .when()
                    .post(RENTS_URI + "/" + rent.get("userID") + "/rentals/clear");
        }
    }

    @Test
    public void rentManyFromCart() {
        Map<String, Object> secondRent = new HashMap<String, Object>();
        secondRent.put("userID", clientID);
        secondRent.put("recordID", secondRecordID);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{" + "\"recordID\": " +  "\"" + secondRent.get("recordID") + "\"" + "}")
                .when()
                .post(RENTS_URI + "/" + secondRent.get("userID") + "/cart");

        try {
            given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body("{" + "\"renterID\": " + "\"" + renterID + "\"" + "}")
                    .when()
                    .post(RENTS_URI + "/" + rent.get("userID") + "/rentals")
                        .then().assertThat()
                            .statusCode(HttpStatus.SC_OK)
                            .body("recordID", hasItems(rent.get("recordID"), secondRent.get("recordID")));

            get(RENTS_URI + "/" + rent.get("userID") + "/rentals")
                    .then().assertThat()
                        .body("recordID", hasItems(rent.get("recordID"), secondRent.get("recordID")));
        } finally {
            given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body("{" + "\"renterID\": " + "\"" + renterID + "\"" + "}")
                    .when()
                    .post(RENTS_URI + "/" + rent.get("userID") + "/rentals/clear");
        }
    }

    @Test
    public void testClearRentals() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{" + "\"renterID\": " + "\"" + renterID + "\"" + "}")
                .when()
                .post(RENTS_URI + "/" + rent.get("userID") + "/rentals")
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("recordID", hasItem(rent.get("recordID")));

        get(RENTS_URI + "/" + rent.get("userID") + "/rentals")
                .then().assertThat()
                .body("recordID", hasItem(rent.get("recordID")));

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{" + "\"renterID\": " + "\"" + renterID + "\"" + "}")
                .when()
                .post(RENTS_URI + "/" + rent.get("userID") + "/rentals/clear")
                    .then().assertThat()
                        .body("recordID", not(hasItem(rent.get("recordID"))));

        get(RENTS_URI + "/" + rent.get("userID") + "/rentals")
                .then().assertThat()
                .body("recordID", not(hasItem(rent.get("recordID"))));
    }

    @Test
    public void testRentAlreadyRentedRecord() {
        try {
            given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body("{" + "\"renterID\": " + "\"" + renterID + "\"" + "}")
                    .when()
                    .post(RENTS_URI + "/" + rent.get("userID") + "/rentals")
                    .then().assertThat()
                    .statusCode(HttpStatus.SC_OK)
                    .body("recordID", hasItem(rent.get("recordID")));

            get(RENTS_URI + "/" + rent.get("userID") + "/rentals")
                    .then().assertThat()
                    .body("recordID", hasItem(rent.get("recordID")));

            Map<String, Object> secondRent = new HashMap<String, Object>();
            secondRent.put("userID", clientID);
            secondRent.put("recordID", firstRecordID);

            given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body("{" + "\"recordID\": " +  "\"" + secondRent.get("recordID") + "\"" + "}")
                    .when()
                    .post(RENTS_URI + "/" + secondRent.get("userID") + "/cart")
                    .then().assertThat()
                            .statusCode(HttpStatus.SC_BAD_REQUEST);

        } finally {
            given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body("{" + "\"renterID\": " + "\"" + renterID + "\"" + "}")
                    .when()
                    .post(RENTS_URI + "/" + rent.get("userID") + "/rentals/clear");
        }
    }
}
