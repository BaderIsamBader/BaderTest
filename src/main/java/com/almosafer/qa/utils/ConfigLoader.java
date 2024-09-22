package com.almosafer.qa.utils;

import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigLoader {

    // Loads a JSON configuration file from the specified file path
    public static JSONObject loadConfig(String filePath) {
        try {
            // Read the file content as a string
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            // Convert the string content to a JSONObject and return it
            return new JSONObject(content);
        } catch (Exception e) {
            // Print any exception that occurs and return null if an error happens
            e.printStackTrace();
            return null;
        }
    }
}
