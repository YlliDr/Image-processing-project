import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageProcessingApp extends JFrame {

    private BufferedImage originalImage;
    private BufferedImage processedImage;

    private File originalFile;

    private final ImagePanel originalPanel = new ImagePanel("Imazhi origjinal");
    private final ImagePanel processedPanel = new ImagePanel("Imazhi i perpunuar");
    private final HistogramPanel histogramPanel = new HistogramPanel();

    private final JLabel infoLabel = new JLabel("Ngarko nje imazh per te filluar.");

    public ImageProcessingApp() {
        setTitle("Image Processing Application");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createButtonPanel(), BorderLayout.NORTH);

        JPanel imageContainer = new JPanel(new GridLayout(1, 2, 10, 10));
        imageContainer.add(originalPanel);
        imageContainer.add(processedPanel);
        add(imageContainer, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(createSliderPanel(), BorderLayout.NORTH);
        bottomPanel.add(histogramPanel, BorderLayout.CENTER);
        bottomPanel.add(infoLabel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton loadButton = new JButton("Load Image");
        JButton saveButton = new JButton("Save Image");
        JButton resetButton = new JButton("Reset");

        JButton grayscaleButton = new JButton("Grayscale");
        JButton gaussianButton = new JButton("Gaussian Filter");
        JButton equalizationButton = new JButton("Histogram Equalization");
        JButton rotateButton = new JButton("Rotate 90°");
        JButton cropButton = new JButton("Crop Center");
        JButton scaleButton = new JButton("Scale 50%");
        JButton compressButton = new JButton("Basic Compression");
        JButton qualityButton = new JButton("Quality Compare");

        loadButton.addActionListener(e -> loadImage());
        saveButton.addActionListener(e -> saveImage());
        resetButton.addActionListener(e -> resetImage());

        grayscaleButton.addActionListener(e -> applyGrayscale());
        gaussianButton.addActionListener(e -> applyGaussianFilter());
        equalizationButton.addActionListener(e -> applyHistogramEqualization());
        rotateButton.addActionListener(e -> applyRotation());
        cropButton.addActionListener(e -> applyCrop());
        scaleButton.addActionListener(e -> applyScaling());
        compressButton.addActionListener(e -> applyBasicCompression());
        qualityButton.addActionListener(e -> compareQuality());

        panel.add(loadButton);
        panel.add(saveButton);
        panel.add(resetButton);

        panel.add(grayscaleButton);
        panel.add(gaussianButton);
        panel.add(equalizationButton);

        panel.add(rotateButton);
        panel.add(cropButton);
        panel.add(scaleButton);

        panel.add(compressButton);
        panel.add(qualityButton);

        return panel;
    }

    private JPanel createSliderPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1));

        JSlider brightnessSlider = new JSlider(-100, 100, 0);
        brightnessSlider.setMajorTickSpacing(50);
        brightnessSlider.setMinorTickSpacing(10);
        brightnessSlider.setPaintTicks(true);
        brightnessSlider.setPaintLabels(true);
        brightnessSlider.setBorder(BorderFactory.createTitledBorder("Brightness"));

        brightnessSlider.addChangeListener(e -> {
            if (processedImage != null && !brightnessSlider.getValueIsAdjusting()) {
                int value = brightnessSlider.getValue();
                if (value != 0) {
                    processedImage = ImageAlgorithms.changeBrightness(processedImage, value);
                    brightnessSlider.setValue(0);
                    refresh("Brightness u ndryshua me vlere: " + value);
                }
            }
        });

        JSlider contrastSlider = new JSlider(50, 200, 100);
        contrastSlider.setMajorTickSpacing(50);
        contrastSlider.setMinorTickSpacing(10);
        contrastSlider.setPaintTicks(true);
        contrastSlider.setPaintLabels(true);
        contrastSlider.setBorder(BorderFactory.createTitledBorder("Contrast"));

        contrastSlider.addChangeListener(e -> {
            if (processedImage != null && !contrastSlider.getValueIsAdjusting()) {
                int value = contrastSlider.getValue();
                if (value != 100) {
                    double factor = value / 100.0;
                    processedImage = ImageAlgorithms.changeContrast(processedImage, factor);
                    contrastSlider.setValue(100);
                    refresh("Contrast u ndryshua me faktor: " + factor);
                }
            }
        });

        panel.add(brightnessSlider);
        panel.add(contrastSlider);

        return panel;
    }

    private void loadImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(
                "Image Files JPG, PNG, BMP",
                "jpg", "jpeg", "png", "bmp"
        ));

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                originalFile = chooser.getSelectedFile();
                originalImage = ImageIO.read(originalFile);

                if (originalImage == null) {
                    infoLabel.setText("Formati i imazhit nuk mund te lexohet.");
                    return;
                }

                processedImage = ImageAlgorithms.copyImage(originalImage);
                refresh("Imazhi u ngarkua me sukses: " + originalFile.getName());

            } catch (Exception ex) {
                infoLabel.setText("Gabim gjate ngarkimit te imazhit.");
            }
        }
    }

    private void saveImage() {
        if (processedImage == null) {
            infoLabel.setText("Nuk ka imazh per te ruajtur.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(
                "PNG Image",
                "png"
        ));

        int result = chooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();

                String path = file.getAbsolutePath();

                if (!path.toLowerCase().endsWith(".png")) {
                    file = new File(path + ".png");
                }

                ImageIO.write(processedImage, "png", file);
                infoLabel.setText("Imazhi i perpunuar u ruajt me sukses.");

            } catch (Exception ex) {
                infoLabel.setText("Gabim gjate ruajtjes se imazhit.");
            }
        }
    }

    private void resetImage() {
        if (originalImage == null) {
            infoLabel.setText("Nuk ka imazh te ngarkuar.");
            return;
        }

        processedImage = ImageAlgorithms.copyImage(originalImage);
        refresh("Imazhi u kthye ne gjendjen origjinale.");
    }

    private void applyGrayscale() {
        if (!hasImage()) return;

        processedImage = ImageAlgorithms.toGrayscale(processedImage);
        refresh("Grayscale u aplikua.");
    }

    private void applyGaussianFilter() {
        if (!hasImage()) return;

        processedImage = ImageAlgorithms.applyGaussianFilter(processedImage);
        refresh("Gaussian filter u aplikua.");
    }

    private void applyHistogramEqualization() {
        if (!hasImage()) return;

        processedImage = ImageAlgorithms.histogramEqualization(processedImage);
        refresh("Histogram equalization u aplikua.");
    }

    private void applyRotation() {
        if (!hasImage()) return;

        processedImage = ImageAlgorithms.rotate90Clockwise(processedImage);
        refresh("Imazhi u rrotullua 90 grade.");
    }

    private void applyCrop() {
        if (!hasImage()) return;

        processedImage = ImageAlgorithms.cropCenter(processedImage);
        refresh("Crop center u aplikua.");
    }

    private void applyScaling() {
        if (!hasImage()) return;

        processedImage = ImageAlgorithms.scaleHalf(processedImage);
        refresh("Scaling 50% u aplikua.");
    }

    private void applyBasicCompression() {
        if (!hasImage()) return;

        processedImage = ImageAlgorithms.basicCompression(processedImage);
        refresh("Kompresim bazik u aplikua. " + ImageAlgorithms.qualityComparison(originalImage, processedImage));
    }

    private void compareQuality() {
        if (!hasImage()) return;

        String result = ImageAlgorithms.qualityComparison(originalImage, processedImage);
        infoLabel.setText(result);
    }

    private boolean hasImage() {
        if (processedImage == null) {
            infoLabel.setText("Se pari ngarko nje imazh.");
            return false;
        }

        return true;
    }

    private void refresh(String message) {
        originalPanel.setImage(originalImage);
        processedPanel.setImage(processedImage);

        if (processedImage != null) {
            int[] histogram = ImageAlgorithms.calculateHistogram(processedImage);
            histogramPanel.setHistogram(histogram);
        }

        infoLabel.setText(message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ImageProcessingApp app = new ImageProcessingApp();
            app.setVisible(true);
        });
    }
}