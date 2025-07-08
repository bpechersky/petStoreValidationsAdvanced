package com.petstore.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class StoreInventoryTest {

    @Test
    public void getStoreInventoryTest() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";

        Response response = given()
                .accept("application/json")
                .when()
                .get("/store/inventory")
                .then()
                .statusCode(200)
                .body("$", notNullValue())
                .body("available", greaterThanOrEqualTo(0))
                .body("pending", greaterThanOrEqualTo(0))
                .body("sold", greaterThanOrEqualTo(0))
                .extract().response();

        // Optional: Log entire inventory for debugging
        Map<String, Integer> inventory = response.jsonPath().getMap("$");
        System.out.println("Inventory counts:");
        inventory.forEach((status, count) -> System.out.println(status + ": " + count));
    }
}
