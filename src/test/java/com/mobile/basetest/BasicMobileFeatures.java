package com.mobile.basetest;

import com.basetest.BaseTest;
import com.pages.FirstTopBar;
import com.framework.util.DriverFactory;
import org.testng.annotations.Test;

public class BasicMobileFeatures extends BaseTest {

    FirstTopBar firstTopBar;

    @Test
    public void addPrePaidPlan() {
        firstTopBar = new FirstTopBar(DriverFactory.getDriver());
        firstTopBar.navigateTo("Mobile");
    }
}
