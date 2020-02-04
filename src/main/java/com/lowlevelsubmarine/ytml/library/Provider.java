package com.lowlevelsubmarine.ytml.library;

import com.lowlevelsubmarine.ytml.YTML;
import com.lowlevelsubmarine.ytml.actions.RestAction;

public interface Provider {

    RestAction<Search> search(YTML ytml, String query);

}
