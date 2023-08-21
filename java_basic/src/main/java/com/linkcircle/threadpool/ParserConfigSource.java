package com.linkcircle.threadpool;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:老薛
 * @version:1.1
 * @date:2023/8/21
 * @description: 用来读取配置文件
 * @vx:laoxue004
 */
public class ParserConfigSource {
    private static final Map<String,ParserConfig> CACHE = new HashMap<>();
    /**
     * 读取指定的配置文件 返回对应的解析对象
     * @param configFilePath
     * @return
     */
    public static ParserConfig load(String configFilePath){
        String extension = getFileExtension(configFilePath);
        if(extension==null||extension.isEmpty()) {
            throw new IllegalArgumentException("Rule config file format is not supported: " + configFilePath);
        }
        initCache(configFilePath,extension);
        return CACHE.get(extension);
    }

    private static void initCache(String configFilePath,String extension){
        CACHE.put("xml", new XmlParserConfigFactory(configFilePath.replace(extension,"xml")));
        CACHE.put("yaml", new YamlParserConfigFactory(configFilePath.replace(extension,"yaml")));
        CACHE.put("properties", new PropertiesParserConfigFactory(configFilePath.replace(extension,"properties")));
    }

    /**
     * 读取配置文件的后缀
     * @param filePath
     * @return
     */
    private static String getFileExtension(String filePath){
        if (!filePath.contains("."))
            return null;
        if (!(filePath.contains("json")||filePath.contains("properties")||filePath.contains("yaml")))
            return null;
        return filePath.substring(filePath.lastIndexOf(".")+1);
    }

}
