package com.petstore.tests;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PetstoreAdvancedTest {

    private final int testPetId = 999999;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @Test(priority = 1)
    public void createPetTest() {
        given()
            .header("Content-Type", "application/json")
            .body(new File("src/test/resources/data/new_pet.json"))
        .when()
            .post("/pet")
        .then()
            .statusCode(200)
            .body("id", equalTo(testPetId))
            .body("name", equalTo("Rex"))
            .body("status", equalTo("available"));
    }

    @Test(enabled = false)
    public void getPetByIdTest() {
        given()
        .when()
            .get("/pet/" + testPetId)
        .then()
            .statusCode(200)
            .body("id", equalTo(testPetId))
            .body("name", equalTo("Rex"))
            .body("status", equalTo("available"));
    }

    @Test(priority = 3)
    public void updatePetTest() {
        String updatedPetJson = String.format("{\"id\": %d, \"name\": \"Max\", \"status\": \"sold\"}", testPetId);

        given()
                .header("Content-Type", "application/json")
                .body(updatedPetJson)
                .when()
                .put("/pet")
                .then()
                .statusCode(200)
                .body("name", equalTo("Max"))
                .body("status", equalTo("sold"));
    }

    @Test(priority = 4)
    public void getNonExistentPetTest() {
        given()
        .when()
            .get("/pet/0")
        .then()
            .statusCode(404);
    }

    @Test(priority = 5)
    public void deletePetTest() {
        given()
        .when()
            .delete("/pet/" + testPetId)
        .then()
            .statusCode(200);
    }

    @Test(priority = 6)
    public void validateAvailablePetsSchema() {
        given()
        .when()
            .get("/pet/findByStatus?status=available")
        .then()
            .statusCode(200)
            .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/schemas/availablePets.schema.json")));
    }
}
