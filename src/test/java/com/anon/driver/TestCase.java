package com.anon.driver;

import com.anon.parser.DriverParser;
import static org.junit.Assert.*;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestCase {

    public void executeTestFile(String testFileName) throws Exception {
        URL outputURL = getClass().getClassLoader().getResource("output/outputLocation.txt");
        URL baselineURL = getClass().getClassLoader().getResource("baseline/baselineLocation.txt");
        String outputPath = outputURL.getPath().substring(1);
        String baselinePath = baselineURL.getPath().substring(1);
        outputPath = outputPath.substring(0, outputPath.lastIndexOf("/")+1);
        baselinePath = baselinePath.substring(0, baselinePath.lastIndexOf("/")+1);
        FileOutputStream os = new FileOutputStream(outputPath + testFileName + ".out");
        DriverParser dp = new DriverParser();
        DrivingSummary ds = new DrivingSummary(
            getClass().getClassLoader().getResourceAsStream("input/" + testFileName + ".in"),
                dp, os);
        ds.generateSummaryReport();
        os.close();
        String baseline = Files.readString(Paths.get(baselinePath + testFileName + ".baseline"));
        String output = Files.readString(Paths.get(outputPath + testFileName + ".out"));

        baseline = baseline.replaceAll("\r", "").trim();
        output = output.replaceAll("\r", "").trim();
        assertEquals(baseline, output);
    }
}
