package com.lowlevelsubmarine.ytml.library.interfaces;

import com.lowlevelsubmarine.ytml.YTML;
import com.lowlevelsubmarine.ytml.library.fields.VideoFields;

public class Video extends Content<VideoFields> {

    public Video(YTML ytml, VideoFields fields) {
        super(ytml, fields);
    }

}
