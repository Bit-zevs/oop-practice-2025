package dialogue;

import data.MovieQuestions;
import dialogue.commands.*;
import models.UserSession;
import services.MovieNewsService;

import java.util.HashMap;
import java.util.Map;

public class DialogueManager {

    private final MovieQuestions questions = new MovieQuestions();
    private final MovieNewsService newsService = new MovieNewsService();
    private final Map<String, UserSession> sessions = new HashMap<>();
    private final Map<String, BotCommand> commands = new HashMap<>();

    public DialogueManager() {
        register(new StartCommand());
        register(new ListCommand(questions));
        register(new HelpCommand(commands));
        register(new NewsCommand(questions, newsService));
        register(new AskCommand(questions));
        register(new StopAskCommand());
        register(new WatchCommand(questions));
        register(new FindCommand(questions));
        register(new WatchedCommand());
    }

    private void register(BotCommand command) {
        commands.put(command.getName(), command);
    }

    private UserSession getSession(String userId) {
        if (!sessions.containsKey(userId)) {
            sessions.put(userId, new UserSession());
        }
        return sessions.get(userId);
    }

    public BotResponse handleMessage(String userId, String message) {
        String trimmed = message.trim();
        UserSession session = getSession(userId);

        String[] parts = trimmed.split(" ", 2);
        String commandName = parts[0].toLowerCase();
        String[] args = parts.length > 1 ? parts[1].split(" ") : new String[0];

        BotCommand command = commands.get(commandName);
        if (command != null) {
            return command.execute(userId, args, session);
        }

        if (session.hasPendingQuestion()) {
            String text = new QuestionAnswerHandler(questions).handleAnswer(session, trimmed);
            return new BotResponse(text);
        }

        return new BotResponse("Неизвестная команда. Введите /help для списка доступных команд.");
    }
}
