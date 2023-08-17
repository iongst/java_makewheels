package com.linkcircle.project;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author:老薛
 * @version:1.1
 * @date:2023/8/17
 * @description:
 * @vx:laoxue004
 */
public class JsonConvert<T> {

    // 待转换的实体对象的class
    private Class<T> clazz;
    private int currentIndex = 0;
    private String json;

    public JsonConvert(Class<T> clazz,String json) {
        this.clazz = clazz;
        this.json = json.replaceAll("\\s", "");
    }



    // 解析json字符串，将其封装到一个map集合中
    private Map<String, Object> parseInternal() {
        if(!check(json))
            throw new IllegalArgumentException("json format is error" + json);

        Map<String, Object> resultMap = new HashMap<>();
        currentIndex++; // Skip the opening '{'

        while (currentIndex < json.length()) {
            char c = json.charAt(currentIndex);
            if (c == '}') {
                currentIndex++; // Skip the closing '}'
                break;
            } else if (c == '"') {
                currentIndex++; // Skip the opening '"'
                String key = parseKey();
                currentIndex++; // Skip the colon ':'
                Object value = parseValue();
                resultMap.put(key, value);

                if (currentIndex < json.length() && json.charAt(currentIndex) == ',') {
                    currentIndex++; // Skip the comma ','
                }
            } else {
                currentIndex++;
            }
        }
        return resultMap;
    }

    private String parseKey() {
        int endIndex = json.indexOf('"', currentIndex);
        String value = json.substring(currentIndex, endIndex);
        currentIndex = endIndex + 1; // Skip the closing '"'
        return unescape(value);
    }

    private String unescape(String value) {
        return value.replaceAll("\\\\\"", "\"");
    }

    private String parseString(){
        int endIndex = json.indexOf('"', currentIndex+1);
        String value = json.substring(currentIndex+1, endIndex);
        currentIndex = endIndex + 1; // Skip the closing '"'
        return unescape(value);
    }
    private String parseList(){
        int endIndex = json.indexOf('"', currentIndex+1);
        String value = json.substring(currentIndex, endIndex);
        currentIndex = endIndex + 1; // Skip the closing '"'
        return unescape(value);
    }


    private Object parseValue() {
        char c = json.charAt(currentIndex);

        if (c == '"') {
            return parseString();
        } else if (c == '{') {
            return parseInternal();
        } else if (c == '[') {
            currentIndex++; // Skip the opening '['
            return parseArray();
        } else {
            int endIndex = json.indexOf(',', currentIndex);
            int nextCommaIndex = json.indexOf(',', endIndex + 1);
            int endIndexObject = json.indexOf('}', currentIndex);
            int endIndexArray = json.indexOf(']', currentIndex);

            if (endIndex == -1) {
                endIndex = json.length();
            }
            if (nextCommaIndex != -1 && nextCommaIndex < endIndexObject && nextCommaIndex < endIndexArray) {
                endIndex = nextCommaIndex;
            } else if (endIndexObject != -1 && endIndexObject < endIndexArray) {
                endIndex = endIndexObject;
            } else if (endIndexArray != -1) {
                endIndex = endIndexArray;
            }
            String value = json.substring(currentIndex, endIndex);
            currentIndex = endIndex;
            return value;
        }
    }

    private List<Object> parseArray() {
        List<Object> list = new ArrayList<>();
        while (currentIndex < json.length()) {
            char c = json.charAt(currentIndex);
            if (c == ']') {
                currentIndex++; // Skip the closing ']'
                break;
            } else if (c == '"') {
                currentIndex++; // Skip the opening '"'
                String value = parseList();
                list.add(value);
                if (currentIndex < json.length() && json.charAt(currentIndex) == ',') {
                    currentIndex++; // Skip the comma ','
                }
            } else if (c == '{') {
                currentIndex++; // Skip the opening '{'
                list.add(parseInternal());

                if (currentIndex < json.length() && json.charAt(currentIndex) == ',') {
                    currentIndex++; // Skip the comma ','
                }
            } else {
                currentIndex++;
            }
        }

        return list;
    }

    /**
     * 检查json字符串的格式是否满足要求
     * @param json
     * @return
     */
    public boolean check(String json) {
        if(!json.startsWith("{") || !json.endsWith("}")) {
            return false;
        }
        Stack<Character> stack = new Stack<>();
        for(char c : json.toCharArray()) {
            if (c == '{' || c == '[')
                stack.push(c);
            else if(c=='"' && (stack.peek() != '"'))
                stack.push(c);
            else if (c=='"' && (stack.peek() == '"'))
                stack.pop();
            else if (c == '}' || c == ']' )
                if (stack.isEmpty() || !isPair(stack.pop(), c))
                    return false;
        }
        return stack.isEmpty();
    }

    boolean isPair(char left, char right) {
        if (left == '{' && right == '}') return true;
        return left == '[' && right == ']';
    }


    // 将map中的内容转换为对应的T类型
    public T convertMapToObject() {
        Map<String, Object> stringObjectMap = parseInternal();
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(SeriesOtherNames.class)) {
                    SeriesOtherNames annotation = field.getAnnotation(SeriesOtherNames.class);
                    String[] names = annotation.names();

                    Object value = getValueFromMap(stringObjectMap, names);
                    if (value != null) {
                        field.setAccessible(true);
                        Object convertedValue = convertToFieldType(field.getType(),value);
                        field.set(instance, convertedValue);
                    }
                }else{
                    Object value = stringObjectMap.get(field.getName());
                    if (value != null) {
                        field.setAccessible(true);
                        Object convertedValue = convertToFieldType(field.getType(),value);
                        field.set(instance, convertedValue);
                    }
                }
            }
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    private Object getValueFromMap(Map<String, Object> map, String[] names) {
        for (String name : names) {
            if (map.containsKey(name)) {
                return map.get(name);
            }
        }
        return null;
    }


    private Object convertToFieldType(Class<?> fieldType, Object value) {
        if (fieldType.equals(String.class)) {
            return value.toString();
        } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
            return Integer.parseInt(value.toString());
        } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
            return Double.parseDouble(value.toString());
        }
        // Add more type conversions as needed...

        return value;
    }
}
