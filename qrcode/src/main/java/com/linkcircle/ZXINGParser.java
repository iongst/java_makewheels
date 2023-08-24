package com.linkcircle;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * @author:老薛
 * @version:1.1
 * @date:2023/8/24
 * @description: 通过ZXINGParser完成解析
 * @vx:laoxue004
 */
public class ZXINGParser implements QRParser{
    @Override
    public boolean generate(String text,String filePath,String fileName) throws WriterException, IOException {


        //设置二维码纠错级别ＭＡＰ
        Hashtable<EncodeHintType, Object> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  // 矫错级别 H为高级

        // 设置二维码中携带数据信息的字符集和
        hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        //创建比特矩阵(位矩阵)的QR码编码的字符串
        BitMatrix byteMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 1300, 1300, hintMap);

        // 使BufferedImage勾画QRCode  (matrixWidth 是行二维码像素点)
        int matrixWidth = byteMatrix.getWidth();
        BufferedImage image = new BufferedImage(matrixWidth - 200, matrixWidth - 200, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();
        Graphics2D graphics = (Graphics2D) image.getGraphics();

        // 绘制周边背景
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);


        // 绘制二维码特征点
        graphics.setColor(Color.BLACK);
        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i - 100, j - 100, 1, 1);
                }
            }
        }
        return ImageIO.write(image, "jpg", Files.newOutputStream(Paths.get(filePath+fileName)));
    }

    @Override
    public boolean generate(String text,String filePath,String fileName, BufferedImage image) {
        return false;
    }

    @Override
    public CharSequence parser(BufferedImage image) {
        return null;
    }

    @Override
    public CharSequence parser(String filePath,boolean isColorFul) throws Exception {
        try {
            // 指定解析图片
            BufferedImage image = ImageIO.read(new File(filePath));

            // 将图片解析为对应的位图信息
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            // 设置解析中的配置
            HashMap<DecodeHintType, Object> hints = new HashMap<>();

            //复杂模式，开启PURE_BARCODE模式
            hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
            // 字符集合
            hints.put(DecodeHintType.CHARACTER_SET, "utf-8");

            // 解析
            MultiFormatReader reader = new MultiFormatReader();
            Result result = reader.decode(bitmap,hints);


            return result.getText();

        } catch (IOException | NotFoundException e) {
            throw new IOException("parse fail");
        }
    }

    /**
     * 解析外部二维码
     * @param filePath
     * @return
     * @throws IOException
     */
    public String parseImage(String filePath) throws IOException {
        try {
            BufferedImage image = ImageIO.read(new File(filePath));
            // 设置为黑白图片
            BufferedImage bufferedImage = ParserUtils.convertToGrayscale(image);
            // 互质图片
            bufferedImage = ParserUtils.swapColors(bufferedImage);

            // 将图片解析为对应的位图信息
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));

            // 设置解析中的配置
            HashMap<DecodeHintType, Object> hints = new HashMap<>();

            //优化精度
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            // 字符集合
            hints.put(DecodeHintType.CHARACTER_SET, "utf-8");

            // 解析
            MultiFormatReader reader = new MultiFormatReader();
            Result result = reader.decode(bitmap,hints);


            return result.getText();
        } catch (IOException | NotFoundException e) {
            throw new IOException("parse fail");
        }
    }

}

