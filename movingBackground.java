package timer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.util.Iterator;

public class movingBackground extends JComponent implements ActionListener {
    private Image[] imgList;
    private int imgIdx = 0;
    private Timer timer;
    private int speed = 5;
    private int keepSpeed;

    public movingBackground(String imagePath1) {
        get_imgs(imagePath1);
        timer = new Timer(1000/6, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.create();
        g.drawImage(imgList[imgIdx], 0, 0, null);
        g.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(imgList[imgIdx].getWidth(this), imgList[imgIdx].getHeight(this));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        imgIdx++;
        if (imgIdx >= imgList.length) {
            imgIdx = 0;
        }

        repaint();
    }

    public void pause() {
        keepSpeed = speed;
        speed = 0;
    }

    public void unpause() {
        speed = keepSpeed;
    }

    public void get_imgs(String URL) {
        try {
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(new File(URL));
            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(imageInputStream);

            if (imageReaders.hasNext()) {
                ImageReader imageReader = imageReaders.next();
                imageReader.setInput(imageInputStream);

                int numFrames = imageReader.getNumImages(true);
                imgList = new Image[numFrames];

                for (int i = 0; i < numFrames; i++) {
                    imgList[i] = imageReader.read(i);
                }

                imageReader.dispose();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}