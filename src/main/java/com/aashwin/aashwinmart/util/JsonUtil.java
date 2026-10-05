package com.aashwin.aashwinmart.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

/**
 * Utility for Google Gson JSON serialization and HTTP JSON response delivery.
 */
public final class JsonUtil {

    private static final Gson GSON = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .registerTypeAdapter(Timestamp.class, (com.google.gson.JsonSerializer<Timestamp>) (src, typeOfSrc, context) ->
                    new com.google.gson.JsonPrimitive(src != null ? src.toString() : null))
            .create();

    private JsonUtil() {
        // Prevent instantiation
    }

    public static Gson getGson() {
        return GSON;
    }

    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return GSON.fromJson(json, classOfT);
    }

    public static void writeJsonResponse(HttpServletResponse response, int statusCode, Object data) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(statusCode);
        try (PrintWriter writer = response.getWriter()) {
            writer.write(toJson(data));
            writer.flush();
        }
    }
}
