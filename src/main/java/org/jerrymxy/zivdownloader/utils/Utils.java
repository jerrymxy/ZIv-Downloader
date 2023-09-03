package org.jerrymxy.zivdownloader.utils;

import java.util.regex.Pattern;

public class Utils {
    private static final Pattern FilePattern = Pattern.compile("[\\\\/:*?\"<>|]");

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("[0-9]+");
        return pattern.matcher(str).matches();
    }

    /**
     * Replace illegal characters in the filename
     * @param filename
     * @return Legal filename
     */
    public static String filenameFilter(String filename) {
        return filename == null ? null : FilePattern.matcher(filename).replaceAll("-");
    }
}
