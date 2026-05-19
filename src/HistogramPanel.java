import javax.swing.*;
import java.awt.*;

public class HistogramPanel extends JPanel {

    private int[] histogram = new int[256];

    public HistogramPanel() {
        setPreferredSize(new Dimension(1000, 180));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    public void setHistogram(int[] histogram) {
        if (histogram != null && histogram.length == 256) {
            this.histogram = histogram;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int left = 40;
        int top = 25;
        int width = getWidth() - 70;
        int height = getHeight() - 55;

        g.setColor(Color.BLACK);
        g.drawString("Histogrami i intensitetit", 10, 15);

        g.drawRect(left, top, width, height);

        int max = 1;
        for (int value : histogram) {
            if (value > max) {
                max = value;
            }
        }

        g.setColor(Color.BLUE);

        for (int i = 0; i < 256; i++) {
            int barHeight = histogram[i] * height / max;

            int x = left + i * width / 256;
            int y1 = top + height;
            int y2 = y1 - barHeight;

            g.drawLine(x, y1, x, y2);
        }

        g.setColor(Color.BLACK);
        g.drawString("0", left, top + height + 15);
        g.drawString("255", left + width - 25, top + height + 15);
    }
}