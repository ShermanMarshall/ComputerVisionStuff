import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Enlarge extends JFrame {
    JLabel display;
    BufferedImage original, scaled;
    
    public Enlarge(String filename, int scale) throws IOException {
        super("Enlarge");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	File f = new File(filename);
	
	original = ImageIO.read(f);
        showScaled(scale);
        
        add(display);
        setVisible(true);
    }
    
    public void showScaled(int scale) throws IOException {
        int w = original.getWidth(), h = original.getHeight();
        setBounds(100, 0, w*scale, h*scale);
        scaled = new BufferedImage(w * scale, h * scale, BufferedImage.TYPE_3BYTE_BGR);
            
            for (int y = 0; y < (h * scale); y++)
                for (int x = 0; x < (w * scale); x++)
                    scaled.setRGB(x, y, original.getRGB(x/scale, y/scale));

        display = new JLabel(new ImageIcon(scaled));
        System.out.println("Scaled Dimensions (x" + scale + ")\nHeight: " + (h *scale) + "\nWidth: " + (w*scale));
    }
    
    public static void main (String[] args) throws IOException {
        new Enlarge(args[0], 2);
    }
}
