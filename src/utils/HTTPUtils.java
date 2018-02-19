package utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPUtils {

    /**
     * Return the data from a GET request as a JsonElement
     *
     * @param url the url to request
     * @return the data as a JsonElement
     */
    public static JsonElement getRequest(String url) {
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

            JsonParser jsonParser = new JsonParser();
            return jsonParser.parse(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
