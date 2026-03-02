package model;

public class User {

    private final int userId;
    private final String username;
    private final int totalPoints;

    public User(int userId, String username, int points) {
        this.userId = userId;
        this.username = username;
        this.totalPoints = points;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public int getPoints() {
        return totalPoints;
    }
}