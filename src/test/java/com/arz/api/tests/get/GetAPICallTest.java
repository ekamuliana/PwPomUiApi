package com.arz.api.tests.get;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import java.io.IOException;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class GetAPICallTest {

  Playwright playwright;
  APIRequest request;
  APIRequestContext requestContext;

  @BeforeTest
  public void setup() {
    playwright = Playwright.create();
    request = playwright.request();
    requestContext = request.newContext();
  }

  @Test
  public void getUsersApiTest() throws IOException {
    APIResponse response = requestContext.get("https://gorest.co.in/public/v2/users");

    int statusCode = response.status();
    System.out.println("Response status code : " + statusCode);

    String statusResponseText = response.statusText();
    System.out.println("Response status text : " + statusResponseText);

    //To wrap in a class
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonResponse = objectMapper.readTree(response.body());
    System.out.println("Json pretty response : " + jsonResponse.toPrettyString());
    System.out.println("\nAPI Response URL" + response.url());

    System.out.println("API response in plain text : " + response.text());

    Map<String, String> headers = response.headers();
    System.out.println("\nResponse header " + headers);
    Assert.assertEquals(headers.get("content-type"), "application/json; charset=utf-8");
  }

  @Test
  public void getSpecificUserTest() throws IOException {
    APIResponse response = requestContext.get("https://gorest.co.in/public/v2/users",
        RequestOptions.create()
            .setQueryParam("gender", "male")
            .setQueryParam("status", "active"));
    int statusCode = response.status();
    System.out.println("Response status code : " + statusCode);

    String statusResponseText = response.statusText();
    System.out.println("Response status text : " + statusResponseText);

    //To wrap in a class
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonResponse = objectMapper.readTree(response.body());
    System.out.println("Json pretty response : " + jsonResponse.toPrettyString());
    System.out.println("\nAPI Response URL" + response.url());

    Map<String, String> headers = response.headers();
    System.out.println("\nResponse header " + headers);
    Assert.assertEquals(headers.get("content-type"), "application/json; charset=utf-8");
  }

  @AfterTest
  public void tearDown() {
    playwright.close();
  }
}
