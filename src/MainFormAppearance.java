import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class MainFormAppearance {
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    JFrame frame;
    Font myFont = new Font("Font", Font.BOLD, 80);
    BufferedImage currentImage;
    BufferedImage procImage;
    JPanel imagesPanel;
    JComboBox comboOfMethods;
    ImagesContainer imgCont;

    public void redrawImages(BufferedImage originalImg, BufferedImage processedImage) {
        JLabel imageLabel = new JLabel(new ImageIcon(originalImg));
        JLabel imageLabelProcessed = new JLabel(new ImageIcon(processedImage));
        imagesPanel.removeAll();
        imagesPanel.add(imageLabel);
        imagesPanel.add(imageLabelProcessed);
        imagesPanel.updateUI();
    }


    public JPanel createContentPane(JFrame frame) {
        this.frame = frame;
        JPanel totalGUI = new JPanel();
        totalGUI.setLayout(new BorderLayout());
        //------------------------------------------------
        imgCont = new ImagesContainer();
        currentImage = imgCont.getImage(0);
        procImage = imgCont.getImage(0);
        //------------------------
        JButton divideImageIntoSeg = new JButton("Do segmentation");
        divideImageIntoSeg.setFont(myFont);
        divideImageIntoSeg.setBackground(Color.ORANGE);
        divideImageIntoSeg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 String item = (String) comboOfMethods.getItemAt(comboOfMethods.getSelectedIndex());
                 if(item.equals("Watershed")) {
                     procImage = waterShedAlg(currentImage);
                     redrawImages(currentImage, procImage);
                 }
                 else if(item.equals("MeanShift")) {
                     procImage = meanShiftAlg(currentImage);
                     redrawImages(currentImage, procImage);
                 }
            }
        });
        totalGUI.add(divideImageIntoSeg, BorderLayout.SOUTH);
        //------------------------
        String[] images = {
                "Mountains",
                "Moon",
                "Rainbow Cat",
                "Circle",
                "Green mountains",
                "Anakin Skywalker",
                "House",
                "Game of Thrones",
                "Intel",
                "Wild west",
                "City"
        };

        String[] methods = {
                "Watershed",
                "MeanShift"
        };

        JPanel comboBoxPanel = new JPanel(new GridLayout(1,2));
        JComboBox comboOfImages = new JComboBox(images);
        comboOfImages.setFont(myFont);
        comboOfImages.setBackground(Color.ORANGE);
        comboOfImages.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JComboBox box = (JComboBox)e.getSource();
                    String item = (String)box.getSelectedItem();
                    if(item.equals("Mountains")) {
                        currentImage = imgCont.getImage(0);
                        procImage = imgCont.getImage(0);
                    } else if(item.equals("Moon")) {
                        currentImage = imgCont.getImage(1);
                        procImage = imgCont.getImage(1);
                    } else if(item.equals("Rainbow Cat")) {
                        currentImage = imgCont.getImage(2);
                        procImage = imgCont.getImage(2);
                    } else if(item.equals("Circle")) {
                        currentImage = imgCont.getImage(3);
                        procImage = imgCont.getImage(3);
                    } else if(item.equals("Green mountains")) {
                        currentImage = imgCont.getImage(4);
                        procImage = imgCont.getImage(4);
                    } else if(item.equals("Anakin Skywalker")) {
                        currentImage = imgCont.getImage(5);
                        procImage = imgCont.getImage(5);
                    } else if(item.equals("House")) {
                        currentImage = imgCont.getImage(6);
                        procImage = imgCont.getImage(6);
                    } else if(item.equals("Game of Thrones")) {
                        currentImage = imgCont.getImage(7);
                        procImage = imgCont.getImage(7);
                    } else if(item.equals("Intel")) {
                        currentImage = imgCont.getImage(8);
                        procImage = imgCont.getImage(8);
                    } else if(item.equals("Wild west")) {
                        currentImage = imgCont.getImage(9);
                        procImage = imgCont.getImage(9);
                    } else if(item.equals("City")) {
                        currentImage = imgCont.getImage(10);
                        procImage = imgCont.getImage(10);
                    }
                    redrawImages(currentImage, procImage);
                }
        });

        comboBoxPanel.add(comboOfImages);
        comboOfMethods = new JComboBox(methods);
        comboOfMethods.setBackground(Color.ORANGE);
        comboOfMethods.setFont(myFont);
        comboBoxPanel.add(comboOfMethods);
        totalGUI.add(comboBoxPanel, BorderLayout.NORTH);
        //------------------------
        imagesPanel = new JPanel(new GridLayout(1,2));
        JLabel imageLabel = new JLabel(new ImageIcon(currentImage));
        JLabel imageLabelProcessed = new JLabel(new ImageIcon(procImage));
        imagesPanel.add(imageLabel);
        imagesPanel.add(imageLabelProcessed);
        totalGUI.add(imagesPanel, BorderLayout.CENTER);
        //------------------------
        return totalGUI;
    }

    public BufferedImage waterShedAlg(BufferedImage originalImage) {
        byte[] pixels = ((DataBufferByte) originalImage.getRaster().getDataBuffer()).getData();
        Mat im = new Mat(originalImage.getHeight(), originalImage.getWidth(), CvType.CV_8UC3);
        Mat imFed = new Mat(originalImage.getHeight(), originalImage.getWidth(), CvType.CV_8UC3);
        im.put(0,0, pixels);
        //make image gray
        Imgproc.cvtColor(im, imFed, Imgproc.COLOR_RGB2GRAY);

        Imgproc.threshold(imFed, imFed, 100, 255,
                Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);

        //Noise removal
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3,3));  //19,19
        Mat ret = new Mat(im.size(),CvType.CV_8U);
        Imgproc.morphologyEx(imFed, ret, Imgproc.MORPH_OPEN, kernel);

        //Sure background area
        Mat sure_bg = new Mat(im.size(),CvType.CV_8U);
        Imgproc.dilate(ret, sure_bg, new Mat(), new org.opencv.core.Point(-1,-1), 3);
        Imgproc.threshold(sure_bg,sure_bg,1,
                128,Imgproc.THRESH_BINARY_INV);

        Mat sure_fg = new Mat(im.size(), CvType.CV_8U);
        Imgproc.erode(imFed, sure_fg, new Mat(),new Point(-1,-1),2);

        Mat markers = new Mat(im.size(),CvType.CV_8U, new Scalar(0));
        Core.add(sure_fg, sure_bg, markers);

        markers.convertTo(markers, CvType.CV_32SC1);

        Imgproc.watershed(im, markers);
        Core.convertScaleAbs(markers, markers);

        return imgCont.Mat2BufferedImage(markers);
    }

    public BufferedImage meanShiftAlg(BufferedImage originalImage) {
        byte[] pixels = ((DataBufferByte) originalImage.getRaster().getDataBuffer()).getData();
        Mat im = new Mat(originalImage.getHeight(), originalImage.getWidth(), CvType.CV_8UC3);
        Mat imFed = new Mat(originalImage.getHeight(), originalImage.getWidth(), CvType.CV_8UC3);
        im.put(0,0, pixels);

        TermCriteria tm = new TermCriteria(TermCriteria.MAX_ITER|TermCriteria.EPS,50,0.001);
        //Imgproc.pyrMeanShiftFiltering(im, imFed, 10.0, 30.0, 1, tm);
        Imgproc.pyrMeanShiftFiltering(im, imFed, 10.0, 30.0);
        return imgCont.Mat2BufferedImage(imFed);
    }
}
