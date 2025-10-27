package dialogue.commands;

import models.UserSession;

import java.util.Map;

public class HelpCommand implements BotCommand {

    private final Map<String, BotCommand> commands;

    public HelpCommand(Map<String, BotCommand> commands) {
        this.commands = commands;
    }

    @Override
    public String getName() {
        return "/help";
    }

    @Override
    public String getDescription() {
        return "показать список доступных команд";
    }

    @Override
    public String execute(String userId, String[] args, UserSession session) {
        StringBuilder sb = new StringBuilder("Доступные команды:\n");
        for (BotCommand cmd : commands.values()) {
            sb.append(cmd.getName()).append(" — ").append(cmd.getDescription()).append("\n");
        }
        return sb.toString();
    }
}
