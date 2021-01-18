package com.anon;

import org.apache.commons.io.IOUtils;

import static org.junit.Assert.*;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ExecutableServiceTestCase {

    public String getParentMethodName() {
        return new Throwable()
                .getStackTrace()[2]
                .getMethodName();
    }

    public void executeTestFile(String testFileName, ExecutableService service) throws Exception {
        URL outputURL = getClass().getClassLoader().getResource("output/outputLocation.txt");
        URL baselineURL = getClass().getClassLoader().getResource("baseline/baselineLocation.txt");
        String outputPath = outputURL.getPath().substring(1);
        String baselinePath = baselineURL.getPath().substring(1);
        outputPath = outputPath.substring(0, outputPath.lastIndexOf("/")+1);
        baselinePath = baselinePath.substring(0, baselinePath.lastIndexOf("/")+1);
        FileOutputStream fos = new FileOutputStream(outputPath + testFileName + ".out");

        OutputStreamWriter osw = new OutputStreamWriter(fos);
        String s = service.executeService(IOUtils.toString(getClass().getClassLoader().getResource("input/" + testFileName + ".in"), Charset.forName("US-ASCII")));
        System.out.println("*" + s + "*");
        osw.write(s);
        osw.close();

        fos.close();
        String baseline = Files.readString(Paths.get(baselinePath + testFileName + ".baseline"));
        String output = Files.readString(Paths.get(outputPath + testFileName + ".out"));

        baseline = baseline.replaceAll("\r", "").trim();
        output = output.replaceAll("\r", "").trim();
        assertEquals(baseline, output);
    }
}
