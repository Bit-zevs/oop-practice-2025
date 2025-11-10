import dialogue.DialogueManager;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class Main {
    public static void main(String[] args) {
        DialogueManager manager = new DialogueManager();

        String token = "8312492716:AAG-h5O4vz1ShZwE-D3R8ED8hS8uTWvG2Hs";
        TelegramBot bot = new TelegramBot(token);
        System.out.println("Бот запущен");

        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                if (update.message() != null && update.message().text() != null) {
                    Long chatId = update.message().chat().id();
                    String input = update.message().text();
                    String response = manager.handleMessage("tg-" + chatId, input);

                    bot.execute(new SendMessage(chatId, response));
                    System.out.println("Чат " + chatId + ": " + input + " → " + response);
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
