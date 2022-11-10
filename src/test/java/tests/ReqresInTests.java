package tests;

import config.Project;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import models.lombok.RegistrationBodyLombokModel;
import models.lombok.RegistrationResponseLombokModel;
import models.lombok.UpdateBodyLombokModel;
import models.lombok.UpdateResponseLombokModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static helpers.CustomApiListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static specs.RegistrationSpec.registrationRequestSpec;
import static specs.RegistrationSpec.registrationResponseSpec;

@DisplayName("API tests")
public class ReqresInTests {

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = Project.config.apiUrl();
    }

    @Test
    @Tag("API")
    @Owner("imbaReaver")
    @Feature("Registration")
    @DisplayName("Registration token should be QpwL5tke4Pnpja7X4")
    void successfulRegistrationPlainTest() {
        step("Fill and check registration (API)", () -> {
            String body = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";
            given()
                    .contentType(JSON)
                    .body(body)
                    .log().uri()
                    .log().body()
                    .when()
                    .post("/api/register")
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(200)
                    .body("token", is("QpwL5tke4Pnpja7X4"));
        });
    }

    @Test
    @Tag("API")
    @Owner("imbaReaver")
    @Feature("Updating user info")
    @DisplayName("Updating name and job for user are correct")
    void successfulUpdatePlainTest() {
        step("Check updating user (API)", () -> {
            String body = "{ \"name\": \"neo\", \"job\": \"hacker\" }";
            given()
                    .contentType(JSON)
                    .filter(withCustomTemplates())
                    .body(body)
                    .log().uri()
                    .log().body()
                    .when()
                    .patch("/api/users/2")
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(200)
                    .body("name", is("neo"), "job", is("hacker"));
        });
    }

    @Test
    @Tag("API")
    @Owner("imbaReaver")
    @Feature("Updating user info")
    @DisplayName("Updating name, job and updating time for user are correct")
    void successfulUpdateWithLombokModelsTest() {
        step("Check updating user with models (API)", () -> {
            UpdateBodyLombokModel body = new UpdateBodyLombokModel();
            body.setName("neo");
            body.setJob("hacker");
            UpdateResponseLombokModel response = given()
                    .contentType(JSON)
                    .filter(withCustomTemplates())
                    .body(body)
                    .log().uri()
                    .log().body()
                    .when()
                    .patch("/api/users/2")
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(200)
                    .extract().as(UpdateResponseLombokModel.class);

            assertThat(response.getName()).isEqualTo("neo");
            assertThat(response.getJob()).isEqualTo("hacker");

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String updated = response.getUpdatedAt();
            assertThat(updated.startsWith(dtf.format(LocalDateTime.now())));
        });
    }

    @Test
    @Tag("API")
    @Owner("imbaReaver")
    @Feature("Users list")
    @DisplayName("First id in list is 1")
    void successfulListTest() {
        step("Check id in list (API)", () -> {
            given()
                    .filter(withCustomTemplates())
                    .log().uri()
                    .log().body()
                    .when()
                    .get("/api/unknown")
                    .then()
                    .log().status()
                    .log().body()
                    .body("data[0].id", is(1));
        });
    }

    @Test
    @Tag("API")
    @Owner("imbaReaver")
    @Feature("Users list")
    @DisplayName("Single resource is empty")
    void negativeResourceTest() {
        step("Check that single resource is empty (API)", () -> {
            given()
                    .filter(withCustomTemplates())
                    .log().uri()
                    .log().body()
                    .when()
                    .get("/api/unknown/23")
                    .then()
                    .log().status()
                    .log().body()
                    .body("isEmpty()", is(true));
        });
    }

    @Test
    @Tag("API")
    @Owner("imbaReaver")
    @Feature("Registration")
    @DisplayName("Partially filled registration form should give error")
    void negativeRegistrationPlainTest() {
        step("Partially fill registration form and check error (API)", () -> {
            String body = "{ \"email\": \"eve.holt@reqres.in\" }";
            given()
                    .contentType(JSON)
                    .filter(withCustomTemplates())
                    .body(body)
                    .log().uri()
                    .log().body()
                    .when()
                    .post("/api/register")
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(400)
                    .body("error", is("Missing password"));
        });
    }

    @Test
    @Tag("API")
    @Owner("imbaReaver")
    @Feature("Registration")
    @DisplayName("Partially filled registration form should give error")
    void negativeRegistrationWithSpecsTest() {
        step("Partially fill registration form and check error with specs and models (API)", () -> {
            RegistrationBodyLombokModel body = new RegistrationBodyLombokModel();
            body.setEmail("neo");
            RegistrationResponseLombokModel response = given()
                    .filter(withCustomTemplates())
                    .spec(registrationRequestSpec)
                    .body(body)
                    .when()
                    .post("/api/register")
                    .then()
                    .spec(registrationResponseSpec)
                    .extract()
                    .as(RegistrationResponseLombokModel.class);
            assertThat(response.getError()).isEqualTo("Missing password");
        });
    }
}
