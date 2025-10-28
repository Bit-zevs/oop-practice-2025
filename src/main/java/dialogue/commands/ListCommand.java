package dialogue.commands;

import data.MovieQuestions;
import models.Movie;
import models.UserSession;

public class ListCommand implements BotCommand {

    private final MovieQuestions questions;

    public ListCommand(MovieQuestions questions) {
        this.questions = questions;
    }

    @Override
    public String getName() {
        return "/list";
    }

    @Override
    public String getDescription() {
        return "показать новинки кино";
    }

    @Override
    public String execute(String userId, String[] args, UserSession session) {
        StringBuilder result = new StringBuilder("Новинки:\n");
        for (Movie m : questions.getLatestMovies()) {
            result.append("- ").append(m.toString()).append("\n");
        }
        return result.toString();
    }
}
