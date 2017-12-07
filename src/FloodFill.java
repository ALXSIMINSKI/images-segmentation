import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class FloodFill {

    ImagesContainer imgCont = new ImagesContainer();
    private int x = 100;
    private int y = 100;
    Font myFont = new Font("Font", Font.BOLD, 80);

    FloodFill() {

    }

    public BufferedImage meanShiftSegmentation(BufferedImage img) {
        Mat im = imgCont.bufferedImage2Mat(img, 3);
        Mat mask = new Mat(img.getHeight() + 2, img.getWidth() + 2, CvType.CV_8UC1);
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
            try {
                if(Integer.parseInt(xFld.getText()) >=0 &&
                        Integer.parseInt(yFld.getText()) >=0 &&
                         Integer.parseInt(yFld.getText()) < im.width() &&
                          Integer.parseInt(yFld.getText()) < im.height()) {

                    x = Integer.parseInt(xFld.getText());
                    y = Integer.parseInt(yFld.getText());

                } else {
                    x = 100;
                    y = 100;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
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
}
