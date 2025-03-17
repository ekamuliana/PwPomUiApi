package com.arz.ui.opencart;

import com.arz.ui.opencart.base.BaseTest;
import com.arz.ui.opencart.constant.AppConstants;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginPageTest extends BaseTest {

  @Test(priority = 1)
  public void loginPageNavigationTest() {
    loginPage = homePage.navigateToLoginPage();
    String actualTitle = loginPage.getLoginPageTitle();
    System.out.println("Page actual title : " + actualTitle);
    Assert.assertEquals(actualTitle, AppConstants.Login_PAGE_TITLE);
  }

  @Test(priority = 2)
  public void forgotPasswordLinkExistTest() {
    Assert.assertTrue(loginPage.isForgotPasswordLinkExist());
  }

  @Test(priority = 3)
  public void appLoginTest() {
    Assert.assertTrue(loginPage.doLogin(properties.getProperty("username").trim(),
        properties.getProperty("password").trim()));
  }

}
