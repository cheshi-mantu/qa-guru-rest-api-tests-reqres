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

@Feature("Bulk import")
@Story("Check the results of bulk import")

class ReqResInApiTests extends TestBase {
    private String baseUrl = "https://reqres.in";
    String email;

    @Test
    @DisplayName("Checking 3rd record in the JSON response is " + CREATED_USER)
    @Description("Request 2nd page, check the expected user is present in the resuts")
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
        step("email should be " + CREATED_USER, ()-> {
            AttachmentsHelper.attachAsText("Email is ", email);
            assertThat(email, is(CREATED_USER));

        });
    }


}

