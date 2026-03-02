package ui;

import ui.components.BackgroundPanel;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    JLabel dashboard;
    JButton newGame, leaderboard, analytics, logout;

    int userId;

    public DashboardFrame(int userId) {

        this.userId = userId;

        setTitle("Game Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel bg = new BackgroundPanel("C:\\Users\\godar\\OneDrive\\Desktop\\SnakeGame\\src\\resources\\download (3).jpg");
        bg.setLayout(new GridBagLayout());
        setContentPane(bg);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 30, 10, 20);


        // ===== DASHBOARD TITLE =====
        dashboard = new JLabel("Game\n Dashboard");
        dashboard.setFont(new Font(Font.SERIF, Font.BOLD, 50));
        dashboard.setForeground(new Color(160, 160, 160));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        bg.add(dashboard, gbc);


        // ===== NEW GAME BUTTON =====
        newGame = new JButton("New Game");
        newGame.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        newGame.setForeground(new Color(160, 160, 160));
        newGame.setBorder(BorderFactory.createLineBorder(new Color(160, 160, 160),5));


        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 30, 10, 20);
        bg.add(newGame, gbc);


        // ===== LEADERBOARD BUTTON =====
        leaderboard = new JButton("Leaderboard");
        leaderboard.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        leaderboard.setForeground(new Color(160, 160, 160));
        leaderboard.setBorder(BorderFactory.createLineBorder(new Color(160, 160, 160),5));


        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 30, 10, 20);
        bg.add(leaderboard, gbc);


        // ===== ANALYTICS BUTTON =====
        analytics = new JButton("Your Analysis");
        analytics.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        analytics.setForeground(new Color(160, 160, 160));
        analytics.setBorder(BorderFactory.createLineBorder(new Color(160, 160, 160),5));


        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 30, 10, 20);
        bg.add(analytics, gbc);


        // ===== LOGOUT BUTTON =====
        logout = new JButton("Logout");
        logout.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        logout.setForeground(new Color(160, 160, 160));
        logout.setBorder(BorderFactory.createLineBorder(new Color(160, 160, 160),5));


        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 30, 10, 20);
        bg.add(logout, gbc);


        // ===== RIGHT SPACER COLUMN =====
        GridBagConstraints spacer = new GridBagConstraints();
        spacer.gridx = 2;
        spacer.gridy = 0;
        spacer.weightx = 1.0;  // pushes form to LEFT
        spacer.fill = GridBagConstraints.HORIZONTAL;
        bg.add(Box.createHorizontalStrut(0), spacer);


        // ===== ACTION LISTENER FOR NEW GAME BUTTON =====
        newGame.addActionListener(e ->{
            new GameFrame(userId);
            dispose();
        });


        // ===== ACTION LISTENER FOR LEADERBOARD BUTTON =====
        leaderboard.addActionListener(e ->{
            new LeaderboardFrame(userId);
            dispose();
        });


        // ===== ACTION LISTENER FOR ANALYTICS BUTTON =====
        analytics.addActionListener(e -> {
            new AnalyticsFrame(userId);
            dispose();
        });


        // ===== ACTION LISTENER FOR LOGOUT BUTTON =====
        logout.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });


        setVisible(true);
    }
}
