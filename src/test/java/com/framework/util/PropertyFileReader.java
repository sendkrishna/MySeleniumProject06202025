package com.framework.util;

import com.framework.listeners.ExtentTestNGITestListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFileReader {
    private static final Logger LOG = LogManager.getLogger(PropertyFileReader.class);

    public Properties loadPropertiesFile(String filePath) {

        Properties prop = new Properties();

        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            prop.load(resourceAsStream);
        } catch (IOException e) {
            LOG.error("Unable to load properties file : " + filePath);
        }

        return prop;

    }

}
