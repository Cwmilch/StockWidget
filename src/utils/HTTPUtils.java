package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPUtils {

    public static String getRequest(String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL request = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) request.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String next;
            while ((next = reader.readLine()) != null) {
                json.append(next);
            }
            reader.close();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.setPrettyPrinting().create();
            JsonParser jsonParser = new JsonParser();
            return gson.toJson(jsonParser.parse(json.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
