import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class PositiveROI extends JFrame {
    BufferedImage image;
    boolean showROI;
    JLabel label = new JLabel();
    Rectangle roi;
    int[] roiValues;
    
    public PositiveROI() {
        super("Positive Region of Interest");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        try {
	    File[] files = new File("Images").listFiles();
	    File f;
   	    do {
		f = files[(int) (Math.random() * files.length)];
	    } while (f.getName().charAt(0) == '.');
            image = ImageIO.read(f);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        label.setIcon(new ImageIcon(image));
        label.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {    }
            @Override
            public void mousePressed(MouseEvent e) {
                roi = new Rectangle();
                roi.x = e.getX();
                roi.y = e.getY();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                showROI = false;
		for (int x = 0; x < roi.width; x++) {
			image.setRGB(roi.x + x, roi.y, roiValues[x]);
			image.setRGB(roi.x + x, roi.y + roi.height, roiValues[x + roi.width]);
		}
		for (int y = 0; y < roi.height; y++) {
			image.setRGB(roi.x, roi.y + y, roiValues[(roi.width*2) + y]);
			image.setRGB(roi.x + roi.width, roi.y + y, roiValues[(roi.width*2) + (y + roi.height)]);
		}
		image.setRGB(roi.x + roi.width, roi.y + roi.height, roiValues[(roi.width*2) + (roi.height*2)]);
                label.setIcon(new ImageIcon(image));
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        BufferedImage img = new BufferedImage(roi.width, roi.height, BufferedImage.TYPE_3BYTE_BGR);
                        for (int y = 0; y < roi.height; y++)
                            for (int x = 0; x < roi.width; x++) {
                                img.setRGB(x, y, image.getRGB(x + roi.x, y + roi.y));
			    }
                        try {
                            ImageIO.write(img, "jpg", new File("output.jpg"));
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }); t.start();
            }
            @Override
            public void mouseEntered(MouseEvent e) {    }
            @Override
            public void mouseExited(MouseEvent e) { }            
        });
        label.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {                
                if (showROI) {
                    for (int x = 0; x < roi.width; x++) {
                        image.setRGB(roi.x + x, roi.y, roiValues[x]);
                        image.setRGB(roi.x + x, roi.y + roi.height, roiValues[x + roi.width]);
                    }
                    for (int y = 0; y < roi.height; y++) {
                        image.setRGB(roi.x, roi.y + y, roiValues[(roi.width*2) + y]);
                        image.setRGB(roi.x + roi.width, roi.y + y, roiValues[(roi.width*2) + (y + roi.height)]);
                    } 
		    image.setRGB(roi.x + roi.width, roi.y + roi.height, roiValues[(roi.width*2) + (roi.height*2)]);
                }
                
                showROI = true;
                roi.height = (int) Math.abs(e.getY() - roi.getY());
                roi.width = (int) Math.abs(e.getX() - roi.getX());
                roiValues = new int[(roi.width*2) + (roi.height*2) + 1];
                
                for (int x = 0; x < roi.width; x++) {                    
                    roiValues[x] = image.getRGB(roi.x + x, roi.y);
                    roiValues[x+roi.width] = image.getRGB(roi.x + x, roi.y + roi.height);
                }
                for (int y = 0; y < roi.height; y++) {
                    roiValues[(roi.width*2) + y] = image.getRGB(roi.x, roi.y + y);
                    roiValues[(roi.width*2) + (roi.height + y)] = image.getRGB(roi.x + roi.width, roi.y + y);
                }                roiValues[(roi.width*2) + (roi.height*2)] = image.getRGB(roi.x + roi.width, roi.y + roi.height);
                BufferedImage tmp = image.getSubimage(0, 0, image.getWidth(), image.getHeight());
                tmp.getGraphics().drawRect(roi.x, roi.y, roi.width, roi.height);
                label.setIcon(new ImageIcon(tmp));
            }
            @Override
            public void mouseMoved(MouseEvent e) {  }   
        });
        setBounds(100, 100, image.getWidth(), image.getHeight());
        add(label);
        setVisible(true); 
    }
    
    public static void main (String[] args) { new PositiveROI(); }
}
