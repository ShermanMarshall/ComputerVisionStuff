import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ReflectOverX extends JFrame {
    BufferedImage image;
    
    public ReflectOverX() {
        super("Reflect Over X-axis");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
	    File[] files = new File("Images").listFiles();
	    File f;
	    do {
		f = files[(int) (Math.random() * files.length)];
	    } while (f.getName().charAt(0) == '.');
            image = ImageIO.read(f);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        setBounds(100, 100, image.getWidth(), image.getHeight());
        int w = image.getWidth(), xmax = w/2;
        
        for (int y = 0; y < image.getHeight(); ++y)
            for (int x = 0; x < xmax; ++x) {
                int value = image.getRGB(x, y);
                image.setRGB(x, y, image.getRGB(w-x-1, y));
                image.setRGB(w-x-1, y, value);
            }
        add(new JLabel(new ImageIcon(image)));
        setVisible(true);
    }
    
    public static void main (String[] args) { new ReflectOverX(); }
}
