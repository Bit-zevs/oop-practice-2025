package dialogue.commands;

import dialogue.BotResponse;
import data.MovieQuestions;
import models.Movie;
import models.UserSession;

public class WatchCommand implements BotCommand {

    private final MovieQuestions questions;

    public WatchCommand(MovieQuestions questions) {
        this.questions = questions;
    }

    @Override
    public String getName() {
        return "/watch";
    }

    @Override
    public String getDescription() {
        return "отметить фильм как просмотренный";
    }

    @Override
    public BotResponse execute(String userId, String[] args, UserSession session) {
        if (args.length == 0) {
            return new BotResponse("Использование: /watch <название фильма>");
        }

        String titlePart = String.join(" ", args);
        Movie found = questions.findMovieByTitle(titlePart);
        if (found != null) {
            session.markWatched(found);
            return new BotResponse("Фильм \"" + found.getTitle() + "\" добавлен в список просмотренных.");
        } else {
            return new BotResponse("Фильм не найден.");
        }
    }
}
