package dialogue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DialogueManagerTest {

    private DialogueManager manager;
    private final String userId = "test-user";

    @BeforeEach
    void setUp() {
        System.setProperty("DB_URL", "jdbc:postgresql://localhost:5432/testdb?sslmode=disable");
        System.setProperty("DB_USER", "postgres");
        System.setProperty("DB_PASSWORD", "password");

        manager = new DialogueManager();
    }

    @Test
    void testStartCommand() {
        BotResponse response = manager.handleMessage(userId, "/start");
        String text = response.getText();
        assertNotNull(text);
        assertTrue(text.contains("Кино бот"));
        assertTrue(text.contains("/help"));
        assertFalse(response.hasPhoto());
    }

    @Test
    void testHelpCommand() {
        BotResponse response = manager.handleMessage(userId, "/help");
        String text = response.getText();
        assertNotNull(text);
        assertTrue(text.contains("/list"));
        assertTrue(text.contains("/news"));
        assertTrue(text.contains("/ask"));
        assertFalse(response.hasPhoto());
    }

    @Test
    void testListCommand() {
        BotResponse response = manager.handleMessage(userId, "/list");
        String text = response.getText();
        assertNotNull(text);
        assertTrue(text.contains("Новинки"));
        assertFalse(response.hasPhoto());
    }

    @Test
    void testNewsCommand() {
        BotResponse response = manager.handleMessage(userId, "/news");
        String text = response.getText();
        assertNotNull(text);
        assertTrue(text.toLowerCase().contains("новость") || text.toLowerCase().contains("вышел"));
        assertFalse(response.hasPhoto());
    }

    @Test
    void testAskAndAnswerFlow() {
        BotResponse questionResp = manager.handleMessage(userId, "/ask");
        String question = questionResp.getText();
        assertNotNull(question);
        assertTrue(question.contains("фильм"), "Должен быть задан вопрос о фильме");

        BotResponse wrongAnswerResp = manager.handleMessage(userId, "1234");
        String wrongAnswer = wrongAnswerResp.getText();
        assertNotNull(wrongAnswer);
        assertTrue(wrongAnswer.toLowerCase().contains("верно") || wrongAnswer.toLowerCase().contains("неверно"), "Бот должен сообщить, правильный ответ или нет");
        assertTrue(wrongAnswer.contains("?"), "После ответа бот должен задать новый вопрос");
    }

    @Test
    void testStopAskCommand() {
        manager.handleMessage(userId, "/ask");

        BotResponse responseResp = manager.handleMessage(userId, "/stopask");
        String response = responseResp.getText();
        assertNotNull(response);
        assertTrue(response.toLowerCase().contains("приостановлены"));
        assertFalse(responseResp.hasPhoto());

        BotResponse afterStopResp = manager.handleMessage(userId, "2000");
        String afterStop = afterStopResp.getText();
        assertTrue(afterStop.toLowerCase().contains("неизвестная команда") || afterStop.contains("/help"));
    }

    @Test
    void testWatchAndWatchedCommands() {
        BotResponse watchResp = manager.handleMessage(userId, "/watch Титаник");
        String watchText = watchResp.getText();
        assertNotNull(watchText);
        assertTrue(watchText.contains("Титаник"), "Должен быть добавлен фильм Титаник");
        assertFalse(watchResp.hasPhoto());

        BotResponse watchedResp = manager.handleMessage(userId, "/watched");
        String watchedText = watchedResp.getText();
        assertNotNull(watchedText);
        assertTrue(watchedText.contains("Титаник"), "Список просмотренных должен содержать Титаник");
        assertFalse(watchedResp.hasPhoto());
    }

    @Test
    void testWatchUnknownMovie() {
        BotResponse responseResp = manager.handleMessage(userId, "/watch НеизвестныйФильм");
        String response = responseResp.getText();
        assertNotNull(response);
        assertTrue(response.toLowerCase().contains("не найден"));
        assertFalse(responseResp.hasPhoto());
    }

    @Test
    void testWatchedEmptyList() {
        String newUser = "new-user";
        BotResponse responseResp = manager.handleMessage(newUser, "/watched");
        String response = responseResp.getText();
        assertNotNull(response);
        assertTrue(response.toLowerCase().contains("не отметили"));
        assertFalse(responseResp.hasPhoto());
    }

    @Test
    void testUnknownCommand() {
        BotResponse responseResp = manager.handleMessage(userId, "/abracadabra");
        String response = responseResp.getText();
        assertNotNull(response);
        assertTrue(response.toLowerCase().contains("неизвестная команда"));
        assertFalse(responseResp.hasPhoto());
    }
}
