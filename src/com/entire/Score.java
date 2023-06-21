package com.entire;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Score {
    Font font;
    private int applesEaten;
    private int points;

    public void setScore(int applesEaten) {
        this.applesEaten = applesEaten;
    }

    public Score() {
        points = 0;
        font = new Font("Arial", Font.PLAIN, 36);
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(font);
        g.drawString("Score: " + points, 320, 25);
        g.setColor(Color.white);
    }

    public int getPoints() {
        return points;
    }


}
