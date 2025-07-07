package com.framework.listeners;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.framework.util.DriverFactory;
import com.framework.util.PropertyFileReader;
import com.framework.util.Utilities;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class ExtentTestNGITestListener implements ITestListener, IReporter {

    static String testEnv="local";
    static String screenshotsDir = null;
    private static String screenshotExtenstion = ".jpeg";
    private static String screenshotFilePath = null;
    private static ExtentReports extent =null;
    public static String dryrun="false";
    static String extentReportLocation = null;
    public static String reportName="Default report";
    static String extentReportName = null;
    static String extentReportFilePath = null;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(5);
    ExtentTest classNode;
    ExtentTest methodNode;
    ExtentTest parent;
    static String CurrentDate = Utilities.GetCurrentDate("yyyy-MM-dd_HH.mm.ss");
    private static ThreadLocal<ExtentTest> parentTest = new ThreadLocal<ExtentTest>();
    private static ThreadLocal<ExtentTest> childTest = new ThreadLocal<ExtentTest>();
    private static ThreadLocal<ExtentTest> grandChildTest = new ThreadLocal<ExtentTest>();
    private static Map<String, ExtentTest> classTestMap = new HashMap<>();
    private static final List<TestResultData> testResults = Collections.synchronizedList(new ArrayList<TestResultData>());


    private static final Logger LOG = LogManager.getLogger(ExtentTestNGITestListener.class);


    @Override
    public synchronized void onStart(ITestContext context) {

        LOG.debug("Listener on start");

        LOG.debug("Parameters " + context.getCurrentXmlTest().getAllParameters());

        context.setAttribute("TestEnv", testEnv);

        if(context.getCurrentXmlTest().getParameter("ReportName")!=null) {
            reportName =context.getCurrentXmlTest().getParameter("ReportName");
        }

        setup();

        extent = getInstance();

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


    @Override
    public synchronized void onTestStart(ITestResult result) {
        LOG.debug("in onTestStart");

        LOG.debug(result.getTestClass().getXmlTest().getName());
        String testName = result.getTestClass().getXmlTest().getName();

        LOG.debug(result.getInstance().getClass().getSimpleName());
        String className = result.getInstance().getClass().getSimpleName();

        LOG.info("starting " + className + "::" + testName + "::" + result.getMethod().getMethodName());

        String jsonDataFilePath = (String) result.getTestContext().getAttribute("JsonDatFilePath");
        String expectedJsonFilePath = (String) result.getTestContext().getAttribute("ExpectedJsonFilePath");

        if (classTestMap.containsKey(className)) {
            classNode = classTestMap.get(className);
            methodNode = classNode.createNode(result.getMethod().getMethodName());
            grandChildTest.set(methodNode);

            ((ExtentTest) childTest.get()).log(Status.INFO,"Description - " + result.getMethod().getDescription());
            ((ExtentTest) childTest.get()).log(Status.INFO, result.getMethod().getMethodName() + " started");

            if(jsonDataFilePath!=null) {
                ((ExtentTest) grandChildTest.get()).log(Status.INFO,"Json Data file - " + result.getTestContext().getAttribute("JsonDatFilePath"));
            }
            if(expectedJsonFilePath!=null) {
                ((ExtentTest) grandChildTest.get()).log(Status.DEBUG,"Expected file - " + result.getTestContext().getAttribute("ExpectedJsonFilePath"));
            }
        } else {

            LOG.debug("Creating new node");
            classNode = parentTest.get().createNode(className);
            classTestMap.put(className, classNode);
            childTest.set(classNode);
            LOG.debug("Created new node " + className);
            ((ExtentTest) childTest.get()).log(Status.INFO, "Description - " + result.getMethod().getDescription());
            ((ExtentTest) childTest.get()).log(Status.INFO, result.getMethod().getMethodName() + " started");
            methodNode = classNode.createNode(result.getMethod().getMethodName());
            grandChildTest.set(methodNode);

            if(jsonDataFilePath!=null) {
                ((ExtentTest) grandChildTest.get()).log(Status.INFO,"Json Data file - " + result.getTestContext().getAttribute("JsonDatFilePath"));
            }
            if(expectedJsonFilePath!=null) {
                ((ExtentTest) grandChildTest.get()).log(Status.DEBUG,"Expected file - " + result.getTestContext().getAttribute("ExpectedJsonFilePath"));
            }
        }


    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        LOG.debug("in onTestSuccess");
        File screenshot = null;

        childTest.get().log(Status.PASS, result.getMethod().getMethodName() + " passed");
        if(grandChildTest.get()!=null)
            grandChildTest.get().log(Status.PASS, "Test passed");


        LOG.info(result.getInstance().getClass().getSimpleName() + ":: " + result.getMethod().getMethodName() + " finished successfully" );
        result.getTestContext().removeAttribute("JsonDatFilePath");
        result.getTestContext().removeAttribute("ExpectedJsonFilePath");
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        LOG.debug("in onTestFailure");
        File screenshot = null;
        ((ExtentTest) childTest.get()).fail(result.getInstance().getClass().getSimpleName() + " failed");

        if(grandChildTest.get()!=null)
            grandChildTest.get().log(Status.FAIL, "Test failed");
        LOG.error("*Test failed*");


        try {
            screenshot = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.FILE);
            String screenshotFileName = getScreenShotFileName(result);
            String filepath = screenshotFilePath + screenshotFileName;
            FileUtils.copyFile(screenshot, new File(filepath));
            LOG.debug(grandChildTest == null);
            LOG.debug(grandChildTest.get() == null);
            LOG.debug(filepath);

            LOG.error(result.getThrowable().getMessage());
            LOG.error(result.getThrowable().getStackTrace());
            ((ExtentTest) grandChildTest.get()).log(Status.ERROR,result.getThrowable().getMessage());
            ((ExtentTest) grandChildTest.get()).log(Status.ERROR, result.getThrowable().fillInStackTrace());

            ((ExtentTest) grandChildTest.get()).log(Status.INFO, "Screenshot - " + screenshotFileName ,
                    MediaEntityBuilder.createScreenCaptureFromPath(filepath, "Failure screenshot")
                            .build());

            result.getTestContext().removeAttribute("JsonDatFilePath");
            result.getTestContext().removeAttribute("ExpectedJsonFilePath");
        } catch (Exception e) {
            LOG.error("Error while taking screenshot",e.fillInStackTrace());
        }

    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        //Reporter.getOutput().forEach(s->parent.log(Status.INFO, s));
        parent.log(Status.INFO, context.getName() + " finished");
        classTestMap.clear();
        Reporter.clear();
        LOG.debug("in onFinish");
        extent.flush();

        executorService.shutdown();
        try {
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void setup() {
        //Properties properties = new PropertyFileReader().loadPropertiesFile("config.properties");

        testEnv = PropertyFileReader.getEnv();

        screenshotsDir = PropertyFileReader.getExtentScreenshotsLocation();
        LOG.debug("screen shot dir from property file = " +screenshotsDir);
        screenshotFilePath = screenshotsDir.concat("/").concat(CurrentDate).concat("/") ;
        LOG.debug("screen shot dir=" + screenshotFilePath);

        if (System.getProperty("testng.mode.dryrun")!=null) {
            dryrun = System.getProperty("testng.mode.dryrun");
            LOG.debug(dryrun);
        }
        extentReportLocation = PropertyFileReader.getExtentReportPath();
        if(reportName.equals("Default report")) {
            extentReportName = PropertyFileReader.getExtentReportName();
        }else {
            extentReportName = reportName;
            if(dryrun.equalsIgnoreCase("true")) {
                extentReportName = extentReportName.concat("_DRY_RUN_");
            }

        }
        extentReportFilePath = extentReportLocation.concat("/")
                .concat(extentReportName)
                .concat("_")
                .concat(testEnv.toUpperCase())
                .concat("_")
                .concat(CurrentDate)
                .concat(".html");

        LOG.debug("* TEST ENVIRONMENT IS " + testEnv + " *");

        if(dryrun!=null) {
            if(dryrun.equalsIgnoreCase("true")) {
                LOG.debug("*DRY RUN MODE*");
            }
        }
    }

    public static ExtentReports getInstance() {

        if (extent == null) {
            createInstance(extentReportFilePath);
        }
        LOG.debug("Test Report path is " + extentReportFilePath);
        return extent;
    }

    private static ExtentReports createInstance(String fileName) {

        LOG.debug(fileName);
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);

        htmlReporter.config()
                .setTheme(Theme.DARK);
        htmlReporter.config()
                .setDocumentTitle("HMI Automated Regression Tests");
        htmlReporter.config()
                .setEncoding("utf-8");
        htmlReporter.config()
                .setReportName(reportName);
        htmlReporter.setAnalysisStrategy(AnalysisStrategy.SUITE);


        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("Test Environment", testEnv);
        return extent;
    }

    private static class TestResultData {
        String testName;
        String status;
        String message;

        TestResultData(String testName, String status, String message) {
            this.testName = testName;
            this.status = status;
            this.message = message;
        }
    }

    private String getScreenShotFileName(ITestResult result) {
        return result.getMethod().getMethodName() +"_"+ Utilities.GetRandomNumber()+ screenshotExtenstion;
    }


}
