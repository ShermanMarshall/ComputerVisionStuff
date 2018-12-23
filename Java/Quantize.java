import java.awt.image.BufferedImage; 
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.Scanner;

public class Quantize extends JFrame {
    ImageIcon icon;
    JLabel display;
    BufferedImage image;
    
    public Quantize () throws IOException {
        super("Quantize");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	Scanner input = new Scanner(System.in);
	File[] files = new File("Images").listFiles();
	File file;
	do {
		file = files[(int) (Math.random() * files.length)];
	} while (file.getName().charAt(0) == '.');

	System.out.println("Input the number of bits used to represent each color component of each pixel in the image (ideal is 4): ");
	    
	int quanta = input.nextInt();
	if ((quanta < 0) || (quanta > 7))
		throw new NumberFormatException("Number of bits must be 0 < x < 8");

	image = ImageIO.read(file);
	float scale = 255.0f / (255 >> quanta);
	System.out.println("Scale: " + scale);

	for (int y = 0; y < image.getHeight(); y++) {
		for (int x = 0; x < image.getWidth(); x++) {
			int color = image.getRGB(x, y), tmp = 0;
			for (int z = 0, q = 0; z < 3; z++) {
				q = Math.round(scale * (((byte) (color & 0xFF)) >> quanta)) << (8 * z);
				tmp += q;
				color = color >>> 8;
			}
			image.setRGB(x, y, tmp);
		}
	}
	
	icon = new ImageIcon(image);
	display = new JLabel(icon);
	setBounds(100, 100, image.getWidth(), image.getHeight());
        add(display);
        setVisible(true);
    }
    
    public static void main (String[] args) throws IOException {
	try {
        	new Quantize();
	} catch (IOException | NumberFormatException e) { 
		System.out.println("Error: " + e.getMessage()); 
	}
    }
}
