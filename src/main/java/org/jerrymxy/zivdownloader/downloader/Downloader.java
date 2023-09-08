package org.jerrymxy.zivdownloader.downloader;

import io.github.antivoland.cpb.ConsoleProgressBar;
import org.jerrymxy.zivdownloader.utils.Utils;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Downloader {
    protected URL url;
    protected String path;
    protected String fileName;
    protected String formatSuffix = ".zip";
    protected long fileLength;

    public Downloader() {}

    public Downloader(String url, String fileName, String path) {
        try {
            this.url = new URL(url);
            this.fileName = fileName;
            this.path = path;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public int download() {
        int byteRead = 0;

        try {
            System.out.println("Downloading: " + this.fileName);
            // Open Connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(false);
            conn.addRequestProperty("Accept-Charset", "UTF-8");
            conn.connect();

            int responseCode = conn.getResponseCode();
            String finalUrl;

            // Process 30x redirect
            if (responseCode >= 300 && responseCode < 400) {
                finalUrl = conn.getHeaderField("Location");
                // The ZIv server's response request is encoded in ISO-8859-1
                finalUrl = new String(finalUrl.getBytes(StandardCharsets.ISO_8859_1));
            } else {
                finalUrl = url.toString();
            }
            conn.disconnect();

            finalUrl = processParentPath(url.toString(), finalUrl);
            System.out.println(finalUrl);
            conn = (HttpURLConnection) new URL(finalUrl).openConnection();
            conn.addRequestProperty("Accept-Charset", "UTF-8");
            conn.connect();
            fileLength = conn.getContentLengthLong();

            InputStream inputStream = conn.getInputStream();
            String localFileName = Utils.filenameFilter(fileName);
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }

            // Download
            try (FileOutputStream fos = new FileOutputStream(path + "/" + localFileName + formatSuffix);
                 ConsoleProgressBar bar = new ConsoleProgressBar(fileLength)) {
                byte[] buffer = new byte[1024];
                // Console progress bar by https://github.com/antivoland/console-progress-bar
                // Write files & show step bar
                while ((byteRead = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteRead);
                    bar.stepBy(byteRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    public String encodeUrl(String url) throws UnsupportedEncodingException {
        String[] urls = url.split("/");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < urls.length; ++i) {
            if (urls[i] != null && !urls[i].isEmpty() && !urls[i].equals("..")) {
                sb.append(URLEncoder.encode(urls[i], StandardCharsets.UTF_8));
            } else {
                sb.append(urls[i]);
            }
            if (i < urls.length - 1) {
                sb.append("/");
            }
        }
        return sb.toString().replaceAll("\\+", "%20");
    }

    public String processParentPath(String prefix, String suffix) throws UnsupportedEncodingException {
        String[] suffixes = suffix.split("/");
        int slashIndex = 0;
        StringBuilder sb = new StringBuilder();
        for (String ele : suffixes) {
            if (ele == null || ele.isEmpty()) {
                continue;
            } else if (ele.equals("..")) {
                slashIndex = prefix.lastIndexOf('/');
                if (slashIndex == prefix.length() - 1) {
                    slashIndex = prefix.lastIndexOf('/', slashIndex - 1);
                }
                slashIndex = prefix.lastIndexOf('/', slashIndex - 1);
            } else {
                sb.append(ele).append("/");
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return prefix.substring(0, slashIndex + 1) + encodeUrl(sb.toString());
    }
}
