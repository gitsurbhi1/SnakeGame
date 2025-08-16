import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    private static class Tile {
        int x, y; // grid coordinates
        Tile(int x, int y) { this.x = x; this.y = y; }
    }

    // Board
    private final int boardWidth;
    private final int boardHeight;
    private final int tileSize = 25;
    private final int cols;
    private final int rows;

    // Snake
    private Tile snakeHead;
    private final ArrayList<Tile> snakeBody = new ArrayList<>();

    // Food
    private final Random random = new Random();
    private Tile food;

    // Game
    private int velocityX = 1, velocityY = 0;   // moving right initially
    private boolean gameOver = false;
    private int highScore = 0;

    // Speed control
    private int delay = 100;   // ms per tick
    private final int minDelay = 40;
    private final int speedStep = 5;
    private Timer gameLoop;

    public SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.cols = boardWidth / tileSize;
        this.rows = boardHeight / tileSize;

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);

        startGame();
    }

    // --- Game lifecycle ---
    private void startGame() {
        // Reset snake
        snakeBody.clear();
        snakeHead = new Tile(cols / 2, rows / 2);
        snakeBody.add(new Tile(snakeHead.x - 1, snakeHead.y));
        snakeBody.add(new Tile(snakeHead.x - 2, snakeHead.y));

        // Reset state
        velocityX = 1; velocityY = 0;
        gameOver = false;
        delay = 100;

        placeFood();

        if (gameLoop != null) gameLoop.stop();
        gameLoop = new Timer(delay, this);
        gameLoop.start();
    }

    private void placeFood() {
        // ensure food is not on the snake
        while (true) {
            int fx = random.nextInt(cols);
            int fy = random.nextInt(rows);
            boolean onSnake = (snakeHead.x == fx && snakeHead.y == fy);
            if (!onSnake) {
                for (Tile part : snakeBody) {
                    if (part.x == fx && part.y == fy) { onSnake = true; break; }
                }
            }
            if (!onSnake) { food = new Tile(fx, fy); return; }
        }
    }

    // --- Painting ---
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw((Graphics2D) g);
    }

    private void draw(Graphics2D g) {
        // Antialias for circles/text
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Gradient background
        GradientPaint gp = new GradientPaint(
                0, 0, new Color(20, 24, 33),
                0, boardHeight, new Color(48, 62, 82)
        );
        g.setPaint(gp);
        g.fillRect(0, 0, boardWidth, boardHeight);

        // Subtle grid
        g.setColor(new Color(255, 255, 255, 20));
        for (int c = 0; c <= cols; c++) g.drawLine(c * tileSize, 0, c * tileSize, boardHeight);
        for (int r = 0; r <= rows; r++) g.drawLine(0, r * tileSize, boardWidth, r * tileSize);

        // Draw food (circle)
        int pad = 4; // padding so the circle looks nice inside the tile
        int d = tileSize - 2 * pad;
        g.setColor(new Color(255, 90, 90));
        g.fillOval(food.x * tileSize + pad, food.y * tileSize + pad, d, d);

        // Draw snake (circles with gradient along body)
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile part = snakeBody.get(i);
            float t = (float) i / Math.max(1, snakeBody.size()); // 0..1
            Color bodyColor = blend(new Color(80, 220, 120), new Color(20, 160, 90), t);
            g.setColor(bodyColor);
            g.fillOval(part.x * tileSize + pad, part.y * tileSize + pad, d, d);
        }
        // Head on top
        g.setColor(new Color(120, 255, 160));
        g.fillOval(snakeHead.x * tileSize + pad, snakeHead.y * tileSize + pad, d, d);

        // Score / High score
        g.setFont(new Font("Inter", Font.BOLD, 16));
        g.setColor(Color.white);
        int score = snakeBody.size();
        g.drawString("Score: " + score, 10, 20);
        g.drawString("High: " + highScore, 10, 40);

        if (gameOver) {
            // overlay
            g.setColor(new Color(0, 0, 0, 140));
            g.fillRect(0, 0, boardWidth, boardHeight);

            String msg = "Game Over";
            String msg2 = "Press ENTER to Restart";
            g.setFont(new Font("Inter", Font.BOLD, 36));
            drawCenteredString(g, msg, boardWidth, boardHeight - 20);
            g.setFont(new Font("Inter", Font.PLAIN, 18));
            drawCenteredString(g, msg2, boardWidth, boardHeight + 20);
        }
    }

    private static void drawCenteredString(Graphics2D g, String text, int w, int yCenter) {
        FontMetrics fm = g.getFontMetrics();
        int x = (w - fm.stringWidth(text)) / 2;
        int y = (yCenter) / 2;
        g.setColor(Color.white);
        g.drawString(text, x, y);
    }

    private static Color blend(Color a, Color b, float t) {
        t = Math.max(0, Math.min(1, t));
        int r = (int) (a.getRed() * (1 - t) + b.getRed() * t);
        int g = (int) (a.getGreen() * (1 - t) + b.getGreen() * t);
        int bl = (int) (a.getBlue() * (1 - t) + b.getBlue() * t);
        return new Color(r, g, bl);
    }

    // --- Logic ---
    private void move() {
        // move body
        for (int i = snakeBody.size() - 1; i > 0; i--) {
            Tile prev = snakeBody.get(i - 1);
            Tile cur = snakeBody.get(i);
            cur.x = prev.x; cur.y = prev.y;
        }
        if (!snakeBody.isEmpty()) {
            snakeBody.get(0).x = snakeHead.x;
            snakeBody.get(0).y = snakeHead.y;
        }

        // move head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // wrap around
        if (snakeHead.x < 0) snakeHead.x = cols - 1;
        if (snakeHead.x >= cols) snakeHead.x = 0;
        if (snakeHead.y < 0) snakeHead.y = rows - 1;
        if (snakeHead.y >= rows) snakeHead.y = 0;

        // eat food
        if (snakeHead.x == food.x && snakeHead.y == food.y) {
            // grow by adding a new segment at the end (will follow next tick)
            Tile tail = snakeBody.isEmpty() ? new Tile(snakeHead.x, snakeHead.y) :
                    new Tile(snakeBody.get(snakeBody.size() - 1).x, snakeBody.get(snakeBody.size() - 1).y);
            snakeBody.add(new Tile(tail.x, tail.y));
            placeFood();

            // speed up
            if (delay > minDelay) {
                delay -= speedStep;
                gameLoop.setDelay(delay);
            }
        }

        // self-collision ends game
        for (Tile part : snakeBody) {
            if (snakeHead.x == part.x && snakeHead.y == part.y) {
                gameOver = true;
                break;
            }
        }
    }

    // --- Swing callbacks ---
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
        } else {
            // update high score once per game over
            highScore = Math.max(highScore, snakeBody.size());
            gameLoop.stop();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver) {
            int code = e.getKeyCode();
            if (code == KeyEvent.VK_UP && velocityY != 1)    { velocityX = 0; velocityY = -1; }
            else if (code == KeyEvent.VK_DOWN && velocityY != -1) { velocityX = 0; velocityY = 1; }
            else if (code == KeyEvent.VK_LEFT && velocityX != 1)  { velocityX = -1; velocityY = 0; }
            else if (code == KeyEvent.VK_RIGHT && velocityX != -1){ velocityX = 1; velocityY = 0; }
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            startGame(); // restart, keep highScore
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
