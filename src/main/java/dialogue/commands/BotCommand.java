package dialogue.commands;

import dialogue.BotResponse;
import models.UserSession;

public interface BotCommand {
    String getName();
    String getDescription();
    BotResponse execute(String userId, String[] args, UserSession session);
}
