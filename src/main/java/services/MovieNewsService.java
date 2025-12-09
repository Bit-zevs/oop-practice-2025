package services;

import models.Movie;

import java.util.List;

public class MovieNewsService {
    public String getLatestNews(List<Movie> movies) {
        if (movies == null || movies.isEmpty()) {
            return "Пока нет новинок.";
        }
        Movie movie = movies.getFirst();
        String news = "Новость: фильм \"" + movie.getTitle() + "\" вышел в " + movie.getYear();
        if (movie.hasSequel())
            news += " и уже готовится продолжение!";
        else
            news += ".";
        return news;
    }
}
