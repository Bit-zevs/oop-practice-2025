package handlers;

import com.pengrad.telegrambot.model.Update;

public interface UpdateHandler {
    void handleMessage(Update update);

    void handleCallback(Update update);
}