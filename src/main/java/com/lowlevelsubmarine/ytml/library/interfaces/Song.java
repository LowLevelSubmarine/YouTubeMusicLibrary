package com.lowlevelsubmarine.ytml.library.interfaces;

import com.lowlevelsubmarine.ytml.YTML;
import com.lowlevelsubmarine.ytml.library.fields.SongFields;

public class Song extends Content<SongFields> {

    public Song(YTML ytml, SongFields fields) {
        super(ytml, fields);
    }

}
