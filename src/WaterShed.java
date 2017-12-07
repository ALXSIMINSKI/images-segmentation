import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class WaterShed {

    ImagesContainer imgCont = new ImagesContainer();

    WaterShed(){

    }

    public BufferedImage waterShedSegmentation(BufferedImage img) {
        byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        Mat im = imgCont.bufferedImage2Mat(img, 3);
        Mat imFed = imgCont.bufferedImage2Mat(img, 3);
        im.put(0,0, pixels);
        //make image gray
        Imgproc.cvtColor(im, imFed, Imgproc.COLOR_RGB2GRAY);

        Imgproc.threshold(imFed, imFed, 100, 255,
                Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);

        //Noise removal
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3,3));
        //19,19  Imgproc.MORPH_ELLIPSE - сегментирует шум в простые геометрические фигуры
        Mat ret = new Mat(im.size(), CvType.CV_8U);
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
}
