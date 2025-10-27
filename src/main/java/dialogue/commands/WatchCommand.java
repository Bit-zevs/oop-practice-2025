package dialogue.commands;

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
    public String execute(String userId, String[] args, UserSession session) {
        if (args.length == 0) {
            return "Использование: /watch <название фильма>";
        }

        String titlePart = String.join(" ", args);
        Movie found = questions.findMovieByTitle(titlePart);
        if (found != null) {
            session.markWatched(found);
            return "Фильм \"" + found.getTitle() + "\" добавлен в список просмотренных.";
        } else {
            return "Фильм не найден.";
        }
    }
}
