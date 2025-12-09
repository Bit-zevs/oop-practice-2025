package models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserSessionTest {

    @Test
    void testPendingQuestionLifecycle() {
        UserSession session = new UserSession();
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
        UserSession session = new UserSession();
        Movie movie = new Movie("m001", "Test", 2020, false);

        session.markWatched(movie);
        assertEquals(1, session.getWatched().size());

        try {
            session.markWatched(movie);
        } catch (IllegalStateException _){}
        assertEquals(1, session.getWatched().size(), "Дубликаты не должны добавляться");
    }
}
