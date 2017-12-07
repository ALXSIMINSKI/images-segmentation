import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class MeanShift {

    ImagesContainer imgCont = new ImagesContainer();

    MeanShift() {

    }

    public BufferedImage meanShiftSegmentation(BufferedImage img) {
        byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        Mat im = imgCont.bufferedImage2Mat(img, 3);
        Mat imFed = imgCont.bufferedImage2Mat(img, 3);
        im.put(0,0, pixels);

        TermCriteria tm = new TermCriteria(TermCriteria.MAX_ITER|TermCriteria.EPS,50,0.001);
        //Imgproc.pyrMeanShiftFiltering(im, imFed, 10.0, 30.0, 1, tm);
        Imgproc.pyrMeanShiftFiltering(im, imFed, 10.0, 30.0); //sp - радиус окна расстояния, sr - радиус окна цвета
        return imgCont.Mat2BufferedImage(imFed);
    }
}
