package com.lowlevelsubmarine.ytml.library;

import com.lowlevelsubmarine.ytml.YTML;
import com.lowlevelsubmarine.ytml.actions.RestAction;

import java.io.IOException;

public class DefaultProvider implements Provider {

    private final Search search;

    public DefaultProvider() {
        this(null);
    }

    public DefaultProvider(Search search) {
        this.search = search;
    }

    @Override
    public RestAction<Search> search(YTML ytml, String query) {
        return new RestAction<>(() -> createSearch(ytml, query));
    }

    private Search createSearch(YTML ytml, String query) {
        try {
            return new DefaultSearch(ytml, query);
        } catch (IOException e) {
            return null;
        }
    }

}
