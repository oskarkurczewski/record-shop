import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasItems;

public class RecordTests {

    private final String ROOT_URI = "http://localhost:8080/record-shop-1.0-SNAPSHOT/record-shop/records";
    private final Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void testCreateAndGetRecord() {
        Map<String, Object> record = new HashMap<>();
        record.put("artist", "Animals As Leaders");
        record.put("releaseDate", "2005-06-25");
        record.put("title", "The Joy Of Motion");
        record.put("rented", false);

        Response postResponse = given().
                contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gsonBuilder.toJson(record))
                .when()
                .post(ROOT_URI);

        try {
            JSONObject postJSON = (JSONObject) new JSONParser().parse(postResponse.asString());
            String recordID = (String) postJSON.get("recordID");


            try {
                postResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_CREATED)
                        .body("artist", is(record.get("artist")))
                        .body("releaseDate", is(record.get("releaseDate")))
                        .body("title", is(record.get("title")))
                        .body("rented", is(record.get("rented")));

                Response getResponse = get(ROOT_URI + "/" + recordID);

                getResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .body("artist", is(record.get("artist")))
                        .body("releaseDate", is(record.get("releaseDate")))
                        .body("title", is(record.get("title")))
                        .body("rented", is(record.get("rented")));
            } finally {
                delete(ROOT_URI + "/" + recordID);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetRecords() {
        Map<String, Object> firstRecord = new HashMap<>();
        firstRecord.put("artist", "Animals As Leaders");
        firstRecord.put("releaseDate", "2005-06-25");
        firstRecord.put("title", "The Joy Of Motion");
        firstRecord.put("rented", false);

        Map<String, Object> secondRecord = new HashMap<>();
        secondRecord.put("artist", "Polyphia");
        secondRecord.put("releaseDate", "2010-02-18");
        secondRecord.put("title", "Goat");
        secondRecord.put("rented", false);

        Response firstCreateResponse = given().
                contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gsonBuilder.toJson(firstRecord))
                .when()
                .post(ROOT_URI);

        Response secondCreateResponse = given().
                contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gsonBuilder.toJson(secondRecord))
                .when()
                .post(ROOT_URI);

        try {
            JSONObject firstCreateJson = (JSONObject) new JSONParser().parse(firstCreateResponse.asString());
            JSONObject secondCreateJson = (JSONObject) new JSONParser().parse(secondCreateResponse.asString());
            String firstRecordID = (String) firstCreateJson.get("recordID");
            String secondRecordID = (String) secondCreateJson.get("recordID");

            try {
                firstCreateResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_CREATED)
                        .body("artist", is(firstRecord.get("artist")))
                        .body("releaseDate", is(firstRecord.get("releaseDate")))
                        .body("rented", is(firstRecord.get("rented")))
                        .body("title", is(firstRecord.get("title")));

                secondCreateResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_CREATED)
                        .body("artist", is(secondRecord.get("artist")))
                        .body("releaseDate", is(secondRecord.get("releaseDate")))
                        .body("rented", is(secondRecord.get("rented")))
                        .body("title", is(secondRecord.get("title")));

                Response getResponse = get(ROOT_URI);

                getResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .body("artist", hasItems(firstRecord.get("artist"), secondRecord.get("artist")))
                        .body("releaseDate", hasItems(firstRecord.get("releaseDate"), secondRecord.get("releaseDate")))
                        .body("rented", hasItems(firstRecord.get("rented"), secondRecord.get("rented")))
                        .body("title", hasItems(firstRecord.get("title"), secondRecord.get("title")));
            } finally {
                delete(ROOT_URI + "/" + firstRecordID);
                delete(ROOT_URI + "/" + secondRecordID);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDelete() {
        Map<String, Object> record = new HashMap<>();
        record.put("artist", "Animals As Leaders");
        record.put("releaseDate", "2005-06-25");
        record.put("title", "The Joy Of Motion");
        record.put("rented", false);

        Response createResponse = given().
                contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gsonBuilder.toJson(record))
                .when()
                .post(ROOT_URI);

        try {
            JSONObject postJSON = (JSONObject) new JSONParser().parse(createResponse.asString());
            String recordID = (String) postJSON.get("recordID");

            createResponse.then().assertThat()
                    .statusCode(HttpStatus.SC_CREATED)
                    .body("artist", is(record.get("artist")))
                    .body("releaseDate", is(record.get("releaseDate")))
                    .body("rented", is(record.get("rented")))
                    .body("title", is(record.get("title")));

            delete(ROOT_URI + "/" + recordID).then().assertThat()
                    .statusCode(HttpStatus.SC_OK)
                    .body("recordID", is(recordID))
                    .body("artist", is(record.get("artist")))
                    .body("releaseDate", is(record.get("releaseDate")))
                    .body("rented", is(record.get("rented")));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testModify() {
        Map<String, Object> record = new HashMap<>();
        record.put("artist", "Animals As Leaders");
        record.put("releaseDate", "2005-06-25");
        record.put("title", "The Joy Of Motion");
        record.put("rented", false);

        Response createResponse = given().
                contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gsonBuilder.toJson(record))
                .when()
                .post(ROOT_URI);

        try {
            JSONObject postJSON = (JSONObject) new JSONParser().parse(createResponse.asString());
            String recordID = (String) postJSON.get("recordID");

            createResponse.then().assertThat()
                    .statusCode(HttpStatus.SC_CREATED)
                    .body("artist", is(record.get("artist")))
                    .body("releaseDate", is(record.get("releaseDate")))
                    .body("rented", is(record.get("rented")))
                    .body("title", is(record.get("title")));

            try {

                Map<String, Object> modifiedRecordData = new HashMap<>();
                modifiedRecordData.put("artist", "Polyphia");
                modifiedRecordData.put("releaseDate", "2010-06-30");
                modifiedRecordData.put("title", "Goat");
                modifiedRecordData.put("rented", false);

                given().
                        contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .body(gsonBuilder.toJson(modifiedRecordData))
                        .when()
                        .post(ROOT_URI + "/" + recordID + "/edit").then().assertThat()
                            .statusCode(HttpStatus.SC_OK)
                            .body("recordID", is(recordID))
                            .body("artist", is(modifiedRecordData.get("artist")))
                            .body("releaseDate", is(modifiedRecordData.get("releaseDate")))
                            .body("rented", is(modifiedRecordData.get("rented")))
                            .body("title", is(modifiedRecordData.get("title")));

                get(ROOT_URI + "/" + recordID).then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .body("recordID", is(recordID))
                        .body("artist", is(modifiedRecordData.get("artist")))
                        .body("releaseDate", is(modifiedRecordData.get("releaseDate")))
                        .body("rented", is(modifiedRecordData.get("rented")))
                        .body("title", is(modifiedRecordData.get("title")));
            } finally {
                delete(ROOT_URI + "/" + recordID);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetWrongID() {
        String wrongID = "b3a1a7e5-e8e8-4426-837e-717528282664";

        get(ROOT_URI + "/" + wrongID).then().assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }


}
