package com.linkcircle.jsonparser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author:老薛
 * @version:1.1
 * @date:2023/8/15
 * @description: 通过json字符串完成对于实体对象的封装
 * @vx:laoxue004
 */
public class ConvertJson2Vo {
    public static Map<String, Object> parse(String json) {

        Map<String, Object> result = new HashMap<>();
        parseInternal(json, result);
        return result;

    }

    private static void parseInternal(String json, Map<String, Object> result) {

        Pattern pattern = Pattern.compile("\\{.*?\\}");
        Matcher matcher = pattern.matcher(json);

        while(matcher.find()) {
            String jsonObjectStr = matcher.group();
            Map<String, Object> object = parseJsonObject(jsonObjectStr);
            // 解析键值对后放入result
            result.putAll(object);
        }

    }

    private static Map<String, Object> parseJsonObject(String jsonObjectStr) {

        Map<String, Object> result = new HashMap<>();

        // 解析键值对
        String[] entries = jsonObjectStr.split(",");
        for(String entry : entries) {
            int index = entry.indexOf(":");
            String key = entry.substring(0, index).trim();
            String value = entry.substring(index + 1).trim();

            if(isJsonArray(value)) {
                // 解析数组
                result.put(key, parseJsonArray(value));
            } else if (isJsonObject(value)) {
                // 解析嵌套对象
                result.put(key, parseJsonObject(value));
            } else {
                result.put(key, value);
            }
        }

        return result;

    }

    // 解析数组的逻辑
    private static List<Object> parseJsonArray(String jsonArray) {
        //...
        return null;
    }

    // 判断一个值是否是json对象
    private static boolean isJsonObject(String value) {
        return value.startsWith("{") && value.endsWith("}");
    }

    // 判断一个值是否是json数组
    private static boolean isJsonArray(String value) {
        return value.startsWith("[") && value.endsWith("]");
    }


}
