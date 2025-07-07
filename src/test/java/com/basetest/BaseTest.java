package com.basetest;

import com.framework.util.DriverFactory;
import com.framework.util.PropertyFileReader;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    @BeforeMethod
    public void setUp() {
        DriverFactory.setDriver();
        WebDriver driver = DriverFactory.getDriver();
        driver.manage().window().maximize();
        driver.get(PropertyFileReader.getEnvURL());
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }



}
