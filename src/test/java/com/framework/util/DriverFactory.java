package com.framework.util;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Properties;

//This class is to set driver as per browser preference , provide browser instance, and to quit browser instance
public class DriverFactory {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void  setDriver() {
        if (driver.get() == null) {

            String browser = PropertyFileReader.getBrowser();
            switch (browser.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    driver.set(new ChromeDriver());
                    break;
                case "firefox":
                    driver.set(new FirefoxDriver());
                    break;
                // Add more browsers as needed
                default:
                    throw new RuntimeException("Unsupported browser: " + browser);
            }
        }

    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
