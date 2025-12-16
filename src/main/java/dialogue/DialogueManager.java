package dialogue;

import data.MovieQuestions;
import dialogue.commands.*;
import models.UserSession;
import models.Movie;
import services.MovieNewsService;
import database.DatabaseService;

import java.util.HashMap;
import java.util.Map;

public class DialogueManager {

    private final MovieQuestions questions = new MovieQuestions();
    private final MovieNewsService newsService = new MovieNewsService();
    private final DatabaseService dbService = new DatabaseService();

    private final Map<String, UserSession> sessions = new HashMap<>();
    private final Map<String, BotCommand> commands = new HashMap<>();

    public DialogueManager() {
        registerCommands();
    }

    private void registerCommands() {
        register(new StartCommand());
        register(new ListCommand(questions));
        register(new HelpCommand(commands));
        register(new NewsCommand(questions, newsService));
        register(new AskCommand(questions));
        register(new StopAskCommand());
        register(new WatchCommand(questions));
        register(new UnwatchCommand(questions, this));
        register(new FindCommand(questions));
        register(new WatchedCommand());
    }

    private void register(BotCommand command) {
        commands.put(command.getName(), command);
    }

    public UserSession getSession(String userId) {
        return sessions.computeIfAbsent(userId, dbService::loadUserSession);
    }

    public void saveWatchedMovie(String userId, Movie movie) {
        dbService.saveWatchedMovie(userId, movie);
    }

    public void removeWatchedMovie(String userId, Movie movie) {
        dbService.removeWatchedMovie(userId, movie);
    }

    public void saveSession(String userId, UserSession session) {
        dbService.saveSession(userId, session);
    }

    public BotResponse handleMessage(String userId, String message) {
        String trimmed = message.trim();
        UserSession session = getSession(userId);

        String[] parts = trimmed.split(" ", 2);
        String commandName = parts[0].toLowerCase();
        String[] args = parts.length > 1 ? parts[1].split(" ") : new String[0];

        BotCommand command = commands.get(commandName);
        if (command != null) {
            BotResponse response = command.execute(userId, args, session);
            persistSessionData(userId, session);
            return response;
        }

        if (session.hasPendingQuestion()) {
            String text = new QuestionAnswerHandler(questions).handleAnswer(session, trimmed);
            persistSessionData(userId, session);
            return new BotResponse(text);
        }

        return new BotResponse("Неизвестная команда. Введите /help для списка доступных команд.");
    }

    private void persistSessionData(String userId, UserSession session) {
        for (Movie movie : session.getWatched()) {
            saveWatchedMovie(userId, movie);
        }
        saveSession(userId, session);
    }
}
