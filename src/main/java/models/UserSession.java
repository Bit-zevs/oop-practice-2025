package models;

import java.util.*;

public class UserSession {
    private Movie pendingMovie;
    private String pendingQuestionText;
    private final User user;

    private final Set<String> watchedIds = new HashSet<>();
    private final List<Movie> watched = new ArrayList<>();

    private final Map<String, Integer> movieRatings = new HashMap<>();

    public UserSession(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
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

    public void rateMovie(Movie movie, int rating) {
        if (rating < 1 || rating > 10) {
            throw new IllegalArgumentException("Рейтинг должен быть от 1 до 10");
        }
        movieRatings.put(movie.getTitle(), rating);
    }

    public void unrateMovie(Movie movie) {
        if (movieRatings.containsKey(movie.getTitle())) {
            movieRatings.remove(movie.getTitle());
        } else {
            throw new IllegalArgumentException("Вы не оценивали этот фильм");
        }
    }

    public Integer getUserRating(Movie movie) {
        return movieRatings.get(movie.getTitle());
    }

    public Map<String, Integer> getAllRatings() {
        return new HashMap<>(movieRatings);
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
