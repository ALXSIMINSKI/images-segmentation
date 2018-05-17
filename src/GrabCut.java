import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class GrabCut {

    private ImagesContainer imgCont = new ImagesContainer();

    GrabCut() {

    }

    public BufferedImage grabCutSegmentation(BufferedImage img) {
        byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        Mat im = imgCont.bufferedImage2Mat(img, 3);
        im.put(0,0, pixels);
        Mat mask = new Mat();
        Mat bgModel = new Mat();
        Mat fgModel = new Mat();
        Rect rect = new Rect(1, 1,im.width()/3,im.height()/3);
        Mat source = new Mat(1, 1, CvType.CV_8U, new Scalar(3));
        Imgproc.grabCut(im, mask, rect, bgModel, fgModel, 1, 0);
        Core.compare(mask, source, mask, Core.CMP_EQ);
        Mat fg = new Mat(im.size(), CvType.CV_8UC1, new Scalar(0, 0, 0));
        im.copyTo(fg, mask);
        return imgCont.Mat2BufferedImage(fg);
    }
}
