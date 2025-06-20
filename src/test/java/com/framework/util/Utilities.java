package com.framework.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Utilities {

    @Test
    public void sampleTest()  {

        WebDriverManager.chromedriver().setup();

        WebDriver driver = new ChromeDriver();

        driver.get("https://www.jio.com/selfcare/plans/mobility/prepaid-plans-list/");

        driver.quit();
    }
}
