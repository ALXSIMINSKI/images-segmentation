import javax.swing.*;
import java.awt.*;


public class StartForm {
    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("ImageSegmentation");

        MainFormAppearance demo = new MainFormAppearance();
        frame.setContentPane(demo.createContentPane(frame));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
}