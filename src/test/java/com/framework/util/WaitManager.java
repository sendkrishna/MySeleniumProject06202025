package com.framework.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class WaitManager {

    private static ThreadLocal<WaitManager> instance = new ThreadLocal<>();
    private WebDriver driver;
    int timeoutInSeconds;
    private WebDriverWait wait;

    private WaitManager(WebDriver driver){
        this.driver = driver;
        this.timeoutInSeconds = Integer.parseInt(PropertyFileReader.gettimeoutInSeconds());
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    public static WaitManager getInstance(WebDriver driver){
        if (instance.get()== null){
            instance.set(new WaitManager(driver));
        }
        return instance.get();
    }

    public static void removeInstance(){
        instance.remove();
    }

    public WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitToCheckClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean waitForText(By locator, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public WebElement waitForVisibilityOfElement(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

}
