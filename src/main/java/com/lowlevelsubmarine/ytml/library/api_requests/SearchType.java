package com.lowlevelsubmarine.ytml.library.api_requests;

public enum SearchType {

    ARTIST("Artists"),
    SONG("Songs"),
    ALBUM("Albums"),
    VIDEO("Videos");

    final String key;

    SearchType(String key) {
        this.key = key;
    }

    static SearchType getByKey(String key) {
        for (SearchType searchType : values()) {
            if (key.equals(searchType.key)) {
                return searchType;
            }
        }
        return null;
    }

}