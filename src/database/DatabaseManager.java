package database;

import model.GameSession;
import model.User;

import java.sql.*;
import java.util.*;

public class DatabaseManager {
    final String url = "jdbc:mysql://localhost:3306/snake_game_db";
    final String username = "root";
    final String password = "Surbhi@123";
    public Connection getConnection(){
        try {
            return DriverManager.getConnection(url,username,password);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean registerUser(String username, String password){
        String query = "INSERT INTO users(username,password) values(?,?)";
        try {
            PreparedStatement ps = getConnection().prepareStatement(query);
            ps.setString(1,username);
            ps.setString(2,password);
            int affectedRows = ps.executeUpdate();
            if(affectedRows > 0){
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int validateLogin(String username, String password){
        String query = "SELECT id FROM users WHERE username = ? AND password = ?";
        try {
            PreparedStatement ps = getConnection().prepareStatement(query);
            ps.setString(1,username);
            ps.setString(2,password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int id = rs.getInt("id");
                return id;
            }else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void saveGameSession(int userId, int score, int playTime){
        String query = "INSERT INTO game_sessions(user_id,score,play_time) values(?,?,?)";
        try {
            PreparedStatement ps = getConnection().prepareStatement(query);
            ps.setInt(1,userId);
            ps.setInt(2,score);
            ps.setInt(3,playTime);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> fetchUsersFromDatabase() {

        List<User> users = new ArrayList<>();

        try {
            String query = "SELECT u.id, u.username, SUM(g.score) AS total_points FROM users u JOIN game_sessions g ON u.id = g.user_id GROUP BY u.id, u.username ORDER BY total_points DESC";

            PreparedStatement ps = getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getInt("total_points")
                ));
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public int getTotalGames(int userId) {

        int total = 0;

        try {
            String query = "SELECT COUNT(*) FROM game_sessions WHERE user_id = ?";

            PreparedStatement ps = getConnection().prepareStatement(query);

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    public int getHighestScore(int userId) {

        int max = 0;

        try {
            String query = "SELECT MAX(score) FROM game_sessions WHERE user_id = ?";

            PreparedStatement ps = getConnection().prepareStatement(query);

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                max = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return max;
    }

    public double getAverageScore(int userId) {

        double avg = 0;

        try {
            String query = "SELECT AVG(score) FROM game_sessions WHERE user_id = ?";

            PreparedStatement ps = getConnection().prepareStatement(query);

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                avg = rs.getDouble(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return avg;
    }

    public int getTotalPoints(int userId) {

        int sum = 0;

        try {
            String query = "SELECT SUM(score) FROM game_sessions WHERE user_id = ?";

             PreparedStatement ps = getConnection().prepareStatement(query);

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                sum = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sum;
    }

    public int getTotalPlayTime(int userId) {

        int total = 0;

        try {
            String query = "SELECT SUM(play_time) FROM game_sessions WHERE user_id = ?";

            PreparedStatement ps = getConnection().prepareStatement(query);

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    public List<GameSession> getGameHistory(int userId) {

        List<GameSession> sessions = new ArrayList<>();

        try {
            String query = "SELECT score, play_time, played_at FROM game_sessions WHERE user_id = ? ORDER BY played_at DESC";

            PreparedStatement ps = getConnection().prepareStatement(query);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                sessions.add(new GameSession(rs.getTimestamp("played_at"), rs.getInt("score"), rs.getInt("play_time")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sessions;
    }

}
