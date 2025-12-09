package dialogue.commands;

import dialogue.BotResponse;
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
    public BotResponse execute(String userId, String[] args, UserSession session) {

        if (session.getWatched().isEmpty()) {
            return new BotResponse("Вы еще не отметили ни одного просмотренного фильма.");
        }

        StringBuilder sb = new StringBuilder("Ваши просмотренные фильмы:\n");
        for (Movie m : session.getWatched()) {
            sb.append("- ").append(m.getTitle())
                    .append(" (").append(m.getYear()).append(")\n");
        }

        return new BotResponse(sb.toString());
    }
}
