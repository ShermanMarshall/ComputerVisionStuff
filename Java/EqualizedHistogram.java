import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.image.WritableRaster;

public class EqualizedHistogram extends JFrame {
    ImageIcon icon;
    JLabel display;
    BufferedImage image, newImg;
    int[] colors = new int[256];
    float[] equalized = new float[256];

    public EqualizedHistogram() throws IOException {
        super("Equalized Histogram");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	File file = null;
	File[] files = new File("GrayImages").listFiles();
	do {
		file = files[(int) (Math.random() * files.length)];
	} while (file.getName().charAt(0) == '.');

	image = ImageIO.read(file);
	newImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        setBounds(100, 0, image.getWidth(), image.getHeight());

	WritableRaster raster = image.getRaster();
	for (int y = 0; y < image.getHeight(); y++)
		for (int x = 0, color; x < image.getWidth(); x++) {
			color = raster.getSample(x, y, 0);
			colors[color] += 1;
		}

	float a = 256.0f/((float) image.getWidth() * image.getHeight());
	equalized[0] = (float) (colors[0] * a);
	for (int x = 1; x < equalized.length; x++)
		equalized[x] = equalized[x - 1] + (a * colors[x]);

	for (int y = 0, color; y < image.getHeight(); y++)
		for (int x = 0; x < image.getWidth(); x++) {
			color = raster.getSample(x, y, 0);
			raster.setSample(x, y, 0, (int) equalized[color]);
		}

	newImg.getRaster().setRect(raster);
        add(new JLabel(new ImageIcon(newImg)));
        setVisible(true);
    }
    
    public static void main (String[] args) throws IOException {
	new EqualizedHistogram();
    }
}
