package com.lowlevelsubmarine.ytml.library;

import com.lowlevelsubmarine.ytml.actions.RestAction;

import java.util.List;

public interface Search {

    String getQuery();
    Result<Song> getSongs();
    Result<Artist> getArtists();
    Result<Video> getVideos();

    interface Result<T> {

        int pos();
        List<T> get();
        RestAction<List<T>> parse();
        //RestAction<List<T>> parse(int page);

    }

    enum ResultType {
        SONG, VIDEO, ARTIST, ALBUM,
    }

}
