package data;

import models.Movie;

import java.util.List;

public class MovieQuestions {
    private static final int DEFAULT_LATEST_LIMIT = 5;
    private final MovieApiClient apiClient = new MovieApiClient();

    public List<Movie> getLatestMovies() {
        return getLatestMovies(DEFAULT_LATEST_LIMIT);
    }

    public List<Movie> getLatestMovies(int limit) {
        return apiClient.latestMovies(limit);
    }


    public Movie pickRandomMovie() {
        return apiClient.randomMovie();
    }

    public Movie findMovieByTitle(String titlePart) {
        return apiClient.movieByTitle(titlePart);
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
}
