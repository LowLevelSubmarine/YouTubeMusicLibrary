package com.lowlevelsubmarine.ytml.library;

public interface ThumbnailManifest {

    Thumbnail get(ThumbnailSize size);

    interface Thumbnail {
        int getHeight();
        int getWidth();
        String getUrl();
    }

    enum ThumbnailSize {

        SMALL(60), MEDIUM(120), LARGE(225), MAX_RES(544);

        private final int length;

        ThumbnailSize(int length) {
            this.length = length;
        }

        public int getLength() {
            return this.length;
        }

    }

}
