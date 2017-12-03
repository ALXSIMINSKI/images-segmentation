import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

public class ImagesContainer {

    private List<BufferedImage> images;

    ImagesContainer() {
        images = new ArrayList<>();
        try {
            images.add(ImageIO.read(this.
                    getClass().getResource("images/pic.jpg")));
            images.add(ImageIO.read(this.
                    getClass().getResource("images/pic1.jpg")));
            images.add(ImageIO.read(this.
                    getClass().getResource("images/pic2.jpg")));
            images.add(ImageIO.read(this.
                    getClass().getResource("images/pic3.jpg")));
            images.add(ImageIO.read(this.
                    getClass().getResource("images/pic4.jpg")));
            images.add(ImageIO.read(this.
                    getClass().getResource("images/pic5.jpg")));
            images.add(ImageIO.read(this.
                    getClass().getResource("images/pic6.jpg")));
            images.add(ImageIO.read(this.
                    getClass().getResource("images/pic7.jpg")));
            images.add(ImageIO.read(this.
                    getClass().getResource("images/pic8.jpg")));
            images.add(ImageIO.read(this.
                    getClass().getResource("images/pic9.jpg")));
            images.add(ImageIO.read(this.
                    getClass().getResource("images/pic10.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    BufferedImage getImage(int number) {
        if(number >= 0 && number < 11)
            return images.get(number);
        else
            return images.get(0);
    }

    Mat bufferedImage2Mat(BufferedImage image, int chanelNumber) {
        byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        Mat im = null;
        if(chanelNumber == 3) {
            im = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        } else
            if(chanelNumber == 2) {
                im = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC2);
            } else
                if(chanelNumber == 1) {
                    im = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC1);
                }
        im.put(0,0, pixels);
        return im;
    }

    BufferedImage Mat2BufferedImage(Mat imageInMat) {
        BufferedImage out = null;
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".jpg", imageInMat, mob);
        try {
            out = ImageIO.read(new ByteArrayInputStream(mob.toArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }
}
