package data;

import models.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MovieQuestions {

    private final Random rnd = new Random();

    public List<Movie> getLatestMovies() {
        List<Movie> list = new ArrayList<>();
        list.add(new Movie("m001", "Оппенгеймер", 2023, false));
        list.add(new Movie("m002", "Оно", 2017, true));
        list.add(new Movie("m003", "Титаник", 1997, false));
        list.add(new Movie("m004", "Голодные игры", 2012, true));
        list.add(new Movie("m005", "F1", 2025, false));
        return list;
    }

    public Movie pickRandomMovie() {
        List<Movie> movies = getLatestMovies();

        if (movies.isEmpty()) {
            return null;
        }

        int index = rnd.nextInt(movies.size());

        return movies.get(index);
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

            if (answer.equals("да")) {
                return movie.hasSequel();
            } else if (answer.equals("нет")) {
                return !movie.hasSequel();
            }
        }

        return false;
    }

    public Movie findMovieByTitle(String titlePart) {
        String q = titlePart.trim().toLowerCase();

        for (Movie m : getLatestMovies()) {
            if (m.getTitle().toLowerCase().contains(q)) {
                return m;
            }
        }

        return null;
    }

}
