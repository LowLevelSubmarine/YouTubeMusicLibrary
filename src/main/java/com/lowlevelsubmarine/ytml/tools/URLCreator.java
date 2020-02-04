package com.lowlevelsubmarine.ytml.tools;

import java.net.URL;

public class URLCreator {

    public static URL create(String rawUrl) {
        try {
            return new URL(rawUrl);
        } catch (Exception e) {
            throw new InternalError(e);
        }
    }

}
