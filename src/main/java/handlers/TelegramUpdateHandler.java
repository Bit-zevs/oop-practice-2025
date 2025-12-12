package handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.*;
import data.MovieQuestions;
import dialogue.BotResponse;
import dialogue.DialogueManager;

public class TelegramUpdateHandler implements UpdateHandler {

    private final TelegramBot bot;
    private final MessageHandler handler;
    private final DialogueManager dialogue;
    private final MovieQuestions questions = new MovieQuestions();

    public TelegramUpdateHandler(TelegramBot bot, MessageHandler handler) {
        this.bot = bot;
        this.handler = handler;
        this.dialogue = handler.getDialogueManager();
    }

    @Override
    public void handleMessage(Update update) {
        Long chatId = update.message().chat().id();
        String input = update.message().text();
        String userId = "tg-" + chatId;

        BotResponse response = handler.process(userId, input);

        InlineKeyboardMarkup keyboard = null;
        if (input.toLowerCase().startsWith("/find ")) {
            String movieName = input.substring(6).trim();

            if (!movieName.isEmpty()) {
                boolean isWatched = dialogue.getSession(userId).isMovieWatched(questions.findMovieByTitle(movieName));
                if (isWatched) {
                    keyboard = new InlineKeyboardMarkup(
                            new InlineKeyboardButton("Не просмотрен")
                                    .callbackData("/unwatch " + movieName)
                    );
                } else {
                    keyboard = new InlineKeyboardMarkup(
                            new InlineKeyboardButton("Просмотрен")
                                    .callbackData("/watch " + movieName)
                    );
                }
            }
        }

        sendResponse(chatId, response, keyboard);
    }

    @Override
    public void handleCallback(Update update) {
        Long chatId = update.callbackQuery().message().chat().id();
        Integer messageId = update.callbackQuery().message().messageId();
        String callbackData = update.callbackQuery().data();
        String userId = "tg-" + chatId;

        handler.process(userId, callbackData);

        String movieName = callbackData.replace("/watch ", "").replace("/unwatch ", "").trim();

        boolean isWatched = dialogue.getSession(userId).isMovieWatched(questions.findMovieByTitle(movieName));

        InlineKeyboardMarkup newKeyboard;

        if (isWatched) {
            newKeyboard = new InlineKeyboardMarkup(
                    new InlineKeyboardButton("Не просмотрен").callbackData("/unwatch " + movieName)
            );
        } else {
            newKeyboard = new InlineKeyboardMarkup(
                    new InlineKeyboardButton("Просмотрен").callbackData("/watch " + movieName)
            );
        }

        EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup(chatId, messageId)
                .replyMarkup(newKeyboard);
        bot.execute(editMarkup);

        bot.execute(new AnswerCallbackQuery(update.callbackQuery().id()));
    }

    private void sendResponse(Long chatId, BotResponse response, InlineKeyboardMarkup keyboard) {
        if (response.hasPhoto()) {
            SendPhoto photo = new SendPhoto(chatId, response.getPhotoUrl())
                    .caption(response.getText())
                    .parseMode(ParseMode.Markdown);
            if (keyboard != null) photo.replyMarkup(keyboard);
            bot.execute(photo);
        } else {
            SendMessage msg = new SendMessage(chatId, response.getText())
                    .parseMode(ParseMode.Markdown);
            if (keyboard != null) msg.replyMarkup(keyboard);
            bot.execute(msg);
        }
    }
}
