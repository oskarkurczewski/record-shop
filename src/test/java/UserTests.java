import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import javax.persistence.criteria.Root;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;

public class UserTests {

    final static String ROOT_URI = "http://localhost:8080/record-shop-1.0-SNAPSHOT/record-shop/users";

    @Test
    public void testCreateAndRead() {
        Response postResponse = given().
                contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\"login\": \"Janet\", \"active\": true, \"type\": \"CLIENT\"}")
                .when()
                .post(ROOT_URI);
        System.out.println("RESPONSE: " + postResponse.asString());


        postResponse.then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("login", is("Janet"))
                .body("type", is("CLIENT"));
    }
    
}
