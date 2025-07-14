package com.test.regression;

import com.mobile.basetest.BasicMobileFeatures;
import org.testng.annotations.Test;

public class Mobile_TC001_AddPrepaidPlan_Guest extends BasicMobileFeatures {
    @Test(priority = 1, description = "This method is to add a prepaid plan for a given mobile number")
    public void sampleTest()  {
        super.addPrePaidPlan();
    }

}
