package com.lowlevelsubmarine.ytml.deprecated_library;

import com.lowlevelsubmarine.ytml.YTML;
import com.lowlevelsubmarine.ytml.actions.RestAction;
import com.lowlevelsubmarine.ytml.meta_extraction.MetaExtractor;

public interface Provider {

    RestAction<Search> search(YTML ytml, String query);
    MetaExtractor getMetaExtractor(YTML ytml);

}
