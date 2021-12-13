import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.delete;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import javax.persistence.criteria.Root;
import javax.swing.text.html.parser.Parser;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import org.json.simple.parser.JSONParser;

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

        System.out.println("RESPONSE: " + postResponse.asString());

        postResponse.then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("login", is("janet12345"))
                .body("type", is("CLIENT"));

        try {
            JSONObject postJSON = (JSONObject) new JSONParser().parse(postResponse.asString());
            String userID = (String) postJSON.get("userID");

            System.out.println(userID);
            Response getResponse = get(ROOT_URI + "/" + userID);
            getResponse.then()
                    .body("login", is("janet12345"))
                    .body("type", is("CLIENT"))
                    .body("userID", is(userID))
                    .body("active", is(true));

            delete(ROOT_URI + "/" + userID);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
