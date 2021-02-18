package com.lowlevelsubmarine.ytml.library.interfaces;

import com.lowlevelsubmarine.ytml.YTML;
import com.lowlevelsubmarine.ytml.library.api_requests.Searchable;
import com.lowlevelsubmarine.ytml.library.fields.ContentFields;
import com.lowlevelsubmarine.ytml.library.fields.Thumbnail;

import java.util.Objects;

public class Content<T extends ContentFields & Searchable> {

    private final YTML ymtl;
    private final String id;
    private final String title;
    private final String artist;
    private final long duration;
    private final Thumbnail thumbnail;

    public Content(YTML ytml, T fields) {
        this.ymtl = ytml;
        this.id = fields.getId();
        this.title = fields.getTitle();
        this.artist = fields.getArtist();
        this.duration = fields.getDuration();
        this.thumbnail = fields.getThumbnail();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public long getDuration() {
        return duration;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content<?> other = (Content<?>) o;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
