package models;

public class Movie {
    private final String id;
    private final String title;
    private final int year;
    private final boolean hasSequel;
    private final String description;
    private final String posterUrl;

    public Movie(String id, String title, int year, boolean hasSequel) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.hasSequel = hasSequel;
        this.description = null;
        this.posterUrl = null;
    }

    public Movie(String id, String title, int year, boolean hasSequel, String description) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.hasSequel = hasSequel;
        this.description = description;
        this.posterUrl = null;
    }

    public Movie(String id, String title, int year, boolean hasSequel, String description, String posterUrl) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.hasSequel = hasSequel;
        this.description = description;
        this.posterUrl = posterUrl;
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
