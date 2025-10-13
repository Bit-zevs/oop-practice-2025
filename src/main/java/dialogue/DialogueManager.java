package dialogue;

import data.MovieQuestions;
import models.Movie;
import models.UserSession;
import services.MovieNewsService;

import java.util.HashMap;
import java.util.Map;

public class DialogueManager {

    private final MovieQuestions questions = new MovieQuestions();
    private final MovieNewsService newsService = new MovieNewsService();
    private final Map<String, UserSession> sessions = new HashMap<>();

    private UserSession getSession(String userId) {
        if (!sessions.containsKey(userId)) {
            sessions.put(userId, new UserSession());
        }
        return sessions.get(userId);
    }

    public String handleMessage(String userId, String message) {
        String trimmed = message.trim();
        UserSession session = getSession(userId);

        if (session.hasPendingQuestion()) {
            String reply;
            if (questions.checkAnswer(session.getPendingMovie(), session.getPendingQuestionText(), trimmed)) {
                reply = "Да, верно!";
            } else {
                if (session.getPendingQuestionText().contains("году")) {
                    reply = "Неверно. Фильм вышел в " + session.getPendingMovie().getYear() + ".";
                } else {
                    reply = "Неверно. Фильм " + session.getPendingMovie().getTitle() + (session.getPendingMovie().hasSequel() ? " имеет продолжение" : " не имеет продолжения") + ".";
                }
            }

            session.clearPendingQuestion();
            return reply;
        }


        String[] parts = trimmed.split(" ", 2);
        String command = parts[0].toLowerCase();

        switch (command) {
            case "/start" -> {
                return "Привет! Я — Кино бот. Расскажу о новинках кино и задам вопросы о фильмах.\n" +
                        "Команды: /help, /list, /news, /ask, /watch <название>, /watched";
            }
            case "/help" -> {
                return "Доступные команды:\n" +
                        "/list — показать новинки\n" +
                        "/news — новости кино\n" +
                        "/ask — задать вопрос о случайном фильме\n" +
                        "/watch <название> — отметить фильм как просмотренный\n" +
                        "/watched — список просмотренных фильмов";
            }
            case "/list" -> {
                String result = "Новинки:\n";
                for (Movie m : questions.getLatestMovies()) {
                    result += "- " + m.toString() + "\n";
                }
                return result;

            }
            case "/news" -> {
                return newsService.getLatestNews(questions.getLatestMovies());
            }
            case "/ask" -> {
                Movie movie = questions.pickRandomMovie();

                if (movie == null) {
                    return "Фильмы не найдены.";
                }

                String q = questions.generateQuestionFor(movie);
                session.setPendingQuestion(movie, q);

                return q;
            }
            case "/watch" -> {
                if (parts.length < 2) {
                    return "Использование: /watch <название фильма>";
                }

                Movie found = questions.findMovieByTitle(parts[1]);
                if (found != null) {
                    session.markWatched(found);
                    return "Фильм \"" + found.getTitle() + "\" добавлен в список просмотренных.";
                } else {
                    return "Фильм не найден.";
                }
            }
            case "/watched" -> {
                if (session.getWatched().isEmpty()) {
                    return "Вы ещё не отметили ни одного фильма.";
                }

                String watchedList = "Ваши просмотренные фильмы:\n";
                for (Movie m : session.getWatched()) {
                    watchedList += "- " + m.toString() + "\n";
                }
                return watchedList;
            }
        }

        return "Неизвестная команда. Введите /help для списка доступных команд.";
    }
}
