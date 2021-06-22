package tests;

import helpers.AttachmentsHelper;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.params.CoreConnectionPNames;
import org.junit.jupiter.api.*;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("QA.GURU QA automation course")
@Feature("Work with REST API")
@Story("REST API tests with REST Assured")
@Tag("rest_api_tests_reqres")
class ReqResInApiTests extends TestBase {
    private String baseUrl = "https://reqres.in";
    Integer resultsTotal;
    String usersList;
    String email;
    String returnedValue;
    String token;
    String errorText;
    Response response;
    RestAssuredConfig config;

    @Test
    @DisplayName("1. Simple get, no assertions. Server response attached.")
    @Description("1. Simple RestAssured get request and sending it to System.out. No assertion.")
    void simpleGetRestAssured() {
        step("get by RestAssured, print to the std system output", ()-> {
            RestAssured.baseURI = baseUrl;
            usersList = get("/api/users?page=2").asString();
            AttachmentsHelper.attachAsText("Server response: ", usersList);
            System.out.println(usersList);
        });
    }

    @Test
    @DisplayName("2. Assertion of length of response. Must be greater than zero.")
    @Description("2. Simple RestAssured with assert by JUnit5 means usersList length > 0")
    void simpleApiGetJUnitAssertTrue() {
        step("get with rest assured, assert by JUnit5 with comments", ()-> {
            RestAssured.baseURI = baseUrl;
            usersList = get("/api/users?page=2").asString();
            assertTrue(usersList.length() > 0, "usersList length: " +
                    usersList.length() + " with data: " + usersList);
            AttachmentsHelper.attachAsText("Server response: ", usersList);
        });
    }

    @Test
    @DisplayName("3. Using hamcrest assertThat to assert response size.")
    @Description("3. Simple RestAssured get request assertion by means of hamcrest assertThat ans 'is'")
    void simpleApiGetHamcrestAssertThat() {
        step("set usersList as the result of the get response by RestAssured" +
                "check usersList length is not null (hamcrest)", ()-> {
            RestAssured.baseURI = baseUrl;
            usersList = get("/api/users?page=2").asString();
            assertThat(usersList.length(), is(not(nullValue())));
            AttachmentsHelper.attachAsText("Server response: ", usersList);
        });
    }

    @Test
    @DisplayName("3. Using hamcrest assertThat to assert response size. Different way to build request.")
    @Description("3.1 Simple RestAssured get request no assertion just different way to build the request")
    void restAssuredNormalget() {
        step("Build request. Gert response. No assertion. Attach response", ()->{
            RestAssured.baseURI = baseUrl;
            RequestSpecification httpRequest = given();
            Response response = httpRequest.request(Method.GET, "/api/users?page=2");
            System.out.println(response.getBody().asString());
            AttachmentsHelper.attachAsText("Server response: ", usersList);
        });
    }

    @Test
    @DisplayName("4. Using hamcrest 'is' to assert response value.")
    @Description("4. Assert that returned result if 12 in one go, JSON parse by rest assured")
    void parseJsonFromApiGetSimplified() {
        RestAssured.baseURI = baseUrl;
            given()
            .log().all()
            .filter(new AllureRestAssured());
        get("/api/users?page=2")
            .then()
            .body("total", is(12));
    }

    @Test
    @DisplayName("5. Well formed rest-assured text. assert returned value")
    @Description("5. Std way to write rest-assured tests.")
    void parseJsonFromApiGetHamcrestAssert() {
        step("Initialize baseURI = " + baseUrl, ()->{
            RestAssured.baseURI = baseUrl;
        });
        step("Assign the results of REST API request to resultsTotal from JSON var total", ()-> {
            resultsTotal = given()
                    .filter(new AllureRestAssured())
                    .log().all()
                    .when()
                    .get("/api/users?page=2")
                    .then()
                    .statusCode(200)
                    .extract()
                    .response()
                    .path("total");
            assertThat(resultsTotal, is(12));
            AttachmentsHelper.attachAsText("Server response: ", String.valueOf(resultsTotal));
        });
    }
    @Test
    @DisplayName("6. Well formed rest-assured text. assert value selected by JSON path")
    @Description("6. Variation of 5. Assertion by hamcrest's 'is'")
    void parseJsonFromApiGetRestAssrdOnly() {
        RestAssured.baseURI = baseUrl;
        step("Build get request for group of users, getting the reponse. Assert.", ()-> {
                given()
                .filter(new AllureRestAssured())
                .log().all()
                .when()
                .get("/api/users?page=2")
                .then()
                .statusCode(200)
                .body("total", is(12));
            AttachmentsHelper.attachAsText("Server response: ", usersList);
        });
    }
    @Test
    @DisplayName("7. Parsing JSON response selecting deeper nodes")
    @Description("7. Deeper parsing of response. Parse response by index in array")
    void parseJsonParseJsonDeeper() {
        RestAssured.baseURI = baseUrl;
        step("hello", ()-> {
                given()
                    .log().all()
                    .filter(new AllureRestAssured());
            email = get("/api/users?page=2")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .path("data[2].email");
        });
        System.out.println("Email is;" + email);
        step("email should be tobias.funke@reqres.in", ()-> {
            assertThat(email, is("tobias.funke@reqres.in"));
            AttachmentsHelper.attachAsText("Email is ", email);
        });
    }
    @Test
    @DisplayName("8. Parsing JSON's get dictionary's value by key")
    @Description("8. Deeper parsing of response. Get response by key in dict")
    void parseJsonParseDictValues() {
        RestAssured.baseURI = baseUrl;
        step("creating get request for ", ()-> {
            given()
                    .filter(new AllureRestAssured())
                    .log().all();
            returnedValue = get("/api/users?page=2")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response()
                        .path("support.url");
        });
        System.out.println("Response value from BE:" + returnedValue);
        step("Should be StatusCode Wekly", ()-> {
            assertThat(returnedValue, containsString("#support-heading"));
            AttachmentsHelper.attachAsText("JSON Parsing result: ", returnedValue);
        });
    }
    @Test
    @DisplayName("9. Single user get. Validate 1st name.")
    @Description("9. Single User data check")
    void parseJsonForSingleUser() {
        RestAssured.baseURI = baseUrl;
        step("Creating get request for single user '/api/users/2' ", ()-> {
            given()
                    .filter(new AllureRestAssured())
                    .log().all();
            returnedValue = get("/api/users/2")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response()
                        .path("data.first_name");
        });
        System.out.println("Response value from BE:" + returnedValue);
        step("Returned first name should be Janet", ()-> {
            assertThat(returnedValue, is("Janet"));
            AttachmentsHelper.attachAsText("First name: ", returnedValue);
        });
    }
    @Test
    @DisplayName("10. Request missing user with id = 23. 404 to be returned")
    @Description("10. Non existing user data check. Expecting status 404 and null")
    void parseJsonUserDontExist() {
        RestAssured.baseURI = baseUrl;
        step("Creating get request for single user '/api/users/23' ", ()-> {
            given()
                    .filter(new AllureRestAssured())
                    .log().all();
            returnedValue = get("/api/users/23")
                        .then()
                        .statusCode(404)
                        .extract()
                        .response()
                        .path("data.first_name");
        });
        System.out.println("Response value from BE:" + returnedValue);
        step("Returned first name should be NULL", ()-> {
            //assertThat(returnedValue, isEmptyOrNullString());
            assertThat(returnedValue, is(nullValue()));
        });
    }
    @Test
    @DisplayName("11. Simplified syntax to test missing user.")
    @Description("11. Non existing user data check. Expecting status 404 and null. Simplified syntax.")
    void parseJsonUserDontExistSimplified() {
        RestAssured.baseURI = baseUrl;
        step("Creating get request for single user '/api/users/23' ", ()-> {
            get("/api/users/23")
                .then()
                .statusCode(404)
                .body("data.first_name", is(nullValue()));
        });
    }
    @Test
    @DisplayName("12. POST request for authentication by email and password")
    @Description("12. POST login request. Successful attempt. Check status code and token")
    void postSuccessfulLoginRequest() {
//        {
//            "email": "eve.holt@reqres.in",
//                "password": "cityslicka"
//        }
        RestAssured.baseURI = baseUrl;
        step("Preparing POST request to login to '/api/login' ", ()-> {
            Response response =
                given()
                    .filter(new AllureRestAssured())
                    .log().all()
                    .contentType("application/json")
                    .body("{\"email\": \"eve.holt@reqres.in\",\"password\": \"cityslicka\"}")
                .when()
                    .post("/api/login")
                .then()
                    .statusCode(200)
                    .extract()
                    .response();
                token = response.path("token");
        });
        step("Asserting that received token is not null value", ()->{
            assertThat(token, is(notNullValue()));
            AttachmentsHelper.attachAsText("Returned token:", token);
            System.out.println("Returned token: " + token);
        });
    }
    @Test
    @DisplayName("13. Unsuccessful attempt to login without password")
    @Description("13. POST login request. Unsuccessful attempt du to password absence.")
    void postUnsuccessfulLoginRequest() {
//        {
//            "email": "eve.holt@reqres.in",
//                "password": "cityslicka" << will be missing in the request
//        }
        RestAssured.baseURI = baseUrl;
        step("Preparing POST request to login to '/api/login' ", ()-> {
            response =
                given()
                    .filter(new AllureRestAssured())
                    .log().all()
                    .contentType("application/json")
                    .body("{\"email\": \"eve.holt@reqres.in\"}")
                .when()
                    .post("/api/login")
                .then()
                    .statusCode(400)
                    .extract()
                    .response();
        });
        step("ACT: set error to error from response", ()-> {
            errorText = response
                    .path("error");
        });
        step("CHECK: error string is 'Missing password'", ()-> {
            assertThat(errorText, is("Missing password"));
            AttachmentsHelper.attachAsText("Response from server: ",
                    response.statusCode() +" " + response.prettyPrint());
        });

    }
    @Test
    @DisplayName("14. Processing response timeouts")
    @Description("14. Processing response timeouts")
    void restAssuredDelaysProcessing() {
        step("PREP: setting baseURI", ()-> {
            RestAssured.baseURI = baseUrl;
        });
        step("PREP rest assured config' ", ()-> {
            config = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                    .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000)
                    .setParam(CoreConnectionPNames.SO_TIMEOUT, 5000));
        });
        step("ACT: sending request ", ()-> {
            response =
                given()
                    .filter(new AllureRestAssured())
                    .config(config)
                    .contentType("application/json")
                    .when()
                    .get("/api/users?delay=3")
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();
        });
        step("CHECK: Checking the server response.", ()-> {
            AttachmentsHelper.attachAsText("Response from server: ",
                    response.statusCode() +" " + response.prettyPrint());
            assertThat(response.statusCode(), is(equalTo(200)));
        });


    }


}

