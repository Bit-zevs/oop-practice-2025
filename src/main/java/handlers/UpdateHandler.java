package handlers;

import com.pengrad.telegrambot.model.Update;
public interface UpdateHandler {
    public void handleMessage(Update update);
    public void handleCallback(Update update);
}
