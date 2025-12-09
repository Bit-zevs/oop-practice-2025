package models;


import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class UserSession {
    private Movie pendingMovie;
    private String pendingQuestionText;
    private final Set<String> watchedIds = new HashSet<>();
    private final List<Movie> watched = new ArrayList<>();

    public boolean hasPendingQuestion() {
        return pendingMovie != null && pendingQuestionText != null;
    }

    public Movie getPendingMovie() { return pendingMovie; }
    public String getPendingQuestionText() { return pendingQuestionText; }

    public void setPendingQuestion(Movie movie, String text) {
        this.pendingMovie = movie;
        this.pendingQuestionText = text;
    }

    public void clearPendingQuestion() {
        this.pendingMovie = null;
        this.pendingQuestionText = null;
    }

    public void markWatched(Movie movie) {
        if (watchedIds.add(movie.getId())) {
            watched.add(movie);
        }
    }

    public List<Movie> getWatched() {
        return new ArrayList<>(watched);
    }

}
