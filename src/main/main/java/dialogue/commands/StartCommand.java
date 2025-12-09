package dialogue.commands;

import dialogue.BotResponse;
import models.UserSession;

public class StartCommand implements BotCommand {
    @Override
    public String getName() {
        return "/start";
    }

    @Override
    public String getDescription() {
        return "начать работу с ботом";
    }

    @Override
    public BotResponse execute(String userId, String[] args, UserSession session) {
        return new BotResponse(
                "Привет! Я — Кино бот. Расскажу о новинках кино и задам вопросы о фильмах.\n" +
                        "Команды: /help, /list, /news, /ask, /watch <название>, /watched"
        );
    }
}
