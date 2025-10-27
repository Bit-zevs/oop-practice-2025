package services;

import models.Movie;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieNewsServiceTest {

    private final MovieNewsService service = new MovieNewsService();

    @Test
    void testGetLatestNews_WithSequel() {
        List<Movie> movies = List.of(new Movie("m001", "Test Movie", 2020, true));
        String news = service.getLatestNews(movies);
        assertEquals("Новость: фильм \"Test Movie\" вышел в 2020 и уже готовится продолжение!", news);
    }

    @Test
    void testGetLatestNews_WithoutSequel() {
        List<Movie> movies = List.of(new Movie("m002", "Old Classic", 1990, false));
        String news = service.getLatestNews(movies);
        assertEquals("Новость: фильм \"Old Classic\" вышел в 1990.", news);
    }

    @Test
    void testGetLatestNews_NoMovies() {
        String news = service.getLatestNews(List.of());
        assertEquals("Пока нет новинок.", news);
    }
}
