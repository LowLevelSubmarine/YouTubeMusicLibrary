package com.lowlevelsubmarine.ytml.deprecated_library;

public class MetaConverter {

    private final String title;
    private final String artist;
    private final String artists;

    public MetaConverter(String titleColumn, String artistColumn) {
        this.title = titleColumn;
        this.artist = artistColumn;
        this.artists = artistColumn;
    }

    public String getTitle() {
        return this.title;
    }

    public String getArtist() {
        return this.artist;
    }

    public String getArtists() {
        return this.artists;
    }

}
