package com.almosafer.qa.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;

import java.util.Map;

public class ApiUtils {

    // Sends a GET request to the given URL with the provided headers
    public static Response getApi(String url, Map<String, String> headers) {
        // Use RestAssured to send a GET request with headers and return the response
        return RestAssured.given().headers(headers).get(url);
    }

    // Sends a POST request to the given URL with the provided headers and JSON body
    public static Response postApi(String url, Map<String, String> headers, JSONObject body) {
        // Use RestAssured to send a POST request with headers and a JSON body, and return the response
        return RestAssured.given().headers(headers).body(body.toString()).post(url);
    }
}
