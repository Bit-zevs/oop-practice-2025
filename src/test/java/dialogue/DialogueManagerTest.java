package dialogue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DialogueManagerTest {

    private final DialogueManager manager = new DialogueManager();
    private final String userId = "test-user";

    @Test
    void testStartCommand() {
        String response = manager.handleMessage(userId, "/start");
        assertTrue(response.contains("Привет!"));
    }

    @Test
    void testUnknownCommand() {
        String response = manager.handleMessage(userId, "/unknown");
        assertTrue(response.contains("Неизвестная команда"));
    }

    @Test
    void testAskAndAnswerQuestion() {
        String question = manager.handleMessage(userId, "/ask");
        assertNotNull(question);

        String reply = manager.handleMessage(userId, "да");
        assertNotNull(reply);
    }

    @Test
    void testWatchAndWatchedCommands() {
        manager.handleMessage(userId, "/watch Оппенгеймер");
        String watched = manager.handleMessage(userId, "/watched");
        assertTrue(watched.contains("Оппенгеймер"));
    }
}
