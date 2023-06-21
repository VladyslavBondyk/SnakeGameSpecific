package com.entire;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;

public class MenuPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public interface MenuListener {
        void onStartGame();
        void onOptions();
        void onExit();
    }

    private MenuListener menuListener;

    public MenuPanel(MenuListener listener) {
        menuListener = listener;
        setPreferredSize(new Dimension(500, 500));
        setBackground(Color.BLACK);
        setFocusable(true);
        addMouseListener(new MenuMouseListener());
        addKeyListener(new TAdapter());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMenu(g);
    }

    private void drawMenu(Graphics g) {
        String title = "Snake Game";
        String startGame = "1. Start Game";
        String options = "2. Options";
        String exit = "3. Exit";
        Font font = new Font("Helvetica", Font.BOLD, 20);
        FontMetrics metrics = g.getFontMetrics(font);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(title, centerX - metrics.stringWidth(title) / 2, centerY - 30);
        g.setFont(font.deriveFont(16f));
        g.drawString(startGame, centerX - metrics.stringWidth(startGame) / 2, centerY);
        g.drawString(options, centerX - metrics.stringWidth(options) / 2, centerY + 30);
        g.drawString(exit, centerX - metrics.stringWidth(exit) / 2, centerY + 60);
    }

    private class MenuMouseListener extends java.awt.event.MouseAdapter {
        @Override
        public void mousePressed(java.awt.event.MouseEvent evt) {
            handleMouseEvent(evt.getX(), evt.getY());
        }
    }

    private class TAdapter extends java.awt.event.KeyAdapter {
        @Override
        public void keyPressed(java.awt.event.KeyEvent e) {
            int key = e.getKeyCode();

            // Check for number button presses
            if (key == KeyEvent.VK_1) {
                handleKeyPress(1);
            } else if (key == KeyEvent.VK_2) {
                handleKeyPress(2);
            } else if (key == KeyEvent.VK_3) {
                handleKeyPress(3);
            }

        }
    }

    private void handleMouseEvent(int mouseX, int mouseY) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        if (mouseY >= centerY - 5 && mouseY <= centerY + 15) {
            if (mouseX >= centerX - 60 && mouseX <= centerX + 60) {
                // Start Game
                menuListener.onStartGame();
            } else if (mouseX >= centerX - 60 && mouseX <= centerX + 60 + 20) {
                // Options
                menuListener.onOptions();
            } else if (mouseX >= centerX - 60 && mouseX <= centerX + 60 - 10) {
                // Exit
                menuListener.onExit();
            }
        }
    }

    private void handleKeyPress(int key) {
        if (key == 1) {
            // Start Game
            menuListener.onStartGame();
        } else if (key == 2) {
            // Options
            menuListener.onOptions();
        } else if (key == 3) {
            // Exit
            menuListener.onExit();
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocus();
    }
}
