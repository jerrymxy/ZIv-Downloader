package org.jerrymxy.zivdownloader.parser;

import org.jerrymxy.zivdownloader.downloader.Downloader;
import org.jerrymxy.zivdownloader.utils.Utils;
import org.jerrymxy.zivdownloader.utils.Globals;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class HtmlParser {
    public String zivUrl = "https://zenius-i-vanisher.com/v5.2/";
    protected String categoryUrl;
    protected String songPackName;
    // Simfiles to be downloaded
    protected Queue<List<String>> simfileQueue;

    public HtmlParser(String url) {
        if (url.startsWith("https")) {
            this.categoryUrl = url;
        } else if (Utils.isInteger(url)) {
            this.categoryUrl = zivUrl + "viewsimfilecategory.php?categoryid=" + url;
        } else {
            System.out.println("URL not valid. Please check.");
            System.exit(0);
        }
        this.simfileQueue = new LinkedList<>();
    }

    public String getSongPackName() {
        return songPackName;
    }

    public String getCategoryUrl() {
        return categoryUrl;
    }

//    public String getSimfileUrl() {
//        return simfileUrl;
//    }

    public void setCategoryUrl(String categoryUrl) {
        if (categoryUrl.startsWith(zivUrl)) {
            this.categoryUrl = categoryUrl;
        } else if (Utils.isInteger(categoryUrl)) {
            this.categoryUrl += categoryUrl;
        }
    }

//    public void setSimfileUrl(String simfileUrl) {
//        this.simfileUrl = simfileUrl;
//    }

    public Queue<List<String>> parseCategoryUrl() throws IOException {
        // GET method
        System.out.println("Connecting...");
        Document document = Jsoup.connect(this.categoryUrl).get();

        // Get category name
        Elements headertop = document.getElementsByClass("headertop");
        Elements h1 = headertop.first().getElementsByTag("h1");
        this.songPackName = h1.text();
        System.out.println("Got category: " + songPackName);
        // Split content
        Elements content = document.getElementsByClass("content");

        // Get tables
        Elements tables = content.first().getElementsByTag("table");
        for (Element table : tables) {
            Elements trs = table.getElementsByTag("tr");

            // Print current info
            System.out.print(trs.first().getElementsByTag("h2").first().html());
            System.out.print(": ");
            System.out.println(trs.get(1).child(0).html());

            // Split links
            for (int i = 3; i < trs.size(); ++i) {
                Element currentElement = trs.get(i);
                System.out.println(i);
                Element eleA = currentElement.getElementsByTag("strong").first().child(0);
                // Simfile ID: sim + xxxx(numbers)
                String simfileID = eleA.attr("id").substring(2);
                String simfileUrl = parseZipUrl(eleA.attr("href"));
                String simfileTitle = eleA.attr("title");
                ArrayList<String> curList = new ArrayList<>(Arrays.asList(simfileID, simfileTitle, simfileUrl));
                this.simfileQueue.add(curList);
            }
        }
        return this.simfileQueue;
    }

    public void parseAndDownloadCategoryUrl() throws IOException {
        // GET method
        System.out.println("Connecting...");
        Document document = Jsoup.connect(this.categoryUrl).get();

        // Get category name
        Elements headertop = document.getElementsByClass("headertop");
        Elements h1 = headertop.first().getElementsByTag("h1");
        this.songPackName = h1.text();
        System.out.println("Got category: " + songPackName);
        // Split content
        Elements content = document.getElementsByClass("content");

        // Get tables
        Elements tables = content.first().getElementsByTag("table");
        for (Element table : tables) {
            Elements trs = table.getElementsByTag("tr");

            // Print current info
            System.out.print(trs.first().getElementsByTag("h2").first().html());
            System.out.print(": ");
            System.out.println(trs.get(1).child(0).html());

            // Split links
            for (int i = 3; i < trs.size(); ++i) {
                System.out.print("[" + (i - 2) + "/" + (trs.size() - 3) + "] ");
                Element currentElement = trs.get(i);
                Element eleA = currentElement.getElementsByTag("strong").first().child(0);

                String simfileUrl = Globals.URL_PREFIX +  parseZipUrl(eleA.attr("href"));
                String simfileTitle = eleA.attr("title");
                Downloader downloader = new Downloader(simfileUrl, simfileTitle, Globals.PATH + "/" + songPackName);
                downloader.download();
            }
        }
    }

    public String parseZipUrl(String simfileUrl) throws IOException {
        // GET method
        Document document = Jsoup.connect(zivUrl + simfileUrl).get();

        Elements content = document.getElementsByClass("content");
        Elements tables = content.first().getElementsByTag("table");
        Elements trs = tables.get(1).getElementsByTag("tr");
        Elements tds = trs.get(1).getElementsByTag("td");
        Elements eleA = tds.get(1).getElementsByTag("a");
        return eleA.attr("href");
    }
}
