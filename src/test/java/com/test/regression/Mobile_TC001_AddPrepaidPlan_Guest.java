package com.test.regression;

import com.framework.data.StaticDataProvider;
import com.framework.interfaces.JsonData;
import com.mobile.basetest.BasicMobileFeatures;
import org.testng.annotations.Test;

import java.util.Map;

public class Mobile_TC001_AddPrepaidPlan_Guest extends BasicMobileFeatures {
    @JsonData(jsonPath = "./src/test/resources/data/Mobile/MobileInformation.json")
    @Test(dataProvider = "MapDataProvider", dataProviderClass = StaticDataProvider.class,priority = 1, description = "This method is to add a prepaid plan for a given mobile number")
    public void sampleTest(Map<String, String> maps)  {
        super.addPrePaidPlan(maps);
        
    }

}
