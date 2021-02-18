package com.lowlevelsubmarine.ytml.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JsonTools {

    public static String extractRun(JsonObject obj) {
        JsonArray texts = obj.getAsJsonArray("runs");
        if (texts.size() > 0) {
            return texts.get(0).getAsJsonObject().get("text").getAsString();
        }
        return null;
    }

}
