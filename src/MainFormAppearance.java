
import org.opencv.core.Core;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
                 else if(item.equals("FloodFill")) {
                     procImage = floodFillAlg(currentImage);
                     redrawImages(currentImage, procImage);
                 }
                 else if(item.equals("Canny edge detector")) {
                     procImage = edgeDetector(currentImage);
                     redrawImages(currentImage, procImage);
                 }
                 else if(item.equals("KMean")) {
                     procImage = kMean(currentImage);
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
                "Rounds"
        };

        String[] methods = {
                "Watershed",
                "MeanShift",
                "FloodFill",
                "Canny edge detector",
                "KMean"
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
                    } else if(item.equals("Rounds")) {
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
        Mat im = imgCont.bufferedImage2Mat(originalImage, 3);
        Mat imFed = imgCont.bufferedImage2Mat(originalImage, 3);
        im.put(0,0, pixels);
        //make image gray
        Imgproc.cvtColor(im, imFed, Imgproc.COLOR_RGB2GRAY);

        Imgproc.threshold(imFed, imFed, 100, 255,
                Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);

        //Noise removal
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3,3));
        //19,19  Imgproc.MORPH_ELLIPSE - сегментирует шум в простые геометрические фигуры
        Mat ret = new Mat(im.size(),CvType.CV_8U);
        Imgproc.morphologyEx(imFed, ret, Imgproc.MORPH_OPEN, kernel);

        //Sure background area
        Mat sure_bg = new Mat(im.size(),CvType.CV_8U);
        Imgproc.dilate(ret, sure_bg, new Mat(), new org.opencv.core.Point(-1,-1), 3);
        Imgproc.threshold(sure_bg, sure_bg,1,
                128,Imgproc.THRESH_BINARY_INV);

        Mat sure_fg = new Mat(im.size(), CvType.CV_8U);
        Imgproc.erode(ret, sure_fg, new Mat(),new Point(-1,-1),2);

        Mat markers = new Mat(im.size(),CvType.CV_8U, new Scalar(0));
        Core.add(sure_fg, sure_bg, markers);

        markers.convertTo(markers, CvType.CV_32SC1);

        Imgproc.watershed(im, markers);
        Core.convertScaleAbs(markers, markers);

        return imgCont.Mat2BufferedImage(markers);
    }

    public BufferedImage meanShiftAlg(BufferedImage originalImage) {
        byte[] pixels = ((DataBufferByte) originalImage.getRaster().getDataBuffer()).getData();
        Mat im = imgCont.bufferedImage2Mat(originalImage, 3);
        Mat imFed = imgCont.bufferedImage2Mat(originalImage, 3);
        im.put(0,0, pixels);

        TermCriteria tm = new TermCriteria(TermCriteria.MAX_ITER|TermCriteria.EPS,50,0.001);
        //Imgproc.pyrMeanShiftFiltering(im, imFed, 10.0, 30.0, 1, tm);
        Imgproc.pyrMeanShiftFiltering(im, imFed, 10.0, 30.0); //sp - радиус окна расстояния, sr - радиус окна цвета
        return imgCont.Mat2BufferedImage(imFed);
    }
    static int x =100;
    static int y = 100;
    public BufferedImage floodFillAlg(BufferedImage originalImage) {
        Mat im = imgCont.bufferedImage2Mat(originalImage, 3);
        Mat mask = new Mat(originalImage.getHeight() + 2, originalImage.getWidth() + 2, CvType.CV_8UC1);
        //------------ DIALOG MESSAGE ------------
        JTextField xFld = new JTextField();
        xFld.setFont(myFont);
        JTextField yFld = new JTextField();
        yFld.setFont(myFont);
        JLabel xLabel = new JLabel("X");
        xLabel.setFont(myFont);
        JLabel yLabel = new JLabel("Y");
        yLabel.setFont(myFont);
        final JComponent[] inputs = new JComponent[] {
                xLabel,
                xFld,
                yLabel,
                yFld
        };
        int result = JOptionPane.showConfirmDialog(null,
                inputs, "CHOOSE POINT", JOptionPane.PLAIN_MESSAGE);
        if(result == JOptionPane.OK_OPTION) {
            if(Integer.parseInt(xFld.getText()) >=0 &&
                    Integer.parseInt(yFld.getText()) >=0) {
                try {
                    x = Integer.parseInt(xFld.getText());
                    y = Integer.parseInt(yFld.getText());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    x = 100;
                    y = 100;
                }
            } else {
                x = 100;
                y = 100;
            }

        }
        //------------ DIALOG MESSAGE ------------
        Point seedPoint = new Point(x,y);
        final int NULL_RANGE = 0;
        final int FIXED_RANGE = 1;
        boolean masked = true;
        int range = FIXED_RANGE;
        Random random = new Random();
        int connectivity = 4;
        int newMaskVal = 255;
        int lowerDiff = 20;
        int upperDiff = 20;
        int b = random.nextInt(256);
        int g = random.nextInt(256);
        int r = random.nextInt(256);
        Rect rect = new Rect();
        Scalar newVal = new Scalar(b, g, r);
        Scalar lowerDifference = new Scalar(lowerDiff,lowerDiff,lowerDiff);
        Scalar upperDifference = new Scalar(upperDiff,upperDiff,upperDiff);
        if(range == NULL_RANGE){
            lowerDifference = new Scalar (0,0,0);
            upperDifference = new Scalar (0,0,0);
        }
        int flags = connectivity + (newMaskVal << 8) +
                (range == FIXED_RANGE ? Imgproc.FLOODFILL_FIXED_RANGE : 0);
        int area = 0;
        if(masked){
            area = Imgproc.floodFill(im, mask, seedPoint, newVal, rect,
                    lowerDifference, upperDifference, flags);
        }
        else {
            area = Imgproc.floodFill(im, new Mat(), seedPoint, newVal, rect,
                    lowerDifference, upperDifference, flags);
        }
        return imgCont.Mat2BufferedImage(im);
    }

    public BufferedImage edgeDetector(BufferedImage originalImage) {
        Mat im = imgCont.bufferedImage2Mat(originalImage, 3);

        Mat gray = new Mat();
        Mat draw = new Mat();
        Mat wide = new Mat();

        Imgproc.cvtColor(im, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(gray, wide, 50 ,150, 3 ,false);
        wide.convertTo(draw, CvType.CV_8U);
        return imgCont.Mat2BufferedImage(wide);
    }

    static int k;
    public BufferedImage kMean(BufferedImage originalImage) {
//        Mat im = imgCont.bufferedImage2Mat(originalImage, 3);
//        Mat imFed = new Mat();
//        Imgproc.cvtColor(im, imFed, Imgproc.COLOR_RGBA2RGB,3);
//        Imgproc.cvtColor(im, imFed, Imgproc.COLOR_RGB2HSV,3);
//        ArrayList<Mat> hsv_planes = new ArrayList(3);
//        Core.split(imFed, hsv_planes);
//
//        Mat channel = hsv_planes.get(2);
//        channel = Mat.zeros(imFed.rows(),imFed.cols(),CvType.CV_8UC1);
//        hsv_planes.set(2, channel);
//        Core.merge(hsv_planes, imFed);
//
//        Mat clusteredHSV = new Mat();
//        Mat centers = new Mat();
//        imFed.convertTo(imFed, CvType.CV_32F);
//        TermCriteria criteria = new TermCriteria(TermCriteria.EPS + TermCriteria.MAX_ITER,100,0.1);
//        Core.kmeans(imFed, 2, clusteredHSV, criteria, 10,
//                Core.KMEANS_PP_CENTERS, centers);

        JTextField kFld = new JTextField();
        kFld.setFont(myFont);
        JLabel kLabel = new JLabel("NUMBER OF CLUSTERS");
        kLabel.setFont(myFont);
        final JComponent[] inputs = new JComponent[] {
                kLabel,
                kFld
        };
        int result = JOptionPane.showConfirmDialog(null,
                inputs, "CHOOSE POINT", JOptionPane.PLAIN_MESSAGE);
        if(result == JOptionPane.OK_OPTION) {
            if(Integer.parseInt(kFld.getText()) >=0) {
                try {
                    k = Integer.parseInt(kFld.getText());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    k = 8;
                }
            } else {
                k = 8;
            }

        }
        KMeans km = new KMeans();
        return km.calculate(originalImage, k ,1);
    }
}
