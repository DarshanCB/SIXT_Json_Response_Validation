package com.sixt.httpcomparator;

import org.testng.annotations.Test;

import java.io.IOException;

public class APIResponseComparatorTest {

    APIResponseComparator utils = new APIResponseComparator();

    @Test
    public void testResponse() throws IOException {

        long starttime = System.currentTimeMillis();
        utils.inputResponseComparator("/Applications/Coding Challenge/File1.txt", "/Applications/Coding Challenge/File2.txt");
        long stoptime = System.currentTimeMillis();
        System.out.println("Time taken for comparison   ::"  + (stoptime-starttime));

    }

}
