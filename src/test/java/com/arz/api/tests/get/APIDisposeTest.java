package com.arz.api.tests.get;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class APIDisposeTest {

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
  public void disposeResponseTest() {
    APIResponse response = requestContext.get("https://gorest.co.in/public/v2/users");

    int statusCode = response.status();
    System.out.println("Response status code : " + statusCode);
    Assert.assertEquals(statusCode, 200);
    Assert.assertEquals(response.ok(), true);

    String statusResponseText = response.statusText();
    System.out.println("Response status text : " + statusResponseText);
    System.out.println("Response in plain text : " + response.text());

    //Dispose will only dispose the body not status code, can be done in response or context level
    response.dispose();
    System.out.println("Response body after disposing : " + response.text());
    System.out.println("Response code after disposing : " + response.status());


  }

  @AfterTest
  public void tearDown() {
    playwright.close();
  }

}
