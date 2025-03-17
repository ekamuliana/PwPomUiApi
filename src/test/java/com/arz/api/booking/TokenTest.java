package com.arz.api.booking;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import java.io.IOException;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TokenTest {

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
  public void createTokenTest() throws IOException {

    final String request = """
        {
            "username" : "admin",
            "password" : "password123"
        }
        """;

    APIResponse response = requestContext.post("https://restful-booker.herokuapp.com/auth",
        RequestOptions.create()
            .setHeader("Content-Type", "application/json")
            .setData(request));

    Assert.assertEquals(response.status(), 200);
    Assert.assertEquals(response.statusText(), "OK");

    ObjectMapper mapper = new ObjectMapper();
    JsonNode responseNode = mapper.readTree(response.body());
    System.out.println(responseNode.toPrettyString());

    //Retrieve token
    String tokenId = responseNode.get("token").asText();
    System.out.println("Token ID : " + tokenId);
    Assert.assertNotNull(tokenId);
  }

  @AfterTest
  public void tearDown() {
    playwright.close();
  }


}
