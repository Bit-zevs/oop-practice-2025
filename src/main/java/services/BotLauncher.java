package services;

import dialogue.DialogueManager;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import handlers.*;

import java.util.Scanner;

public class BotLauncher {

    public void runTelegram(DialogueManager manager) {
        String token = System.getenv("TELEGRAM_BOT_TOKEN");
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("TELEGRAM_BOT_TOKEN не установлен.");
        }

        TelegramBot bot = new TelegramBot(token);
        MessageHandler core = new MessageHandler(manager);
        TelegramUpdateHandler tg = new TelegramUpdateHandler(bot, core);

        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                if (update.message() != null && update.message().text() != null) {
                    tg.handleMessage(update);
                } else if (update.callbackQuery() != null) {
                    tg.handleCallback(update);
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

        System.out.println("Бот запущен в Telegram");
    }

    public void runConsole(DialogueManager manager) {
        MessageHandler core = new MessageHandler(manager);
        ConsoleHandler console = new ConsoleHandler(core);

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

            System.out.println(console.handleInput(userId, input));
        }
    }

    public void runBoth(DialogueManager manager) {
        new Thread(() -> runTelegram(manager)).start();
        runConsole(manager);
    }
}
