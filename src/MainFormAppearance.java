import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class MainFormAppearance {
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    private JFrame frame;
    private Font myFont = new Font("Font", Font.BOLD, 80);
    private BufferedImage currentImage;
    private BufferedImage procImage;
    private JPanel imagesPanel;
    private JComboBox comboOfMethods;
    private ImagesContainer imgCont;

    private void redrawImages(BufferedImage originalImg, BufferedImage processedImage) {
        JLabel imageLabel = new JLabel(new ImageIcon(originalImg));
        imagesPanel.removeAll();
        imagesPanel.add(imageLabel);
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
        divideImageIntoSeg.addActionListener(e -> {
             String item = (String) comboOfMethods.getItemAt(comboOfMethods.getSelectedIndex());
             if(item.equals("Watershed")) {
                 procImage = new WaterShed().waterShedSegmentation(currentImage);
                 createFrame(procImage);
             }
             else if(item.equals("MeanShift")) {
                 procImage = new MeanShift().meanShiftSegmentation(currentImage);
                 createFrame(procImage);
             }
             else if(item.equals("FloodFill")) {
                 procImage = new FloodFill().meanShiftSegmentation(currentImage);
                 createFrame(procImage);
             }
             else if(item.equals("Canny Edge Detector")) {
                 procImage = new CannyEdgeDetector().cannyEdgeDetectorSegmentation(currentImage);
                 createFrame(procImage);
             }
             else if(item.equals("KMean")) {
                 procImage = kMean(currentImage);
                 createFrame(procImage);
             }
             else if(item.equals("GrabCut")) {
                 procImage = new GrabCut().grabCutSegmentation(currentImage);
                 createFrame(procImage);
             }
             else if(item.equals("Threshold")) {
                 procImage = new Threshold().thresholdSegmentation(currentImage);
                 createFrame(procImage);
             }
             else if(item.equals("BradlyThreshold")) {
                 procImage = new BradleyThreshold().bradleyThresholdBinary(currentImage);
                 createFrame(procImage);
             }
             else if(item.equals("OtsuThreshold")) {
                 procImage = new OtsuThreshold().otsuThreshold(currentImage);
                 createFrame(procImage);
             }
             else if(item.equals("SplitAndMerge")) {
                 ImageMatrix imageMatrix = new ImageMatrix(procImage);
                 SplitAndMerge splitAndMergeAlgorithm = new SplitAndMerge();
                 FeatureMatrix featureMatrix = new FeatureMatrix(imageMatrix.getWidth(), imageMatrix.getHeight(), 3);
                 for (int i=0; i<imageMatrix.getHeight(); i++) {
                     for (int j=0; j<imageMatrix.getWidth(); j++) {
                         Color c = new Color(imageMatrix.getPixels()[i][j]);
                         featureMatrix.getData()[i][j][0] = c.getRed();
                         featureMatrix.getData()[i][j][1] = c.getGreen();
                         featureMatrix.getData()[i][j][2] = c.getBlue();
                     }
                 }
                 splitAndMergeAlgorithm.setImage(featureMatrix);
                 splitAndMergeAlgorithm.run();
                 createFrame(featureMatrix.getImageMatrix().getBufferedImage());
             }
        });
        totalGUI.add(divideImageIntoSeg, BorderLayout.SOUTH);
        //------------------------
        String[] images = new String[20];
        for(int i = 0; i < 20; ++i) {
            images[i] = Integer.toString(i);
        }

        String[] methods = {
                "Watershed",
                "MeanShift",
                "FloodFill",
                "Canny Edge Detector",
                "KMean",
                "GrabCut",
                "Threshold",
                "BradlyThreshold",
                "OtsuThreshold",
                "SplitAndMerge"
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
                    currentImage = imgCont.getImage(Integer.parseInt(item));
                    procImage = imgCont.getImage(Integer.parseInt(item));
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
        imagesPanel.setBackground(Color.DARK_GRAY);
        JLabel imageLabel = new JLabel(new ImageIcon(currentImage));
        imagesPanel.add(imageLabel);
        totalGUI.add(imagesPanel, BorderLayout.CENTER);
        //------------------------
        return totalGUI;
    }

    public static void createFrame(BufferedImage img) {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                JFrame frame = new JFrame("IMAGE");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JLabel imageLabel = new JLabel(new ImageIcon(img));
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setOpaque(true);
                panel.add(imageLabel);
                frame.getContentPane().add(BorderLayout.CENTER, panel);
                frame.pack();
                frame.setLocationByPlatform(true);
                frame.setVisible(true);
                frame.setResizable(false);
            }
        });
    }

    public BufferedImage kMean(BufferedImage originalImage) {
        int k = 0;
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
            try {
                if(Integer.parseInt(kFld.getText()) >=0) {
                    k = Integer.parseInt(kFld.getText());
                } else {
                    k = 8;
                }
            } catch (NumberFormatException e) {
                  e.printStackTrace();
                  k = 8;
            }
        }
        KMeans km = new KMeans();
        return km.calculate(originalImage, k ,1);
    }
}
