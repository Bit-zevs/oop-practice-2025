package models;

public class Movie {
    private final String id;
    private final String title;
    private final int year;
    private final boolean hasSequel;

    public Movie(String id, String title, int year, boolean hasSequel) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.hasSequel = hasSequel;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public int getYear() { return year; }
    public boolean hasSequel() { return hasSequel; }

    public String toString() {
        return title + " (" + year + ")" + (hasSequel ? " — has sequel" : "");
    }
}
