package com.arz.ui.opencart.pages;

import com.microsoft.playwright.Page;

public class HomePage {
  private Page page;

  //1. String Locators - Object Repository
  private String search = "input[name='search']";
  private String searchIcon = "div#search button";
  private String searchPageHeader = "div#content h1";
  private String myAccountDropdown = "a[title='My Account']";
  private String loginLink = "a:text('Login')";


  //2. Page constructor
  public HomePage(Page page) {
    this.page = page;
  }

  //3. Page actions/methods
  public String getHomePageTitle(){
    System.out.println("Page Title : " + page.title());
    return page.title();
  }

  public String getHomePageURL(){
    System.out.println("Page URL : " + page.url());
    return page.url();
  }

  public String doSearch(String productName){
    page.fill(search, productName);
    page.click(searchIcon);
    System.out.println("Search header is : " + page.textContent(searchPageHeader));
    return page.textContent(searchPageHeader);
  }

  public LoginPage navigateToLoginPage(){
    page.click(myAccountDropdown);
    page.click(loginLink);
    return new LoginPage(page);
  }
}
