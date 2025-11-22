import dialogue.DialogueManager;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import handlers.MessageHandler;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DialogueManager manager = new DialogueManager();
        String token = System.getenv("TELEGRAM_BOT_TOKEN");
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("TELEGRAM_BOT_TOKEN is not set. Please configure it in environment variables.");
        }
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
        Thread consoleThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            String userId = "console-user";

            System.out.println("Привет! Я — Кино бот. Расскажу о новинках кино и задам вопросы о фильмах. Команды: /help, /list, /news, /ask, /watch <название>, /watched");

            while (true) {
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("/quit")) {
                    System.out.println("До встречи");
                    break;
                }

                String response = messageHandler.handleConsole(userId, input);

                System.out.println(response);
            }
        });
        consoleThread.setDaemon(true);
        consoleThread.start();
    }
}