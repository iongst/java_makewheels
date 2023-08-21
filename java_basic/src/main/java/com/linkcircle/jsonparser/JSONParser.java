import java.util.*;

public class JSONParser {

    private int currentIndex = 0;
    private String json;

    private JSONParser(String json) {
        this.json = json.replaceAll("\\s", "");
    }

    private Map<String, Object> parseInternal() {
        Map<String, Object> resultMap = new HashMap<>();
        currentIndex++; // Skip the opening '{'

        while (currentIndex < json.length()) {
            char c = json.charAt(currentIndex);

            if (c == '}') {
                currentIndex++; // Skip the closing '}'
                break;
            } else if (c == '"') {
                currentIndex++; // Skip the opening '"'
                String key = parseString();
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

    private String parseString() {
        int endIndex = json.indexOf('"', currentIndex+1);
        String value = json.substring(currentIndex, endIndex);
        currentIndex = endIndex + 1; // Skip the closing '"'
        return unescape(value);
    }

    private String unescape(String value) {
        return value.replaceAll("\\\\\"", "\"");
    }

    private Object parseValue() {
        char c = json.charAt(currentIndex);

        if (c == '"') {
            return parseString();
        } else if (c == '{') {
            currentIndex++; // Skip the opening '{'
            return parseInternal();
        } else if (c == '[') {
            currentIndex++; // Skip the opening '['
            return parseArray();
        } else {
            int endIndex = findValueEndIndex();
            String value = json.substring(currentIndex, endIndex);
            currentIndex = endIndex;
            return unescape(value);
        }
    }

    private int findValueEndIndex() {
        int endIndex = currentIndex;
        boolean inString = false;

        while (endIndex < json.length()) {
            char c = json.charAt(endIndex);

            if (c == '"' && (endIndex == 0 || json.charAt(endIndex - 1) != '\\')) {
                inString = !inString;
            } else if (c == ',' && !inString) {
                break;
            }

            endIndex++;
        }

        return endIndex;
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
                String value = parseString();
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

    public static void main(String[] args) {
        String json = "{\"u_name\":\"张三\",\"age\":\"12\",\"favs_ls\":[\"足球\",\"篮球\"],\"other_msg\":\"other_msg\",\"car\":{\"type\":\"奥迪\",\"color\":\"红色\"}}";
        JSONParser parser = new JSONParser(json);
        Map<String, Object> resultMap = parser.parseInternal();
        resultMap.entrySet().forEach(entry -> System.out.println(entry.getKey() + "=" + entry.getValue()));
    }
}