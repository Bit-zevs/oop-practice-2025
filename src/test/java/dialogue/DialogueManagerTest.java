package dialogue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class DialogueManagerTest {

    private DialogueManager manager;
    private final String userId = "test-user";

    @BeforeEach
    void setUp() {
        manager = new DialogueManager();
    }

    @Test
    void testStartCommand() {
        String response = manager.handleMessage(userId, "/start");
        assertTrue(response.contains("Кино бот"));
        assertTrue(response.contains("/help"));
    }

    @Test
    void testHelpCommand() {
        String response = manager.handleMessage(userId, "/help");
        assertTrue(response.contains("/list"));
        assertTrue(response.contains("/news"));
        assertTrue(response.contains("/ask"));
    }

    @Test
    void testListCommand() {
        String response = manager.handleMessage(userId, "/list");
        assertTrue(response.contains("Новинки"));
    }

    @Test
    void testNewsCommand() {
        String response = manager.handleMessage(userId, "/news");
        assertTrue(response.contains("Новость"));
        assertTrue(response.contains("вышел"));
    }

    @Test
    void testAskAndAnswerFlow() {
        String question = manager.handleMessage(userId, "/ask");
        assertTrue(question.contains("фильм"), "Должен быть задан вопрос о фильме");

        String wrongAnswer = manager.handleMessage(userId, "1234");

        assertTrue(
                wrongAnswer.contains("Неверно") || wrongAnswer.contains("верно"),
                "Бот должен сообщить, правильный ответ или нет"
        );
        assertTrue(
                wrongAnswer.contains("?"),
                "После ответа бот должен задать новый вопрос"
        );
    }

    @Test
    void testStopAskCommand() {
        manager.handleMessage(userId, "/ask");

        String response = manager.handleMessage(userId, "/stopask");

        assertTrue(response.toLowerCase().contains("приостановлены"));

        String afterStop = manager.handleMessage(userId, "2000");
        assertTrue(afterStop.contains("Неизвестная команда") || afterStop.contains("/help"));
    }

    @Test
    void testWatchAndWatchedCommands() {
        String watchResp = manager.handleMessage(userId, "/watch Титаник");
        assertTrue(watchResp.contains("Титаник"), "Должен быть добавлен фильм Титаник");

        String watchedResp = manager.handleMessage(userId, "/watched");
        assertTrue(watchedResp.contains("Титаник"), "Список просмотренных должен содержать Титаник");
    }

    @Test
    void testWatchUnknownMovie() {
        String response = manager.handleMessage(userId, "/watch НеизвестныйФильм");
        assertTrue(response.toLowerCase().contains("не найден"));
    }

    @Test
    void testWatchedEmptyList() {
        String response = manager.handleMessage(userId, "/watched");
        assertTrue(response.toLowerCase().contains("не отметили"));
    }

    @Test
    void testUnknownCommand() {
        String response = manager.handleMessage(userId, "/abracadabra");
        assertTrue(response.toLowerCase().contains("неизвестная команда"));
    }
}
