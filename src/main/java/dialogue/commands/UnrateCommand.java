package dialogue.commands;

import dialogue.BotResponse;
import data.MovieQuestions;
import models.Movie;
import models.UserSession;

public class UnrateCommand implements BotCommand {

    private final MovieQuestions questions;

    public UnrateCommand(MovieQuestions questions) {
        this.questions = questions;
    }

    @Override
    public String getName() {
        return "/unrate";
    }

    @Override
    public String getDescription() {
        return "убрать оценку фильма";
    }

    @Override
    public BotResponse execute(String userId, String[] args, UserSession session) {
        if (args.length < 1) {
            return new BotResponse("Использование: /unrate <название фильма>");
        }

        String titlePart = String.join(" ", args);
        Movie movie = questions.findMovieByTitle(titlePart);
        if (movie == null) {
            return new BotResponse("Фильм не найден.");
        }

        try {
            session.unrateMovie(movie);
            return new BotResponse("Вы убрали оценку у фильма \"" + movie.getTitle());
        } catch (IllegalArgumentException e) {
            return new BotResponse(e.getMessage());
        }
    }
}
