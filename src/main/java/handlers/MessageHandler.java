package handlers;

import dialogue.BotResponse;
import dialogue.DialogueManager;

public class MessageHandler {

    private final DialogueManager manager;

    public MessageHandler(DialogueManager manager) {
        this.manager = manager;
    }

    public BotResponse process(String userId, String input) {
        return manager.handleMessage(userId, input);
    }
}
