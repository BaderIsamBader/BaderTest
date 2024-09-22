# API Tests Project for Eclipse

## Overview
The API Tests Project is designed to validate the functionality of various APIs using RestAssured and JUnit. It includes tests for retrieving the currency list and posting flight data, ensuring that the APIs return the expected results. **Comments have been added to all Java files to explain the code logic and functionality.**

## Project Structure
- `src/main/resources/config.json`: Configuration file containing API headers and base URL.
- `src/main/resources/request_body.json`: JSON request body for POST requests.
- `src/test/java/com/almosafer/qa/tests/ApiTests.java`: Main test class that contains the API tests.
- `src/main/java/com/almosafer/qa/utils/ApiUtils.java`: Utility class for making API requests (GET, POST).
- `src/main/java/com/almosafer/qa/utils/ConfigLoader.java`: Utility class for loading configuration files.
- `pom.xml`: Maven project configuration file.

## Prerequisites
- Java Development Kit (JDK) 11 or higher.
- Maven 3.6.0 or higher.
- Eclipse IDE with Maven support.

## Importing the Project into Eclipse
1. Open Eclipse IDE.
2. Go to **File** > **Import**.
3. Select **Existing Maven Projects** and click **Next**.
4. Browse to the directory where the project is located and click **Finish**.

## Building the Project
- Right-click on the project in the Project Explorer and select **Run As** > **Maven build...**.
- In the Goals field, enter `clean install` and click **Run**.

## Running Tests
- Right-click on the `ApiTests.java` file in the Project Explorer.
- Select **Run As** > **JUnit Test** to execute the tests.

## Expected Reports
Test reports can be found in the `target/surefire-reports` directory after running the tests. The reports will include details of the test execution, including passed and failed tests, as well as additional logs for each test case.

## Configuration
The `config.json` file contains the following keys:
- `baseUrl`: The base URL for the API endpoints.
- `authorization`: The authorization token for accessing protected resources.
- Additional headers defined under the `headers` key.

Make sure to update the `config.json` file with the correct values for your testing environment.

## Example Test Cases
### Get Currency List
The `testGetCurrencyList` method sends a GET request to retrieve the currency list. It checks if the response status code is 200 and prints the result.

### Post Flights
The `testPostFlights` method loads a JSON request body from `request_body.json`, sets today's date as the departure date, and sends a POST request to fetch flight fares. It checks if the response status code is 200 and prints the result.
