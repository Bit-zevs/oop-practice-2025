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
import java.util.*;

public class MovieQuestions {

    private final Random rnd = new Random();
//    private static final String API_TOKEN_TEST = "CE3Q1NW-PTZ4A4Z-M8PNE4D-91X7H1D";
    private static final String API_TOKEN = "RPQH847-PMK4727-HXX41CQ-NEXJY7N";
    private static final String BASE_URL = "https://api.poiskkino.dev";
    private static final String DEFAULT_LANGUAGE = "ru";

    private final HttpClient client = HttpClient.newHttpClient();

    public List<Movie> getLatestMovies() {
        return getLatestMovies(5);
    }

    public List<Movie> getLatestMovies(int limit) {
        String endpoint = "/v1.4/movie?page=1&limit=5&lists=top250";
        return fetchMovies(endpoint, limit);
    }

    private List<Movie> fetchMovies(String endpoint, int limit) {
        List<Movie> movies = new ArrayList<>();
        try {
            URI uri = URI.create(BASE_URL + endpoint);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Accept-Language", DEFAULT_LANGUAGE)
                    .header("X-API-KEY", API_TOKEN)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (response.statusCode() != 200) {
                System.err.println("Kinopoisk API returned status: " + response.statusCode());
                return movies;
            }

            JSONObject obj = new JSONObject(response.body());
            JSONArray results = obj.optJSONArray("docs");
            if (results == null) return movies;

            for (int i = 0; i < Math.min(limit, results.length()); i++) {
                JSONObject m = results.getJSONObject(i);
                String id = String.valueOf(m.optInt("id", 0));

                String name = m.optString("name", "");
                String altName = m.optString("alternativeName", "");
                String title = name != null && !(name.isEmpty()) ? name
                        : (!altName.isEmpty() ? altName : "Неизвестно");

                int year = m.optInt("year", 0);

                movies.add(new Movie(id, title, year, false));
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Ошибка при запросе к API КиноПоиска: " + e.getMessage());
        }

        return movies;
    }


    public Movie pickRandomMovie() {
        List<Movie> movies = getLatestMovies(5);
        if (movies.isEmpty()) return null;
        return movies.get(rnd.nextInt(movies.size()));
    }

    public String generateQuestionFor(Movie movie) {
        if (rnd.nextBoolean()) {
            return "В каком году вышел фильм \"" + movie.getTitle() + "\"?";
        } else {
            return "У фильма \"" + movie.getTitle() + "\" есть продолжение? (да/нет)";
        }
    }

    public boolean checkAnswer(Movie movie, String questionText, String userAnswer) {
        if (questionText.contains("году")) {
            try {
                int answerYear = Integer.parseInt(userAnswer.trim());
                return answerYear == movie.getYear();
            } catch (NumberFormatException e) {
                return false;
            }
        }

        if (questionText.toLowerCase().contains("есть продолжение")) {
            String answer = userAnswer.trim().toLowerCase();
            if (answer.equals("да")) return movie.hasSequel();
            else if (answer.equals("нет")) return !movie.hasSequel();
        }

        return false;
    }

    public Movie findMovieByTitle(String titlePart) {
        List<Movie> searchResults = searchMovies(titlePart);
        if (searchResults.isEmpty()) return null;
        return searchResults.getFirst();
    }

    private List<Movie> searchMovies(String query) {
        List<Movie> resultsList = new ArrayList<>();
        try {
            String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
            URI uri = URI.create(BASE_URL + "/v1.4/movie/search?page=1&limit=1&query=" + encoded);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Accept-Language", DEFAULT_LANGUAGE)
                    .header("X-API-KEY", API_TOKEN)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (response.statusCode() != 200) {
                System.err.println("Kinopoisk search returned status: " + response.statusCode());
                return resultsList;
            }

            JSONObject obj = new JSONObject(response.body());
            JSONArray results = obj.optJSONArray("docs");
            if (results == null) return resultsList;

            for (int i = 0; i < Math.min(5, results.length()); i++) {
                JSONObject m = results.getJSONObject(i);
                String id = String.valueOf(m.optInt("id", 0));

                String name = m.optString("name", "");
                String altName = m.optString("alternativeName", "");
                String title = name != null && !(name.isEmpty()) ? name
                        : (!altName.isEmpty() ? altName : "Неизвестно");

                int year = m.optInt("year", 0);

                resultsList.add(new Movie(id, title, year, false));
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Ошибка при поиске через API КиноПоиска: " + e.getMessage());
        }

        return resultsList;
    }
}
