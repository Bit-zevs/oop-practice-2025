package handlers;

import dialogue.DialogueManager;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;

public class MessageHandler implements UpdateHandler {
    TelegramBot bot;
    DialogueManager manager;

    public MessageHandler(TelegramBot bot, DialogueManager manager) {
        this.bot = bot;
        this.manager = manager;
    }

    @Override
    public String handleMessage(Update update) {
        Long chatId = update.message().chat().id();
        String input = update.message().text();
        String userId = "tg-" + chatId;
        String response = manager.handleMessage(userId, input);
        SendMessage reply = new SendMessage(chatId, response);
        if (input.equals("/start")) {
            reply.replyMarkup(getMainKeyboard());
        }
        bot.execute(reply);
        return "Чат " + chatId + ": " + input + " → " + response;
    }

    @Override
    public String handleCallback(Update update) {
        Long chatId = update.callbackQuery().message().chat().id();
        String callbackData = update.callbackQuery().data();
        String userId = "tg-" + chatId;
        String response = manager.handleMessage(userId, callbackData);
        SendMessage reply = new SendMessage(chatId, response);
        reply.replyMarkup(getMainKeyboard());
        bot.execute(reply);
        bot.execute(new AnswerCallbackQuery(update.callbackQuery().id()));
        return "Кнопка " + chatId + ": " + callbackData + " → " + response;
    }

    private InlineKeyboardMarkup getMainKeyboard() {
        InlineKeyboardButton[] row = {
                new InlineKeyboardButton("Список").callbackData("/list"),
                new InlineKeyboardButton("Новости").callbackData("/news"),
                new InlineKeyboardButton("Помощь").callbackData("/help")
        };

        return new InlineKeyboardMarkup(row);
    }
}
