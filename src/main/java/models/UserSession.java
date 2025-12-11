package models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserSession {
    private Movie pendingMovie;
    private String pendingQuestionText;
    private User user; // Используем новый класс User

    private final Set<String> watchedIds = new HashSet<>();
    private final List<Movie> watched = new ArrayList<>();

    public UserSession(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean hasPendingQuestion() {
        return pendingMovie != null && pendingQuestionText != null;
    }

    public Movie getPendingMovie() {
        return pendingMovie;
    }

    public String getPendingQuestionText() {
        return pendingQuestionText;
    }

    public void setPendingQuestion(Movie movie, String text) {
        this.pendingMovie = movie;
        this.pendingQuestionText = text;
    }

    public void clearPendingQuestion() {
        this.pendingMovie = null;
        this.pendingQuestionText = null;
    }

    public void markWatched(Movie movie) {
        if (!watchedIds.add(movie.getId())) {
            throw new IllegalStateException("Movie in watched list yet!");
        }
        watched.add(movie);
    }

    public void markUnwatched(Movie movie) {
        if (!watchedIds.remove(movie.getId())) {
            throw new IllegalStateException("Movie not in watched list!");
        }
        watched.removeIf(m -> m.getId().equals(movie.getId()));
    }

    public boolean isMovieWatched(Movie movie) {
        if (movie == null) return false;
        return watchedIds.contains(movie.getId());
    }

    public void addWatchedMovie(Movie movie) {
        if (!watchedIds.contains(movie.getId())) {
            watchedIds.add(movie.getId());
            watched.add(movie);
        }
    }

    public List<Movie> getWatched() {
        return new ArrayList<>(watched);
    }
}
