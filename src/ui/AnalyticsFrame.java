package ui;

import database.DatabaseManager;
import ui.components.BackgroundPanel;
import model.GameSession;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AnalyticsFrame extends JFrame {

    private final int userId;
    BackgroundPanel bg;
    private JLabel totalGamesLabel, highestScoreLabel, avgScoreLabel, totalPointsLabel;
    private DefaultTableModel tableModel;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy | hh:mm a");

    public AnalyticsFrame(int userId) {

        this.userId = userId;

        setTitle("Your Analytics");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        bg = new BackgroundPanel("C:\\Users\\godar\\OneDrive\\Desktop\\SnakeGame\\src\\resources\\download (3).jpg");
        bg.setLayout(new BorderLayout());
        setContentPane(bg);

        initializeUI();
        loadAnalyticsData();

        setVisible(true);
    }

    private void initializeUI() {

        // ====== LEFT SIDE PANEL ======
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(450, 600));

        bg.add(leftPanel, BorderLayout.WEST);

        // ====== HEADING: ANALYTICS ======
        JLabel analyticsHeading = new JLabel("ANALYTICS");
        analyticsHeading.setFont(new Font(Font.SERIF, Font.BOLD, 40));
        analyticsHeading.setForeground(new Color(160,160,160));
        analyticsHeading.setAlignmentX(Component.CENTER_ALIGNMENT);
        analyticsHeading.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        leftPanel.add(analyticsHeading);

        // ====== STAT LABELS ======
        totalGamesLabel = new JLabel("0");
        highestScoreLabel = new JLabel("0");
        avgScoreLabel = new JLabel("0");
        totalPointsLabel = new JLabel("0");

        leftPanel.add(createStatCard("Total Games", totalGamesLabel));
        leftPanel.add(createStatCard("Highest Score", highestScoreLabel));
        leftPanel.add(createStatCard("Average Score", avgScoreLabel));
        leftPanel.add(createStatCard("Total Points", totalPointsLabel));

        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // ====== HEADING: GAME HISTORY ======
        JLabel historyHeading = new JLabel("GAME HISTORY");
        historyHeading.setFont(new Font(Font.SERIF, Font.BOLD, 26));
        historyHeading.setForeground(new Color(160,160,160));
        historyHeading.setAlignmentX(Component.CENTER_ALIGNMENT);
        historyHeading.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        leftPanel.add(historyHeading);

        // ====== TABLE ======
        String[] columns = {"Sr.No", "Date & Time", "Duration", "Score"};

        tableModel = new DefaultTableModel(columns, 0);
        JTable historyTable = new JTable(tableModel);

        historyTable.setShowVerticalLines(true);
        historyTable.setShowHorizontalLines(false);
        historyTable.setGridColor(Color.DARK_GRAY);
        historyTable.setRowSelectionAllowed(false);
        historyTable.setFocusable(false);

        // ===== SET COLUMNS WIDTHS =====
        historyTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        historyTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        historyTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        historyTable.getColumnModel().getColumn(3).setPreferredWidth(60);


        // ===== SET ROW HEIGHT AND FONT =====
        historyTable.setRowHeight(28);
        historyTable.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        historyTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // ===== TABLE COLOR =====
        historyTable.setBackground(new Color(30, 30, 30));
        historyTable.setForeground(Color.WHITE);
        historyTable.setGridColor(Color.DARK_GRAY);

        historyTable.getTableHeader().setBackground(new Color(30, 30, 30));
        historyTable.getTableHeader().setForeground(Color.WHITE);

        // ===== ROW SEPARATOR FOR DAY CHANGE =====
        historyTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBackground(new Color(30, 30, 30));
                label.setForeground(Color.WHITE);

                if (row < table.getRowCount() - 1) {

                    String currentDateStr = (String) table.getValueAt(row, 1);
                    String nextDateStr = (String) table.getValueAt(row + 1, 1);

                    LocalDate currentDate = LocalDate.parse(currentDateStr.split("\\|")[0].trim(), DateTimeFormatter.ofPattern("dd MMM yyyy"));

                    LocalDate nextDate = LocalDate.parse(nextDateStr.split("\\|")[0].trim(), DateTimeFormatter.ofPattern("dd MMM yyyy"));

                    if (!currentDate.equals(nextDate)) {
                        label.setBorder(BorderFactory.createMatteBorder(0, 0, 6, 0, Color.GRAY));
                    } else {
                        label.setBorder(null);
                    }

                } else {
                    label.setBorder(null);
                }

                return label;
            }
        });

        // ===== SCROLLBAR =====
        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setPreferredSize(new Dimension(420, 200));
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));

        // ===== SET TABLE POSITION =====
        JPanel tableWrapper = new JPanel();
        tableWrapper.setOpaque(false);
        tableWrapper.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        tableWrapper.add(scrollPane);

        leftPanel.add(tableWrapper);

        // ===== RIGHT SIDE PANEL =====
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

    private JPanel createStatCard(String title, JLabel valueLabel) {

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(300, 40));
        card.setMaximumSize(new Dimension(300, 40));
        card.setBackground(new Color(30, 30, 30));
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        JLabel titleLabel = new JLabel(title + ":");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        card.add(titleLabel);
        card.add(valueLabel);

        return card;
    }

    private void loadAnalyticsData() {

        DatabaseManager dm = new DatabaseManager();

        int totalGames = dm.getTotalGames(userId);
        int highestScore = dm.getHighestScore(userId);
        double avgScore = dm.getAverageScore(userId);
        int totalPoints = dm.getTotalPoints(userId);


        totalGamesLabel.setText(String.valueOf(totalGames));
        highestScoreLabel.setText(String.valueOf(highestScore));
        avgScoreLabel.setText(String.format("%.2f", avgScore));
        totalPointsLabel.setText(String.valueOf(totalPoints));

        loadGameHistory();
    }

    private void loadGameHistory() {

        DatabaseManager dm = new DatabaseManager();
        List<GameSession> sessions = dm.getGameHistory(userId);

        tableModel.setRowCount(0);
        int srNo = 1;

        for (GameSession session : sessions) {

            LocalDateTime dateTime = session.getPlayedAt().toLocalDateTime();

            String formattedDate = dateTime.format(formatter);

            tableModel.addRow(new Object[]{ srNo++, formattedDate, formatDuration(session.getPlayTime()), session.getScore()});

        }

    }

    private String formatDuration(int totalSeconds) {

        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        return String.format("%02dm %02ds", minutes, seconds);
    }

}
