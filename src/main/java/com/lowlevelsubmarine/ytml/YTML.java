package com.lowlevelsubmarine.ytml;

import com.lowlevelsubmarine.ytml.actions.RestAction;
import com.lowlevelsubmarine.ytml.http.HttpManager;
import com.lowlevelsubmarine.ytml.deprecated_library.*;
import com.lowlevelsubmarine.ytml.library.RequestRunner;
import com.lowlevelsubmarine.ytml.library.interfaces.Search;
import com.lowlevelsubmarine.ytml.tools.URLCreator;

import java.io.IOException;
import java.net.URL;

public class YTML {

    private static final String API_ENDPOINT_PREFIX = "https://music.youtube.com/youtubei/v1/";

    private final RequestRunner requestRunner = new RequestRunner(this);
    private final String apiKey;
    private final HttpManager httpManager;

    YTML(String apiKey, HttpManager httpManager) {
        this.apiKey = apiKey;
        this.httpManager = httpManager;
    }

    public RequestRunner getRequestRunner() {
        return this.requestRunner;
    }

    public RestAction<Search> search(String query) {
        return new RestAction<>(() -> runSearch(query));
    }

    public Search runSearch(String query) {
        try {
            return new Search(this, query);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HttpManager getHttpManager() {
        return this.httpManager;
    }

    public URL createApiEndpointURL(String apiEndpointSuffix) throws IllegalStateException {
        return URLCreator.create(API_ENDPOINT_PREFIX + apiEndpointSuffix + "?alt=json&key=" + this.apiKey);
    }

}
