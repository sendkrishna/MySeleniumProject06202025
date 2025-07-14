package com.pages;

import com.framework.util.DriverFactory;
import com.framework.util.WaitManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.List;

public class FirstTopBar {

    private static final Logger LOG = LogManager.getLogger(FirstTopBar.class);

    @FindBy(id = "jds-text")
    private List<WebElement> menuItems;

    @FindBy(id = "text")
    private List<WebElement> pageTitle;

    WebDriver driver;
    WaitManager wait;


    public FirstTopBar(WebDriver driver) {
        this.driver = DriverFactory.getDriver();;
        PageFactory.initElements(driver, this);
        this.wait = WaitManager.getInstance(driver);
    }

    public void navigateTo (String tabName){

        LOG.debug("Clicking on menu and waiting for " + tabName);
        Assert.assertFalse(menuItems.isEmpty(), "No menu items were returned!");
        WebElement weTabName = menuItems.stream().filter(we -> we.getText().contains(tabName)).findFirst().orElse(null);
        Assert.assertNotNull(weTabName," Top navigation menu item " + tabName + " not found");

        WebElement weTabNameClick = weTabName.findElement(By.xpath("ancestor::*[3]"));
        Assert.assertNotNull(weTabNameClick," Top navigation menu item " + tabName + " not clickable");

        try {
            weTabNameClick.click();
            WebElement secondElement = pageTitle.get(1);
            WebElement we = wait.waitForVisibilityOfElement(secondElement);


        } catch (Exception e){

        }

    }
}
