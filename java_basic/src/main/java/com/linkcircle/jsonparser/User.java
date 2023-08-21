package com.linkcircle.jsonparser;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

/**
 * @author:老薛
 * @version:1.1
 * @date:2023/8/15
 * @description:
 * @vx:laoxue004
 */
@Getter
@Setter
public class User {
    @SeriesOtherNames(names="u_name")
    private String name;
    private Integer age;
    @SeriesOtherNames(names="favs_ls")
    private List<String> favs;
    private Car car;
    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", favs=" + Arrays.toString(favs.toArray()) +
                ", car=" + car +
                '}';
    }
}
