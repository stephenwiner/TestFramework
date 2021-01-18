package com.anon;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReverseServiceTest extends ExecutableServiceTestCase {
/*
    @Test
    public void nullTest() throws Exception {
        ReverseService rs = new ReverseService();
        assertTrue(rs.executeService(null)==null);
    }
*/
    @Test
    public void emptyTest() throws Exception {
        executeReverseServiceTest();
    }

    @Test
    public void evenCharsTest() throws Exception {
        executeReverseServiceTest();
    }

    @Test
    public void oddCharsTest() throws Exception {
        executeReverseServiceTest();
    }

    private void executeReverseServiceTest() throws Exception {
        executeTestFile(getParentMethodName(), new ReverseService());

    }

}
