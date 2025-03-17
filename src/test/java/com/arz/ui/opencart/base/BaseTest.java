package com.arz.ui.opencart.base;

import com.arz.ui.opencart.factory.PlaywrightFactory;
import com.arz.ui.opencart.pages.HomePage;
import com.arz.ui.opencart.pages.LoginPage;
import com.microsoft.playwright.Page;
import java.util.Properties;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class BaseTest {

  PlaywrightFactory factory;
  Page page;
  protected Properties properties;
  protected HomePage homePage;
  protected LoginPage loginPage;


  @BeforeTest
  public void setup() {
    factory = new PlaywrightFactory();
    properties = factory.initProperties();
    page = factory.initBrowser(properties);
    homePage = new HomePage(page);
  }


  @AfterTest
  public void tearDown() {
    page.context().browser().close();
  }
}
