package tests;

import com.codeborne.selenide.Configuration;
import config.Project;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static helpers.CustomApiListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

@DisplayName("API and UI tests")
public class ExamplePageTests extends TestBase {
    @BeforeAll
    static void configureBaseUrl() {
        RestAssured.baseURI = Project.config.apiUrl();
        Configuration.baseUrl = Project.config.webUrl();
    }

    @Test
    @Tag("API and UI")
    @Owner("imbaReaver")
    @Feature("Users list")
    @DisplayName("The 3rd card email should be emma.wong@reqres.in")
    void successfulListTest() {
        step("Extracting user email (API)", () -> {
            String email = given()
                    .filter(withCustomTemplates())
                    .log().uri()
                    .log().body()
                    .when()
                    .get("/api/users?page=1")
                    .then()
                    .statusCode(200)
                    .log().all()
                    .extract().path("data[2].email");

            step("The 3rd card email should be emma.wong@reqres.in (UI)", () -> {
                examplePage.openPage()
                        .checkEmail(email);
            });
        });
    }
}
