package com.lowlevelsubmarine.ytml.lyrics;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface LyricsParser {

    Lyrics getLyrics(String identifier);
    Collection<Lyrics> searchLyrics(String identifier);

    interface Lyrics {

        String getUrl();
        String getId();
        String getTitle();
        Collection<String> getAuthors();
        CompletableFuture<Collection<Element>> getElements();
        CompletableFuture<Collection<Comment>> getComments();
        CompletableFuture<String> getDescription();

    }

    interface Element {

        String getTitle();
        String getContent();

    }

    interface Comment {

        String getAuthor();
        String getContent();
        String getUrl();
        long getTimestamp();

    }

}
