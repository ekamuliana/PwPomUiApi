package com.arz.api.tests.post;

import com.arz.api.data.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class CreateUserWithJsonFileTest {

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
  public void createUserWithPOJOTest() throws IOException {
    //create user object, can also put json as string here to put in context request:
    //get json file:
    byte[] bytesRequest = null;
    File file = new File("./src/test/data/user.json");
    bytesRequest = Files.readAllBytes(file.toPath());

    APIResponse response = requestContext.post("https://gorest.co.in/public/v2/users",
        RequestOptions.create()
            .setHeader("Content-Type", "application/json")
            .setHeader("Authorization",
                "Bearer 90ba9479e99dfdd40ea75e436b87947b61c7add699ea38aea8d74c3f9dd62d2b")
            .setData(bytesRequest));

    System.out.println(response.status());
    Assert.assertEquals(response.status(), 201);
    Assert.assertEquals(response.statusText(), "Created");

    String responseText = response.text();
    System.out.println("Response text : " + responseText);

    //convert response text/json to POJO -- desrialization
    ObjectMapper objectMapper = new ObjectMapper();
    User actualUser = objectMapper.readValue(responseText, User.class);
    User user = objectMapper.readValue(bytesRequest, User.class);
    System.out.println("actual user from the response---->");
    System.out.println(actualUser);

    Assert.assertEquals(actualUser.getName(), user.getName());
    Assert.assertEquals(actualUser.getEmail(), user.getEmail());
    Assert.assertEquals(actualUser.getStatus(), user.getStatus());
    Assert.assertEquals(actualUser.getGender(), user.getGender());
    Assert.assertNotNull(actualUser.getId());

  }

  @AfterTest
  public void tearDown() {
    playwright.close();
  }

}
