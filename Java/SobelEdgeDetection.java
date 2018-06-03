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

public class SobelEdgeDetection extends JFrame {
    ImageIcon icon;
    JLabel display;
    BufferedImage image, gx, gy, edges;
    Kernel xKernel, yKernel;
    float[] hx = { 	-1, 0, 1,
			-2, 0, 2,
			-1, 0, 1
    		};
    float[] hy = {	-1, -2, -1,
			 0, 0, 0,
			1, 2, 1
		};

    public SobelEdgeDetection() throws IOException {
        super("SobelEdgeDetection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	File[] files = new File("GrayImages").listFiles();
	File f;
	do {
		f = files[(int) (Math.random() * files.length)];
	} while (f.getName().charAt(0) == '.');
	image = ImageIO.read(f);
	setBounds(100, 0, image.getWidth(), image.getHeight());
	gx = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
	gy = new BufferedImage(gx.getWidth(), gx.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

	xKernel = new Kernel(3, 3, hx);
	yKernel = new Kernel(3, 3, hy);

	ConvolveOp convolution = new ConvolveOp(xKernel, ConvolveOp.EDGE_NO_OP, null);
	gx = convolution.filter(image, gx);
	convolution = new ConvolveOp(yKernel, ConvolveOp.EDGE_NO_OP, null);
	gy = convolution.filter(image, gy);
	
	edges = new BufferedImage(gy.getWidth(), gy.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
	WritableRaster xRaster = gx.getRaster(), yRaster = gy.getRaster(), edgeRaster = edges.getRaster();
	for (int y = 0, xSample, ySample; y < edges.getHeight(); y++)
		for (int x = 0; x < edges.getWidth(); x++) {
			xSample = (xRaster.getSample(x, y, 0) > 50) ? 255 : 0;
			ySample = (yRaster.getSample(x, y, 0) > 50) ? 255 : 0;
			edgeRaster.setSample(x, y, 0, (int) Math.sqrt(
				Math.pow(xSample, 2) +
				Math.pow(ySample, 2))
			);
		}

        add(new JLabel(new ImageIcon(edges)));
        setVisible(true);
    }

    public static void main (String[] args) throws IOException {
	new SobelEdgeDetection();
    }
}
