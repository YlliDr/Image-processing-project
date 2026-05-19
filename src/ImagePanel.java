import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {

    private BufferedImage image;
    private final String title;

    public ImagePanel(String title) {
        this.title = title;
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.drawString(title, 10, 20);

        if (image == null) {
            g.drawString("Nuk ka imazh.", 10, 45);
            return;
        }

        int availableWidth = getWidth() - 20;
        int availableHeight = getHeight() - 50;

        if (availableWidth <= 0 || availableHeight <= 0) {
            return;
        }

        double scaleX = (double) availableWidth / image.getWidth();
        double scaleY = (double) availableHeight / image.getHeight();
        double scale = Math.min(scaleX, scaleY);

        int drawWidth = (int) (image.getWidth() * scale);
        int drawHeight = (int) (image.getHeight() * scale);

        int x = (getWidth() - drawWidth) / 2;
        int y = 35;

        g.drawImage(image, x, y, drawWidth, drawHeight, null);

        g.setColor(Color.DARK_GRAY);
        g.drawString(
                "Dimensionet: " + image.getWidth() + " x " + image.getHeight(),
                10,
                getHeight() - 10
        );
    }
}