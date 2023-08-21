package com.linkcircle.threadpool;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * @author:老薛
 * @version:1.1
 * @date:2023/8/21
 * @description:
 * @vx:laoxue004
 */
public class PropertiesParserConfigFactory extends ParserConfig{
    public PropertiesParserConfigFactory(String configFilePath){
        super(configFilePath);
    }
    private Properties properties;
    @Override
    public Config parser() {
        properties = new Properties();
        Config config = new Config();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream(configFilePath));
            config.setCorePoolSize(Integer.parseInt(getValue("corePoolSize")));
            config.setMaxNumPoolSize(Integer.parseInt(getValue("maxNumPoolSize")));
            config.setKeepAliveTime(Long.parseLong(getValue("keepAliveTime")));
            config.setUnit(getUnit(getValue("unit")));
            config.setHandler(Policy.valueOf(getValue("handler")));
            config.setWorkQueue(getQueue(getValue("workQueue")));
        } catch (IOException e) {
            throw new IllegalArgumentException("file path is error："+configFilePath);
        } catch (IllegalArgumentException e){
            throw e;
        }
        return config;
    }

    private Queue<Thread> getQueue(String value){
        if ("linked".equalsIgnoreCase(value))
            return new LinkedBlockingQueue<>();  //链表的阻塞队列
        else if ("priority".equalsIgnoreCase(value))
            return new PriorityBlockingQueue<>();// 支持优先级别的阻塞队列
        else
            return new LinkedBlockingDeque<>(); //双向链表的阻塞队列
    }

    private TimeUnit getUnit(String value){
        if ("seconds".equalsIgnoreCase(value))
            return TimeUnit.SECONDS;
        else if ("milliseconds".equalsIgnoreCase(value))
            return TimeUnit.MILLISECONDS;
        else
            return  TimeUnit.SECONDS;
    }
    private String getValue(String key){
        String value = "";
        try{
            value = (String)properties.get(key);
            return value;
        }catch (Exception e){
            throw new IllegalArgumentException("not support this key:"+key);
        }
    }
}
