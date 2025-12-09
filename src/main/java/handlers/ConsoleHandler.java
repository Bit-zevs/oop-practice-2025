package handlers;

import dialogue.BotResponse;

public class ConsoleHandler {

    private final MessageHandler handler;

    public ConsoleHandler(MessageHandler handler) {
        this.handler = handler;
    }

    public String handleInput(String userId, String input) {
        BotResponse response = handler.process(userId, input);
        return response.getText();
    }
}
