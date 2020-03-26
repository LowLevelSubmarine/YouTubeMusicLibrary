package com.lowlevelsubmarine.ytml.meta_extraction;

public class DefaultMetaExtractor implements MetaExtractor {

    @Override
    public Meta extract(String videoTitle, String channelName) {
        return new Parser(videoTitle, channelName);
    }

    private static class Parser implements Meta {

        private static final String REGEX_SECTION_MARKER = "[ ]*[-–/:|\\\\]+[ ]+";

        private String title;
        private String artist;
        private String artists;

        public Parser(String videoTitle, String channelName) {
            String[] sections = parseSections(videoTitle);
            if (sections.length > 1) {

            } else {
                this.title = videoTitle;
                this.artist = channelName;
            }
        }

        private String[] parseSections(String raw) {
            return raw.split(REGEX_SECTION_MARKER);
        }

        @Override
        public String getTitle() {
            return this.title;
        }

        @Override
        public String getArtist() {
            return this.artist;
        }

        @Override
        public String getArtists() {
            return this.artists;
        }

    }

}
