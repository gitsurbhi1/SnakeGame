import javax.swing.*;

public class App {
    public static void main(String[] args) {
        int boardWidth = 600;
        int boardHeight = 600; // square board

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Snake");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);
            frame.add(snakeGame);
            frame.pack(); // sizes the frame to SnakeGame's preferred size
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            snakeGame.requestFocusInWindow();
        });
    }
}

