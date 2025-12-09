package data;

import models.Movie;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovieQuestionsTest {

    private final MovieQuestions questions = new MovieQuestions();

    @Test
    void testGetLatestMoviesNotEmpty() {
        assertFalse(questions.getLatestMovies().isEmpty());
    }

    @Test
    void testPickRandomMovie() {
        Movie movie = questions.pickRandomMovie();
        assertNotNull(movie);
    }

    @Test
    void testGenerateQuestionFor() {
        Movie movie = new Movie("m001", "Test Movie", 2020, true);
        String question = questions.generateQuestionFor(movie);
        assertTrue(question.contains("Test Movie"));
    }

    @Test
    void testCheckAnswerYear() {
        Movie movie = new Movie("m001", "Test", 2020, true);
        String question = "В каком году вышел фильм \"Test\"?";
        assertTrue(questions.checkAnswer(movie, question, "2020"));
        assertFalse(questions.checkAnswer(movie, question, "2021"));
    }

    @Test
    void testCheckAnswerSequel() {
        Movie movie = new Movie("m002", "Test 2", 2021, true);
        String question = "У фильма \"Test 2\" есть продолжение? (да/нет)";
        assertTrue(questions.checkAnswer(movie, question, "да"));
        assertFalse(questions.checkAnswer(movie, question, "нет"));
    }

    @Test
    void testFindMovieByTitle() {
        Movie movie = questions.findMovieByTitle("Титаник");
        assertNotNull(movie);
        assertEquals("Титаник", movie.getTitle());

        Movie missing = questions.findMovieByTitle("НеСуществующийФильм");
        assertNull(missing);
    }
}
