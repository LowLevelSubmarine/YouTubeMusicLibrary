package com.lowlevelsubmarine.ytml.library;

public interface Video extends Content {

    String getTitle();
    String getChannelName();
    String getChannelId();
    long getDuration();

}
