package com.arz.api.tests.delete;

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

public class DeleteUserTest {

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
  public void deleteUserTest() throws JsonProcessingException {
    //create user object, can also put json as string here to put in context request:
    User user = new User("testusereka", getRandomEmail(), "male", "active");
    UserLombok ul = UserLombok.builder()
        .name("testusereka")
        .email(randomizedEmail)
        .gender("male")
        .status("active")
        .build();

    APIResponse response = requestContext.post("https://gorest.co.in/public/v2/users",
        RequestOptions.create()
            .setHeader("Content-Type", "application/json")
            .setHeader("Authorization",
                "Bearer 90ba9479e99dfdd40ea75e436b87947b61c7add699ea38aea8d74c3f9dd62d2b")
            .setData(user));

    System.out.println(response.status());
    Assert.assertEquals(response.status(), 201);
    Assert.assertEquals(response.statusText(), "Created");

    String responseText = response.text();
    System.out.println("Response text : " + responseText);

    //convert response text/json to POJO -- desrialization
    ObjectMapper objectMapper = new ObjectMapper();
    User actualUser = objectMapper.readValue(responseText, User.class);
    System.out.println("actual user from the response---->");
    System.out.println(actualUser);

    Assert.assertEquals(actualUser.getName(), ul.getName());
    Assert.assertEquals(actualUser.getEmail(), ul.getEmail());
    Assert.assertEquals(actualUser.getStatus(), ul.getStatus());
    Assert.assertEquals(actualUser.getGender(), ul.getGender());
    Assert.assertNotNull(actualUser.getId());

    //2. delete user -- user id -- 204

    APIResponse deleteResponse = requestContext.delete(
        "https://gorest.co.in/public/v2/users/" + actualUser.getId(),
        RequestOptions.create()
            .setHeader("Authorization",
                "Bearer 90ba9479e99dfdd40ea75e436b87947b61c7add699ea38aea8d74c3f9dd62d2b")
    );

    System.out.println("Delete Response Status : " + deleteResponse.status());
    System.out.println("Delete Response Status Text " + deleteResponse.statusText());

    Assert.assertEquals(deleteResponse.status(), 204);

    System.out.println("delete user response body ====" + deleteResponse.text());

    //3. get user -- user id -- 404
    APIResponse apiResponse = requestContext.get("https://gorest.co.in/public/v2/users/" + actualUser.getId(),
        RequestOptions.create()
            .setHeader("Authorization",
                "Bearer e4b8e1f593dc4a731a153c5ec8cc9b8bbb583ae964ce650a741113091b4e2ac6")
    );

    System.out.println(apiResponse.text());

    int statusCode = apiResponse.status();
    System.out.println("response status code: " + statusCode);
    Assert.assertEquals(statusCode, 404);
    Assert.assertEquals(apiResponse.statusText(), "Not Found");

    Assert.assertTrue(apiResponse.text().contains("Resource not found"));
  }


  @AfterTest
  public void tearDown() {
    playwright.close();
  }
}
