package com.framework.util;

import com.framework.listeners.ExtentTestNGITestListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// This class is to read configuration for test execution
public class PropertyFileReader {
    private static final Logger LOG = LogManager.getLogger(PropertyFileReader.class);

    private static final Properties prop = new Properties();

    static {

        try (InputStream resourceAsStream = PropertyFileReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            prop.load(resourceAsStream);
        } catch (IOException e) {
            LOG.error("Unable to load properties file : " + "config.properties");
        }

    }

    public static String getEnv() {
        return prop.getProperty("env.name");
    }

    public static String getBrowser() {
        return prop.getProperty("browser");
    }

    public static String getEnvURL() {
        return prop.getProperty("env.url");
    }

    public static String getExtentReportPath() {
        return prop.getProperty("extent.report.location");
    }

    public static String getExtentScreenshotsLocation() {
        return prop.getProperty("extent.screenshots.location");
    }

    public static String getExtentReportName() {
        return prop.getProperty("extent.report.name");
    }

    public static String getTimeoutInSeconds() {
        return prop.getProperty("timeoutInSeconds");
    }

}
