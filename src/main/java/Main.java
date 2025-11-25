import dialogue.DialogueManager;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import handlers.MessageHandler;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Пожалуйста, укажите режим работы: telegram,console или both");
            return;
        }
        String mode = args[0].toLowerCase();
        DialogueManager manager = new DialogueManager();
        MessageHandler messageHandler;
        if (mode.equals("telegram")) {
            String token = System.getenv("TELEGRAM_BOT_TOKEN");
            if (token == null || token.isBlank()) {
                throw new IllegalStateException("TELEGRAM_BOT_TOKEN не установлен. Установите переменную окружения.");
            }

            TelegramBot bot = new TelegramBot(token);
            messageHandler = new MessageHandler(bot, manager);
            System.out.println("Бот запущен в Telegram");

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
        } else if (mode.equals("console")) {
            messageHandler = new MessageHandler(null, manager);
            System.out.println("Бот запущен в консоли");

            Scanner scanner = new Scanner(System.in);
            String userId = "console-user";

            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("/quit")) {
                    System.out.println("До встречи!");
                    break;
                }

                String response = messageHandler.handleConsole(userId, input);
                System.out.println(response);
            }
        } else if (mode.equals("both")) {

            String token = System.getenv("TELEGRAM_BOT_TOKEN");
            if (token == null || token.isBlank()) {
                throw new IllegalStateException("TELEGRAM_BOT_TOKEN не установлен. Установите переменную окружения.");
            }

            System.out.println("Бот запущен в режиме Telegram + Console");
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
            MessageHandler consoleHandler = new MessageHandler(null, manager);
            Scanner scanner = new Scanner(System.in);
            String userId = "console-user";

            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("/quit")) {
                    System.out.println("До встречи!");
                    break;
                }

                String response = consoleHandler.handleConsole(userId, input);
                System.out.println(response);
            }

        } else {
            System.out.println("Неверный режим. Используйте: telegram, console или both");

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
                    String response = messageHandler.handleMessage(update);
                    System.out.println(response);
                } else if (update.callbackQuery() != null) {
                    String response = messageHandler.handleCallback(update);
                    System.out.println(response);
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