import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

public class ImagesContainer {

    private List<File> images;
    private List<BufferedImage> bufferedImages = new ArrayList<>();

    ImagesContainer() {
        images = new ArrayList<>();
        try {
            images = Files.walk(Paths.get("C:\\Users\\User\\IdeaProjects\\Segmentation_of_Images\\src\\images"))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
            for(File img : images) {
                bufferedImages.add(ImageIO.read(img));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    BufferedImage getImage(int number) {
        if(number >= 0 && number < bufferedImages.size())
            return bufferedImages.get(number);
        else
            return bufferedImages.get(0);
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
