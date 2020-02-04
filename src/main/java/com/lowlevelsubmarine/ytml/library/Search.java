package com.lowlevelsubmarine.ytml.library;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Search {

    String getQuery();
    Result<Song> getSongs();
    Result<Artist> getArtists();
    Result<Video> getVideos();

    interface Result<T> {

        int pos();
        List<T> get();
        CompletableFuture<List<T>> parse();

    }

}
