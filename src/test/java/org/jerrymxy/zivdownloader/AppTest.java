package org.jerrymxy.zivdownloader;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jerrymxy.zivdownloader.downloader.Downloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    public void testEncoder() {
        Downloader downloader = new Downloader();
        try {
            System.out.println(downloader.encodeUrl("https://zenius-i-vanisher.com//simfiles/Z-I-v Battle Royale 2023/The Hope of Tomorrow -Ryu✩ Remix-_custom.zip"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void testDownload() {
        Downloader downloader = new Downloader("https://zenius-i-vanisher.com/v5.2/download.php?type=ddrsimfilecustom&simfileid=55327",
                "test",".");
        downloader.download();
    }
}
