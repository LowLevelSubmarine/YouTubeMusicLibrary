package com.lowlevelsubmarine.ytml.library.api_requests;

import com.google.gson.JsonElement;

public interface Request {

    String getEndpointSuffix();
    RequestType getType();
    String getReferrer();
    String getContent();
    void parse(JsonElement element);

}
