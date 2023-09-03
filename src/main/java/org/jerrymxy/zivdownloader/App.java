package org.jerrymxy.zivdownloader;

import org.jerrymxy.zivdownloader.downloader.Downloader;
import org.jerrymxy.zivdownloader.parser.HtmlParser;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

/**
 * Hello world!
 */
public class App {
    public static String urlPrefix = "https://zenius-i-vanisher.com/v5.2/";
    public static String url = "";
    public static String path = ".";

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please input category URL or ID:");
            Scanner input = new Scanner(System.in);
            url = input.nextLine();
        } else {
            url = args[0];
            if (args.length >= 2) {
                path = args[1];
            }
        }
        HtmlParser htmlParser = new HtmlParser(url);

        try {
            Queue<List<String>> simfileQueue = htmlParser.parseCategoryUrl();
            System.out.println("Done! Downloading...");
            while (!simfileQueue.isEmpty()) {
                List<String> currentSimfile = simfileQueue.remove();
                String name = currentSimfile.get(1);
                String simfileUrl = urlPrefix + currentSimfile.get(2);
                Downloader downloader = new Downloader(simfileUrl, name, path + "/" + htmlParser.getSongPackName());
                downloader.download();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO

    }
}
