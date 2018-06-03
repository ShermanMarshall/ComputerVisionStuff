import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.Scanner;

public class Enlarge extends JFrame {
    JLabel display;
    BufferedImage original, scaled;
    
    public Enlarge(boolean showScaled, double scale) throws IOException {
        super("Enlarge");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	File[] files = new File("Images").listFiles();
	File f;
	do {
		f = files[(int) (Math.random() * files.length)];
	} while (f.getName().charAt(0) == '.');
	
	original = ImageIO.read(f);
        if (showScaled)
            showScaled(scale);
        else
            showOriginal();
        
        add(display);
        setVisible(true);
    }
    
    public void showOriginal() {
        int w = original.getWidth(), h = original.getHeight(), scale = 1;
        setBounds(100, 0, (int) (w*scale), (int) (h*scale));
        System.out.println("Original Dimensions\nHeight: " + 
		h + "\nWidth: " + w);
        display = new JLabel(new ImageIcon(original));
    }
    
    public void showScaled(double scale) throws IOException {
        int w = original.getWidth(), h = original.getHeight();
        setBounds(100, 0, (int) (w*scale), (int) (h*scale));
        scaled = new BufferedImage((int) (w * scale), (int) (h * scale), 
		BufferedImage.TYPE_3BYTE_BGR);
            for (double y = 0; y < (h * scale); y++)
                for (double x = 0; x < (w * scale); x++)
                    scaled.setRGB((int) x, (int) y, original.getRGB((int) 
			(x/scale), (int) (y/scale))); 
        display = new JLabel(new ImageIcon(scaled));
        System.out.println("Scaled Dimensions (x" + scale + ")\nHeight: " 
		+ (h *scale) + "\nWidth: " + (w*scale));
    }
    
    public static void main (String[] args) throws IOException {
	Scanner s = new Scanner(System.in);
	System.out.print("Input the scale factor for the image: ");
	String in = s.nextLine();
	double val = 0;
	try {
		val = Double.parseDouble(in);
	} catch (NumberFormatException nfe) {
		System.out.println(nfe.getMessage());
		return;
	}
        new Enlarge(true, val);
    }
}
