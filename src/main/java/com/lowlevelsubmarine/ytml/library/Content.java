package com.lowlevelsubmarine.ytml.library;

import com.lowlevelsubmarine.ytml.actions.RestAction;
import com.lowlevelsubmarine.ytml.urls.StreamUrlParser;

public interface Content {

    String getId();
    RestAction<StreamUrlParser.Manifest> getUrls();

}
