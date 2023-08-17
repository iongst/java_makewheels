package com.linkcircle.project;

import java.util.Map;

/**
 * @author:老薛
 * @version:1.1
 * @date:2023/8/15
 * @description: 用来测试
 * @vx:laoxue004
 */
public class T {

    static{
        // 解析自定义注解

        //
    }

    public static void main(String[] args) {

        String json = "{\"u_name\":\"张三\",\"age\":\"12\",\"favs_ls\":[\"足球\",\"篮球\"],\"other_msg\":\"other_msg\",\"car\":{\"type\":\"奥迪\",\"color\":\"红色\"}}";


        JsonConvert<User> jsonConvert = new JsonConvert<>(User.class,json);
        //jsonConvert.parse(json);

        User user = jsonConvert.convertMapToObject();

        System.out.println(user);


    }






    private static void convertObject(String jsonString, Class<User> userClass) {




    }



}
