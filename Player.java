package timer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.Random;


public class Player extends JComponent {
    private int speed;
    private int x, y;
    private int up = 0;
    private int keepSpeed = 0;
    private boolean pause = true;

    private int AnimationCounter = 0;
    private int limit;
    private Boolean jump = false;

    private Image[] characterAnimation;
    private int[] speeds = {2, 1, 3, -1};
    private Random rand = new Random();
    
    private Timer AnimationTimer;
    private Timer SpeedChangeTimer;
    private Obstacle obstacle;

    public Player(String[] imagePaths, int boardSize) {
        characterAnimation = new Image[imagePaths.length];
        try {
            for (int i = 0; i < imagePaths.length; i++) {
                Image temp = ImageIO.read(new File(imagePaths[i])).getScaledInstance(125, 125, Image.SCALE_DEFAULT);
                characterAnimation[i] = temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        x = 30;
        y = 100;
        limit = 300;
    
    }

    public void resetLimit(int newLimit) {
        // AnimationTimer.stop();
        // SpeedChangeTimer.stop();
        
        // AnimationTimer = new Timer(1000 / speed, new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         if (x < newLimit) {
        //             x += speed;
        //         } else {
        //             x = 10;
        //         }
        //         repaint();
        //     }
        // });
        // AnimationTimer.start();
    }

    public int selectSpeed() {
        int randomInt = rand.nextInt(4);
        return speeds[randomInt];
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.create();
        g.drawImage(characterAnimation[AnimationCounter++], x, y, null);
        g.dispose();

        if (AnimationCounter >= characterAnimation.length) {
            AnimationCounter = 0;
        }
    }

    public void jump() {
        jump = true;
    }

    public void pause() {
        pause = true;
        keepSpeed = speed;
        speed = 0;
    }

    public void unpause() {
        pause = false;
        speed = selectSpeed();
        setNewSpeed();
        SpeedChangeTimer = new Timer(3500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                speed = selectSpeed();
                setNewSpeed();
            }
        });
        SpeedChangeTimer.start();
        //speed = keepSpeed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setObstacle(Obstacle obstacle) {
        this.obstacle = obstacle;
    }

    public void setNewSpeed() {
        if (AnimationTimer != null && AnimationTimer.isRunning()) {
            AnimationTimer.stop();
        }
        AnimationTimer = new Timer(1000 / 4, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                

                if (jump) {
                    System.out.println(String.format("up : %d", up));
                    if (up >= 0 && up <= 1) {
                        y -= 35;
                    } else if (up > 3 && up < 6) {
                        y += 35;
                    } else if (up == 6) {
                        up = 0;
                        jump = false;
                        y = 100;
                    }
                    x += 2;
                    up++;
                } else {
                    if (x <= 0) {
                        speed = 1;
                    } else if (x >= 450) {
                        speed = -1;
                    } 
                    x += speed;
                }

                Main.checkCollision();
                repaint();
            }
        });
        AnimationTimer.start();
    }
}