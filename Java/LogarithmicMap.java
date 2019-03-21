import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class LogarithmicMap extends JFrame {
	private BufferedImage bi, map;
	private int[] table = new int[256];

	LogarithmicMap () {
		super("Logarithmic Map");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		File[] files = new File("Images").listFiles();
		File f;
		try {
			do {
				f = files[(int) (Math.random() * files.length)];
			} while (f.getName().charAt(0) == '.');
			bi = ImageIO.read(f);
		} catch (IOException e) { 
			System.out.println(e.getMessage()); 
		}
		
		map = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		setBounds(0, 0, bi.getWidth(), bi.getHeight());

		initTable();

		for (int y = 0; y < bi.getHeight(); y++) {
			for (int x = 0; x < bi.getWidth(); x++) {
				int color = bi.getRGB(x, y), tmp = 0;
				byte component;
				for (int z = 0, q = 0; z < 3; z++) {
					component = (byte) (color & 0xFF);
					q = (component > -1) ? component : (256 - (component * -1));
					q = table[q];
					q = q << (z * 8);
					tmp += q;
					color = color >>> 8;
				}
				map.setRGB(x, y, tmp);
			}
		}

		ImageIcon icon = new ImageIcon(bi);
		add(new JLabel(icon));
		setVisible(true);
	}
	
	public void initTable() {
		int val = 16;
		for (int x = 1; x < 256; x++)
			table[x] = (int) (val * Math.sqrt(x));
	}

	public static void main (String[] args) {
		new LogarithmicMap();
	}
}
