import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.image.Kernel;
import java.awt.image.ConvolveOp;

public class Convolution extends JFrame {
    private ImageIcon icon;
    private JLabel display;
    private BufferedImage image, newImg;
    private Kernel kernel;
    private float[] data = new float[9];

    public Convolution(String fname) throws IOException {
        super("Convolution");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	File[] files = new File("Images").listFiles();
	File f;
	if (fname == null) {
		do {
			f = files[(int) (Math.random() * files.length)];
		} while (f.getName().charAt(0) == '.');
	} else {
		f = new File(fname);
	}

	image = ImageIO.read(f);
	setBounds(100, 0, image.getWidth(), image.getHeight());
	newImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

	//This is temporary; however, performing convolution outside of the awt.image API 
	//has proven to be an enormous nuisance
	for (int x = 0; x < data.length; x++)
		data[x] = 0.125f;

	data[4] = 0.0f;
	data[4] = 1.0f;

	kernel = new Kernel(3, 3, data);
	ConvolveOp convolution = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
	newImg = convolution.filter(image, newImg);
	//The preceding approach does properly convolve the image, however.
	try {
		ImageIO.write(newImg, "jpg", new File("lighter.jpg"));
	} catch (IOException ioe) {
		System.out.println(ioe);
	}
        add(new JLabel(new ImageIcon(newImg)));
        setVisible(true);
    }

    public static void main (String[] args) throws IOException {
	new Convolution(args.length > 0 ? args[0] : null);
    }
}
