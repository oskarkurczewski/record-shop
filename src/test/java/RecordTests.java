import groovy.util.MapEntry;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasItems;

public class RecordTests {

    private final String ROOT_URI = "http://localhost:8080/record-shop-1.0-SNAPSHOT/record-shop/records";

    @Test
    public void testCreate() {
        Response postResponse = given().
                contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(("{\"artist\": \"Animals As Leaders\", "
                        + "\"releaseDate\": \"2005-06-25\","
                        + "\"title\": \"The Joy Of Motion\","
                        + "\"rented\": false}"))
                .when()
                .post(ROOT_URI);

        try {
            System.out.println(postResponse.asString());
            JSONObject postJSON = (JSONObject) new JSONParser().parse(postResponse.asString());
            String recordID = (String) postJSON.get("recordID");


            try {
                postResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_CREATED)
                        .body("artist", is("Animals As Leaders"))
                        .body("releaseDate", is("2005-06-25"))
                        .body("rented", is(false))
                        .body("title", is("The Joy Of Motion"));

                Response getResponse = get(ROOT_URI + "/" + recordID);

                getResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .body("artist", is("Animals As Leaders"))
                        .body("releaseDate", is("2005-06-25"))
                        .body("rented", is(false))
                        .body("title", is("The Joy Of Motion"));
            } finally {
                delete(ROOT_URI + "/" + recordID);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetRecords() {
        Response firstCreateResponse = given().
                contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(("{\"artist\": \"Animals As Leaders\", "
                        + "\"releaseDate\": \"2005-06-25\","
                        + "\"title\": \"The Joy Of Motion\","
                        + "\"rented\": false}"))
                .when()
                .post(ROOT_URI);

        Response secondCreateResponse = given().
                contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(("{\"artist\": \"Metallica\", "
                        + "\"releaseDate\": \"1989-06-25\","
                        + "\"title\": \"Killem All\","
                        + "\"rented\": false}"))
                .when()
                .post(ROOT_URI);

        try {
            System.out.println(firstCreateResponse.asString());
            System.out.println(secondCreateResponse.asString());
            JSONObject firstPOSTJSON = (JSONObject) new JSONParser().parse(firstCreateResponse.asString());
            JSONObject secondPOSTJSON = (JSONObject) new JSONParser().parse(secondCreateResponse.asString());
            String frstRecordID = (String) firstPOSTJSON.get("recordID");
            String secondRecordID = (String) secondPOSTJSON.get("recordID");


            try {
                firstCreateResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_CREATED)
                        .body("artist", is("Animals As Leaders"))
                        .body("releaseDate", is("2005-06-25"))
                        .body("rented", is(false))
                        .body("title", is("The Joy Of Motion"));

                secondCreateResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_CREATED)
                        .body("artist", is("Metallica"))
                        .body("releaseDate", is("1989-06-25"))
                        .body("rented", is(false))
                        .body("title", is("Killem All"));

                Response getResponse = get(ROOT_URI);

                getResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .body("artist", hasItems("Animals As Leaders", "Metallica"))
                        .body("releaseDate", hasItems("2005-06-25", "1989-06-25"))
                        .body("rented", hasItems(false, false))
                        .body("title", hasItems("The Joy Of Motion", "Killem All"));
            } finally {
                delete(ROOT_URI + "/" + frstRecordID);
                delete(ROOT_URI + "/" + secondRecordID);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


}
