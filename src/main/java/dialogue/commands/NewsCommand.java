package dialogue.commands;

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
    public String execute(String userId, String[] args, UserSession session) {
        return newsService.getLatestNews(questions.getLatestMovies());
    }
}
