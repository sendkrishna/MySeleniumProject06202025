package com.framework.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.framework.util.PropertyFileReader;
import com.framework.util.Utilities;
import org.testng.IReporter;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Reporter;
import java.util.Properties;
public class ExtentTestNGITestListener implements ITestListener, IReporter {

    static String testEnv="local";
    static String screenshotsDir = null;
    private static String screenshotExtenstion = ".jpeg";
    private static String screenshotFilePath = null;
    static String CurrentDate = Utilities.GetCurrentDate("yyyy-MM-dd_HH.mm.ss");
    private static ExtentReports extent =null;
    ExtentTest classNode;
    ExtentTest methodNode;
    ExtentTest parent;

    private static ThreadLocal<ExtentTest> parentTest = new ThreadLocal<ExtentTest>();
    private static ThreadLocal<ExtentTest> childTest = new ThreadLocal<ExtentTest>();
    private static ThreadLocal<ExtentTest> grandChildTest = new ThreadLocal<ExtentTest>();

    private static final Logger LOG = LogManager.getLogger(ExtentTestNGITestListener.class);


    @Override
    public synchronized void onStart(ITestContext context) {

        LOG.debug("Listener on start");
        setup();

        LOG.debug("Parameters " + context.getCurrentXmlTest().getAllParameters());

        context.setAttribute("TestEnv", testEnv);

        if(context.getCurrentXmlTest().getParameter("ReportName")!=null) {
            ExtentManager.reportName =context.getCurrentXmlTest().getParameter("ReportName");
        }

        extent = ExtentManager.getInstance();

        String testname = context.getCurrentXmlTest().getName();
        LOG.debug("Name= " + testname);

        parent = extent.createTest(testname,context.getSuite().getXmlSuite().getFileName());
        parentTest.set(parent);
        parent.assignAuthor("Selenium Tester");

        if(context.getSuite().getParameter("Category")!=null)
            parent.assignCategory(context.getSuite().getParameter("Category"));
        parent.log(Status.INFO, testname +" started");
        Reporter.setEscapeHtml(false);
    }


    private static void setup() {
        Properties properties = new PropertyFileReader().loadPropertiesFile("config.properties");

        testEnv = properties.getProperty("env.name");
        screenshotsDir = properties.getProperty("extent.screenshots.location");
        LOG.debug("screen shot dir from property file = " +screenshotsDir);
        screenshotFilePath = screenshotsDir.concat("/").concat(CurrentDate).concat("/") ;
        LOG.debug("screen shot dir=" + screenshotFilePath);
    }


}
