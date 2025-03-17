package com.arz.api.tests.get;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.HttpHeader;
import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class APIResponseHeadersTest {

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
  public void getHeadersTest() {
    APIResponse response = requestContext.get("https://gorest.co.in/public/v2/users");

    int statusCode = response.status();
    System.out.println("Response status code : " + statusCode);
    Assert.assertEquals(statusCode, 200);
    Assert.assertEquals(response.ok(), true);

    //Wrap in map
    Map<String, String> headersMap = response.headers();
    headersMap.forEach((k, v) -> {
      System.out.println("Headers Map : " + "key: " + k + " value :" + v);
    });
    Assert.assertEquals(headersMap.size(), 31);

    //wrap in list
    List<HttpHeader> headerList = response.headersArray();
    headerList.forEach(httpHeader -> System.out.println("Headers in list : Name:"
        + httpHeader.name + " Value: " +  httpHeader.value));
  }

  @AfterTest
  public void tearDown() {
    playwright.close();
  }
}
