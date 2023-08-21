package com.linkcircle.threadpool;

/**
 * 配置解析的接口
 */
public abstract class ParserConfig {
    protected String configFilePath;
    public ParserConfig(String configFilePath){
        this.configFilePath = configFilePath;
    }
    abstract Config parser();

}
