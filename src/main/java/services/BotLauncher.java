package services;

import dialogue.DialogueManager;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import handlers.MessageHandler;

import java.util.Scanner;

public class BotLauncher {

    public void runTelegram(DialogueManager manager) {
        String token = System.getenv("TELEGRAM_BOT_TOKEN");

        if (token == null || token.isBlank()) {
            throw new IllegalStateException("TELEGRAM_BOT_TOKEN не установлен. Установите переменную окружения.");
        }

        TelegramBot bot = new TelegramBot(token);
        MessageHandler handler = new MessageHandler(bot, manager);

        System.out.println("Бот запущен в Telegram");

        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                if (update.message() != null && update.message().text() != null) {
                    handler.handleMessage(update);
                } else if (update.callbackQuery() != null) {
                    handler.handleCallback(update);
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    public void runConsole(DialogueManager manager) {
        MessageHandler handler = new MessageHandler(null, manager);
        Scanner scanner = new Scanner(System.in);
        String userId = "console-user";

        System.out.println("Бот запущен в консоли");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("/quit")) {
                System.out.println("До встречи!");
                break;
            }

            System.out.println(handler.handleConsole(userId, input));
        }
    }

    public void runBoth(DialogueManager manager) {
        String token = System.getenv("TELEGRAM_BOT_TOKEN");

        if (token == null || token.isBlank()) {
            throw new IllegalStateException("TELEGRAM_BOT_TOKEN не установлен. Установите переменную окружения.");
        }

        System.out.println("Бот запущен в режиме Telegram");
        new Thread(() -> {
            TelegramBot bot = new TelegramBot(token);
            MessageHandler tgHandler = new MessageHandler(bot, manager);

            bot.setUpdatesListener(updates -> {
                for (Update update : updates) {
                    if (update.message() != null && update.message().text() != null) {
                        tgHandler.handleMessage(update);
                    } else if (update.callbackQuery() != null) {
                        tgHandler.handleCallback(update);
                    }
                }
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            });
        }).start();

        runConsole(manager);
    }
}
