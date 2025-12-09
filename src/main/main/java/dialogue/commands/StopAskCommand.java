package dialogue.commands;

import dialogue.BotResponse;
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
    public BotResponse execute(String userId, String[] args, UserSession session) {
        session.clearPendingQuestion();
        return new BotResponse(
                "Вопросы приостановлены. Для продолжения работы с вопросами напишите /ask или /help чтобы узнать другие команды бота"
        );
    }
}
