package com.basetest;

import com.framework.util.DriverFactory;
import com.framework.util.PropertyFileReader;
import com.framework.util.WaitManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.List;

public class BaseTest {

    @BeforeMethod
    public void setUp() {
        DriverFactory.setDriver();
        WebDriver driver = DriverFactory.getDriver();
        WaitManager wait = WaitManager.getInstance(driver);
        driver.manage().window().maximize();
        driver.get(PropertyFileReader.getEnvURL());

        List<WebElement> elements = driver.findElements(By.id("text"));
        WebElement secondElement = elements.get(1);

        WebElement we = wait.waitForVisibilityOfElement(secondElement);

        if (we.getText().equals("JioHome, Jio more")){
            System.out.println("Success");
        }else {
            System.out.println("Fail");
        }

    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }



}
