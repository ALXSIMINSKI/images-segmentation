import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import java.awt.image.BufferedImage;

public class CannyEdgeDetector {

    private ImagesContainer imgCont = new ImagesContainer();

    CannyEdgeDetector() {

    }

    public BufferedImage cannyEdgeDetectorSegmentation(BufferedImage img) {
        Mat im = imgCont.bufferedImage2Mat(img, 3);
        Mat gray = new Mat();
        Mat draw = new Mat();
        Mat wide = new Mat();
        Imgproc.cvtColor(im, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(gray, wide, 50 ,150, 3 ,false);
        wide.convertTo(draw, CvType.CV_8U);
        return imgCont.Mat2BufferedImage(wide);
    }
}
