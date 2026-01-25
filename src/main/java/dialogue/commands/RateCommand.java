package dialogue.commands;

import dialogue.BotResponse;
import data.MovieQuestions;
import models.Movie;
import models.UserSession;

public class RateCommand implements BotCommand {

    private final MovieQuestions questions;

    public RateCommand(MovieQuestions questions) {
        this.questions = questions;
    }

    @Override
    public String getName() {
        return "/rate";
    }

    @Override
    public String getDescription() {
        return "оценить фильм";
    }

    @Override
    public BotResponse execute(String userId, String[] args, UserSession session) {
        if (args.length < 2) {
            return new BotResponse("Использование: /rate <название фильма> <1-10>");
        }

        int rating;
        try {
            rating = Integer.parseInt(args[args.length - 1]);
        } catch (NumberFormatException e) {
            return new BotResponse("Рейтинг должен быть числом от 1 до 10");
        }

        String title = String.join(" ",
                java.util.Arrays.copyOf(args, args.length - 1));

        Movie movie = questions.findMovieByTitle(title);
        if (movie == null) {
            return new BotResponse("Фильм не найден.");
        }

        try {
            session.rateMovie(movie, rating);
            return new BotResponse(
                    "Вы поставили фильму \"" + movie.getTitle() +
                            "\" оценку " + rating + "/10"
            );
        } catch (IllegalArgumentException e) {
            return new BotResponse(e.getMessage());
        }
    }
}
