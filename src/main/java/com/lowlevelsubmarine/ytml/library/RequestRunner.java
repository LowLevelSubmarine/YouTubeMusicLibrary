package com.lowlevelsubmarine.ytml.library;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.lowlevelsubmarine.ytml.YTML;
import com.lowlevelsubmarine.ytml.library.api_requests.Request;
import com.lowlevelsubmarine.ytml.library.api_requests.RequestType;

import java.io.IOException;
import java.net.HttpURLConnection;

public class RequestRunner {

    private final YTML ytml;

    public RequestRunner(YTML ytml) {
        this.ytml = ytml;
    }

    public <T extends Request> T run(T request) throws IOException {
        HttpURLConnection connection = ytml.getHttpManager().getConnection(ytml.createApiEndpointURL(request.getEndpointSuffix()));
        if (request.getReferrer() != null) {
            connection.setRequestProperty("referer", request.getReferrer());
        }
        JsonElement element = null;
        if (request.getType() == RequestType.GET) {
            element = executeGet(connection);
        } else if (request.getType() == RequestType.POST) {
            element = executePost(connection, request.getContent());
        } else {
            throw new InternalError("Unknown RequestType: " + request.getType().name());
        }
        request.parse(element);
        return request;
    }

    public JsonElement executeGet(HttpURLConnection connection) {
        return JsonParser.parseString(this.ytml.getHttpManager().executeGet(connection));
    }

    private JsonElement executePost(HttpURLConnection connection, String postContent) {
        String post = this.ytml.getHttpManager().executePost(connection, postContent);
        return JsonParser.parseString(post);
    }

}
