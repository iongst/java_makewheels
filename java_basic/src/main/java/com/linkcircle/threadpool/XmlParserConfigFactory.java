package com.linkcircle.threadpool;

/**
 * @author:老薛
 * @version:1.1
 * @date:2023/8/21
 * @description:
 * @vx:laoxue004
 */
public class XmlParserConfigFactory extends ParserConfig{

    public XmlParserConfigFactory(String configFilePath){
        super(configFilePath);
    }
    @Override
    public Config parser() {
        return null;
    }
}
