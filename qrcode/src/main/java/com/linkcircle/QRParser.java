package com.linkcircle;

import com.google.zxing.WriterException;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface QRParser {

    /**
     * 生成一个指定的二维码图片
     * @param text 可以包含url、文字信息
     * @param filePath 图片保存路径 路径后无需加/
     * @return
     */
    public boolean generate(String text, String filePath, String fileName) throws WriterException, IOException;

    /**
     * 生成一个指定的二维码图片
     * @param text 包含url、文字信息
     * @param filePath 图片保存路径 路径后无需加/
     * @param fileName 图片保存名称
     * @param image 包含图片信息
     * @return
     */
    public boolean generate(String text, String filePath, String fileName, BufferedImage image);

    /**
     * 解析指定的二维码信息
     * @param image 二维码图片
     * @return
     */
    public CharSequence parser(BufferedImage image);

    /**
     * 解析指定的二维码信息
     * @param filePath 二维码图片地址
     * @return
     */
    public CharSequence parser(String filePath, boolean isColorFul) throws Exception;


}
