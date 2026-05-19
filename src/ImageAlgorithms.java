import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageAlgorithms {

    public static BufferedImage copyImage(BufferedImage image) {
        BufferedImage copy = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        Graphics g = copy.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return copy;
    }

    public static BufferedImage toGrayscale(BufferedImage image) {
        BufferedImage result = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y));

                int gray = (int) (
                        0.299 * color.getRed()
                                + 0.587 * color.getGreen()
                                + 0.114 * color.getBlue()
                );

                gray = limit(gray);

                Color newColor = new Color(gray, gray, gray);
                result.setRGB(x, y, newColor.getRGB());
            }
        }

        return result;
    }

    public static BufferedImage changeBrightness(BufferedImage image, int value) {
        BufferedImage result = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y));

                int r = limit(color.getRed() + value);
                int g = limit(color.getGreen() + value);
                int b = limit(color.getBlue() + value);

                result.setRGB(x, y, new Color(r, g, b).getRGB());
            }
        }

        return result;
    }

    public static BufferedImage changeContrast(BufferedImage image, double factor) {
        BufferedImage result = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y));

                int r = limit((int) ((color.getRed() - 128) * factor + 128));
                int g = limit((int) ((color.getGreen() - 128) * factor + 128));
                int b = limit((int) ((color.getBlue() - 128) * factor + 128));

                result.setRGB(x, y, new Color(r, g, b).getRGB());
            }
        }

        return result;
    }

    public static BufferedImage applyGaussianFilter(BufferedImage image) {
        BufferedImage result = copyImage(image);

        int[][] kernel = {
                {1, 2, 1},
                {2, 4, 2},
                {1, 2, 1}
        };

        int kernelSum = 16;

        for (int y = 1; y < image.getHeight() - 1; y++) {
            for (int x = 1; x < image.getWidth() - 1; x++) {

                int redSum = 0;
                int greenSum = 0;
                int blueSum = 0;

                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        Color color = new Color(image.getRGB(x + kx, y + ky));
                        int weight = kernel[ky + 1][kx + 1];

                        redSum += color.getRed() * weight;
                        greenSum += color.getGreen() * weight;
                        blueSum += color.getBlue() * weight;
                    }
                }

                int r = limit(redSum / kernelSum);
                int g = limit(greenSum / kernelSum);
                int b = limit(blueSum / kernelSum);

                result.setRGB(x, y, new Color(r, g, b).getRGB());
            }
        }

        return result;
    }

    public static int[] calculateHistogram(BufferedImage image) {
        int[] histogram = new int[256];

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y));

                int gray = (int) (
                        0.299 * color.getRed()
                                + 0.587 * color.getGreen()
                                + 0.114 * color.getBlue()
                );

                gray = limit(gray);
                histogram[gray]++;
            }
        }

        return histogram;
    }

    public static BufferedImage histogramEqualization(BufferedImage image) {
        BufferedImage grayImage = toGrayscale(image);

        int[] histogram = calculateHistogram(grayImage);
        int[] cdf = new int[256];

        cdf[0] = histogram[0];

        for (int i = 1; i < 256; i++) {
            cdf[i] = cdf[i - 1] + histogram[i];
        }

        int cdfMin = 0;

        for (int i = 0; i < 256; i++) {
            if (cdf[i] != 0) {
                cdfMin = cdf[i];
                break;
            }
        }

        int totalPixels = grayImage.getWidth() * grayImage.getHeight();

        BufferedImage result = new BufferedImage(
                grayImage.getWidth(),
                grayImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        for (int y = 0; y < grayImage.getHeight(); y++) {
            for (int x = 0; x < grayImage.getWidth(); x++) {
                Color color = new Color(grayImage.getRGB(x, y));
                int oldValue = color.getRed();

                int newValue;

                if (totalPixels == cdfMin) {
                    newValue = oldValue;
                } else {
                    newValue = ((cdf[oldValue] - cdfMin) * 255) / (totalPixels - cdfMin);
                }

                newValue = limit(newValue);

                result.setRGB(x, y, new Color(newValue, newValue, newValue).getRGB());
            }
        }

        return result;
    }

    public static BufferedImage rotate90Clockwise(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage result = new BufferedImage(
                height,
                width,
                BufferedImage.TYPE_INT_RGB
        );

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int newX = height - 1 - y;
                int newY = x;

                result.setRGB(newX, newY, image.getRGB(x, y));
            }
        }

        return result;
    }

    public static BufferedImage cropCenter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        int cropWidth = width / 2;
        int cropHeight = height / 2;

        int startX = width / 4;
        int startY = height / 4;

        BufferedImage result = new BufferedImage(
                cropWidth,
                cropHeight,
                BufferedImage.TYPE_INT_RGB
        );

        for (int y = 0; y < cropHeight; y++) {
            for (int x = 0; x < cropWidth; x++) {
                result.setRGB(x, y, image.getRGB(startX + x, startY + y));
            }
        }

        return result;
    }

    public static BufferedImage scaleHalf(BufferedImage image) {
        int newWidth = image.getWidth() / 2;
        int newHeight = image.getHeight() / 2;

        if (newWidth < 1) newWidth = 1;
        if (newHeight < 1) newHeight = 1;

        BufferedImage result = new BufferedImage(
                newWidth,
                newHeight,
                BufferedImage.TYPE_INT_RGB
        );

        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                int sourceX = x * 2;
                int sourceY = y * 2;

                result.setRGB(x, y, image.getRGB(sourceX, sourceY));
            }
        }

        return result;
    }

    public static BufferedImage basicCompression(BufferedImage image) {
        BufferedImage result = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        int step = 32;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y));

                int r = (color.getRed() / step) * step;
                int g = (color.getGreen() / step) * step;
                int b = (color.getBlue() / step) * step;

                r = limit(r);
                g = limit(g);
                b = limit(b);

                result.setRGB(x, y, new Color(r, g, b).getRGB());
            }
        }

        return result;
    }

    public static String qualityComparison(BufferedImage original, BufferedImage processed) {
        if (original == null || processed == null) {
            return "Nuk ka imazhe per krahasim.";
        }

        if (original.getWidth() != processed.getWidth()
                || original.getHeight() != processed.getHeight()) {
            return "Krahasimi MSE/PSNR nuk mund te behet sepse dimensionet jane ndryshuar.";
        }

        double mse = calculateMSE(original, processed);

        if (mse == 0) {
            return "MSE: 0.00 | PSNR: Infinity dB | Imazhet jane identike.";
        }

        double psnr = 10 * Math.log10((255 * 255) / mse);

        return String.format("MSE: %.2f | PSNR: %.2f dB", mse, psnr);
    }

    private static double calculateMSE(BufferedImage original, BufferedImage processed) {
        double sum = 0.0;

        int width = original.getWidth();
        int height = original.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color c1 = new Color(original.getRGB(x, y));
                Color c2 = new Color(processed.getRGB(x, y));

                sum += Math.pow(c1.getRed() - c2.getRed(), 2);
                sum += Math.pow(c1.getGreen() - c2.getGreen(), 2);
                sum += Math.pow(c1.getBlue() - c2.getBlue(), 2);
            }
        }

        return sum / (width * height * 3.0);
    }

    private static int limit(int value) {
        if (value < 0) {
            return 0;
        }

        if (value > 255) {
            return 255;
        }

        return value;
    }
}