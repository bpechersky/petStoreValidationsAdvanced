package com.petstore.tests;

import io.restassured.RestAssured;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class UploadPetImageTest {

    @Test
    public void uploadPetImage() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";

        File imageFile = new File("src/test/resources/petstore.PNG");

        given()
                .multiPart("file", imageFile, "image/png")
                .accept("application/json")
                .when()
                .post("/pet/1/uploadImage")
                .then()
                .statusCode(200)
                .body("message", containsString("petstore.PNG"));
    }
    @Test
    public void createValidPetTest() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";

        String payload = """
        {
            "id": 1,
            "name": "Fluffy",
            "status": "available"
        }
        """;

        given()
                .contentType("application/json")
                .accept("application/json")
                .body(payload)
                .when()
                .post("/pet")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", equalTo("Fluffy"))
                .body("status", equalTo("available"));
    }

}
