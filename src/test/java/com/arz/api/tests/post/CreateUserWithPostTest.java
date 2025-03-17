package com.arz.api.tests.post;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class CreateUserWithPostTest {

  Playwright playwright;
  APIRequest request;
  APIRequestContext requestContext;

  private static String randomizedEmail;

  @BeforeTest
  public void setup() {
    playwright = Playwright.create();
    request = playwright.request();
    requestContext = request.newContext();
  }

  public static String getRandomEmail() {
    randomizedEmail = "testusereka" + System.currentTimeMillis() + "@gmail.com";
    return randomizedEmail;
  }

  @Test
  public void createUserTest() throws IOException {
    Map<String, Object> toSend = new HashMap<String, Object>();
    toSend.put("name", "usertesteka");
    toSend.put("gender", "male");
    toSend.put("email", getRandomEmail());
    toSend.put("status", "active");

    APIResponse response = requestContext.post("https://gorest.co.in/public/v2/users",
        RequestOptions.create()
            .setHeader("Content-Type", "application/json")
            .setHeader("Authorization",
                "Bearer 90ba9479e99dfdd40ea75e436b87947b61c7add699ea38aea8d74c3f9dd62d2b")
            .setData(toSend));

    System.out.println(response.status());
    System.out.println(response.text());
    Assert.assertEquals(response.status(), 201);
    Assert.assertEquals(response.statusText(), "Created");

    //Read Response
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode responseNode = objectMapper.readTree(response.body());
    System.out.println(responseNode.toPrettyString());

    //Capture specific property
    Long userId = responseNode.get("id").longValue();
    System.out.println("User ID : " + userId);

    //Fetch created user
    System.out.println("==========Fetch User Data==========");

    APIResponse getUserResponse = requestContext.get(
        "https://gorest.co.in/public/v2/users/" + userId,
        RequestOptions.create()
            .setHeader("Authorization",
                "Bearer 90ba9479e99dfdd40ea75e436b87947b61c7add699ea38aea8d74c3f9dd62d2b"));

    Assert.assertEquals(getUserResponse.status(), 200);
    Assert.assertEquals(getUserResponse.statusText(), "OK");
    System.out.println(getUserResponse.text());
    Assert.assertTrue(getUserResponse.text().contains("testusereka"));
    Assert.assertTrue(getUserResponse.text().contains(randomizedEmail));
  }

  @AfterTest
  public void tearDown() {
    playwright.close();
  }
}
