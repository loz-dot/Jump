package timer;

import javax.swing.*;
import java.awt.*;
import java.lang.Character;

public class Rectangle extends JComponent {
    public static char[] array = {'0', '0', ':', '0', '0', ':', '0', '0'};
    private int pressCount = 1;
    private static Button[] buttonList = new Button[12]; // Increased the size to 12

    public Rectangle() {
        setPreferredSize(new Dimension(150, 40)); // Set the preferred size of the rectangle
    }

    public void go() {
        int seconds = 0;
        for (int i = 0; i < array.length; i++) {
            if (i == 2 || i == 5) continue;
            if (i == 0) seconds += Character.getNumericValue(array[i]) * 36000;
            if (i == 1) seconds += Character.getNumericValue(array[i]) * 3600;
            if (i == 3) seconds += Character.getNumericValue(array[i]) * 600;
            if (i == 4) seconds += Character.getNumericValue(array[i]) * 60;
            if (i == 6) seconds += Character.getNumericValue(array[i]) * 10;
            if (i == 7) seconds += Character.getNumericValue(array[i]);
        }
        Main.setMainTimer(seconds);
    }

    public void delete() {
        for (int i = 0; i < array.length; i++) {
            if (i == 2 || i == 5) continue;
            array[i] = '0';
        }
    }

    public void setTime(char x) {
        // Set the time based on user input
        if (pressCount < array.length - 2) {
            int idx = (array.length - pressCount == 2 || array.length - pressCount == 5) ? array.length - (pressCount++) : array.length - pressCount;

            while (idx != array.length - 1) {
                if (idx + 1 == 2 || idx + 1 == 5) {
                    array[idx] = array[idx + 2];
                    idx+=2;
                    continue;
                }
                array[idx] = array[++idx];
            }
            array[idx] = x;
            pressCount++;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN); 
        g.fillRect(0, 0, getWidth(), getHeight()); 
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1); // Draw the outline of the rectangle
        
        Font font = new Font("Serif", Font.BOLD, 20);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(new String(array), 35, getHeight() / 2 + 4); // Draw the text in the center vertically
    }
}