package timer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main {
    private static String[] filepaths = {"/Users/laurencronin/Downloads/jpgs/robot1.png", 
    "/Users/laurencronin/Downloads/jpgs/robot2.png",
    "/Users/laurencronin/Downloads/jpgs/robot3.png",
    "/Users/laurencronin/Downloads/jpgs/robot4.png",
    "/Users/laurencronin/Downloads/jpgs/robot5.png",
    "/Users/laurencronin/Downloads/jpgs/robot6.png",
    "/Users/laurencronin/Downloads/jpgs/robot7.png"
    };

    private static String[] hurdle_file = {"/Users/laurencronin/Downloads/jpgs/hurdle.png",
    "/Users/laurencronin/Downloads/jpgs/hurdle2.png",
    "/Users/laurencronin/Downloads/jpgs/hurdle3.png",
    "/Users/laurencronin/Downloads/jpgs/hurdle4.png",
    "/Users/laurencronin/Downloads/jpgs/hurdle5.png",
    "/Users/laurencronin/Downloads/jpgs/hurdle6.png"};
    private static Timer mainTimer;
    private static Player character;
    private static movingBackground background;
    private static Button[] buttonList = new Button[12];
    private static JFrame frame;
    private static JPanel panel;
    private static JLabel jlabel;
    private static JLabel timerLabel;
    private static Rectangle rectangle;
    private static Obstacle obstacle;
    private static String[] buttonLabels = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "GO", "Delete"};

    static class MutableInt {
        int value;
        public MutableInt(int val) {
            this.value = val;
        }
    }

    public static void setMainTimer(int seconds) {
        //timerLabel = new JLabel(new String(Rectangle.array)); 
        timerLabel = new JLabel(String.format("Time: %d seconds", seconds));
        MutableInt newSeconds = new MutableInt(seconds);
        timerLabel.setBounds(0, 0, 150, 40);
        timerLabel.setFont(new Font("Serif", Font.BOLD, 15));
        timerLabel.setForeground(Color.WHITE);
        mainTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newSeconds.value--;
                timerLabel.setText(String.format("Time: %d seconds", newSeconds.value));
            }
        });
        mainTimer.start();
        begin();
    }

    public static void begin(){
        if (mainTimer != null && mainTimer.isRunning()) {
            panel.remove(rectangle);
            panel.remove(jlabel);
            for (int i = 0; i < buttonLabels.length; i++) {
                panel.remove(buttonList[i]);
            }
            panel.add(timerLabel);
            panel.add(character);
            panel.add(obstacle);
            panel.add(background);
            panel.revalidate(); // Refreshes the panel layout
            panel.repaint();
            character.unpause();
            panel.requestFocusInWindow();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Timer App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(Color.PINK);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };

            panel.setLayout(null); // Use null layout to manually set component bounds
            frame.add(panel);

            rectangle = new Rectangle();
            rectangle.setBounds(260, 80, 150, 40);
            panel.add(rectangle);
            jlabel = new JLabel("<html><h1>Set Timer</h1></html>");
            jlabel.setBounds(275, 25, 150, 40);
            jlabel.setForeground(Color.WHITE);
            
            for (int i = 0; i < buttonLabels.length; i++) {
                buttonList[i] = new Button(buttonLabels[i]);
                buttonList[i].setBackground(Color.GREEN);
                buttonList[i].setForeground(Color.GREEN);
                Font customFont = new Font("Serif", Font.BOLD, 12);
                buttonList[i].setFont(customFont);
                int j = i / 3;
                buttonList[i].setBounds(235 + (i % 3) * 70, 150 + j * 50, 50, 40);
                int index = i; // Required for lambda expression
                buttonList[i].addActionListener(e -> {
                    if (index == 10) {
                        rectangle.go();
                    } else if (index == 11) {
                        rectangle.delete();
                    } else {
                        rectangle.setTime(buttonLabels[index].charAt(0));
                    }
                });
            panel.add(buttonList[i]);
            }
            panel.add(jlabel);

            background = new movingBackground("/Users/laurencronin/Downloads/jpgs/Parallax_No_Car.gif");
            background.setBounds(0, 0, 670, 400);

            character = new Player(filepaths, 626);
            character.setBounds(10, 30, 670, 400);

            obstacle = new Obstacle(hurdle_file, 5);
            obstacle.setBounds(555, 130, 700, 500);
            character.setObstacle(obstacle);

            panel.setFocusable(true); // Ensure that the panel can receive keyboard focus
            panel.requestFocusInWindow(); // Request focus for the panel

            panel.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        character.jump();
                    }
                }
            });

            panel.grabFocus();

            frame.add(panel);
            frame.setSize(670, 400);
            frame.setVisible(true);

        });
    }

    public static void checkCollision() {
        System.out.println(String.format("Character: %d, %d, Hurdle: %d, %d", character.getX(), character.getY(), (int)(obstacle.getX() / 1.8), obstacle.getY()));
        if (Math.abs(character.getX() - obstacle.getX()) < 15 && Math.abs(character.getY() - obstacle.getY()) <= 30){
            character.pause();
            background.pause();
            obstacle.fallover();
        }
    }

    public static void unpause() {
        character.unpause();
        background.unpause();
        obstacle.stopFallover();
    }
}
