import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class Threshold {

    private ImagesContainer imgCont = new ImagesContainer();
    private int lowerThreshold = 127;
    private int upperThreshold = 255;
    private Font myFont = new Font("Font", Font.BOLD, 80);

    Threshold() {

    }

    public BufferedImage thresholdSegmentation(BufferedImage img) {
        Button button = new Button("CHOOSE MODE");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        JTextField lFld = new JTextField();
        lFld.setFont(myFont);
        JTextField hFld = new JTextField();
        hFld.setFont(myFont);
        JLabel lLabel = new JLabel("LOWER THRESHOLD");
        lLabel.setFont(myFont);
        JLabel hLabel = new JLabel("UPPER THRESHOLD");
        hLabel.setFont(myFont);
        final JComponent[] inputs = new JComponent[] {
                lLabel,
                lFld,
                hLabel,
                hFld
        };
        int result = JOptionPane.showConfirmDialog(null,
                inputs, "CHOOSE LOWER AND UPPER THRESHOLD", JOptionPane.PLAIN_MESSAGE);
        if(result == JOptionPane.OK_OPTION) {
            try {
                if(Integer.parseInt(lFld.getText()) >=0 &&
                   Integer.parseInt(hFld.getText()) >=0 &&
                   Integer.parseInt(hFld.getText()) <= 255 &&
                   Integer.parseInt(lFld.getText()) <= 255) {

                    lowerThreshold = Integer.parseInt(lFld.getText());
                    upperThreshold = Integer.parseInt(hFld.getText());

                } else {
                    lowerThreshold = 127;
                    upperThreshold = 255;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                lowerThreshold = 127;
                upperThreshold = 255;
            }

        }
        byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        Mat im = imgCont.bufferedImage2Mat(img, 3);
        Mat imFed = imgCont.bufferedImage2Mat(img, 3);
        im.put(0,0, pixels);
        Imgproc.threshold(im, imFed, lowerThreshold, upperThreshold, Imgproc.THRESH_BINARY);
        return imgCont.Mat2BufferedImage(imFed);
    }
}
