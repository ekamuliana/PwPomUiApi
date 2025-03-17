package com.arz.ui.opencart;

import com.arz.ui.opencart.base.BaseTest;
import com.arz.ui.opencart.constant.AppConstants;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HomePageTest extends BaseTest {

  @DataProvider
  public Object[][] getProductData() {
    return new Object[][]{
        {"Macbook"},
        {"iMac"},
        {"Samsung"}
    };
  }

  @Test
  public void homePageTitleTest() {
    String actualTitle = homePage.getHomePageTitle();
    Assert.assertEquals(actualTitle, AppConstants.HOME_PAGE_TITLE);
  }

  @Test
  public void homePageUrlTest() {
    String actualUrl = homePage.getHomePageURL();
    Assert.assertEquals(actualUrl, properties.getProperty("url"));
  }

  @Test(dataProvider = "getProductData")
  public void searchTest(String productName) {
    String actualSearchHeader = homePage.doSearch(productName);
    Assert.assertEquals(actualSearchHeader, "Search - " + productName);
  }

}
