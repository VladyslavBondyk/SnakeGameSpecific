package com.entire;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class SnakeGame  extends JPanel {
    private boolean inMenu = true;
    private boolean paused = false;
    private int applesToWin;
    //private int applesCaught;
    private boolean winConditionMet = false;
    private Score score;
    private int applesEaten;
    private int points;


    private static final long serialVersionUID = 1L;

    private int[] x;
    private int[] y;
    private int apple_x, apple_y;
    private int direction;
    private int dots;
    private final int DOT_SIZE = 10;
    private final int B_WIDTH = 500;
    private final int B_HEIGHT = 500;
    private final int DELAY = 55;

    private Timer timer;
    private boolean inGame = true;

    private final int xSize = B_WIDTH / DOT_SIZE;
    private final int ySize = B_HEIGHT / DOT_SIZE;

    private MenuPanel menuPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Snake Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);

            SnakeGame game = new SnakeGame();
            frame.add(game);
            frame.pack();
            frame.setVisible(true);
        });
    }

    public SnakeGame() {
        initGame();
    }

    private void initGame() {
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        setBackground(Color.black);
        score = new Score();
        applesEaten = 0;


        menuPanel = new MenuPanel(new MenuPanel.MenuListener() {
            @Override
            public void onStartGame() {
                startGame();
            }

            @Override
            public void onOptions() {
                showOptions();
            }

            @Override
            public void onExit() {
                exitGame();
            }
        });

        setLayout(new BorderLayout());
        add(menuPanel);

        x = new int[xSize];
        y = new int[ySize];

        x[0] = B_WIDTH / 2 - DOT_SIZE;
        y[0] = B_HEIGHT / 2 - DOT_SIZE;

        setFocusable(true);
        addKeyListener(new TAdapter());

        timer = new Timer(DELAY, new GameCycle());
        timer.start();
    }
    private void startGame() {
        inMenu = false;
        inGame = true;
        removeKeyListener(new TAdapter());
        addKeyListener(new TAdapter());
        x[0] = B_WIDTH / 2 - DOT_SIZE;
        y[0] = B_HEIGHT / 2 - DOT_SIZE;
        direction = 0;
        dots = 3;
        locateApple();
        timer.start();
        remove(menuPanel);
    }

    private void showOptions() {
        JOptionPane.showMessageDialog(this, "Options:\n\n- Use W - for the Up; A - for the Left; S - for the Down; D - for the Right; keys to move the snake.\n- Press the Space bar to pause the game.");
    }

    private void exitGame() {
        System.exit(0);
    }

    private void move() {
        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (direction == KeyEvent.VK_A) {
            x[0] -= DOT_SIZE;
        } else if (direction == KeyEvent.VK_D) {
            x[0] += DOT_SIZE;
        } else if (direction == KeyEvent.VK_W) {
            y[0] -= DOT_SIZE;
        } else if (direction == KeyEvent.VK_S) {
            y[0] += DOT_SIZE;
        }

        if (x[0] == apple_x && y[0] == apple_y) {
            points++;
            dots++;
            locateApple();
            checkApple();
        }

        checkCollision();
        checkWinCondition();
    }


    private void checkApple() {


        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            applesEaten++;
            //applesCaught = applesEaten;
            points = applesEaten;

            if (applesEaten == applesToWin) {
                winConditionMet = true;
                inGame = false;
            }

            locateApple();
        }
    }
//
    private void checkWinCondition() {
        if (points == 2) {
            winConditionMet = true;
            inGame = false;
        }
    }


    private void checkCollision() {
        for (int z = dots; z > 0; z--) {
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT) {
            y[0] = 0; // Move snake to the top
        }

        if (y[0] < 0) {
            y[0] = B_HEIGHT - DOT_SIZE; // Move snake to the bottom
        }

        if (x[0] >= B_WIDTH) {
            x[0] = 0; // Move snake to the left
        }

        if (x[0] < 0) {
            x[0] = B_WIDTH - DOT_SIZE; // Move snake to the right
        }

        if (!inGame) {
            timer.stop();
        }
    }


    private void locateApple() {
        int xRange = B_WIDTH / DOT_SIZE;
        int yRange = B_HEIGHT / DOT_SIZE;

        int appleXIndex = (int) (Math.random() * xRange);
        int appleYIndex = (int) (Math.random() * yRange);

        apple_x = appleXIndex * DOT_SIZE;
        apple_y = appleYIndex * DOT_SIZE;
    }

    private void drawMenu(Graphics g) {
        String message = "Press Space to Start";
        Font font = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(message, (B_WIDTH - metrics.stringWidth(message)) / 2, B_HEIGHT / 2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (inGame) {
            if (paused) {
                String message = "Game Paused";
                Font font = new Font("Helvetica", Font.BOLD, 14);
                FontMetrics metrics = getFontMetrics(font);

                g.setColor(Color.white);
                g.setFont(font);
                g.drawString(message, (B_WIDTH - metrics.stringWidth(message)) / 2, B_HEIGHT / 2);
            } else {
                g.setColor(Color.green);
                g.fillOval(apple_x, apple_y, DOT_SIZE, DOT_SIZE);

                for (int z = 0; z < dots; z++) {
                    if (z == 0) {
                        g.setColor(Color.green);
                    } else {
                        g.setColor(Color.white);
                    }
                    g.fillRect(x[z], y[z], DOT_SIZE, DOT_SIZE);
                }
            }
            score.draw(g);
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        if (winConditionMet) {
            String message = "You Win! You caught 2 apples! \nIf you want make me more than 2 caught apples – let me know. \nOr if you want to finally go out with me";
            Font font = new Font("Helvetica", Font.BOLD, 14);
            FontMetrics metrics = getFontMetrics(font);

            g.setColor(Color.white);
            g.setFont(font);

            String[] lines = message.split("\n"); // Split the message into separate lines

            int y = B_HEIGHT / 2 - (lines.length * metrics.getHeight()) / 2; // Calculate the y-coordinate for centering the text vertically

            for (String line : lines) {
                int x = (B_WIDTH - metrics.stringWidth(line)) / 2; // Calculate the x-coordinate for centering the text horizontally
                g.drawString(line, x, y);
                y += metrics.getHeight(); // Move to the next line
            }
        } else {
            String message = "Game Over";
            Font font = new Font("Helvetica", Font.BOLD, 14);
            FontMetrics metrics = getFontMetrics(font);

            g.setColor(Color.white);
            g.setFont(font);
            int x = (B_WIDTH - metrics.stringWidth(message)) / 2;
            int y = B_HEIGHT / 2;
            g.drawString(message, x, y);
        }
    }


    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (inMenu && key == KeyEvent.VK_SPACE) {
                inMenu = false;
                inGame = true;
                timer.start();
            } else if (inGame && !paused) {
                if (key == KeyEvent.VK_SPACE) {
                    paused = true;
                } else {
                    handleDirectionKey(key);
                }
            } else if (inGame && paused && key == KeyEvent.VK_SPACE) {
                paused = false;
            }
        }

        private void handleDirectionKey(int key) {
            switch (key) {
                case KeyEvent.VK_A:
                    if (direction != KeyEvent.VK_D) {
                        direction = KeyEvent.VK_A;
                    }
                    break;
                case KeyEvent.VK_D:
                    if (direction != KeyEvent.VK_A) {
                        direction = KeyEvent.VK_D;
                    }
                    break;
                case KeyEvent.VK_W:
                    if (direction != KeyEvent.VK_S) {
                        direction = KeyEvent.VK_W;
                    }
                    break;
                case KeyEvent.VK_S:
                    if (direction != KeyEvent.VK_W) {
                        direction = KeyEvent.VK_S;
                    }
                    break;
            }
        }

    }

    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!inMenu && inGame && !paused) {
                move();
                checkCollision();
                checkApple();
            }
            repaint();
        }
    }
}
