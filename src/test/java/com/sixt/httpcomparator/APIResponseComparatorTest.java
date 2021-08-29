package com.sixt.httpcomparator;

import org.testng.annotations.Test;

import java.io.IOException;

public class APIResponseComparatorTest {

    APIResponseComparator utils = new APIResponseComparator();

    @Test
    public void testresponse() throws IOException
    {
        long starttime = System.currentTimeMillis();
        utils.InputFilePath("/Applications/Coding Challenge/File1.txt", "/Applications/Coding Challenge/File2.txt");
        long stoptime = System.currentTimeMillis();
        System.out.println("Time taken by test response 1   " + (stoptime - starttime));
    }

    @Test
    public void testresponse2() throws IOException
    {
        long starttime = System.currentTimeMillis();
        utils.InputFilePathAsyc("/Applications/Coding Challenge/File1.txt", "/Applications/Coding Challenge/File2.txt");
        long stoptime = System.currentTimeMillis();
        System.out.println("Time taken by test response 2    "  + (stoptime-starttime));
    }

    @Test
    public void testresponse3() throws IOException
    {
        long starttime = System.currentTimeMillis();
        utils.InputFilePath3("/Applications/Coding Challenge/File1.txt", "/Applications/Coding Challenge/File2.txt");
        long stoptime = System.currentTimeMillis();
        System.out.println("Time taken by test response 2    "  + (stoptime-starttime));
    }
}
