package dialogue.commands;

import dialogue.BotResponse;
import dialogue.DialogueManager;
import data.MovieQuestions;
import models.Movie;
import models.UserSession;

public class UnwatchCommand implements BotCommand {

    private final MovieQuestions questions;
    private final DialogueManager manager;

    public UnwatchCommand(MovieQuestions questions, DialogueManager manager) {
        this.questions = questions;
        this.manager = manager;
    }

    @Override
    public String getName() {
        return "/unwatch";
    }

    @Override
    public String getDescription() {
        return "отметить фильм как не просмотренный";
    }

    @Override
    public BotResponse execute(String userId, String[] args, UserSession session) {
        if (args.length == 0) {
            return new BotResponse("Использование: /unwatch <название фильма>");
        }

        String titlePart = String.join(" ", args);
        Movie found = questions.findMovieByTitle(titlePart);

        if (found == null) {
            return new BotResponse("Фильм не найден.");
        }

        try {
            session.markUnwatched(found);
            manager.removeWatchedMovie(userId, found);

            return new BotResponse("Фильм \"*" + found.getTitle() + "*\" удалён из списка просмотренных");
        } catch (IllegalStateException e) {
            return new BotResponse("Фильма \"*" + found.getTitle() + "*\" нет в списке просмотренных");
        }
    }
}
