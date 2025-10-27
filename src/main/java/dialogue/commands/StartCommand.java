package dialogue.commands;

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
    public String execute(String userId, String[] args, UserSession session) {
        return "Привет! Я — Кино бот. Расскажу о новинках кино и задам вопросы о фильмах.\n" +
                "Команды: /help, /list, /news, /ask, /watch <название>, /watched";
    }
}
