package timer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.Random;

public class Obstacle extends JComponent {
    private int x, y;
    private int startingX;
    private int speed;
    private Image[] animation;
    private int counter = 0;
    private Timer timer;
    private Timer animationTimer;

    public Obstacle(String[] filepaths, int speed) {
        this.speed = speed;
        animation = new Image[filepaths.length];
        try {
            for (int i = 0; i < filepaths.length; i++) {
                Image temp = ImageIO.read(new File(filepaths[i])).getScaledInstance(75, 75, Image.SCALE_DEFAULT);
                animation[i] = temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        x = 450;
        y = 130;
        startingX = x;
        timer = new Timer(1000/6, (ActionEvent e) -> {
            x -= speed;
            if (x < 0) {
                x = startingX;
                counter = 0;
            }
            repaint();
        });
        timer.start();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.create();
        g.drawImage(animation[counter], x, y, null);
        g.dispose();
    }

    public void fallover() {
        animationTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                counter++;
                repaint();
                if (counter == animation.length-1) {
                    Main.unpause();
                    counter--;
                }
            }
        }); 
        animationTimer.start();
    }

    public void stopFallover() {
        animationTimer.stop();
        counter = animation.length-1;
    }
}