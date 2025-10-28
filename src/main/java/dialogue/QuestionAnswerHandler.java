package dialogue;

import data.MovieQuestions;
import models.Movie;
import models.UserSession;

public class QuestionAnswerHandler {

    private final MovieQuestions questions;

    public QuestionAnswerHandler(MovieQuestions questions) {
        this.questions = questions;
    }

    public String handleAnswer(UserSession session, String userAnswer) {
        Movie movie = session.getPendingMovie();
        String questionText = session.getPendingQuestionText();

        String reply;
        if (questions.checkAnswer(movie, questionText, userAnswer)) {
            reply = "Да, верно!";
        } else {
            if (questionText.contains("году")) {
                reply = "Неверно. Фильм вышел в " + movie.getYear() + ".";
            } else {
                reply = "Неверно. Фильм " + movie.getTitle() +
                        (movie.hasSequel() ? " имеет продолжение." : " не имеет продолжения.");
            }
        }

        Movie nextMovie = questions.pickRandomMovie();
        if (nextMovie == null) {
            session.clearPendingQuestion();
            return reply + "\nФильмы закончились!";
        }

        String nextQuestion = questions.generateQuestionFor(nextMovie);
        session.setPendingQuestion(nextMovie, nextQuestion);

        return reply + "\n\n" + nextQuestion;
    }
}
