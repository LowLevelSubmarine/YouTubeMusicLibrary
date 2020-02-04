package com.lowlevelsubmarine.ytml.library;

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

public class DefaultKeyProvider implements KeyParser {

    private static final URL URL = URLCreator.create("https://music.youtube.com");
    private static final Pattern PATTERN_KEY = Pattern.compile("<script >ytcfg.set\\(([\\W\\w]*)\\);</script>");

    private final YTML ytml;

    public DefaultKeyProvider(YTML ytml) {
        this.ytml = ytml;
    }

    @Override
    public RestAction<String> getKey() {
        return new RestAction<>(new KeyParseAction());
    }

    private class KeyParseAction implements Action<String> {

        @Override
        public String run() {
            try {
                HttpManager httpManager = DefaultKeyProvider.this.ytml.getHttpManager();
                HttpURLConnection con = httpManager.getConnection(URL);
                String result = httpManager.executeGet(con);
                Matcher matcher = PATTERN_KEY.matcher(result);
                if (matcher.find()) {
                    Gson gson = new Gson();
                    YtCfgFields ytCfgFields = gson.fromJson(matcher.group(1), YtCfgFields.class);
                    return ytCfgFields.INNERTUBE_API_KEY;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    private class YtCfgFields {

        String INNERTUBE_API_KEY;

    }

}
