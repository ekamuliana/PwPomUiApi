package com.arz.api.tests.put;

import com.arz.api.data.User;
import com.arz.api.data.UserLombok;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class UpdateUserTest {

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
  public void updateUserTest() throws JsonProcessingException {

    //create users object: using builder pattern:
    UserLombok ul = UserLombok.builder()
        .name("testusereka")
        .email(getRandomEmail())
        .gender("male")
        .status("active").build();

    //1. POST Call: create a user
    APIResponse createResp = requestContext.post("https://gorest.co.in/public/v2/users",
        RequestOptions.create()
            .setHeader("Content-Type", "application/json")
            .setHeader("Authorization", "Bearer 90ba9479e99dfdd40ea75e436b87947b61c7add699ea38aea8d74c3f9dd62d2b")
            .setData(ul));

    System.out.println(createResp.url());
    System.out.println(createResp.status());
    Assert.assertEquals(createResp.status(), 201);
    Assert.assertEquals(createResp.statusText(), "Created");

    String responseText = createResp.text();
    System.out.println(responseText);

    //map response text/json to POJO
    ObjectMapper objectMapper = new ObjectMapper();
    User actualUser = objectMapper.readValue(responseText, User.class);
    System.out.println("User from response : ");
    System.out.println(actualUser);

    Assert.assertEquals(actualUser.getName(), ul.getName());
    Assert.assertEquals(actualUser.getEmail(), ul.getEmail());
    Assert.assertEquals(actualUser.getStatus(), ul.getStatus());
    Assert.assertEquals(actualUser.getGender(), ul.getGender());
    Assert.assertNotNull(actualUser.getId());

    String userId = actualUser.getId();
    System.out.println("new user id is : " + userId);

    //update status active to inactive
    ul.setStatus("inactive");
    ul.setName("testusereka");

    System.out.println("---------------PUT CALL----------------");
    //2. PUT Call - update user:
    APIResponse putResp = requestContext.put("https://gorest.co.in/public/v2/users/" + userId,
        RequestOptions.create()
            .setHeader("Content-Type", "application/json")
            .setHeader("Authorization", "Bearer 90ba9479e99dfdd40ea75e436b87947b61c7add699ea38aea8d74c3f9dd62d2b")
            .setData(ul));

    System.out.println(putResp.status() + " : " + putResp.statusText());
    Assert.assertEquals(putResp.status(), 200);

    String putResponseText = putResp.text();
    System.out.println("Updated user : " + putResponseText);

    User actPutUser = objectMapper.readValue(putResponseText, User.class);
    Assert.assertEquals(actPutUser.getId(), userId);
    Assert.assertEquals(actPutUser.getStatus(), ul.getStatus());
    Assert.assertEquals(actPutUser.getName(), ul.getName());

    System.out.println("---------------GET CALL----------------");

    //3. Get the updates user with GET CALL:
    APIResponse getResp = requestContext.get("https://gorest.co.in/public/v2/users/"+userId,
        RequestOptions.create()
            .setHeader("Authorization", "Bearer 90ba9479e99dfdd40ea75e436b87947b61c7add699ea38aea8d74c3f9dd62d2b"));

    System.out.println(getResp.url());

    int statusCode = getResp.status();
    System.out.println("response status code: " + statusCode);
    Assert.assertEquals(statusCode, 200);
    Assert.assertEquals(getResp.ok(), true);

    String statusGETStatusText = getResp.statusText();
    System.out.println(statusGETStatusText);

    String getResponseText = getResp.text();

    User updatedUser = objectMapper.readValue(getResponseText, User.class);
    Assert.assertEquals(updatedUser.getId(), userId);
    Assert.assertEquals(updatedUser.getStatus(), ul.getStatus());
    Assert.assertEquals(updatedUser.getName(), ul.getName());

  }

  @AfterTest
  public void tearDown() {
    playwright.close();
  }

}
