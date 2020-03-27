package com.lowlevelsubmarine.ytml.tools;

public class FormatTools {

    public static long durationTextToMillis(String durationText) {
        int length = 0;
        for (String part : durationText.split("[:.]")) {
            length = length * 60 + Integer.valueOf(part);
        }
        return length * 1000L;
    }

}