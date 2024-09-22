package com.almosafer.qa.tests;

import com.almosafer.qa.utils.ApiUtils;
import com.almosafer.qa.utils.ConfigLoader;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class ApiTests {

    // Declare static variables for headers and base URL to be used in all tests
    private static Map<String, String> headers;
    private static String baseUrl;

    @BeforeClass
    public static void setup() {
        // Load configuration from the config.json file located in resources
        // The config contains API base URL, authorization tokens, and any additional headers
        JSONObject config = ConfigLoader.loadConfig("src/main/resources/config.json");
        
        // Extract and set the base URL for the API
        baseUrl = config.getString("baseUrl");

        // Initialize headers map and add the authorization header from the config file
        headers = new HashMap<>();
        headers.put("Authorization", config.getString("authorization"));

        // Load any additional headers from the config and add them to the headers map
        JSONObject configHeaders = config.getJSONObject("headers");
        for (String key : configHeaders.keySet()) {
            headers.put(key, configHeaders.getString(key));
        }
    }

    // Test case for retrieving a list of currencies via a GET API call
    @Test
    public void testGetCurrencyList() {
        String endpoint = "/system/currency/list"; // Define the endpoint for the currency list API

        // Perform a GET request to the API with the base URL, endpoint, and headers
        Response response = ApiUtils.getApi(baseUrl + endpoint, headers);

        // Assert that the response status code is 200 (Success)
        assertEquals(200, response.getStatusCode());
        System.out.println("**Currency list retrieved successfully!**");

        // Assert that the response body is not null and print it for verification
        assertNotNull("Response body should not be null", response.getBody().asString());
        String responseBody = response.getBody().asString();
        System.out.println("  - Response Body:\n" + responseBody);

        // Parse the response body as JSON to validate specific fields
        JSONObject jsonResponse = new JSONObject(responseBody);
        
        // Check if the base currency exists and assert that it's "SAR" (Saudi Riyal)
        JSONObject baseCurrency = jsonResponse.getJSONObject("base");
        assertNotNull(baseCurrency);
        assertEquals("SAR", baseCurrency.getString("code"));
        assertEquals("Saudi Riyal", baseCurrency.getString("name"));

        // Verify the list of equivalent currencies and ensure it's not empty
        JSONArray equivalentCurrencies = jsonResponse.getJSONArray("equivalent");
        assertTrue("Equivalent currencies should not be empty", equivalentCurrencies.length() > 0);
        
        // Loop through each currency and check that the currency code exists and the exchange rate is valid
        for (int i = 0; i < equivalentCurrencies.length(); i++) {
            JSONObject currency = equivalentCurrencies.getJSONObject(i);
            assertNotNull(currency.getString("code"));
            assertTrue("Rate should be greater than 0", currency.getDouble("rate") > 0);
        }
    }

    // Test case for making a POST request to book flights and retrieve flight fare information
    @Test
    public void testPostFlights() {
        // Load the request body from an external JSON file containing predefined flight search parameters
        JSONObject requestBody = ConfigLoader.loadConfig("src/main/resources/request_body.json");

        // Get today's date using the LocalDate class and format it as "yyyy-MM-dd" for the API request
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Set dynamic start and end dates for the flight search; for example, search from today to 9 days later
        String startDate = today.format(formatter);
        String endDate = today.plusDays(9).format(formatter);

        // Update the request body with these dynamically generated dates in the flight search criteria
        requestBody.getJSONArray("leg").getJSONObject(0).put("departureFrom", startDate);
        requestBody.getJSONArray("leg").getJSONObject(0).put("departureTo", endDate);

        // Define the endpoint for retrieving flight fare information
        String endpoint = "/v3/flights/flight/get-fares-calender";
        
        // Perform a POST request with the base URL, endpoint, headers, and the request body
        Response response = ApiUtils.postApi(baseUrl + endpoint, headers, requestBody);

        // Assert that the response status code is 200 (Success)
        assertEquals(200, response.getStatusCode());
        System.out.println("**Flight booking successful!**");

        // Assert that the response body is not null and print it for verification
        assertNotNull("Response body should not be null", response.getBody().asString());
        String responseBody = response.getBody().asString();
        System.out.println("  - Response Body:\n" + responseBody);

        // Parse the response body as JSON to check if it contains the expected flight details
        JSONObject jsonResponse = new JSONObject(responseBody);
        
        // Ensure that the response contains data by checking its length
        assertTrue("Response should contain booking details", jsonResponse.length() > 0);
        
        // Loop through each date in the response and verify that the flight details (price, airline) are present and valid
        for (String date : jsonResponse.keySet()) {
            JSONObject flightDetails = jsonResponse.getJSONObject(date);
            assertNotNull(flightDetails.getDouble("price"));
            assertTrue("Price should be greater than 0", flightDetails.getDouble("price") > 0);
            assertNotNull(flightDetails.getString("airline"));
        }
    }
}
