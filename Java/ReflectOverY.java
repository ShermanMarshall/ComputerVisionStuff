import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ReflectOverY extends JFrame {
    BufferedImage image;
    
    public ReflectOverY() {
        super("Reflect Over Y-axis");
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
        int h = image.getHeight(), ymax = h/2;
        
        for (int x = 0; x < image.getWidth(); ++x)
            for (int y = 0; y < ymax; ++y) {
                int value = image.getRGB(x, y);
                image.setRGB(x, y, image.getRGB(x, h-y-1));
                image.setRGB(x, h-y-1, value);
            }
        add(new JLabel(new ImageIcon(image)));
        setVisible(true);
    }
    
    public static void main (String[] args) { new ReflectOverY(); }
}
