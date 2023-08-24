package com.linkcircle;

import com.google.zxing.WriterException;

import javax.naming.OperationNotSupportedException;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author:老薛
 * @version:1.1
 * @date:2023/8/24
 * @description: 用来创建二维码生成和解析的工程类
 * @vx:laoxue004
 */
public class QRFactory implements QRParser {

    @Override
    public boolean generate(String text, String filePath, String fileName) throws WriterException, IOException {
        return false;
    }

    @Override
    public boolean generate(String text, String filePath, String fileName, BufferedImage image) {
        return false;
    }

    @Override
    public CharSequence parser(BufferedImage image) {
        return null;
    }

    @Override
    public CharSequence parser(String filePath, boolean isColorFul) throws Exception {
        return null;
    }

    /**
     * TODO：可以在再增加一些其他的解析方式
     * @param parserEnum
     * @return
     * @throws OperationNotSupportedException
     */
    public static QRParser getParser(ParserEnum parserEnum) throws OperationNotSupportedException {
        if (parserEnum == ParserEnum.ZXING){
            return new ZXINGParser();
        }else if (parserEnum == ParserEnum.BOOFCV){
            return new BoofcvParser();
        }else{
            throw new OperationNotSupportedException("no supported this parser");

        }
    }
}
