package com.linkcircle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author:老薛
 * @version:1.1
 * @date:2023/8/24
 * @description: 解析过程中需要使用的一些工具类
 * @vx:laoxue004
 */
public class ParserUtils extends ZXINGParser {

    // 将图片变为黑白
    protected static BufferedImage convertToGrayscale(BufferedImage colorImage) {
        BufferedImage grayscaleImage = new BufferedImage(colorImage.getWidth(), colorImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        grayscaleImage.getGraphics().drawImage(colorImage, 0, 0, null);
        return grayscaleImage;
    }
    // 互换颜色
    protected static BufferedImage swapColors(BufferedImage image) throws IOException {
        // 遍历图像的每个像素
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);

                // 提取原始像素的红、绿、蓝分量
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                // 定义特定颜色的阈值范围，例如您提到的颜色 #707070 和 #E1E1E1
                int colorThreshold = 30; // 色差阈值，可以根据需要调整

                // 判断是否是需要互换的颜色
                if (isColorWithinThreshold(red, green, blue, 255, 255, 255, colorThreshold)) {
                    image.setRGB(x, y, 0); // 将白色变为黑色
                } else if (isColorWithinThreshold(red, green, blue, 0, 0, 0, colorThreshold)) {
                    image.setRGB(x, y, 0xFFFFFF); // 将黑色变为白色
                }
            }
        }
        // 将处理后的图像保存为文件
        ImageIO.write(image, "jpg", new File("/Users/laoxue/Desktop/convert.jpg"));
        return ImageIO.read(new File("/Users/laoxue/Desktop/convert.jpg"));
    }

    private static boolean isColorWithinThreshold(int r1, int g1, int b1, int r2, int g2, int b2, int threshold) {
        int deltaR = Math.abs(r1 - r2);
        int deltaG = Math.abs(g1 - g2);
        int deltaB = Math.abs(b1 - b2);
        return deltaR <= threshold && deltaG <= threshold && deltaB <= threshold;
    }
}
