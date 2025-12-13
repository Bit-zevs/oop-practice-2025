package database;

import models.Movie;
import models.User;
import models.UserSession;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {

    private final String DB_URL;
    private final String DB_USER;
    private final String DB_PASSWORD;

    public DatabaseService() {
        DB_URL = System.getenv("DB_URL");
        DB_USER = System.getenv("DB_USER");
        DB_PASSWORD = System.getenv("DB_PASSWORD");

        if (DB_URL == null || DB_URL.isBlank()) throw new IllegalStateException("DB_URL not set");
        if (DB_USER == null || DB_USER.isBlank()) throw new IllegalStateException("DB_USER not set");
        if (DB_PASSWORD == null || DB_PASSWORD.isBlank()) throw new IllegalStateException("DB_PASSWORD not set");

        initDatabase();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private void initDatabase() {
        try (Connection conn = getConnection()) {
            String usersSql = """
                        CREATE TABLE IF NOT EXISTS users (
                            telegram_id VARCHAR(50) PRIMARY KEY,
                            username VARCHAR(100),
                            first_name VARCHAR(100),
                            last_name VARCHAR(100)
                        )
                    """;
            conn.prepareStatement(usersSql).execute();

            String moviesSql = """
                        CREATE TABLE IF NOT EXISTS watched_movies (
                            id SERIAL PRIMARY KEY,
                            telegram_id VARCHAR(50) NOT NULL,
                            movie_id VARCHAR(50) NOT NULL,
                            movie_title VARCHAR(255) NOT NULL,
                            movie_year INT NOT NULL,
                            watched_at TIMESTAMP DEFAULT NOW(),
                            UNIQUE (telegram_id, movie_id)
                        )
                    """;
            conn.prepareStatement(moviesSql).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public UserSession loadUserSession(String userId) {
        User user;

        try (Connection conn = getConnection()) {

            String sqlUser = """
                        SELECT username, first_name, last_name
                        FROM users
                        WHERE telegram_id = ?
                    """;

            PreparedStatement stmtUser = conn.prepareStatement(sqlUser);
            stmtUser.setString(1, userId);

            ResultSet rs = stmtUser.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name")
                );
            } else {
                user = new User("", "", "");

                String insertUser = """
                            INSERT INTO users (telegram_id, username, first_name, last_name)
                            VALUES (?, ?, ?, ?)
                        """;

                PreparedStatement insertStmt = conn.prepareStatement(insertUser);
                insertStmt.setString(1, userId);
                insertStmt.setString(2, "");
                insertStmt.setString(3, "");
                insertStmt.setString(4, "");
                insertStmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            user = new User("", "", "");
        }

        UserSession session = new UserSession(user);

        List<Movie> watchedMovies = loadWatchedMovies(userId);

        for (Movie movie : watchedMovies) {
            session.addWatchedMovie(movie);
        }

        return session;
    }


    public void saveSession(String userId, UserSession session) {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE users SET username=?, first_name=?, last_name=? WHERE telegram_id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            User user = session.getUser();
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getFirstName());
            stmt.setString(3, user.getLastName());
            stmt.setString(4, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveWatchedMovie(String userId, Movie movie) {
        try (Connection conn = getConnection()) {
            String sql = """
                        INSERT INTO watched_movies (telegram_id, movie_id, movie_title, movie_year)
                        VALUES (?, ?, ?, ?)
                        ON CONFLICT (telegram_id, movie_id) DO NOTHING
                    """;
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

    public List<Movie> loadWatchedMovies(String userId) {
        List<Movie> movies = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT movie_id, movie_title, movie_year FROM watched_movies WHERE telegram_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                movies.add(new Movie(
                        rs.getString("movie_id"),
                        rs.getString("movie_title"),
                        rs.getInt("movie_year"),
                        false
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }
}
