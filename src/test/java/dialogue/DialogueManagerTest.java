package dialogue;

import models.Movie;
import models.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import db.DatabaseService;
import data.MovieQuestions;
import services.MovieNewsService;

import static org.junit.jupiter.api.Assertions.*;

public class DialogueManagerTest {

    private DialogueManager manager;
    private final String userId = "test-user";

    @BeforeEach
    void setUp() {
        System.setProperty("DB_URL", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        System.setProperty("DB_USER", "sa");
        System.setProperty("DB_PASSWORD", "");

        MovieQuestions questions = new MovieQuestions();
        MovieNewsService newsService = new MovieNewsService();

        manager = new DialogueManager();
    }

    @Test
    void testStartCommand() {
        BotResponse response = manager.handleMessage(userId, "/start");
        assertNotNull(response.getText());
        assertTrue(response.getText().contains("Кино бот"));
        assertTrue(response.getText().contains("/help"));
        assertFalse(response.hasPhoto());
    }

    @Test
    void testHelpCommand() {
        BotResponse response = manager.handleMessage(userId, "/help");
        assertNotNull(response.getText());
        assertTrue(response.getText().contains("/list"));
        assertTrue(response.getText().contains("/news"));
        assertTrue(response.getText().contains("/ask"));
        assertFalse(response.hasPhoto());
    }

    @Test
    void testListCommand() {
        BotResponse response = manager.handleMessage(userId, "/list");
        assertNotNull(response.getText());
        assertTrue(response.getText().contains("Новинки"));
        assertFalse(response.hasPhoto());
    }

    @Test
    void testNewsCommand() {
        BotResponse response = manager.handleMessage(userId, "/news");
        assertNotNull(response.getText());
        String text = response.getText().toLowerCase();
        assertTrue(text.contains("новость") || text.contains("вышел"));
        assertFalse(response.hasPhoto());
    }

    @Test
    void testAskAndAnswerFlow() {
        BotResponse questionResp = manager.handleMessage(userId, "/ask");
        String question = questionResp.getText();
        assertNotNull(question);
        assertTrue(question.toLowerCase().contains("фильм"));

        BotResponse answerResp = manager.handleMessage(userId, "тестовый ответ");
        assertNotNull(answerResp.getText());
        assertTrue(answerResp.getText().toLowerCase().contains("верно") || answerResp.getText().toLowerCase().contains("неверно"));
        assertTrue(answerResp.getText().contains("?"));
    }

    @Test
    void testStopAskCommand() {
        manager.handleMessage(userId, "/ask");

        BotResponse stopResp = manager.handleMessage(userId, "/stopask");
        assertNotNull(stopResp.getText());
        assertTrue(stopResp.getText().toLowerCase().contains("приостановлены"));
        assertFalse(stopResp.hasPhoto());

        BotResponse afterStop = manager.handleMessage(userId, "какой-то ответ");
        assertTrue(afterStop.getText().toLowerCase().contains("неизвестная команда") || afterStop.getText().contains("/help"));
    }

    @Test
    void testWatchAndWatchedCommands() {
        BotResponse watchResp = manager.handleMessage(userId, "/watch Титаник");
        assertNotNull(watchResp.getText());
        assertTrue(watchResp.getText().contains("Титаник"));
        assertFalse(watchResp.hasPhoto());

        BotResponse watchedResp = manager.handleMessage(userId, "/watched");
        assertNotNull(watchedResp.getText());
        assertTrue(watchedResp.getText().contains("Титаник"));
        assertFalse(watchedResp.hasPhoto());
    }

    @Test
    void testWatchUnknownMovie() {
        BotResponse resp = manager.handleMessage(userId, "/watch НеизвестныйФильм");
        assertNotNull(resp.getText());
        assertTrue(resp.getText().toLowerCase().contains("не найден"));
        assertFalse(resp.hasPhoto());
    }

    @Test
    void testWatchedEmptyList() {
        String newUser = "new-user";
        BotResponse resp = manager.handleMessage(newUser, "/watched");
        assertNotNull(resp.getText());
        assertTrue(resp.getText().toLowerCase().contains("не отметили"));
        assertFalse(resp.hasPhoto());
    }

    @Test
    void testUnknownCommand() {
        BotResponse resp = manager.handleMessage(userId, "/abracadabra");
        assertNotNull(resp.getText());
        assertTrue(resp.getText().toLowerCase().contains("неизвестная команда"));
        assertFalse(resp.hasPhoto());
    }
}
