package com.lowlevelsubmarine.ytml.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatTools {

    public static long durationTextToMillis(String durationText) {
        int length = 0;
        for (String part : durationText.split("[:.]")) {
            length = length * 60 + Integer.valueOf(part);
        }
        return length * 1000L;
    }

    private int parseViews(String raw) {
        Matcher matcher = Pattern.compile("(\\d{1,3})(\\S)* views").matcher(raw);
        if (matcher.find()) {
            int val = Integer.parseInt(matcher.group(1));
            if (matcher.group(2) != null) {
                switch (matcher.group(2)) {
                    case "B":
                        return val*1000*3;
                    case "M":
                        return val*1000*2;
                    case "K":
                        return val*1000;
                }
            } else {
                return val;
            }
        }
        return -1;
    }

}