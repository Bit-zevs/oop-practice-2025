import dialogue.DialogueManager;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import handlers.MessageHandler;

public class Main {
    public static void main(String[] args) {
        DialogueManager manager = new DialogueManager();

        String token = "8312492716:AAG-h5O4vz1ShZwE-D3R8ED8hS8uTWvG2Hs";
        TelegramBot bot = new TelegramBot(token);
        MessageHandler messageHandler = new MessageHandler(bot, manager);
        System.out.println("Бот запущен");

        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                if (update.message() != null && update.message().text() != null) {
                    messageHandler.handleMessage(update);
                } else if (update.callbackQuery() != null) {
                    messageHandler.handleCallback(update);
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            System.out.println("Бот остановлен");
        }
    }
}