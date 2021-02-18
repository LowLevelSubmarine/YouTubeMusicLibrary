package com.lowlevelsubmarine.ytml.deprecated_library;

import com.google.gson.Gson;

public class PostFields {

    public final static String TUNER_SETTING_AUTOMIX_SETTING_NORMAL = "AUTOMIX_SETTING_NORMAL";

    public Context context = new Context();
    public String query;
    public String params;

    public static class Context {

        public Client client = new Client();
        public String videoId;
        public Boolean isAudioOnly;
        public String tunerSettingValue;
        public Boolean enablePersistentPlaylistPanel;

        public static class Client {

            public String clientName = "WEB_REMIX";
            public String clientVersion = "0.1";
            public String gl = "US";
            public String hl = "en";

        }

    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
