package com.nsync.service.csv;

import com.nsync.exception.custom.MissingCsvFieldException;
import com.nsync.generic.service.csv.GenericCsvProcessorService;
import com.nsync.service.csv.mapper.DistributorMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

/**
 * Integration test for the CSV Processor REST API.
 * This class contains several test cases to validate the processing of CSV files for different resources.
 */
@QuarkusTest
public class CsvProcessorResourceTest {

    @SuppressWarnings("removal")
    @InjectMock
    GenericCsvProcessorService genericCsvProcessorService;

    /**
     * Sets up the test environment by resetting the mock service before each test case.
     */
    @BeforeEach
    public void setUp() {
        // Resetting mock, if needed
    }

    /**
     * Tests the successful processing of a valid distributor CSV file.
     * Verifies that the API returns an HTTP 202 Accepted status.
     * @throws IOException if an error occurs while reading the file.
     */
    @Test
    public void testProcessDistributorCsv_Success() throws IOException {
        // Arrange
        File validCsv = new File("src/test/resources/csvs/valid-distributor.csv");

        // Act and Assert
        given()
            .multiPart("file", validCsv, "text/csv")  // Pass File directly
            .contentType("multipart/form-data")
            .when()
            .post("/csv-processor/distributor")
            .then()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    /**
     * Tests the processing of a distributor CSV file with a missing field.
     * Verifies that the API returns an HTTP 400 Bad Request status when a required field is missing.
     * @throws IOException if an error occurs while reading the file.
     */
    @Test
    public void testProcessDistributorCsv_MissingField() throws IOException {
        // Arrange
        File invalidCsv = new File("src/test/resources/csvs/invalid-distributor-missing-field.csv");
        doThrow(new MissingCsvFieldException("Missing field"))
                .when(genericCsvProcessorService).processCsvFile(any(InputStream.class), any(DistributorMapper.class));
        
        // Act and Assert
        given()
            .multiPart("file", invalidCsv, "text/csv")  // Pass File directly
            .contentType("multipart/form-data")
            .when()
            .post("/csv-processor/distributor")
            .then()
            .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    /**
     * Tests the processing of a distributor CSV file that triggers an internal server error.
     * Verifies that the API returns an HTTP 500 Internal Server Error status.
     * @throws IOException if an error occurs while reading the file.
     */
    @Test
    public void testProcessDistributorCsv_InternalServerError() throws IOException {
        // Arrange
        File validCsv = new File("src/test/resources/csvs/valid-distributor.csv");
        doThrow(new RuntimeException("Unexpected error"))
                .when(genericCsvProcessorService).processCsvFile(any(InputStream.class), any(DistributorMapper.class));

        // Act and Assert
        given()
            .multiPart("file", validCsv, "text/csv")  // Pass File directly
            .contentType("multipart/form-data")
            .when()
            .post("/csv-processor/distributor")
            .then()
            .statusCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }

    /**
     * Tests the successful processing of a valid product CSV file.
     * Verifies that the API returns an HTTP 202 Accepted status.
     * @throws IOException if an error occurs while reading the file.
     */
    @Test
    public void testProcessProductCsv_Success() throws IOException {
        // Arrange
        File validCsv = new File("src/test/resources/csvs/valid-product.csv");

        // Act and Assert
        given()
            .multiPart("file", validCsv, "text/csv")  // Pass File directly
            .contentType("multipart/form-data")
            .when()
            .post("/csv-processor/product")
            .then()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    /**
     * Tests the successful processing of both distributor and product CSV files in one request.
     * Verifies that the API returns an HTTP 202 Accepted status.
     * @throws IOException if an error occurs while reading the files.
     */
    @Test
    public void testProcessAllCsvs_Success() throws IOException {
        // Arrange
        File validDistributorCsv = new File("src/test/resources/csvs/valid-distributor.csv");
        File validProductCsv = new File("src/test/resources/csvs/valid-product.csv");

        // Act and Assert
        given()
            .multiPart("fileDistributor", validDistributorCsv, "text/csv")  // Pass File directly
            .multiPart("fileProduct", validProductCsv, "text/csv")  // Pass File directly
            .contentType("multipart/form-data")
            .when()
            .post("/csv-processor/populate-all")
            .then()
            .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }
}
