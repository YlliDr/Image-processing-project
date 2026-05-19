# Image Processing Application

## Përshkrimi

Ky projekt është një aplikacion grafik për përpunimin bazik të imazheve, i zhvilluar në Java me Java Swing.

Aplikacioni lejon ngarkimin, shfaqjen, përpunimin dhe ruajtjen e imazheve. Algoritmet kryesore janë implementuar manualisht duke punuar me pikselët e imazhit.

## Funksionet

- Load image JPG, PNG, BMP
- Save processed image
- Display original and processed image
- Grayscale
- Brightness adjustment
- Contrast adjustment
- Gaussian filter
- Histogram calculation
- Histogram display
- Histogram equalization
- Rotation 90°
- Center cropping
- Scaling 50%
- Basic compression
- Quality comparison with MSE and PSNR

## Teknologjitë

- Java
- Java Swing
- BufferedImage
- ImageIO
- AWT Graphics

Nuk përdoren librari të jashtme.

## Struktura

ImageProcessingProject/
│
├── src/
│   ├── ImageProcessingApp.java
│   ├── ImagePanel.java
│   ├── HistogramPanel.java
│   └── ImageAlgorithms.java
│
├── test-images/
├── screenshots/
├── report/
└── README.txt

## Ekzekutimi

Nga folderi kryesor i projektit:

javac -d out src/*.java

Pastaj:

java -cp out ImageProcessingApp

Në Windows PowerShell mund të përdoret:

javac -d out src\ImageAlgorithms.java src\ImagePanel.java src\HistogramPanel.java src\ImageProcessingApp.java

java -cp out ImageProcessingApp

## Përdorimi

1. Kliko Load Image.
2. Zgjidh një imazh JPG, PNG ose BMP.
3. Apliko funksionet e dëshiruara.
4. Përdor slider-at për brightness dhe contrast.
5. Shiko histogramin në pjesën e poshtme.
6. Kliko Quality Compare për MSE dhe PSNR.
7. Kliko Save Image për ruajtje.

## Autor

Ylli Dragusha
Erblind Musliu
Luis Tunaj
Erzen Neziri

Student i Shkencave Kompjuterike  
Universiteti i Prishtinës