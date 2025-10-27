package dialogue.commands;

import data.MovieQuestions;
import models.Movie;
import models.UserSession;

public class AskCommand implements BotCommand {

    private final MovieQuestions questions;

    public AskCommand(MovieQuestions questions) {
        this.questions = questions;
    }

    @Override
    public String getName() {
        return "/ask";
    }

    @Override
    public String getDescription() {
        return "задать вопрос о случайном фильме";
    }

    @Override
    public String execute(String userId, String[] args, UserSession session) {
        Movie movie = questions.pickRandomMovie();
        if (movie == null) {
            return "Фильмы не найдены.";
        }

        String q = questions.generateQuestionFor(movie);
        session.setPendingQuestion(movie, q);
        return q;
    }
}
