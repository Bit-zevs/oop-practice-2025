package models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserSessionTest {

    @Test
    void testPendingQuestionLifecycle() {
        User user = new User();
        UserSession session = new UserSession(user);

        assertFalse(session.hasPendingQuestion());

        Movie movie = new Movie("m001", "Test", 2020, false);
        session.setPendingQuestion(movie, "Вопрос?");
        assertTrue(session.hasPendingQuestion());
        assertEquals(movie, session.getPendingMovie());
        assertEquals("Вопрос?", session.getPendingQuestionText());

        session.clearPendingQuestion();
        assertFalse(session.hasPendingQuestion());
    }

    @Test
    void testMarkWatched() {
        User user = new User();
        UserSession session = new UserSession(user);

        Movie movie = new Movie("m001", "Test", 2020, false);

        session.markWatched(movie);
        assertEquals(1, session.getWatched().size());

        assertThrows(IllegalStateException.class, () -> session.markWatched(movie));

        assertEquals(1, session.getWatched().size(), "Дубликаты не должны добавляться");
    }
}
