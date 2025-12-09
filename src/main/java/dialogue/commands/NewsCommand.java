package dialogue.commands;

import dialogue.BotResponse;
import data.MovieQuestions;
import models.UserSession;
import services.MovieNewsService;

public class NewsCommand implements BotCommand {

    private final MovieQuestions questions;
    private final MovieNewsService newsService;

    public NewsCommand(MovieQuestions questions, MovieNewsService newsService) {
        this.questions = questions;
        this.newsService = newsService;
    }

    @Override
    public String getName() {
        return "/news";
    }

    @Override
    public String getDescription() {
        return "новости кино";
    }

    @Override
    public BotResponse execute(String userId, String[] args, UserSession session) {
        String news = newsService.getLatestNews(questions.getLatestMovies());
        return new BotResponse(news);
    }
}
