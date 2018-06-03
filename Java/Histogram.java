import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Histogram extends JFrame {
    ImageIcon icon;
    JLabel display;
    BufferedImage image;
    int[] r = new int[256];
    int[] g = new int[256];
    int[] b = new int[256];

    public Histogram() throws IOException {
        super("Histogram");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	File[] files = new File("Images").listFiles();
	File f;
	do {
		f = files[(int) (Math.random() * files.length)];
	} while (f.getName().charAt(0) == '.');

	image = ImageIO.read(f);
        setBounds(100, 0, image.getWidth(), image.getHeight());

	for (int y = 0, color; y < image.getHeight(); y++)
		for (int x = 0; x < image.getWidth(); x++) {
			color = image.getRGB(x, y);
			r[((color >>> 16) & 0xFF)]++;
			g[((color >>> 8) & 0xFF)]++;
			b[(color & 0xFF)]++;
		}

	for (int x = 0; x < 256; x++) {
		System.out.println(x + "R: " + r[x]);
		System.out.println(x + "G: " + g[x]);
		System.out.println(x + "B: " + b[x]);
	}

        add(new JLabel(new ImageIcon(image)));
        setVisible(true);
    }
    
    public static void main (String[] args) throws IOException {
        new Histogram();
    }
}
