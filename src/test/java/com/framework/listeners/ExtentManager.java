package com.framework.listeners;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.framework.util.PropertyFileReader;
import com.framework.util.Utilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.util.Properties;

public class ExtentManager {

    private static final Logger LOG = LogManager.getLogger(ExtentManager.class);

    private static ExtentReports extent;

    public static String dryrun="false";
    static String extentReportLocation = null;
    static String testEnv = "local";
    public static String reportName="Default report";
    static String extentReportName = null;
    static String extentReportFilePath = null;
    static String CurrentDate = Utilities.GetCurrentDate("yyyy-MM-dd_HH.mm.ss");

    public static ExtentReports getInstance() {

        if (extent == null) {
            setup();
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

    private static void setup() {
        Properties properties = new PropertyFileReader().loadPropertiesFile("config.properties");

        if (System.getProperty("testng.mode.dryrun")!=null) {
            dryrun = System.getProperty("testng.mode.dryrun");
            LOG.debug(dryrun);
        }

        testEnv = properties.getProperty("env.name")
                .trim()
                .toUpperCase();

        extentReportLocation = properties.getProperty("extent.report.location");
        if(reportName.equals("Default report")) {
            extentReportName = properties.getProperty("extent.report.name");
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

}
