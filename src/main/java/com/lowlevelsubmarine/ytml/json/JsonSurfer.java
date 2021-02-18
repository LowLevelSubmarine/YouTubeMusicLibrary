package com.lowlevelsubmarine.ytml.json;

import com.google.gson.JsonElement;

public class JsonSurfer {

    private final JsonElement element;

    public JsonSurfer(JsonElement element) {
        this.element = element;
    }

    public JsonElement get(Object... keys) {
        JsonElement element = this.element.deepCopy();
        try {
            for (Object key : keys) {
                if (key instanceof Integer) {
                    int index = (Integer) key;
                    element = element.getAsJsonArray().get(index);
                } else if (key instanceof String) {
                    String index = (String) key;
                    element = element.getAsJsonObject().get(index);
                }
            }
            return element;
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return this.element.toString();
    }

    public static class InvalidPathException extends RuntimeException {
        public InvalidPathException() {
            super();
        }
        public InvalidPathException(String string) {
            super(string);
        }
    }

}
