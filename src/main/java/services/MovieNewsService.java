package services;

import models.Movie;

import java.util.List;

public class MovieNewsService {
    public String getLatestNews(List<Movie> movies) {
        if (movies == null || movies.isEmpty()) {
            return "Пока нет новинок.";
        }
        Movie m = movies.getFirst();
        String news = "Новость: фильм \"" + m.getTitle() + "\" вышел в " + m.getYear();
        if (m.hasSequel())
            news += " и уже готовится продолжение!";
        else
            news += ".";
        return news;
    }
}
