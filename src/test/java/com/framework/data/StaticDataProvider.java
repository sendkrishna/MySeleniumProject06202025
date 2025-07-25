package com.framework.data;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.interfaces.JsonData;
import com.framework.util.Utilities;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StaticDataProvider {
    private static final Logger LOG = LogManager.getLogger(StaticDataProvider.class);

    @DataProvider(name = "MapDataProvider")
    public static Iterator<Object[]> getMapData(ITestContext context, Method m) {
        List<Object[]> data = new ArrayList<>();
        Map<String, String> map = null;
        JsonData anns = m.getAnnotation(JsonData.class);

        if(anns==null) {
            LOG.error("No JsonData annotation supplied. To use MapDataProvider, provide JsonData annotation for the test method " + m.getName());
            Assert.assertNotNull(anns, "No JsonData annotation provided for method " + m.getName());
        }

        String path = m.getAnnotation(JsonData.class).jsonPath();

        if(path==null) {
            LOG.error("JsonData annotation was supplied. To use MapDataProvider, provide jsonPath parameter for the test method " + m.getName());
            Assert.assertNotNull(path, "No jsonPath parameter provided for method " + m.getName());
        }

        path = getJsonFileNameByMethod(context,m, anns);

        context.setAttribute("JsonDatFilePath", path);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            LOG.debug("Using MapDataProvider for " + m.getName());
            map = objectMapper.readValue(new File(path), new TypeReference<Map<String, String>>() {});
        } catch (IOException e) {
            LOG.error("Json data file not found at " + path);
            e.printStackTrace();
        }catch (Exception e) {
            LOG.error("Error reading json " + path);
            e.printStackTrace();
        }

        data.add(new Object[] { map });

        return data.iterator();
    }

    private static String getJsonFileNameByMethod(ITestContext context, Method m, JsonData anns) {
        Map<String, String> testParams = context.getCurrentXmlTest().getLocalParameters();
        Map<String, String> suiteParams = context.getCurrentXmlTest().getSuite().getParameters();
        Path p = Paths.get(context.getCurrentXmlTest().getSuite().getFileName());
        String testngFileName = p.getFileName().toString();

        String testmode = System.getProperty("testng.mode.dryrun");
        String expectedJsonFile = null;
        String className =  m.getDeclaringClass().getSimpleName();
        String methodName = m.getName();
        String autoGenerateTestdataFileName = m.getAnnotation(JsonData.class).useAutoGeneratedFileName();
        String displayName = String.join("::", className, methodName);
        String path = anns.jsonPath();

        String jsonpath = FilenameUtils.getPath(path);
        String filename = FilenameUtils.getBaseName(path);
        String testdatafile = null;

        if (autoGenerateTestdataFileName.toLowerCase().equals("n")  || autoGenerateTestdataFileName.toLowerCase().equals("No")) {
            testdatafile = path;
            if(testmode!=null) {
                if(testmode.equalsIgnoreCase("true")) {
                    String values = Stream.of(context.getSuite().getName(),testngFileName,context.getName(),className, m.getName(),jsonpath, FilenameUtils.getBaseName(testdatafile).concat(".json"), expectedJsonFile,autoGenerateTestdataFileName, Utilities.GetCurrentDate("MM/dd/yyyy"))
                            .map(s -> s == null ? "" : s)
                            .collect(Collectors.joining("','", "'", "'"));
                }
            }
            return testdatafile;
        }

        testdatafile = jsonpath.concat(".json");
        File f = new File(testdatafile);
        if (!f.exists()) {
            LOG.error(testdatafile + " does not exist. Defaulting to " + path );
            expectedJsonFile = testdatafile;
            context.setAttribute("ExpectedJsonFilePath", testdatafile);
            testdatafile = path;
        }
        LOG.info("Using data file " + testdatafile + " for method " + m.getName());

        return testdatafile;
    }

}
