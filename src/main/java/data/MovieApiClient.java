package data;

import models.Movie;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MovieApiClient {
    private final String baseUrl;
    private final String apiKey;
    private final String defaultLanguage;
    private final HttpClient client = HttpClient.newHttpClient();

    public MovieApiClient(String baseUrl, String apiKey, String defaultLanguage) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.defaultLanguage = defaultLanguage;
    }

    public List<Movie> fetchMovies(String endpoint) {
        List<Movie> movies = new ArrayList<>();
        try {
            URI uri = URI.create(baseUrl + endpoint);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Accept-Language", defaultLanguage)
                    .header("X-API-KEY", apiKey)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (response.statusCode() != 200) {
                System.err.println("Kinopoisk API returned status: " + response.statusCode());
                return movies;
            }

            JSONObject obj = new JSONObject(response.body());
            JSONArray results = obj.optJSONArray("docs");
            if (results == null) {
                results = new JSONArray();
                results.put(obj);
            }

            for (int i = 0; i < results.length(); i++) {
                JSONObject m = results.getJSONObject(i);

                String id = String.valueOf(m.optInt("id", 0));

                String name = m.optString("name", "");
                String altName = m.optString("alternativeName", "");
                String title = (name != null && !name.isEmpty()) ? name : (!altName.isEmpty() ? altName : "Неизвестно");

                int year = m.optInt("year", 0);

                boolean hasSequel = m.optBoolean("hasSequel", false);

                movies.add(new Movie(id, title, year, hasSequel));
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Ошибка при запросе к API КиноПоиска: " + e.getMessage());
        }

        return movies;
    }
}
