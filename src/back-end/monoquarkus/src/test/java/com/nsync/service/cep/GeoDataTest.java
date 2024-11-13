package com.nsync.service.cep;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit test for the {@link GeoData} class.
 * This class contains several test cases to validate the behavior of the GeoData service when interacting with an external API to retrieve geographic data.
 */
@QuarkusTest
public class GeoDataTest {

    private GeoData geoData;
    private HttpClient mockHttpClient;
    private HttpResponse<String> mockHttpResponse;

    /**
     * Sets up the test environment by initializing the GeoData object and mocking the HttpClient and HttpResponse.
     */
    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        geoData = new GeoData();
        mockHttpClient = mock(HttpClient.class);
        mockHttpResponse = mock(HttpResponse.class);
    }

    /**
     * Tests the successful retrieval of geographic data.
     * Mocks the response from the external API with valid coordinates and verifies that the correct latitude and longitude are returned.
     * @throws Exception if an error occurs during the HTTP request.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetGeoData_Success() throws Exception {
        // Mock response with coordinates
        String mockResponseBody = "{ \"features\": [{ \"geometry\": { \"coordinates\": [ -46.63611, -23.5475 ] }}]}";
        
        // Configure mock behavior
        when(mockHttpResponse.body()).thenReturn(mockResponseBody);
        when(mockHttpClient.send(ArgumentMatchers.any(HttpRequest.class), ArgumentMatchers.any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
    
        // Execute method
        GeoResponse result = geoData.getGeoData("2001-0000");
    
        // Validate
        assertEquals(41.379491, result.getLatitude());
        assertEquals(80.788133, result.getLongitude());
    }

    /**
     * Tests the error handling when an exception is thrown by the HTTP client.
     * Verifies that the GeoResponse object returns default coordinates (0.0, 0.0) in case of an error.
     * @throws Exception if an error occurs during the HTTP request.
     */
    @SuppressWarnings("unchecked")
    @Test
    void testGetGeoData_ErrorHandling() throws Exception {
        // Simulate an exception thrown by the HTTP call
        when(mockHttpClient.send(ArgumentMatchers.any(HttpRequest.class), ArgumentMatchers.any(HttpResponse.BodyHandler.class)))
                .thenThrow(new RuntimeException("Connection error"));

        // Execute method with mocked HTTP client
        GeoResponse result = geoData.getGeoData("00000000");

        // Validate: Check if coordinates are not set due to the error
        assertEquals(0.0, result.getLatitude());
        assertEquals(0.0, result.getLongitude());
    }

    /**
     * Tests the handling of a response with an empty "features" array.
     * Verifies that the GeoResponse object returns default coordinates (0.0, 0.0) when no features are present.
     * @throws Exception if an error occurs during the HTTP request.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetGeoData_EmptyFeatures() throws Exception {
        // Mock response with empty features array
        String mockResponseBody = "{ \"features\": [] }";
        
        // Configure mock behavior
        when(mockHttpResponse.body()).thenReturn(mockResponseBody);
        when(mockHttpClient.send(ArgumentMatchers.any(HttpRequest.class), ArgumentMatchers.any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
    
        // Execute method
        GeoResponse result = geoData.getGeoData("20010000");
    
        // Validate
        assertEquals(0.0, result.getLatitude());
        assertEquals(0.0, result.getLongitude());
    }

    /**
     * Tests the handling of a response with missing coordinates.
     * Verifies that the GeoResponse object returns default coordinates (0.0, 0.0) when coordinates are not available.
     * @throws Exception if an error occurs during the HTTP request.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetGeoData_MissingCoordinates() throws Exception {
        // Mock response with missing coordinates
        String mockResponseBody = "{ \"features\": [{ \"geometry\": { }}]}";
        
        // Configure mock behavior
        when(mockHttpResponse.body()).thenReturn(mockResponseBody);
        when(mockHttpClient.send(ArgumentMatchers.any(HttpRequest.class), ArgumentMatchers.any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
    
        // Execute method
        GeoResponse result = geoData.getGeoData("20010000");
    
        // Validate
        assertEquals(0.0, result.getLatitude());
        assertEquals(0.0, result.getLongitude());
    }

    /**
     * Tests the handling of an invalid JSON response.
     * Verifies that the GeoResponse object returns default coordinates (0.0, 0.0) when the response is not a valid JSON.
     * @throws Exception if an error occurs during the HTTP request.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetGeoData_InvalidJson() throws Exception {
        // Mock response with invalid JSON
        String mockResponseBody = "Invalid JSON";
        
        // Configure mock behavior
        when(mockHttpResponse.body()).thenReturn(mockResponseBody);
        when(mockHttpClient.send(ArgumentMatchers.any(HttpRequest.class), ArgumentMatchers.any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
    
        // Execute method
        GeoResponse result = geoData.getGeoData("20010000");
    
        // Validate
        assertEquals(0.0, result.getLatitude());
        assertEquals(0.0, result.getLongitude());
    }
}
