package org.jerrymxy.zivdownloader;

// import org.jerrymxy.zivdownloader.downloader.Downloader;
import org.jerrymxy.zivdownloader.parser.HtmlParser;
import org.jerrymxy.zivdownloader.utils.Globals;

import java.io.IOException;
// import java.util.List;
// import java.util.Queue;
import java.util.Scanner;


/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please input category URL or ID:");
            Scanner input = new Scanner(System.in);
            Globals.FULL_URL = input.nextLine();
        } else {
            Globals.FULL_URL = args[0];
            if (args.length >= 2) {
                Globals.PATH = args[1];
            }
        }
        HtmlParser htmlParser = new HtmlParser(Globals.FULL_URL);

        try {
            // Queue<List<String>> simfileQueue = htmlParser.parseCategoryUrl();
            // System.out.println("Done! Downloading...");
            // while (!simfileQueue.isEmpty()) {
            //     List<String> currentSimfile = simfileQueue.remove();
            //     String name = currentSimfile.get(1);
            //     String simfileUrl = Globals.URL_PREFIX + currentSimfile.get(2);
            //     Downloader downloader = new Downloader(simfileUrl, name, Globals.PATH + "/" + htmlParser.getSongPackName());
            //     downloader.download();
            // }
            htmlParser.parseAndDownloadCategoryUrl();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO

    }
}
