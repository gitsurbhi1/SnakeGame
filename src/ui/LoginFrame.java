package ui;

import database.DatabaseManager;
import ui.components.BackgroundPanel;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    JLabel log, username, password;
    JTextField user;
    JPasswordField pass;
    JButton login,back;

    public LoginFrame() {

        setTitle("Login");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel bg = new BackgroundPanel("C:\\Users\\godar\\OneDrive\\Desktop\\SmartSnakeGame\\src\\resources\\download (3).jpg");
        bg.setLayout(new GridBagLayout());
        setContentPane(bg);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 30, 10, 20);


        // ===== LOGIN TITLE =====
        log = new JLabel("LOGIN");
        log.setFont(new Font(Font.SERIF, Font.BOLD, 50));
        log.setForeground(new Color(160, 160, 160));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        bg.add(log, gbc);

        gbc.gridwidth = 1; // reset


        // ===== USERNAME LABEL =====
        username = new JLabel("Username");
        username.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        username.setForeground(new Color(160, 160, 160));

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        bg.add(username, gbc);


        // ===== USERNAME FIELD =====
        user = new JTextField(20);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        bg.add(user, gbc);


        // ===== PASSWORD LABEL =====
        password = new JLabel("Password");
        password.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        password.setForeground(new Color(160, 160, 160));

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        bg.add(password, gbc);


        // ===== PASSWORD FIELD =====
        pass = new JPasswordField(20);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        bg.add(pass, gbc);


        // ===== LOGIN BUTTON =====
        login = new JButton("Login");
        login.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        login.setForeground(new Color(160, 160, 160));

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(30, 80, 10, 20);
        bg.add(login, gbc);


        // ===== BACK BUTTON =====
        back = new JButton("Back");
        back.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        back.setForeground(new Color(160, 160, 160));

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(30, 80, 10, 20);
        bg.add(back, gbc);


        // ===== RIGHT SPACER COLUMN: so that everthing shifts to left =====
        GridBagConstraints spacer = new GridBagConstraints();
        spacer.gridx = 2;
        spacer.gridy = 0;
        spacer.weightx = 1.0;  // pushes form to LEFT
        spacer.fill = GridBagConstraints.HORIZONTAL;
        bg.add(Box.createHorizontalStrut(0), spacer);


        // ===== ACTION LISTENER FOR LOGIN BUTTON =====
        login.addActionListener(e ->{
            String u = user.getText();
            String p = new String(pass.getPassword());
            if( u.isEmpty() || p.isEmpty()){
                JOptionPane.showMessageDialog(this,"Please enter all the details!","Incomplete entry",JOptionPane.ERROR_MESSAGE);
            }else{
                DatabaseManager db = new DatabaseManager();
                int userId = db.validateLogin(u, p);
                if(userId != -1){
                    new DashboardFrame(userId);
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(this,"Please enter correct details!","Invalid credentials",JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        // ===== ACTION LISTENER FOR BACK BUTTON =====
        back.addActionListener(e -> {
            new WelcomeFrame();
            dispose();
        });


        setVisible(true);
    }
}