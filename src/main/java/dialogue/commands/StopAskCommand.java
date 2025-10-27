package dialogue.commands;

import models.UserSession;

public class StopAskCommand implements BotCommand {

    @Override
    public String getName() {
        return "/stopask";
    }

    @Override
    public String getDescription() {
        return "прекращает задавать вопросы";
    }

    @Override
    public String execute(String userId, String[] args, UserSession session) {
        session.clearPendingQuestion();
        return "Вопросы приостановлены. Для продолжения работы с вопросами напишите /ask или /help чтобы узнать другие команды бота";
    }
}
