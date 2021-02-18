package com.lowlevelsubmarine.ytml.library.api_key;

import com.google.gson.Gson;
import com.lowlevelsubmarine.ytml.YTML;
import com.lowlevelsubmarine.ytml.actions.Action;
import com.lowlevelsubmarine.ytml.actions.RestAction;
import com.lowlevelsubmarine.ytml.http.HttpManager;
import com.lowlevelsubmarine.ytml.tools.URLCreator;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultKeyProvider implements KeyProvider {

    private static final URL URL = URLCreator.create("https://music.youtube.com");
    private static final Pattern PATTERN_KEY = Pattern.compile("\"INNERTUBE_API_KEY\":\"([^\"]+)\"");

    private final HttpManager httpManager;

    public DefaultKeyProvider(HttpManager httpManager) {
        this.httpManager = httpManager;
    }

    @Override
    public RestAction<String> fetchKey() {
        return new RestAction<>(new KeyParseAction());
    }

    private class KeyParseAction implements Action<String> {

        @Override
        public String run() {
            try {
                HttpManager httpManager = DefaultKeyProvider.this.httpManager;
                HttpURLConnection con = httpManager.getConnection(URL);
                String result = httpManager.executeGet(con);
                Matcher matcher = PATTERN_KEY.matcher(result);
                if (matcher.find()) {
                    return matcher.group(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}
