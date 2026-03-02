package ui;

import ui.components.BackgroundPanel;

import javax.swing.*;
import java.awt.*;

public class WelcomeFrame extends JFrame{

    JPanel panel;
    JLabel title;
    GridBagConstraints gbc;
    JButton login, register;

    public WelcomeFrame(){
        setTitle("Snake Game");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new BackgroundPanel("C:\\Users\\godar\\OneDrive\\Desktop\\SnakeGame\\src\\resources\\download (3).jpg");
        panel.setLayout(new GridBagLayout());
        add(panel);

        // ===== Game Title =====
        title = new JLabel("SNAKE GAME");
        title.setFont(new Font(Font.SERIF,Font.BOLD,60));
        title.setForeground(new Color(160, 160, 160));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, -400, 20, 0);
        panel.add(title,gbc);

        // ===== LOGIN BUTTON =====
        login = new JButton("Login");
        login.setFont(new Font(Font.MONOSPACED,Font.BOLD,20));
        login.setForeground(new Color(160, 160, 160));
        login.setBorder(BorderFactory.createLineBorder(new Color(160, 160, 160),5));
        login.setFocusPainted(false);
        gbc.gridy = 1;
        panel.add(login,gbc);

        // ===== REGISTER BUTTON =====
        register = new JButton("Register");
        register.setFont(new Font(Font.MONOSPACED,Font.BOLD,20));
        register.setForeground(new Color(160, 160, 160));
        register.setBorder(BorderFactory.createLineBorder(new Color(160, 160, 160),5));
        register.setFocusPainted(false);
        gbc.gridy = 2;
        panel.add(register,gbc);

        // ===== ACTION LISTENER FOR LOGIN BUTTON =====
        login.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        // ===== ACTION LISTENER FOR REGISTER BUTTON =====
        register.addActionListener(e -> {
            new RegisterFrame();
            dispose();
        });


        setVisible(true);
    }
}
