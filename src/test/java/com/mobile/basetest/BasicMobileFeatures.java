package com.mobile.basetest;

import com.basetest.BaseTest;
import com.pages.FirstTopBar;
import com.framework.util.DriverFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BasicMobileFeatures extends BaseTest {

    FirstTopBar firstTopBar;

    @BeforeMethod
    public void setupPages() {
        firstTopBar = new FirstTopBar();
    }

    @Test(priority = 1, enabled = false)
    public void addPrePaidPlan() {
        firstTopBar.navigateTo("Mobile");
    }
}
