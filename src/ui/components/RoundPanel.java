package ui.components;

import javax.swing.*;
import java.awt.*;

public class RoundPanel extends JPanel {

    private Color color;

    public RoundPanel(Color color) {
        this.color = color;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        g2.fillOval(0, 0, getWidth(), getHeight());
    }
}