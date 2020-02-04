package com.lowlevelsubmarine.ytml;

import com.lowlevelsubmarine.ytml.actions.RestAction;
import com.lowlevelsubmarine.ytml.http.HttpManager;
import com.lowlevelsubmarine.ytml.library.*;
import com.lowlevelsubmarine.ytml.tools.URLCreator;

import java.net.URL;

public class YTML {

    private static final String API_ENDPOINT_PREFIX = "https://music.youtube.com/youtubei/v1/";

    private final HttpManager httpManager = new HttpManager();
    private String key = null;
    private final Provider provider;

    public YTML() {
        this(new DefaultProvider());
    }

    public YTML(Provider provider) {
        this.provider = provider;
    }

    public RestAction<Search> search(String query) {
        return this.provider.search(this, query);
    }

    public HttpManager getHttpManager() {
        return this.httpManager;
    }

    public boolean isKeyFetched() {
        return this.key != null;
    }

    public void fetchKey() {
        fetchKey(new DefaultKeyProvider(this));
    }

    public void fetchKeyAsync() {
        fetchKeyAsync(new DefaultKeyProvider(this));
    }

    public URL createApiEndpointURL(String apiEndpointSuffix) throws IllegalStateException {
        if (isKeyFetched()) {
            return URLCreator.create(API_ENDPOINT_PREFIX + apiEndpointSuffix + "?alt=json&key=" + this.key);
        } else {
            throw new IllegalStateException("Key not fetched");
        }
    }

    private void fetchKey(KeyParser keyParser) {
        this.key = keyParser.getKey().complete();
    }

    private void fetchKeyAsync(KeyParser keyParser) {
        keyParser.getKey().queue(this::onKey);
    }

    private void onKey(String key) {
        this.key = key;
    }

}
