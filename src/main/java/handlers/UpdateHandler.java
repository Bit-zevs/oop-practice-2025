package handlers;

import com.pengrad.telegrambot.model.Update;

public interface UpdateHandler {
    String handleMessage(Update update);

    String handleCallback(Update update);
}
