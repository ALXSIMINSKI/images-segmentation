import javax.swing.*;
import java.awt.*;


public class StartForm {
    public static void main(String[] args) {
        JFrame frame = new JFrame("ImageSegmentation");
        MainFormAppearance demo = new MainFormAppearance();
        frame.setContentPane(demo.createContentPane(frame));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1800,1800);
        frame.setVisible(true);
    }
}