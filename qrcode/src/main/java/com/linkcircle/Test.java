package com.linkcircle;

/**
 * @author:老薛
 * @version:1.1
 * @date:2023/8/24
 * @description: 测试类
 * @vx:laoxue004
 */
public class Test extends QRFactory {
    public static void main(String[] args) throws Exception {

        // 创建指定的解析器对象
        ZXINGParser zxingParser = (ZXINGParser) QRFactory.getParser(ParserEnum.ZXING);
        BoofcvParser boofcvParser = (BoofcvParser) QRFactory.getParser(ParserEnum.BOOFCV);

        // 生成一个指定内容的二维码
        zxingParser.generate("https//997coder.com","/Users/laoxue/qrcoder/","a.jpg");


        // 解析生成的二维码
        CharSequence content = zxingParser.parser("/Users/laoxue/qrcoder/a.jpg",false);
        System.out.println(content);

        // 解析外部其他的二维码
        System.out.println(zxingParser.parseImage("1.jpg"));
    }
}
