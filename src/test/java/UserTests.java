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
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

public class UserTests {

    final static String ROOT_URI = "http://localhost:8080/record-shop/users";
    final private Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();


    @Test
    public void testCreateAndGetByID() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("login", "janet12345");
        userMap.put("type", "CLIENT");

        System.out.println(gsonBuilder.toJson(userMap));

        Response postResponse = given().
                contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gsonBuilder.toJson(userMap))
                .when()
                .post(ROOT_URI);
        try {
            JSONObject postJSON = (JSONObject) new JSONParser().parse(postResponse.asString());
            String userID = (String) postJSON.get("userID");

            System.out.println(postResponse.asString());

            postResponse.then().assertThat()
                    .statusCode(HttpStatus.SC_OK)
                    .body("login", is(userMap.get("login")))
                    .body("type", is(userMap.get("type")));

            try {
                Response getResponse = get(ROOT_URI + "/" + userID);

                getResponse.then()
                        .body("login", is(userMap.get("login")))
                        .body("type", is(userMap.get("type")))
                        .body("userID", is(userID));
            } finally {
                delete(ROOT_URI + "/" + userID);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetUserByLogin() {
        Map<String, Object> firstUser = new HashMap<>();
        firstUser.put("login", "janet1234");
        firstUser.put("type", "CLIENT");

        Map<String, Object> secondUser = new HashMap<>();
        secondUser.put("login", "janet5678");
        secondUser.put("type", "RENTER");

        Response firstCreateResponse = given().
                contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gsonBuilder.toJson(firstUser))
                .when()
                .post(ROOT_URI);

        Response secondCreateResponse = given().
                contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gsonBuilder.toJson(secondUser))
                .when()
                .post(ROOT_URI);

        try {
            JSONObject firstCreateJSON = (JSONObject) new JSONParser().parse(firstCreateResponse.asString());
            JSONObject secondCreateJSON = (JSONObject) new JSONParser().parse(secondCreateResponse.asString());

            String firstUserID = (String) firstCreateJSON.get("userID");
            String secondUserID = (String) secondCreateJSON.get("userID");

            try {
                firstCreateResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .body("login", is(firstUser.get("login")))
                        .body("type", is(firstUser.get("type")));

                secondCreateResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .body("login", is(secondUser.get("login")))
                        .body("type", is(secondUser.get("type")));

                Response getResponse = get(ROOT_URI + "?login=janet");
                getResponse.then()
                        .body("login", hasItems(firstUser.get("login"), secondUser.get("login")))
                        .body("type", hasItems(firstUser.get("type"), secondUser.get("type")))
                        .body("userID", hasItems(firstUserID, secondUserID));

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
        Map<String, Object> user = new HashMap<>();
        user.put("login", "janet1234");
        user.put("type", "CLIENT");

        Response createResponse = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gsonBuilder.toJson(user))
                .when()
                .post(ROOT_URI);

        try {
            JSONObject POSTJson = (JSONObject) new JSONParser().parse(createResponse.asString());
            String userID = (String) POSTJson.get("userID");

            try {
                createResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .body("login", is(user.get("login")))
                        .body("type", is(user.get("type")));

                String newLogin = "janet5678";

                Response UpdateResponse = given()
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .body("{\"login\": \"" + newLogin + "\"}")
                        .when()
                        .post(ROOT_URI + "/" + userID + "/changeLogin");

                UpdateResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .body("login", is(newLogin))
                        .body("type", is(user.get("type")));

            } finally {
                delete(ROOT_URI + "/" + userID);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeactivateAndActivateUser() {
        Map<String, Object> user = new HashMap<>();
        user.put("login", "janet1234");
        user.put("type", "CLIENT");

        Response createResponse = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gsonBuilder.toJson(user))
                .when()
                .post(ROOT_URI);

        try {
            JSONObject POSTJson = (JSONObject) new JSONParser().parse(createResponse.asString());
            String userID = (String) POSTJson.get("userID");

            try {
                createResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .body("login", is(user.get("login")))
                        .body("type", is(user.get("type")));

                Response deactivateResponse = given()
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .when()
                        .post(ROOT_URI + "/" + userID + "/deactivate");

                deactivateResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .body("login", is(user.get("login")))
                        .body("type", is(user.get("type")))
                        .body("active", is(false));

                Response activateResponse = given()

                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .when()
                        .post(ROOT_URI + "/" + userID + "/activate");

                activateResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .body("login", is(user.get("login")))
                        .body("type", is(user.get("type")))
                        .body("active", is(true));

            } finally {
                delete(ROOT_URI + "/" + userID);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetUserWrongID() {
        String wrongID = "b3a1a7e3-e8c5-4426-837e-717528282664";

        get(ROOT_URI + "/" + wrongID).then().assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void testCreateUserWithLoginTooShort() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("login", "abcdefg");
        userMap.put("type", "CLIENT");

        System.out.println(gsonBuilder.toJson(userMap));

        given().
                contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gsonBuilder.toJson(userMap))
                .when()
                .post(ROOT_URI)
                .then().assertThat()
                        .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void testCreateUserWithLoginTooLong() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("login", "abcdefghijklmnopq");
        userMap.put("type", "CLIENT");

        System.out.println(gsonBuilder.toJson(userMap));

        given().
                contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gsonBuilder.toJson(userMap))
                .when()
                .post(ROOT_URI)
                .then().assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void testCreateUserWithLoginContainingInvalidCharacters() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("login", "janet12345#");
        userMap.put("type", "CLIENT");

        System.out.println(gsonBuilder.toJson(userMap));

        given().
                contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gsonBuilder.toJson(userMap))
                .when()
                .post(ROOT_URI)
                .then().assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void testModifyUserWithLoginContainingInvalidCharacters() {
        Map<String, Object> user = new HashMap<>();
        user.put("login", "janet1234");
        user.put("type", "CLIENT");

        Response createResponse = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gsonBuilder.toJson(user))
                .when()
                .post(ROOT_URI);

        try {
            JSONObject POSTJson = (JSONObject) new JSONParser().parse(createResponse.asString());
            String userID = (String) POSTJson.get("userID");

            try {
                createResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .body("login", is(user.get("login")))
                        .body("type", is(user.get("type")));

                String newLogin = "janet12345#";

                given()
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .body("{\"login\": \"" + newLogin + "\"}")
                        .when()
                        .post(ROOT_URI + "/" + userID + "/changeLogin")
                        .then().assertThat()
                                .statusCode(HttpStatus.SC_BAD_REQUEST);

            } finally {
                delete(ROOT_URI + "/" + userID);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testModifyUserWithLoginTooShort() {
        Map<String, Object> user = new HashMap<>();
        user.put("login", "janet1234");
        user.put("type", "CLIENT");

        Response createResponse = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gsonBuilder.toJson(user))
                .when()
                .post(ROOT_URI);

        try {
            JSONObject POSTJson = (JSONObject) new JSONParser().parse(createResponse.asString());
            String userID = (String) POSTJson.get("userID");

            try {
                createResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .body("login", is(user.get("login")))
                        .body("type", is(user.get("type")));

                String newLogin = "abcd";

                given()
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .body("{\"login\": \"" + newLogin + "\"}")
                        .when()
                        .post(ROOT_URI + "/" + userID + "/changeLogin")
                        .then().assertThat()
                        .statusCode(HttpStatus.SC_BAD_REQUEST);

            } finally {
                delete(ROOT_URI + "/" + userID);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testModifyUserWithLoginTooLong() {
        Map<String, Object> user = new HashMap<>();
        user.put("login", "janet1234");
        user.put("type", "CLIENT");

        Response createResponse = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gsonBuilder.toJson(user))
                .when()
                .post(ROOT_URI);

        try {
            JSONObject POSTJson = (JSONObject) new JSONParser().parse(createResponse.asString());
            String userID = (String) POSTJson.get("userID");

            try {
                createResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .body("login", is(user.get("login")))
                        .body("type", is(user.get("type")));

                String newLogin = "abcdefghijklmnopq";

                given()
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .body("{\"login\": \"" + newLogin + "\"}")
                        .when()
                        .post(ROOT_URI + "/" + userID + "/changeLogin")
                        .then().assertThat()
                        .statusCode(HttpStatus.SC_BAD_REQUEST);

            } finally {
                delete(ROOT_URI + "/" + userID);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testModifyUserWithRepeatedLogin() {
        Map<String, Object> user = new HashMap<>();
        user.put("login", "janet1234");
        user.put("type", "CLIENT");

        Response createResponse = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gsonBuilder.toJson(user))
                .when()
                .post(ROOT_URI);

        try {
            JSONObject POSTJson = (JSONObject) new JSONParser().parse(createResponse.asString());
            String userID = (String) POSTJson.get("userID");

            try {
                createResponse.then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .body("login", is(user.get("login")))
                        .body("type", is(user.get("type")));

                String newLogin = "janet1234";

                given()
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .body("{\"login\": \"" + newLogin + "\"}")
                        .when()
                        .post(ROOT_URI + "/" + userID + "/changeLogin")
                        .then().assertThat()
                        .statusCode(HttpStatus.SC_BAD_REQUEST);

            } finally {
                delete(ROOT_URI + "/" + userID);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}