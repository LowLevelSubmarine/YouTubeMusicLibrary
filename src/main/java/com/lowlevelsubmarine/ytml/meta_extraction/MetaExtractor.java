package com.lowlevelsubmarine.ytml.meta_extraction;

public interface MetaExtractor {

    Meta extract(String videoTitle, String channelName);

    interface Meta {
        String getTitle();
        String getArtist();
        String getArtists();
    }

}
