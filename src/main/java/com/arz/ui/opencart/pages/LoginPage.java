package com.arz.ui.opencart.pages;

import com.microsoft.playwright.Page;

public class LoginPage {

  private Page page;

  //1. String Locators - OR
  private String emailId = "//input[@id='input-email']";
  private String passwordId = "//input[@id='input-password']";
  private String loginBtn = "//input[@value='Login']";
  private String forgotPasswordLink = "//div[@class='form-group']//a[normalize-space()='Forgotten Password']";
  private String logoutBtn = "//a[@class='list-group-item'][normalize-space()='Logout']";

  //2. Page Constructor
  public LoginPage(Page page) {
    this.page = page;
  }

  //3. Page actions/methods
  public String getLoginPageTitle() {
    return page.title();
  }

  public boolean isForgotPasswordLinkExist() {
    return page.isVisible(forgotPasswordLink);
  }

  public boolean doLogin(String username, String password) {
    System.out.println("App Credential : " + username + " " + password);
    page.fill(emailId, username);
    page.fill(passwordId, password);
    page.click(loginBtn);
    if (page.isVisible(logoutBtn)) {
      System.out.println("User is logged in successfully");
      return true;
    } else {
      return false;
    }
  }

}
