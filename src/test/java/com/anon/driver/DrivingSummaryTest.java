package com.anon.driver;

import org.junit.Test;

public class DrivingSummaryTest extends TestCase {

    @Test
    public void testSample() throws Exception {
        executeTestFile("Sample");
    }

    @Test
    public void testSampleWith200mph() throws Exception {
        executeTestFile("SampleWith200mph");
    }

    @Test
    public void testSampleWithSlowmph() throws Exception {
        executeTestFile("SampleWithSlowmph");
    }

    @Test
    public void testSampleDuplicates() throws Exception {
        executeTestFile("SampleDuplicates");
    }
}
