package com.lowlevelsubmarine.ytml.library.api_requests;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lowlevelsubmarine.ytml.json.JsonSurfer;
import com.lowlevelsubmarine.ytml.deprecated_library.PostFields;
import com.lowlevelsubmarine.ytml.library.fields.SongFields;
import com.lowlevelsubmarine.ytml.library.fields.Thumbnail;
import com.lowlevelsubmarine.ytml.library.fields.VideoFields;
import com.lowlevelsubmarine.ytml.tools.FormatTools;
import com.lowlevelsubmarine.ytml.json.JsonTools;

import java.util.LinkedList;
import java.util.Objects;

public class SearchRequest implements Request {

    private final ResultFields[] RESULT_Fields_ROUTERS = new ResultFields[]{new SongResultFields(), new VideoResultFields()};

    private final String query;
    private final String params;
    private SearchType bestMatchSearchType;
    private ResultFields<SongFields> songsResults;
    private ResultFields<VideoFields> videoResults;

    public SearchRequest(String query) {
        this(query, null);
    }

    public SearchRequest(String query, String params) {
        this.query = query;
        this.params = params;
    }

    public SearchType getBestMatchSearchType() {
        return this.bestMatchSearchType;
    }

    public ResultFields<SongFields> getSongResult() {
        return this.songsResults;
    }

    public ResultFields<VideoFields> getVideoResult() {
        return this.videoResults;
    }

    @Override
    public String getEndpointSuffix() {
        return "search";
    }

    @Override
    public RequestType getType() {
        return RequestType.POST;
    }

    @Override
    public String getReferrer() {
        return "https://music.youtube.com/";
    }

    @Override
    public String getContent() {
        PostFields postFields = new PostFields();
        postFields.query = this.query;
        postFields.params = this.params;
        return postFields.toString();
    }

    @Override
    public void parse(JsonElement root) {
        for (JsonElement element : new JsonSurfer(root).get("contents", "sectionListRenderer" , "contents").getAsJsonArray()) {
            JsonSurfer surfer = new JsonSurfer(element);
            if (surfer.dive("musicShelfRenderer")) {
                String title;
                if (surfer.get("title") != null) {
                    title = JsonTools.extractRun(surfer.get("title").getAsJsonObject());
                } else {
                    title = getHeaderTitle(root);
                }
                SearchType searchType = SearchType.getByKey(title);
                for(ResultFields resultFields : RESULT_Fields_ROUTERS) {
                    if (resultFields.getSearchType() == searchType) {
                        resultFields.parse(surfer.get());
                    }
                }
            }
        }
    }

    private String getHeaderTitle(JsonElement root) {
        JsonArray chips = new JsonSurfer(root).get(
                "header", "musicHeaderRenderer", "header", "chipCloudRenderer", "chips"
        ).getAsJsonArray();
        for (JsonElement chip : chips) {
            JsonSurfer chipSurfer = new JsonSurfer(chip);
            if (chipSurfer.get("chipCloudChipRenderer", "style", "styleType").getAsString().equals("STYLE_PRIMARY")) {
                return JsonTools.extractRun(chipSurfer.get("chipCloudChipRenderer", "text").getAsJsonObject());
            }
        }
        return null;
    }

    public abstract class ResultFields<T extends Searchable> {

        protected abstract SearchType getSearchType();
        protected abstract T convertItem(JsonObject jsonObject, boolean isDetailedSearch);
        protected abstract void setVariable(SearchRequest searchRequests);

        private final LinkedList<T> items = new LinkedList<>();
        private String detailedParams = null;

        public void parse(JsonElement content) {
            JsonSurfer surfer = new JsonSurfer(content);
            for (JsonElement element : surfer.get("contents").getAsJsonArray()) {
                items.add(convertItem(element.getAsJsonObject().getAsJsonObject("musicResponsiveListItemRenderer"), isDetailedSearch()));
            }
            JsonElement detailedParamsElement = surfer.get("bottomEndpoint", "searchEndpoint", "params");
            if (detailedParamsElement != null) {
                this.detailedParams = detailedParamsElement.getAsString();
            }
            setVariable(SearchRequest.this);
        }

        public LinkedList<T> getItems() {
            return this.items;
        }

        public String getDetailedParams() {
            return this.detailedParams;
        }

    }

    private boolean isDetailedSearch() {
        return this.params != null;
    }

    public class SongResultFields extends ResultFields<SongFields> {

        @Override
        protected SearchType getSearchType() {
            return SearchType.SONG;
        }

        @Override
        protected SongFields convertItem(JsonObject jsonObject, boolean isDetailedSearch) {
            JsonSurfer surfer = new JsonSurfer(jsonObject);
            int textOffset = isDetailedSearch ? 0 : 2;
            return new SongFields() {
                @Override public String getId() {
                    return surfer.get(
                            "playlistItemData", "videoId").getAsString();
                }
                @Override public String getTitle() {
                    return JsonTools.extractRun(surfer.get(
                            "flexColumns", 0, "musicResponsiveListItemFlexColumnRenderer", "text").getAsJsonObject()
                    );
                }
                @Override public String getArtist() {
                    return surfer.get(
                            "flexColumns", 1, "musicResponsiveListItemFlexColumnRenderer", "text", "runs", textOffset, "text").getAsString();
                }
                @Override public long getDuration() {
                    JsonSurfer runs = surfer.getSurfer("flexColumns", 1, "musicResponsiveListItemFlexColumnRenderer", "text", "runs");
                    return FormatTools.durationTextToMillis(Objects.requireNonNull(runs.get(runs.size()-1, "text").getAsString()));
                }
                @Override public Thumbnail getThumbnail() {
                    return null;
                }
            };
        }

        @Override
        protected void setVariable(SearchRequest searchRequest) {
            searchRequest.songsResults = this;
        }

    }

    public class VideoResultFields extends ResultFields<VideoFields> {

        @Override
        protected SearchType getSearchType() {
            return SearchType.VIDEO;
        }

        @Override
        protected VideoFields convertItem(JsonObject jsonObject, boolean isDetailedSearch) {
            JsonSurfer surfer = new JsonSurfer(jsonObject);
            int textOffset = isDetailedSearch ? 0 : 2;
            return new VideoFields() {
                @Override public String getId() {
                    return surfer.get(
                            "playlistItemData", "videoId").getAsString();
                }
                @Override public String getTitle() {
                    return JsonTools.extractRun(surfer.get(
                            "flexColumns", 0, "musicResponsiveListItemFlexColumnRenderer", "text").getAsJsonObject()
                    );
                }
                @Override public String getArtist() {
                    return surfer.get(
                            "flexColumns", 1, "musicResponsiveListItemFlexColumnRenderer", "text", "runs", textOffset, "text").getAsString();
                }
                @Override public long getDuration() {
                    JsonSurfer runs = surfer.getSurfer("flexColumns", 1, "musicResponsiveListItemFlexColumnRenderer", "text", "runs");
                    return FormatTools.durationTextToMillis(Objects.requireNonNull(runs.get(runs.size()-1, "text").getAsString()));
                }
                @Override public Thumbnail getThumbnail() {
                    return null;
                }
            };
        }

        @Override
        protected void setVariable(SearchRequest searchRequests) {
            searchRequests.videoResults = this;
        }

    }
}
