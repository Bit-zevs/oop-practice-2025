package dialogue.commands;

import models.UserSession;

public interface BotCommand {
    String getName();
    String getDescription();
    String execute(String userId, String[] args, UserSession session);
}
