package com.arz.ui.opencart.factory;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.ScreenshotOptions;
import com.microsoft.playwright.Playwright;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class PlaywrightFactory {

  Playwright playwright;
  Browser browser;
  BrowserContext browserContext;
  Page page;
  Properties properties;

  private static ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
  private static ThreadLocal<BrowserContext> tlBrowserContext = new ThreadLocal<>();
  private static ThreadLocal<Page> tlPage = new ThreadLocal<>();
  private static ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();

  public static Playwright getPlaywright() {
    return tlPlaywright.get();
  }

  public static Browser getBrowser() {
    return tlBrowser.get();
  }

  public static BrowserContext getBrowserContext() {
    return tlBrowserContext.get();
  }

  public static Page getPage() {
    return tlPage.get();
  }

  public Page initBrowser(Properties prop) {
    String browserName = prop.getProperty("browser").trim();
    System.out.println("Browser browserName : " + browserName);

    //playwright = Playwright.create();
    tlPlaywright.set(Playwright.create());

    switch (browserName.toLowerCase()) {
      case "chrome":
      case "chromium":
        //this.browser = playwright.chromium().launch(new LaunchOptions().setHeadless(false));
        tlBrowser.set(getPlaywright().chromium().launch(new LaunchOptions().setHeadless(false)));
        break;
      case "firefox":
        //this.browser = playwright.firefox().launch(new LaunchOptions().setHeadless(false));
        tlBrowser.set(getPlaywright().firefox().launch(new LaunchOptions().setHeadless(false)));
        break;
      case "webkit":
        //this.browser = playwright.webkit().launch(new LaunchOptions().setHeadless(false));
        tlBrowser.set(getPlaywright().webkit().launch(new LaunchOptions().setHeadless(false)));
        break;
      default:
        System.out.println("Browser doesn't match any conditon...");
        break;
    }

//    page = this.browser.newContext().newPage();
//    page.navigate(prop.getProperty("url").trim());

    tlBrowserContext.set(getBrowser().newContext());
    tlPage.set(getBrowserContext().newPage());
    getPage().navigate(prop.getProperty("url").trim());

    return getPage();
  }

  public Properties initProperties() {
    try {
      FileInputStream fileInputStream = new FileInputStream(
          "./src/test/resources/config/config.properties");
      properties = new Properties();
      properties.load(fileInputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return properties;
  }

  public static String takeScreenshot() {
    String path = System.getProperty("user.dir")
        + "/screenshot/"
        + System.currentTimeMillis() + ".png";

    getPage().screenshot(new ScreenshotOptions().setPath(Paths.get(path)).setFullPage(true));
    return path;
  }

}
