package demo.networking.servers.web.http.utils;

import com.google.gson.Gson;

public class JSONUtils {

    private static final Gson GSON = new Gson();

    public static String get(final Object object) {
        return GSON.toJson(object);
    }

    public static <T> T get(final String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

}
