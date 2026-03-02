package ui;

import database.DatabaseManager;
import ui.components.BackgroundPanel;
import ui.components.RoundPanel;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class LeaderboardFrame extends JFrame {

    private final int userId;
    BackgroundPanel bg;
    JPanel topPanel, centerPanel, bottomPanel;

    public LeaderboardFrame(int userId) {

        this.userId = userId;

        setTitle("Leaderboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        bg = new BackgroundPanel("C:\\Users\\godar\\OneDrive\\Desktop\\SnakeGame\\src\\resources\\download (3).jpg");
        bg.setLayout(new BorderLayout());
        setContentPane(bg);

        initializeUI();
        loadLeaderboardData();

        setVisible(true);
    }

    private void initializeUI() {

        // ===== ADD LEFT PANEL =====
        JPanel leftContainer = new JPanel();
        leftContainer.setLayout(new BorderLayout());
        leftContainer.setOpaque(false); // IMPORTANT (transparent)

        leftContainer.setPreferredSize(new Dimension(450, 600));

        bg.add(leftContainer, BorderLayout.WEST);

        createLeaderboardSections(leftContainer);


        // ===== ADD RIGHT PANEL FOR BACK BUTTON =====
        JPanel rightPanel = new JPanel(null);
        rightPanel.setOpaque(false);

        rightPanel.setPreferredSize(new Dimension(450, 600));

        JButton backBtn = new JButton("BACK");
        backBtn.setBounds(50, 300, 100, 40);
        backBtn.setFont(new Font(Font.MONOSPACED,Font.BOLD,30));
        backBtn.setForeground(new Color(160, 160, 160));
        backBtn.setBorder(BorderFactory.createLineBorder(new Color(160, 160, 160),5));
        backBtn.setFocusPainted(false);

        rightPanel.add(backBtn);

        bg.add(rightPanel, BorderLayout.EAST);

        backBtn.addActionListener(e -> {
            new DashboardFrame(userId);
            dispose();
        });
    }

    private void createLeaderboardSections(JPanel parent) {

        topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(450, 180));
        topPanel.setOpaque(false);

        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(450, 80));
        bottomPanel.setOpaque(false);

        parent.add(topPanel, BorderLayout.NORTH);
        parent.add(centerPanel, BorderLayout.CENTER);
        parent.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadLeaderboardData() {

        DatabaseManager dm = new DatabaseManager();
        List<User> users = dm.fetchUsersFromDatabase();

        // ===== FILL TOP 3 =====
        topPanel.removeAll();
        topPanel.add(createTopThreePanel(users));

        // ===== FILL LEADERBOARD TABLE =====
        centerPanel.removeAll();
        centerPanel.add(createLeaderboardTable(users), BorderLayout.CENTER);

        // ===== CURRENT USER RANK =====
        int rank = findUserRank(users);

        bottomPanel.removeAll();
        JLabel rankLabel = new JLabel("Your Rank: " + rank);
        rankLabel.setForeground(Color.WHITE);
        rankLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        bottomPanel.add(rankLabel);

        revalidate();
        repaint();
    }

    private JPanel createTopThreePanel(List<User> users) {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        panel.setOpaque(false);

        if (users.size() == 0) return panel;

        User first  = users.size() > 0 ? users.get(0) : null;
        User second = users.size() > 1 ? users.get(1) : null;
        User third  = users.size() > 2 ? users.get(2) : null;

        if (second != null)
            panel.add(createTopUserCircle(second, 2));

        if (first != null)
            panel.add(createTopUserCircle(first, 1));

        if (third != null)
            panel.add(createTopUserCircle(third, 3));

        return panel;
    }

    private JPanel createTopUserCircle(User user, int rank) {

        Color color;
        int size;

        if (rank == 1) {
            color = new Color(255, 215, 0); // Gold
            size = 130;
        } else if (rank == 2) {
            color = new Color(192, 192, 192); // Silver
            size = 110;
        } else {
            color = new Color(205, 127, 50); // Bronze
            size = 100;
        }

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);

        JLabel crown = new JLabel("👑");
        crown.setAlignmentX(Component.CENTER_ALIGNMENT);
        crown.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 22));

        RoundPanel circle = new RoundPanel(color);
        circle.setPreferredSize(new Dimension(size, size));
        circle.setMaximumSize(new Dimension(size, size));
        circle.setLayout(new BoxLayout(circle, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(user.getUsername());
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        name.setForeground(Color.BLACK);
        name.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));

        JLabel points = new JLabel(user.getPoints() + " pts");
        points.setAlignmentX(Component.CENTER_ALIGNMENT);
        points.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        circle.add(Box.createVerticalGlue());
        circle.add(name);
        circle.add(points);
        circle.add(Box.createVerticalGlue());

        container.add(crown);
        container.add(circle);

        return container;
    }

    private JScrollPane createLeaderboardTable(List<User> users) {

        String[] columns = {"Rank", "Name", "Points"};

        Object[][] data = new Object[users.size()][3];

        JTable table = new JTable(data, columns);
        table.setRowHeight(28);

        for (int i = 0; i < users.size(); i++) {

            data[i][0] = i + 1;
            data[i][1] = users.get(i).getUsername();
            data[i][2] = users.get(i).getPoints();
        }

        table.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        table.setBackground(new Color(30, 30, 30));
        table.setForeground(Color.WHITE);
        table.setGridColor(Color.DARK_GRAY);

        table.getTableHeader().setBackground(new Color(30, 30, 30));
        table.getTableHeader().setForeground(Color.WHITE);

        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);

        // ===== CENTER ALIGN ALL CELLS =====
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(new Color(30, 30, 30));
        centerRenderer.setForeground(Color.WHITE);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(420, 250));
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));

        return scrollPane;
    }

    private int findUserRank(List<User> users) {

        for (int i = 0; i < users.size(); i++) {

            if (users.get(i).getUserId() == userId) {
                return i + 1;
            }
        }

        return -1;
    }

}
