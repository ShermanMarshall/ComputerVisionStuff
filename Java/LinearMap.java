import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class LinearMap extends JFrame {
	BufferedImage bi;
	float gain; int bias;
	
	LinearMap (float gain, int bias) {
		super("Linear Map");
		this.gain = gain;
		this.bias = bias;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			File[] files = new File("Images").listFiles();
			File f;
			do {
				f = files[(int) (Math.random() * files.length)];
			} while (f.getName().charAt(0) == '.');
			bi = ImageIO.read(f);
		} catch (IOException e) { System.out.println(e.getMessage()); }
		setBounds(0, 0, bi.getWidth(), bi.getHeight());
		for (int y = 0; y < bi.getHeight(); y++) 
			for (int x = 0; x < bi.getWidth(); x++) {
				int color = bi.getRGB(x, y), tmp = 0;
				byte component;
				for (int z = 0, q = 0, pow = 0; z < 3; z++) {
					component = (byte) (color & 0xFF);
					q = (component >= 0) ? component : (256 - (component * -1));
					q = (int) ((gain * q) + bias);
					q = (q > 255) ? 255 : q;
					tmp += q << (8 * z);
					color = color >>> 8;
				}
				bi.setRGB(x, y, tmp);
			}
		ImageIcon icon = new ImageIcon(bi);
		add(new JLabel(icon));
		setVisible(true);
	}

	public static void main (String[] args) {
		if (args.length < 2) {
			System.out.println("Arguments: float(gain), and int(bias) required");
			return;
		} else {
			try {
				new LinearMap(Float.parseFloat(args[0]), Integer.parseInt(args[1]));
			} catch (NumberFormatException nfe) { System.out.println(nfe.getMessage()); }
		}
	}
}
