package models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovieTest {

    @Test
    void testMovieProperties() {
        Movie movie = new Movie("m001", "Test Movie", 2020, true);

        assertEquals("m001", movie.getId());
        assertEquals("Test Movie", movie.getTitle());
        assertEquals(2020, movie.getYear());
        assertTrue(movie.hasSequel());
        assertTrue(movie.toString().contains("Test Movie"));
    }
}
