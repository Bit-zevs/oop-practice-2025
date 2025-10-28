package dialogue.commands;

import models.Movie;
import models.UserSession;

public class WatchedCommand implements BotCommand {

    @Override
    public String getName() {
        return "/watched";
    }

    @Override
    public String getDescription() {
        return "показать список просмотренных фильмов";
    }

    @Override
    public String execute(String userId, String[] args, UserSession session) {
        if (session.getWatched().isEmpty()) {
            return "Вы ещё не отметили ни одного фильма.";
        }

        StringBuilder response = new StringBuilder("Ваши просмотренные фильмы:\n");
        for (Movie m : session.getWatched()) {
            response.append("- ").append(m.toString()).append("\n");
        }
        return response.toString();
    }
}
