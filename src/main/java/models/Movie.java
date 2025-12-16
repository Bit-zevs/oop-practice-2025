package models;

import java.util.HashMap;
import java.util.Map;

public class Movie {
    private final String id;
    private final String title;
    private final int year;
    private final boolean hasSequel;
    private final String description;
    private final String posterUrl;
    private final Map<String, Double> ratings;
    private static final Map<String, String> PLATFORM_NAMES = Map.of(
            "imdb", "IMDb",
            "kp", "Кинопоиск"
    );

    public Movie(String id, String title, int year, boolean hasSequel) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.hasSequel = hasSequel;
        this.description = null;
        this.posterUrl = null;
        this.ratings = new HashMap<>();
    }

    public Movie(String id, String title, int year, boolean hasSequel, String description) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.hasSequel = hasSequel;
        this.description = description;
        this.posterUrl = null;
        this.ratings = new HashMap<>();
    }

    public Movie(String id, String title, int year, boolean hasSequel, String description, String posterUrl) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.hasSequel = hasSequel;
        this.description = description;
        this.posterUrl = posterUrl;
        this.ratings = new HashMap<>();
    }

    public Movie(String id, String title, int year, boolean hasSequel, String description, String posterUrl, Map<String, Double> rating) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.hasSequel = hasSequel;
        this.description = description;
        this.posterUrl = posterUrl;
        this.ratings = rating;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public String getRating() {
        if (ratings.isEmpty()) {
            return "Рейтингов нет";
        }

        StringBuilder response = new StringBuilder();
        for (Map.Entry<String, Double> entry : ratings.entrySet()) {
            String shortKey = entry.getKey();

            if (PLATFORM_NAMES.containsKey(shortKey)) {
                String fullName = PLATFORM_NAMES.get(shortKey);
                response.append(fullName).append(":").append(entry.getValue()).append("\n");
            }
        }

        if (response.isEmpty()) {
            return "Нет рейтингов от известных платформ";
        }

        return response.toString();
    }

    public boolean hasSequel() {
        return hasSequel;
    }

    public String getDescription() {
        return description;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String toString() {
        return "*" + title + "* (" + year + ")" + (hasSequel ? " — имеет продолжение" : "");
    }
}
