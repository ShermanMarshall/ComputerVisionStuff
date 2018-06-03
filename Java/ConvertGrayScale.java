import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.image.WritableRaster;

public class ConvertGrayScale extends JFrame {
	BufferedImage input, output;

	public ConvertGrayScale() {
		super("Convert to Gray Scale");
		File[] files = new File("Images").listFiles();
		File f;
		do {
			f = files[(int) (Math.random() * files.length)];
		} while (f.getName().charAt(0) == '.');

		try { input = ImageIO.read(f);
		} catch (IOException e) { System.out.println("Error: " + e.getMessage()); }

		output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		
		WritableRaster raster = output.getRaster();
		for (int y = 0, color, r, g, b; y < input.getHeight(); y++)
			for (int x = 0; x < input.getWidth(); x++) {
				color = input.getRGB(x, y);
				//RGB to greyscale conversion factors
				//x = 0.299r + 0.587g + 0.114b.
				r = (int) (((color >>> 16) & 0xFF) * 0.299f);
				g = (int) (((color >>> 8) & 0xFF) * 0.587f);
				b = (int) (((color & 0xFF) & 0xFF) * 0.144f);
				color = (r + g + b);
				raster.setSample(x, y, 0, (color > 255) ? 255 : color);
			}
		output.setData(raster);

		setBounds(100, 0, input.getWidth(), input.getHeight());
		add(new JLabel(new ImageIcon(output)));
		setVisible(true);
		try { ImageIO.write(output, "jpg", new File("grayimg.jpg"));
		} catch (IOException e) { System.out.println("Write error: " + e.getMessage()); }
	}
	public static void main (String[] args) {
		new ConvertGrayScale();
	}
}
