package model;

import java.sql.Timestamp;

public class GameSession {

    private final Timestamp playedAt;
    private final int score;
    private final int playTime;

    public GameSession(Timestamp playedAt, int score, int playTime) {
        this.playedAt = playedAt;
        this.score = score;
        this.playTime = playTime;
    }

    public Timestamp getPlayedAt() {
        return playedAt;
    }

    public int getScore() {
        return score;
    }

    public int getPlayTime() {
        return playTime;
    }
}