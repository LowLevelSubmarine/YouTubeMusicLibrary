package com.lowlevelsubmarine.ytml;

import com.lowlevelsubmarine.ytml.actions.RestAction;
import com.lowlevelsubmarine.ytml.http.HttpManager;
import com.lowlevelsubmarine.ytml.library.api_key.DefaultKeyProvider;
import com.lowlevelsubmarine.ytml.library.api_key.KeyProvider;

public class YTMLBuilder {

    private final HttpManager httpManager = new HttpManager();
    private KeyProvider keyProvider;

    public YTMLBuilder() {
        this.keyProvider = new DefaultKeyProvider(this.httpManager);
    }

    public void setKeyProvider(KeyProvider keyProvider) {
        this.keyProvider = keyProvider;
    }

    public RestAction<YTML> build() {
        return new RestAction<>(this::createYTML);
    }

    private YTML createYTML() {
        return new YTML(this.keyProvider.fetchKey().complete(), this.httpManager);
    }

}
