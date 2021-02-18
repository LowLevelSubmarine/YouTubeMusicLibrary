package com.lowlevelsubmarine.ytml.library.api_key;

import com.lowlevelsubmarine.ytml.actions.RestAction;

public interface KeyProvider {

    RestAction<String> fetchKey();

}
