package dialogue.commands;

import dialogue.BotResponse;
import data.MovieQuestions;
import models.Movie;
import models.UserSession;

public class FindCommand implements BotCommand {

    private final MovieQuestions questions;

    public FindCommand(MovieQuestions questions) {
        this.questions = questions;
    }

    @Override
    public String getName() {
        return "/find";
    }

    @Override
    public String getDescription() {
        return "Поиск фильма";
    }

    @Override
    public BotResponse execute(String userId, String[] args, UserSession session) {
        if (args.length == 0) {
            return new BotResponse("Использование: /find <название фильма>");
        }

        String titlePart = String.join(" ", args);
        Movie found = questions.findMovieByTitle(titlePart);

        if (found == null) {
            return new BotResponse("Фильм не найден.");
        }

        String text = "🎬" + found + "\n\n" +
                found.getDescription() + "\n\n";

        return new BotResponse(text, found.getPosterUrl());
    }
}
