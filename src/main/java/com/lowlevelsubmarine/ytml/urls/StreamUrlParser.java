package com.lowlevelsubmarine.ytml.urls;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface StreamUrlParser {

    CompletableFuture<Manifest> parse(String url);

    interface Manifest {

        Collection<Url> getStreamUrls();

    }

    interface Url {

        String getStreamUrl();
        String getBps();

    }

}
