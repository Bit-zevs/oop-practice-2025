package data;

import models.Movie;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MovieQuestions {

    private static final String BASE_URL = "https://api.poiskkino.dev";
    private static final String DEFAULT_LANGUAGE = "ru";
    private static final int DEFAULT_LATEST_LIMIT = 5;

    private final MovieApiClient apiClient;

    public MovieQuestions() {
        String apiKey = System.getenv("KINOPOISK_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("API token is not set. Please set KINOPOISK_API_KEY environment variable.");
        }

        this.apiClient = new MovieApiClient(BASE_URL, apiKey, DEFAULT_LANGUAGE);
    }

    public List<Movie> getLatestMovies() {
        return getLatestMovies(DEFAULT_LATEST_LIMIT);
    }

    public List<Movie> getLatestMovies(int limit) {
        String endpoint = "/v1.4/movie?page=1&limit=" + limit + "&lists=popular";
        return apiClient.fetchMovies(endpoint);
    }


    public Movie pickRandomMovie() {
        String endpoint = "/v1.4/movie/random?lists=oscar-best-film-nominees";
        List<Movie> movies = apiClient.fetchMovies(endpoint);
        if (movies.isEmpty()) return null;
        return movies.getFirst();
    }

    public String generateQuestionFor(Movie movie) {
        return "В каком году вышел фильм \"" + movie.getTitle() + "\"?";
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
        String endpoint = "/v1.4/movie/search?page=1&limit=1&query=" + URLEncoder.encode(titlePart, StandardCharsets.UTF_8);
        List<Movie> searchResults = apiClient.fetchMovies(endpoint);
        if (searchResults.isEmpty()) return null;
        return searchResults.getFirst();

    }
}

