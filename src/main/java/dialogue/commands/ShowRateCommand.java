package dialogue.commands;

import dialogue.BotResponse;
import models.UserSession;

import java.util.Map;

public class ShowRateCommand implements BotCommand {

    @Override
    public String getName() {
        return "/showrate";
    }

    @Override
    public String getDescription() {
        return "показать оцененные фильмы";
    }

    @Override
    public BotResponse execute(String userId, String[] args, UserSession session) {
        Map<String, Integer> movieRatings = session.getAllRatings();

        if (movieRatings.isEmpty()) {
            return new BotResponse("\uD83C\uDFAC Вы еще не оценили ни одного фильма");
        }

        StringBuilder response = new StringBuilder("\uD83C\uDFA5 *Ваши оценки фильмов:*\n\n");
        for (Map.Entry<String, Integer> entry : movieRatings.entrySet()) {
            response.append("*").append(entry.getKey()).append("* (").append(entry.getValue()).append("/10)\n");
        }
        return new BotResponse(response.toString());
    }
}
