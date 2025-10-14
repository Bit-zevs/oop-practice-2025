package services;

import models.Movie;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieNewsServiceTest {

    private final MovieNewsService service = new MovieNewsService();

    @Test
    void testGetLatestNews() {
        List<Movie> movies = List.of(new Movie("m001", "Test Movie", 2020, true));
        String news = service.getLatestNews(movies);
        assertTrue(news.contains("Test Movie"));

        String emptyNews = service.getLatestNews(List.of());
        assertEquals("Пока нет новинок.", emptyNews);
    }
}
