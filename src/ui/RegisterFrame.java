package ui;

import database.DatabaseManager;
import ui.components.BackgroundPanel;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    JLabel reg, username, password, conPassword;
    JTextField user;
    JPasswordField pass, conPass;
    JButton register, backToLogin;

    public RegisterFrame() {

        setTitle("Registeration");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel bg = new BackgroundPanel("C:\\Users\\godar\\OneDrive\\Desktop\\SnakeGame\\src\\resources\\download (3).jpg");
        bg.setLayout(new GridBagLayout());
        setContentPane(bg);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 30, 10, 20);


        // ===== LOGIN TITLE =====
        reg = new JLabel("REGISTER");
        reg.setFont(new Font(Font.SERIF, Font.BOLD, 50));
        reg.setForeground(new Color(160, 160, 160));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        bg.add(reg, gbc);

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


        // ===== CONFIRM PASSWORD LABEL =====
        conPassword = new JLabel("Confirm Password");
        conPassword.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        conPassword.setForeground(new Color(160, 160, 160));

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        bg.add(conPassword, gbc);


        // ===== CONFIRM PASSWORD FIELD =====
        conPass = new JPasswordField(20);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        bg.add(conPass, gbc);


        // ===== REGISTER BUTTON =====
        register = new JButton("Register");
        register.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        register.setForeground(new Color(160, 160, 160));
        register.setBorder(BorderFactory.createLineBorder(new Color(160, 160, 160),5));

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(30, 30, 10, 20);
        bg.add(register, gbc);


        // ===== BACK TO LOGIN BUTTON =====
        backToLogin = new JButton("Back To Login");
        backToLogin.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        backToLogin.setForeground(new Color(160, 160, 160));
        backToLogin.setBorder(BorderFactory.createLineBorder(new Color(160, 160, 160),5));

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(30, 30, 10, 20);
        bg.add(backToLogin, gbc);


        // ===== RIGHT SPACER COLUMN: so that everthing shifts on left side =====
        GridBagConstraints spacer = new GridBagConstraints();
        spacer.gridx = 2;
        spacer.gridy = 0;
        spacer.weightx = 1.0;  // pushes form to LEFT
        spacer.fill = GridBagConstraints.HORIZONTAL;
        bg.add(Box.createHorizontalStrut(0), spacer);


        // ===== ACTION LISTENER FOR REGISTER BUTTON =====
        register.addActionListener(e ->{
            String u = user.getText();
            String p = new String(pass.getPassword());
            String cp = new String(conPass.getPassword());
            if( u.isEmpty() || p.isEmpty() || cp.isEmpty()){
                JOptionPane.showMessageDialog(this,"Please enter all the details!","Incomplete Entry",JOptionPane.ERROR_MESSAGE);
            }else if(!p.equals(cp)){
                JOptionPane.showMessageDialog(this,"Passwords do not match!\nPlease enter matched passwords!","Password Miss Matched",JOptionPane.ERROR_MESSAGE);
            }else{
                DatabaseManager db = new DatabaseManager();
                boolean b = db.registerUser(u,p);
                if(b){
                    JOptionPane.showMessageDialog(this,"Registered Successfully!","Successful Registration!",JOptionPane.PLAIN_MESSAGE);
                    new LoginFrame();
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(this,"User already exist with this username\nTry with other username!","User Already Exist",JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        // ===== ACTION LISTENER FOR BACK TO LOGIN BUTTON =====
        backToLogin.addActionListener(e ->{
            new LoginFrame();
            dispose();
        });


        setVisible(true);
    }
}
