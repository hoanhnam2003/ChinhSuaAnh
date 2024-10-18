package org.example;

import org.apache.commons.io.IOUtils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;

public class Main {

    // Đường dẫn URL đến ảnh đầu vào
    private static final String INPUT_IMAGE_URL = "https://www.google.com.vn/url?sa=i&url=https%3A%2F%2Ftamanhhospital.vn%2Fchup-x-quang-dau%2F&psig=AOvVaw0Y1lXRxK2Lc31I000oObCn&ust=1729334535778000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCIij1LLfl4kDFQAAAAAdAAAAABAE";

    // Đường dẫn đến ảnh đầu ra
    private static final String OUTPUT_IMAGE_PATH = "src/main/resources/output/";

    public static BufferedImage invertColors(BufferedImage img) {
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                Color pixelColor = new Color(img.getRGB(x, y));
                Color invertedColor = new Color(255 - pixelColor.getRed(),
                        255 - pixelColor.getGreen(),
                        255 - pixelColor.getBlue());
                img.setRGB(x, y, invertedColor.getRGB());
            }
        }
        return img;
    }

    public static BufferedImage increaseContrast(BufferedImage img, float factor) {
        RescaleOp op = new RescaleOp(factor, 0, null);
        return op.filter(img, null);
    }

    public static BufferedImage logTransform(BufferedImage img) {
        BufferedImage logImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                Color pixelColor = new Color(img.getRGB(x, y));
                int red = (int) (Math.log(1 + pixelColor.getRed()) * 255 / Math.log(256));
                int green = (int) (Math.log(1 + pixelColor.getGreen()) * 255 / Math.log(256));
                int blue = (int) (Math.log(1 + pixelColor.getBlue()) * 255 / Math.log(256));
                logImg.setRGB(x, y, new Color(red, green, blue).getRGB());
            }
        }
        return logImg;
    }

    public static BufferedImage histogramEqualization(BufferedImage img) {
        // Cần cài đặt cho phép cân bằng Histogram
        // Ở đây sẽ trả về ảnh gốc cho ví dụ
        return img; // Trả về ảnh gốc cho ví dụ
    }

    public static void main(String[] args) {
        try {
            // Tạo thư mục đầu ra nếu nó không tồn tại
            File outputDir = new File(OUTPUT_IMAGE_PATH);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            // Tải ảnh từ URL
            InputStream in = new URL(INPUT_IMAGE_URL).openStream();
            BufferedImage img = ImageIO.read(in);

            // Thực hiện các thao tác tăng cường
            BufferedImage inverted = invertColors(img);
            ImageIO.write(inverted, "jpg", new File(OUTPUT_IMAGE_PATH + "inverted.jpg"));

            BufferedImage contrast = increaseContrast(img, 1.5f);
            ImageIO.write(contrast, "jpg", new File(OUTPUT_IMAGE_PATH + "contrast.jpg"));

            BufferedImage logTransformed = logTransform(img);
            ImageIO.write(logTransformed, "jpg", new File(OUTPUT_IMAGE_PATH + "log_transformed.jpg"));

            BufferedImage histogramEq = histogramEqualization(img);
            ImageIO.write(histogramEq, "jpg", new File(OUTPUT_IMAGE_PATH + "histogram_equalized.jpg"));

            System.out.println("Các ảnh đã được xuất thành công!");

            // Đóng InputStream
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
