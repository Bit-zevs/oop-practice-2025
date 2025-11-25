package data;

import models.Movie;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MovieApiClient {
    private static final String baseUrl = "https://api.poiskkino.dev";
    private static final String defaultLanguage = "ru";
    private final String apiKey;
    private final HttpClient client = HttpClient.newHttpClient();

    public MovieApiClient() {
        String apiKey = System.getenv("KINOPOISK_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("API token is not set. Please set KINOPOISK_API_KEY environment variable.");
        }
        this.apiKey = apiKey;
    }

    public List<Movie> latestMovies(int limit){
        String endpoint = "/v1.4/movie?page=1&limit=" + limit + "&lists=popular";
        return fetchMovies(endpoint);
    }

    public Movie randomMovie() {
        String endpoint = "/v1.4/movie/random?lists=oscar-best-film-nominees";
        List<Movie> movies = fetchMovies(endpoint);
        if (movies.isEmpty()) return null;
        return movies.getFirst();
    }

    public Movie movieByTitle(String titlePart){
        String endpoint = "/v1.4/movie/search?page=1&limit=1&query=" + URLEncoder.encode(titlePart, StandardCharsets.UTF_8);
        List<Movie> searchResults = fetchMovies(endpoint);
        if (searchResults.isEmpty()) return null;
        return searchResults.getFirst();
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

                String description = m.optString("description", "");
                if (description.isBlank()) {
                    description = m.optString("shortDescription", "");
                }
                if (description.isBlank()) {
                    description = "Описание отсутствует";
                }

                String posterUrl = null;
                if (m.has("poster")) {
                    JSONObject poster = m.getJSONObject("poster");
                    posterUrl = poster.optString("url", null);

                    if (posterUrl == null || posterUrl.isBlank()) {
                        posterUrl = poster.optString("previewUrl", null);
                    }
                }

                movies.add(new Movie(id, title, year, hasSequel, description, posterUrl));
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Ошибка при запросе к API КиноПоиска: " + e.getMessage());
        }

        return movies;
    }
}