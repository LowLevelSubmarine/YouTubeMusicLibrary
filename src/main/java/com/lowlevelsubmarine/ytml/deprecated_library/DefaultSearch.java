package com.lowlevelsubmarine.ytml.deprecated_library;

import com.google.gson.*;
import com.lowlevelsubmarine.ytml.YTML;
import com.lowlevelsubmarine.ytml.actions.RestAction;
import com.lowlevelsubmarine.ytml.tools.FormatTools;
import com.lowlevelsubmarine.ytml.json.JsonTools;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class DefaultSearch implements Search {

    private static final String API_ENDPOINT_SUFFIX = "search";

    private final YTML ytml;
    private final String query;
    private Result<Song> songResult;
    private Result<Video> videoResult;

    public DefaultSearch(YTML ytml, String query) throws IOException {
        this.ytml = ytml;
        this.query = query;
        JsonObject obj = requestJson(ytml);
        JsonArray contents = obj
                .getAsJsonObject("contents")
                .getAsJsonObject("sectionListRenderer")
                .getAsJsonArray("contents");
        for (int i = 0; i < contents.size(); i++) {
            if (this.songResult == null) {
                this.songResult = new SongResultFields(contents.get(i).getAsJsonObject(), i).getParsedOrNull();
            }
            if (this.videoResult == null) {
                this.videoResult = new VideoResultFields(contents.get(i).getAsJsonObject(), i).getParsedOrNull();
            }
        }
    }

    private JsonObject requestJson(YTML ytml) throws IOException {
        URL url = ytml.createApiEndpointURL(API_ENDPOINT_SUFFIX);
        HttpURLConnection conn = ytml.getHttpManager().getConnection(url);
        conn.setRequestProperty("referer", "https://music.youtube.com/");
        PostFields postFields = new PostFields();
        postFields.query = this.query;
        String postContent = new Gson().toJson(postFields);
        String response = ytml.getHttpManager().executePost(conn, postContent);
        JsonObject obj =  JsonParser.parseString(response).getAsJsonObject();
        if (obj == null) {
            throw new IOException();
        }
        return obj;
    }

    @Override
    public String getQuery() {
        return this.query;
    }

    @Override
    public Result<Song> getSongs() {
        return this.songResult;
    }

    @Override
    public Result<Artist> getArtists() {
        return null;
    }

    @Override
    public Result<Video> getVideos() {
        return this.videoResult;
    }

    private class SongResultFields extends ResultFields<Song> {

        private SongResultFields(JsonObject obj, int pos) throws JsonParseException {
            super(obj, pos);
        }

        @Override
        protected String getType() {
            return "Songs";
        }

        @Override
        protected Song createResultItem(JsonObject obj, boolean hasCategoryColumn) {
            return new SongFields(obj, hasCategoryColumn);
        }

    }

    private class VideoResultFields extends ResultFields<Video> {

        private VideoResultFields(JsonObject obj, int pos) throws JsonParseException {
            super(obj, pos);
        }

        @Override
        protected String getType() {
            return "Videos";
        }

        @Override
        protected Video createResultItem(JsonObject obj, boolean hasCategoryColumn) {
            return new VideoFields(obj, hasCategoryColumn);
        }

    }

    private abstract class ResultFields<T> implements Result<T> {

        protected abstract String getType();
        protected abstract T createResultItem(JsonObject obj, boolean hasCategoryColumn);

        private final int pos;
        private final boolean parsed;
        private final String params;
        private final LinkedList<T> results = new LinkedList<>();

        private ResultFields(JsonObject obj, int pos) {
            obj = obj.getAsJsonObject("musicShelfRenderer");
            String type = JsonTools.extractRun(obj
                    .getAsJsonObject("title"));
            if (type == null || !type.equals(getType())) {
                this.parsed = false;
                this.pos = -1;
                this.params = null;
            } else {
                this.parsed = true;
                this.pos = pos;
                this.params = obj
                        .getAsJsonObject("bottomEndpoint")
                        .getAsJsonObject("searchEndpoint")
                        .get("params").getAsString();
                for (JsonElement jsonElement : obj.getAsJsonArray("contents")) {
                    this.results.add(createResultItem(jsonElement.getAsJsonObject(), true));
                }
            }
        }

        public boolean isParsed() {
            return this.parsed;
        }

        public ResultFields<T> getParsedOrNull() {
            return isParsed()? this : null;
        }

        @Override
        public int pos() {
            return this.pos;
        }

        @Override
        public List<T> get() {
            return this.results;
        }

        @Override
        public RestAction<List<T>> parse() {
            return new RestAction<>(() -> {
                try {
                    JsonObject obj = requestJson(this.params);
                    JsonArray items = obj.getAsJsonObject("contents")
                            .getAsJsonObject("sectionListRenderer")
                            .getAsJsonArray("contents")
                            .get(0).getAsJsonObject()
                            .getAsJsonObject("musicShelfRenderer")
                            .getAsJsonArray("contents");
                    LinkedList<T> results = new LinkedList<>();
                    for (JsonElement item : items) {
                        results.add(createResultItem(item.getAsJsonObject(), false));
                    }
                    return results;
                } catch (IOException e) {
                    return null;
                }

            });
        }

        private JsonObject requestJson(String params) throws IOException {
            URL url = DefaultSearch.this.ytml.createApiEndpointURL(API_ENDPOINT_SUFFIX);
            HttpURLConnection conn = DefaultSearch.this.ytml.getHttpManager().getConnection(url);
            conn.setRequestProperty("referer", "https://music.youtube.com/");
            PostFields postFields = new PostFields();
            postFields.query = DefaultSearch.this.query;
            postFields.params = params;
            String postContent = new Gson().toJson(postFields);
            String response = DefaultSearch.this.ytml.getHttpManager().executePost(conn, postContent);
            JsonObject obj =  JsonParser.parseString(response).getAsJsonObject();
            if (obj == null) {
                throw new IOException();
            }
            return obj;
        }

    }

    private static class SongFields implements Song {

        private final String id;
        private final String name;
        private final String artist;
        private final String artists;
        private final long duration;

        public SongFields(JsonObject obj, boolean hasCategoryColumn) {
            obj = obj.getAsJsonObject("musicResponsiveListItemRenderer");
            this.id = obj
                    .getAsJsonObject("doubleTapCommand")
                    .getAsJsonObject("watchEndpoint")
                    .get("videoId").getAsString();
            String titleColumn = JsonTools.extractRun(obj
                    .getAsJsonArray("flexColumns")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("musicResponsiveListItemFlexColumnRenderer")
                    .getAsJsonObject("text"));
            String artistColumn = JsonTools.extractRun(obj
                    .getAsJsonArray("flexColumns")
                    .get(hasCategoryColumn? 2 : 1).getAsJsonObject()
                    .getAsJsonObject("musicResponsiveListItemFlexColumnRenderer")
                    .getAsJsonObject("text"));
            String durationColumn = JsonTools.extractRun(obj
                    .getAsJsonArray("flexColumns")
                    .get(hasCategoryColumn? 4 : 3).getAsJsonObject()
                    .getAsJsonObject("musicResponsiveListItemFlexColumnRenderer")
                    .getAsJsonObject("text"));
            this.duration = FormatTools.durationTextToMillis(durationColumn);
            MetaConverter metaConverter = new MetaConverter(titleColumn, artistColumn);
            this.name = metaConverter.getTitle();
            this.artist = metaConverter.getArtist();
            this.artists = metaConverter.getArtists();
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getArtist() {
            return this.artist;
        }

        @Override
        public String getArtists() {
            return this.artists;
        }

        @Override
        public long getDuration() {
            return this.duration;
        }

    }

    private static class VideoFields implements Video {

        private final String id;
        private final String title;
        private final String channelName;
        private final String channelId;
        private final long duration;

        public VideoFields(JsonObject obj, boolean hasCategoryColumn) {
            obj = obj.getAsJsonObject("musicResponsiveListItemRenderer");
            this.id = obj
                    .getAsJsonObject("doubleTapCommand")
                    .getAsJsonObject("watchEndpoint")
                    .get("videoId").getAsString();
            this.title = JsonTools.extractRun(obj
                    .getAsJsonArray("flexColumns")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("musicResponsiveListItemFlexColumnRenderer")
                    .getAsJsonObject("text"));
            this.channelName = JsonTools.extractRun(obj
                    .getAsJsonArray("flexColumns")
                    .get(hasCategoryColumn? 2 : 1).getAsJsonObject()
                    .getAsJsonObject("musicResponsiveListItemFlexColumnRenderer")
                    .getAsJsonObject("text"));
            String durationColumn = JsonTools.extractRun(obj
                    .getAsJsonArray("flexColumns")
                    .get(hasCategoryColumn? 4 : 3).getAsJsonObject()
                    .getAsJsonObject("musicResponsiveListItemFlexColumnRenderer")
                    .getAsJsonObject("text"));
            this.channelId = null;
            this.duration = FormatTools.durationTextToMillis(durationColumn);
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public String getTitle() {
            return this.title;
        }

        @Override
        public String getChannelName() {
            return this.channelName;
        }

        @Override
        public String getChannelId() {
            return this.channelId;
        }

        @Override
        public long getDuration() {
            return 0;
        }

    }

}
