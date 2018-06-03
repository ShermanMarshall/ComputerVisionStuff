import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Shrink extends JFrame {
    ImageIcon icon;
    JLabel display;
    BufferedImage original, scaled;
    
    public Shrink(boolean scaled, int scaleFactor) {
        super("Shrink");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	File[] files = new File("Images").listFiles();
	File f;
	do {
		f = files[(int) (Math.random() * files.length)];
	} while (f.getName().charAt(0) == '.');
	try {
		original = ImageIO.read(f);
	} catch (IOException e) { System.out.println(e.getMessage()); }
        if (scaled) 
            showScaled(scaleFactor);
        else
            showOriginal();
                   
        add(display);
        setVisible(true);
    }
    
    public void showOriginal() {
        display = new JLabel(new ImageIcon(original));
        setBounds(100, 0, original.getWidth(), original.getHeight());
        System.out.println("Original Dimensions\nHeight: " + original.getHeight() + "\nWidth: " + original.getWidth());
    }
    
    public void showScaled(int scale) {
        int w = original.getWidth() / scale, h = original.getHeight() / scale;

        scaled = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
	for (int y = 0; y < h; y++)
		for (int x = 0; x < w; x++)
			scaled.setRGB(x, y, original.getRGB(x*scale, y*scale));
            
            
	display = new JLabel(new ImageIcon(scaled));
	setBounds(100, 0, w, h);
	try {
		ImageIO.write(scaled, "jpg", new File("output.jpg"));
	} catch (IOException ioe) {
		System.out.println("abc");
	}
	System.out.println("Scaled Dimensions\nHeight: " + h + "\nWidth: " + w);
    }
    
    public static void main (String[] args) {
        new Shrink(true, 5);
    }
}
