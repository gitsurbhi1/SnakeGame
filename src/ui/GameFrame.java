package ui;

import game.GamePanel;

import javax.swing.JFrame;

public class GameFrame extends JFrame {

    GamePanel panel;
    int userId;

    public GameFrame(int userId) {

        this.userId = userId;

        panel = new GamePanel(userId);
        this.add(panel);

        this.setTitle("Smart Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}