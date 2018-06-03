import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.image.Kernel;
import java.awt.image.ConvolveOp;
import java.awt.image.WritableRaster;

public class LaplacianDetection extends JFrame {
    ImageIcon icon;
    JLabel display;
    BufferedImage image, newImg;
    ConvolveOp convolution;
    Kernel kernel;

    //Ideal Laplacian approximation
    float[] data = {	
	-1, -1, -1,
	-1, 8, -1,
	-1, -1, -1
    };

    public LaplacianDetection() throws IOException {
        super("LaplacianDetection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	File[] files = new File("GrayImages").listFiles();
	File f;
	do {
		f = files[(int) (Math.random() * files.length)];
	} while (f.getName().charAt(0) == '.');
	image = ImageIO.read(f);
	setBounds(100, 0, image.getWidth(), image.getHeight());
	newImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
/*
	float[] convolutionKernel = new float[9];
	for (int x = 0; x < convolutionKernel.length; x++)
		convolutionKernel[x] = 0.11f;

	kernel = new Kernel(3, 3, convolutionKernel);
	ConvolveOp convolution = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
	newImg = convolution.filter(image, newImg);
	WritableRaster old = newImg.getRaster();
	image.getRaster().setRect(old);
*/
	kernel = new Kernel(3, 3, data);
	convolution = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
	newImg = convolution.filter(image, newImg);
	
	WritableRaster raster = newImg.getRaster();
	for (int y = 0, samp; y < newImg.getHeight(); y++)
		for (int x = 0; x < newImg.getWidth(); x++) {
			samp =  (raster.getSample(x, y, 0) > 50) ? 255 : 0;
			raster.setSample(x, y, 0, samp);
		}

        add(new JLabel(new ImageIcon(newImg)));
        setVisible(true);
    }

    public static void main (String[] args) throws IOException {
	new LaplacianDetection();
    }
}
