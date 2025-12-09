package dialogue;

import data.MovieQuestions;
import dialogue.commands.*;
import models.UserSession;
import services.MovieNewsService;
import models.Movie;

import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DialogueManager {

    private final MovieQuestions questions = new MovieQuestions();
    private final MovieNewsService newsService = new MovieNewsService();
    private final Map<String, UserSession> sessions = new HashMap<>();
    private final Map<String, BotCommand> commands = new HashMap<>();
    private final String DB_URL;
    private final String DB_USER;
    private final String DB_PASSWORD;

    public DialogueManager() {
        DB_URL = System.getenv("DB_URL");
        if (DB_URL == null || DB_URL.isBlank()) {
            throw new IllegalStateException("DB_URL environment variable is not set. Please set DB_URL.");
        }

        DB_USER = System.getenv("DB_USER");
        if (DB_USER == null || DB_USER.isBlank()) {
            throw new IllegalStateException("DB_USER environment variable is not set. Please set DB_USER.");
        }

        DB_PASSWORD = System.getenv("DB_PASSWORD");
        if (DB_PASSWORD == null || DB_PASSWORD.isBlank()) {
            throw new IllegalStateException("DB_PASSWORD environment variable is not set. Please set DB_PASSWORD.");
        }
        initDatabase();
        register(new StartCommand());
        register(new ListCommand(questions));
        register(new HelpCommand(commands));
        register(new NewsCommand(questions, newsService));
        register(new AskCommand(questions));
        register(new StopAskCommand());
        register(new WatchCommand(questions));
        register(new UnwatchCommand(questions, this));
        register(new FindCommand(questions));
        register(new WatchedCommand());

    }

    private void initDatabase() {
        try (Connection conn = getConnection()) {

            String usersSql = "CREATE TABLE IF NOT EXISTS users (" +
                    "telegram_id VARCHAR(50) PRIMARY KEY," +
                    "username VARCHAR(100)," +
                    "first_name VARCHAR(100)," +
                    "last_name VARCHAR(100)" +
                    ")";
            conn.prepareStatement(usersSql).execute();

            String moviesSql = "CREATE TABLE IF NOT EXISTS watched_movies (" +
                    "id SERIAL PRIMARY KEY," +
                    "telegram_id VARCHAR(50) NOT NULL," +
                    "movie_id VARCHAR(50) NOT NULL," +
                    "movie_title VARCHAR(255) NOT NULL," +
                    "watched_at TIMESTAMP DEFAULT NOW()" +
                    ")";
            conn.prepareStatement(moviesSql).execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void register(BotCommand command) {
        commands.put(command.getName(), command);
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public UserSession getSession(String userId) {
        if (sessions.containsKey(userId)) return sessions.get(userId);

        UserSession session = new UserSession();

        try (Connection conn = getConnection()) {
            String sql = "SELECT username, first_name, last_name FROM users WHERE telegram_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                session.setUsername(rs.getString("username"));
                session.setFirstName(rs.getString("first_name"));
                session.setLastName(rs.getString("last_name"));
            } else {
                String insertSql = "INSERT INTO users (telegram_id) VALUES (?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setString(1, userId);
                insertStmt.executeUpdate();
            }

            loadWatchedMovies(userId, session);
            sessions.put(userId, session);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        sessions.put(userId, session);
        return session;
    }

    public void saveWatchedMovie(String userId, Movie movie) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO watched_movies (telegram_id, movie_id, movie_title, movie_year) " +
                    "VALUES (?, ?, ?, ?) " +
                    "ON CONFLICT (telegram_id, movie_id) DO NOTHING";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setString(2, movie.getId());
            stmt.setString(3, movie.getTitle());
            stmt.setInt(4, movie.getYear());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void loadWatchedMovies(String userId, UserSession session) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT movie_id, movie_title, movie_year FROM watched_movies WHERE telegram_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String movieId = rs.getString("movie_id");
                String movieTitle = rs.getString("movie_title");
                int movieYear = rs.getInt("movie_year");
                Movie movie = new Movie(movieId, movieTitle, movieYear, false);
                session.addWatchedMovie(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveSession(String userId, UserSession session) {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE users SET username=?, first_name=?, last_name=? WHERE telegram_id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, session.getUsername());
            stmt.setString(2, session.getFirstName());
            stmt.setString(3, session.getLastName());
            stmt.setString(4, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeWatchedMovie(String userId, Movie movie) {
        try (Connection conn = getConnection()) {
            String sql = "DELETE FROM watched_movies WHERE telegram_id = ? AND movie_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setString(2, movie.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public BotResponse handleMessage(String userId, String message) {
        String trimmed = message.trim();
        UserSession session = getSession(userId);

        String[] parts = trimmed.split(" ", 2);
        String commandName = parts[0].toLowerCase();
        String[] args = parts.length > 1 ? parts[1].split(" ") : new String[0];

        BotCommand command = commands.get(commandName);
        if (command != null) {
            BotResponse response = command.execute(userId, args, session);
            for (Movie movie : session.getWatched()) {
                saveWatchedMovie(userId, movie);
            }
            saveSession(userId, session);
            return response;
        }

        if (session.hasPendingQuestion()) {
            String text = new QuestionAnswerHandler(questions).handleAnswer(session, trimmed);
            for (Movie movie : session.getWatched()) {
                saveWatchedMovie(userId, movie);
            }
            saveSession(userId, session);
            return new BotResponse(text);
        }

        return new BotResponse("Неизвестная команда. Введите /help для списка доступных команд.");
    }
}