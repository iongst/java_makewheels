package com.linkcircle.jsonparser;

import lombok.Getter;
import lombok.Setter;

/**
 * @author:老薛
 * @version:1.1
 * @date:2023/8/17
 * @description:
 * @vx:laoxue004
 */
@Getter
@Setter
public class Car {
    private String color;
    private String type;

    @Override
    public String toString() {
        return "Car{" +
                "color='" + color + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
