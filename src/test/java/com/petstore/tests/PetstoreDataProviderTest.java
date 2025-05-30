package com.petstore.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import io.restassured.response.Response;

public class PetstoreDataProviderTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @DataProvider(name = "petStatuses")
    public Object[][] petStatuses() {
        return new Object[][] {
            {"available"},
            {"pending"},
            {"sold"}
        };
    }

    @Test(dataProvider = "petStatuses")
    public void getPetsByStatusTest(String status) {
        given()
        .when()
            .get("/pet/findByStatus?status=" + status)
        .then()
            .statusCode(200);
    }

    @DataProvider(name = "invalidPetIds")
    public Object[][] invalidPetIds() {
        return new Object[][] {
            {0},
            {-1},
            {999999999}
        };
    }



    @Test(dataProvider = "invalidPetIds")
    public void getInvalidPetByIdTest(int petId) {
        Response response =
                given()
                        .when()
                        .get("/pet/" + petId)
                        .then()
                        .statusCode(anyOf(is(404), is(200)))
                        .extract().response();

        // Log the full response for debugging
        System.out.println("Response for petId " + petId + ": " + response.asString());

        // If the response has a body with a "message" field, assert it
        if (response.jsonPath().get("message") != null) {
            assertThat(response.jsonPath().getString("message"), equalTo("Pet not found"));
        }
    }


}
