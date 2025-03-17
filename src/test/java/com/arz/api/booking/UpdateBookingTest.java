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

public class UpdateBookingTest {

  Playwright playwright;
  APIRequest request;
  APIRequestContext requestContext;

  private static String tokenId;

  @BeforeTest
  public void setup() throws IOException {
    playwright = Playwright.create();
    request = playwright.request();
    requestContext = request.newContext();

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
    tokenId = responseNode.get("token").asText();
    System.out.println("Token ID : " + tokenId);
    Assert.assertNotNull(tokenId);
  }

  @Test
  public void updateBookingTest() {
    final String request = """
        {
            "firstname" : "James",
            "lastname" : "Brown",
            "totalprice" : 200,
            "depositpaid" : true,
            "bookingdates" : {
                "checkin" : "2025-03-03",
                "checkout" : "2025-03-03"
            },
            "additionalneeds" : "Lunch"
        }""";
    APIResponse response = requestContext.put("https://restful-booker.herokuapp.com/booking/1",
        RequestOptions.create()
            .setHeader("Content-Type", "application/json")
            .setHeader("Cookie", "token=" + tokenId)
            .setData(request));

    System.out.println("Response status text : " + response.statusText());
    System.out.println("Response : " + response.text());
    Assert.assertEquals(response.status(), 200);
  }

  @AfterTest
  public void tearDown() {
    playwright.close();
  }
}
