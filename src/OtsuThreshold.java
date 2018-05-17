import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class OtsuThreshold {
    private ImagesContainer imgCont = new ImagesContainer();

    OtsuThreshold() {

    }

    public BufferedImage otsuThreshold(BufferedImage img) {
        byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        Mat im = imgCont.bufferedImage2Mat(img, 3);
        Mat imFed = imgCont.bufferedImage2Mat(img, 3);
        im.put(0,0, pixels);

        Imgproc.cvtColor(im, imFed, Imgproc.COLOR_RGB2GRAY);
        Imgproc.threshold(imFed, imFed, 127, 255, Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);
        return imgCont.Mat2BufferedImage(imFed);
    }
}
