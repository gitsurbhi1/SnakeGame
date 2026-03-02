package game;

import database.DatabaseManager;
import ui.DashboardFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel {

    static final int PANEL_WIDTH = 900;
    static final int PANEL_HEIGHT = 600;
    JLabel overLabel, scoreLabel;
    JButton playAgain, exit;

    ArrayList<Segment> segment = new ArrayList<>();
    int objectWidth = 20;
    int objectHeight = 20;
    int velocityX;
    int velocityY;
    int foodX, foodY;
    Random random;
    boolean gameOver;
    int score;
    long startTime, endTime, pauseStart, pauseEnd;
    int playTime;
    boolean paused;
    JPanel gameOverPanel;

    Timer timer;

    int userId;

    public GamePanel(int userId) {

        this.setLayout(null);

        this.userId = userId;

        random = new Random();

        // ===== TIMER CREATION =====
        timer = new Timer( 100,e -> {

            Segment currentHead = segment.get(0);
            int newX = currentHead.x + velocityX;
            int newY = currentHead.y + velocityY;
            if (newX >= PANEL_WIDTH)
                newX = 0;
            if (newX < 0)
                newX = PANEL_WIDTH - objectWidth;
            if (newY >= PANEL_HEIGHT)
                newY = 0;
            if (newY < 0)
                newY = PANEL_HEIGHT - objectHeight;
            segment.add(0, new Segment(newX, newY));


            if (newX == foodX && newY == foodY) {
                score++;
                if (score % 5 == 0 && timer.getDelay() > 50){
                    timer.setDelay(timer.getDelay() - 5);
                }
                generateFood();
            } else {
                segment.remove(segment.size() - 1);
            }

            //Collision Block
            for (int i = 1; i < segment.size(); i++) {
                Segment body = segment.get(i);
                Segment head = segment.get(0);
                if (head.x == body.x && head.y == body.y) {
                    gameOver = true;  // GAME OVER
                    timer.stop();
                    // ===== RECORD END TIME =====
                    endTime = System.currentTimeMillis();
                    // ===== CALCULATE PLAY TIME IN SECONDS =====
                    // ===== pausedTime = pauseEnd - pauseStart =====
                    playTime = (int)(((endTime - startTime)- (pauseEnd - pauseStart)) / 1000);
                    // ===== SAVE TO DATABASE =====
                    DatabaseManager db = new DatabaseManager();
                    db.saveGameSession(userId,score,playTime);
                    scoreLabel.setText("Score: " + score);
                    gameOverPanel.setVisible(true);
                }
            }

            repaint();

        });


        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                // ===== NEW GAME =====
                if (code == KeyEvent.VK_ENTER && gameOver){
                    initializeGame();
                }
                // ===== PAUSE GAME =====
                if (code == KeyEvent.VK_SPACE && !gameOver){
                    paused = !paused;
                    if (paused){
                        timer.stop();
                        pauseStart = System.currentTimeMillis();
                    }else {
                        timer.start();
                        pauseEnd = System.currentTimeMillis();
                    }
                    repaint();
                }
                if (!paused && !gameOver){
                    if (velocityX != 0) {
                        if (code == KeyEvent.VK_UP) {
                            velocityX = 0;
                            velocityY = -objectHeight;
                        } else if (code == KeyEvent.VK_DOWN) {
                            velocityX = 0;
                            velocityY = objectHeight;
                        }
                    } else if (velocityY != 0) {
                        if (code == KeyEvent.VK_RIGHT) {
                            velocityX = objectWidth;
                            velocityY = 0;
                        } else if (code == KeyEvent.VK_LEFT) {
                            velocityX = -objectWidth;
                            velocityY = 0;
                        }
                    }
                }
            }
        });

        initializeGame();

        // ===== GAME OVER PANEL =====
        gameOverPanel = new JPanel();
        gameOverPanel.setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        gameOverPanel.setBackground(new Color(0,0,0,180)); // semi transparent
        gameOverPanel.setLayout(null);
        gameOverPanel.setVisible(false);

        this.add(gameOverPanel);

        overLabel = new JLabel("GAME OVER");
        overLabel.setBounds(375,180,150,40);
        overLabel.setFont(new Font(Font.SERIF,Font.BOLD,20));
        overLabel.setForeground(new Color(160, 160, 160));
        gameOverPanel.add(overLabel);

        scoreLabel = new JLabel();
        scoreLabel.setBounds(400,240,150,40);
        scoreLabel.setFont(new Font(Font.SERIF,Font.BOLD,20));
        scoreLabel.setForeground(new Color(160, 160, 160));
        gameOverPanel.add(scoreLabel);

        playAgain = new JButton("Play Again");
        playAgain.setBounds(370,300,150,40);
        playAgain.setFont(new Font(Font.MONOSPACED,Font.BOLD,15));
        playAgain.setForeground(new Color(160, 160, 160));
        playAgain.setBorder(BorderFactory.createLineBorder(new Color(160, 160, 160),5));

        gameOverPanel.add(playAgain);

        exit = new JButton("Exit");
        exit.setBounds(370,360,150,40);
        exit.setFont(new Font(Font.MONOSPACED,Font.BOLD,15));
        exit.setForeground(new Color(160, 160, 160));
        exit.setBorder(BorderFactory.createLineBorder(new Color(160, 160, 160),5));
        gameOverPanel.add(exit);

        playAgain.addActionListener(e -> {
            gameOverPanel.setVisible(false);
            initializeGame();
        });

        exit.addActionListener(e -> {
            gameOverPanel.setVisible(false);

            Window window = SwingUtilities.getWindowAncestor(GamePanel.this);
            window.dispose();   // closes the GameFrame

            new DashboardFrame(userId);
        });

        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.requestFocusInWindow();

    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(new Color(18,18,18, 34));
        for (int i = 0; i < PANEL_WIDTH; i += objectWidth) {
            g.drawLine(i,0,i,PANEL_HEIGHT);
        }
        for (int i = 0; i < PANEL_HEIGHT; i += objectHeight) {
            g.drawLine(0,i,PANEL_WIDTH,i);
        }
        for(int i=0;i<segment.size();i++){
            g.setColor(new Color(46,196,182));
            Segment s = segment.get(i);
            if (i==0){
                g.setColor(new Color(0, 135, 245));
            }
            g.fillOval(s.x,s.y,objectWidth,objectHeight);
        }
        g.setColor(new Color(255,107,107));
        g.fillOval(foodX,foodY,objectWidth,objectHeight);
        g.setColor(Color.WHITE);
        if (paused){
            g.setFont(new Font(Font.SERIF,Font.BOLD,30));
            FontMetrics fontMetrics = g.getFontMetrics();
            int textWidth = fontMetrics.stringWidth("PAUSED");
            int x = (PANEL_WIDTH - textWidth) / 2;
            int y = (PANEL_HEIGHT - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent();
            g.drawString("PAUSED",x,y);
        }
        g.setFont(new Font(Font.SERIF,Font.BOLD,20));
        g.drawString("Score: "+ score,20,30);

        long currentTime = System.currentTimeMillis();
        int currentPlayTime = (int)((currentTime - startTime)/1000);
        g.drawString("Time: " + currentPlayTime + "s", PANEL_WIDTH - 150, 30);
    }

    void initializeGame(){

        // ===== CLEAR SNAKE LIST =====
        segment.clear();

        // ===== STARTING SNAKE =====
        segment.add(new Segment(200,200));
        segment.add(new Segment(180,200));
        segment.add(new Segment(160,200));

        // ===== RESET DIRECTIONS =====
        velocityX = objectWidth;
        velocityY = 0;

        // ===== RESET SCORE =====
        score = 0;

        generateFood();

        gameOver = false;
        paused = false;

        // ===== START TIMER =====
        timer.start();

        // ===== RECORD START GAME TIME =====
        startTime = System.currentTimeMillis();

    }

    private void generateFood(){
        boolean validPosition;
        do{
            validPosition = true;
            foodX = random.nextInt(PANEL_WIDTH / objectWidth) * objectWidth;
            foodY = random.nextInt(PANEL_HEIGHT / objectHeight) * objectHeight;
            for(Segment s : segment){
                if(s.x == foodX && s.y == foodY){
                    validPosition = false;
                    break;
                }
            }
        } while (!validPosition);
    }
}