package com.petstore.tests;

import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PetstoreTest {

    @Test
    public void getPetsTest() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";

        given()
            .when()
            .get("/pet/findByStatus?status=available")
            .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
    }
}
