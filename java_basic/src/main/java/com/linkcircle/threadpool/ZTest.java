package com.linkcircle.threadpool;

/**
 * @author:老薛
 * @version:1.1
 * @date:2023/8/21
 * @description:
 * @vx:laoxue004
 */
public class ZTest {
    public static void main(String[] args) {

        ParserConfig load = ParserConfigSource.load("config.properties");
        Config parser = load.parser();
        System.out.println(parser.getCorePoolSize());




    }
    static String getFileExtension(String filePath){
        return filePath.substring(filePath.lastIndexOf(".")+1);
    }
}
