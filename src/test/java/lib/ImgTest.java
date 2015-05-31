package lib;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
public class ImgTest extends Component {

  public static void main(String args[]) {

    JFrame frame = new JFrame("Image Label");
    BufferedImage img = null;
    try {
      String urlString = "https://lh3.googleusercontent.com/-pJAr-DuF4KE/VNIV4d8nrEI/AAAAAAAEM6I/W8dS7lKpbCg/_MG_0727.JPG";
      URL url = new URL(urlString);
      img = ImageIO.read(url);
    } catch (IOException e) {
    }
    frame.add("Center", new ImgTest(img, "Java 2D Graphics!"));
    frame.pack();
    frame.setVisible(true);
  }

  BufferedImage img;
  String text;
  Font font;

  public ImgTest(BufferedImage img, String text) {
    this.img = img;
    this.text = text;
    font = new Font("Serif", Font.PLAIN, 36);
  }

  /* We want to size the component around the text. */
  public Dimension getPreferredSize() {
    FontMetrics metrics = img.getGraphics().getFontMetrics(font);
    int width = metrics.stringWidth(text) * 2;
    int height = metrics.getHeight() * 2;
    return new Dimension(width, height);
  }

  public void paint(Graphics g) {

    /*
     * Draw the image stretched to exactly cover the size of the drawing area.
     */
    Dimension size = getSize();
    g.drawImage(img, 0, 0, size.width, size.height, 0, 0, img.getWidth(null),
        img.getHeight(null), null);

    /*
     * Fill a rounded rectangle centered in the drawing area. Calculate the size
     * of the rectangle from the size of the text
     */
    g.setFont(font);
    FontRenderContext frc = ((Graphics2D) g).getFontRenderContext();
    Rectangle2D bounds = font.getStringBounds(text, frc);

    int wText = (int) bounds.getWidth();
    int hText = (int) bounds.getHeight();

    int rX = (size.width - wText) / 2;
    int rY = (size.height - hText) / 2;
    g.setColor(Color.yellow);
    g.fillRoundRect(rX, rY, wText, hText, hText / 2, hText / 2);

    /*
     * Draw text positioned in the rectangle. Since the rectangle is sized based
     * on the bounds of the String we can position it using those bounds.
     */
    int xText = rX - (int) bounds.getX();
    int yText = rY - (int) bounds.getY();
    g.setColor(Color.black);
    g.setFont(font);
    g.drawString(text, xText, yText);
  }
}

   

