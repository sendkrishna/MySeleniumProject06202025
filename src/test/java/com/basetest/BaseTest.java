package com.basetest;

import com.framework.util.DriverFactory;
import com.framework.util.PropertyFileReader;
import com.framework.util.WaitManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;


import java.util.List;

public class BaseTest {

    //This method is set up driver and launch test URL
    @BeforeClass
    public void setUp() {
        DriverFactory.setDriver();
        WebDriver driver = DriverFactory.getDriver();
        WaitManager wait = WaitManager.getInstance(driver);
        driver.manage().window().maximize();
        driver.get(PropertyFileReader.getEnvURL());

        List<WebElement> elements = driver.findElements(By.id("text"));
        WebElement secondElement = elements.get(1);

        WebElement we = wait.waitForVisibilityOfElement(secondElement);

        Assert.assertEquals(we.getText(),"JioHome, Jio more","test is Success" );
    }

    // This method is to quit driver after test
    @AfterClass
    public void tearDown() {
        DriverFactory.quitDriver();
    }

}
