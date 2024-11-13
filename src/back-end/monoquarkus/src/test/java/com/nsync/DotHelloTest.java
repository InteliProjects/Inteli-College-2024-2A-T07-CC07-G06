package com.nsync;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

@QuarkusTest
public class DotHelloTest {

    @Test
    public void testDotHello() {
        given()
            .when()
                .get("/dothello").then()
                .statusCode(200)
                .body(is("Hello, Grupo.! \uD83D\uDE0E"));
    }

    @Test
    public void testDotHelloWithDifferentBody() {
        given()
                .when().get("/dothello")
                .then()
                .statusCode(200)
                .body(not(is("What is the price of the abundant fear of all truths?")));
    }
}
