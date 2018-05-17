import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

public class BradleyThreshold {

    private ImagesContainer imgCont = new ImagesContainer();

    BradleyThreshold() {

    }

    public BufferedImage bradleyThresholdBinary(BufferedImage img) {
        byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();

        Mat im = imgCont.bufferedImage2Mat(img, 3);
        Mat imFed = imgCont.bufferedImage2Mat(img, 3);
        im.put(0,0, pixels);
        Imgproc.cvtColor(im, imFed, Imgproc.COLOR_RGB2GRAY);
        img = imgCont.Mat2BufferedImage(imFed);

        pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        byte[] pixelProceed = new byte[pixels.length];
        
        int width = img.getWidth();
        int height = img.getHeight();
        long[] integralImage = new long[width*height];
        long sum = 0;
        int index;
        int count = 0;
        int x1, x2, y1, y2;
        int s = width/8;
        int s2 = s/2;
        float t = 0.15f;

        //рассчитываем интегральное изображение
        for(int i = 0; i < width; i++) {
            sum = 0;
            for (int j = 0; j < height; j++) {
                index = j * width + i;
                sum += pixels[index];
                if(i == 0) {
                    integralImage[index] = sum;
                } else {
                    integralImage[index] = integralImage[index - 1] + sum;
                }
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                index = j * width + i;

                x1 = i - s2;
                x2 = i + s2;
                y1 = j - s2;
                y2 = j + s2;

                if(x1 < 0) x1 = 0;
                if(x2 >= width) x2 = width - 1;
                if(y1 < 0) y1 = 0;
                if(y2 >= height) y2 = height - 1;

                count = (x2 - x1) * (y2 - y1);

                sum = integralImage[y2 * width + x2] - integralImage[y1 * width + x2]
                        - integralImage[y2 * width + x1] + integralImage[y1 * width + x1];

                if((long)(pixels[index] * count) < (long)(sum * (1.0 - t)))
                    pixelProceed[index] = 0;
                else
                    pixelProceed[index] = (byte)255;
            }
        }

        imFed.put(0, 0, pixelProceed);
        return imgCont.Mat2BufferedImage(imFed);
    }
}
