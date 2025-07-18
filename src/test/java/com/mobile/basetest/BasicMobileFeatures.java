package com.mobile.basetest;

import com.basetest.BaseTest;
import com.framework.data.StaticDataProvider;
import com.framework.interfaces.JsonData;
import com.pages.FirstTopBar;
import com.framework.util.DriverFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.Map;

public class BasicMobileFeatures extends BaseTest {

    FirstTopBar firstTopBar;

    @BeforeMethod
    public void setupPages() {
        firstTopBar = new FirstTopBar();
    }

    @JsonData(jsonPath = "./src/test/resources/data/Mobile/MobileInformation.json")
    @Test(dataProvider = "MapDataProvider", dataProviderClass = StaticDataProvider.class,priority = 1, enabled = false)
    public void addPrePaidPlan(Map<String, String> map) {
        firstTopBar.navigateTo("Mobile");

        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pair = (Map.Entry<String, String>) it.next();
            System.out.println("Key is " + pair.getKey() + " and value is " + pair.getValue());
        }
    }

}
