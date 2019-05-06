package com.lumyjuwon.richwysiwygeditor.WysiwygUtils;

import com.lumyjuwon.richwysiwygeditor.R;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextColor {

    public static final Map<Integer,Integer> colorMap = new HashMap<Integer, Integer>(){{
        put(R.id.color_white, R.color.white); put(R.id.color_black, R.color.black); put(R.id.color_maroon, R.color.maroon); put(R.id.color_red, R.color.red); put(R.id.color_lime, R.color.lime);
        put(R.id.color_magenta, R.color.magenta); put(R.id.color_pink, R.color.pink); put(R.id.color_orange, R.color.orange); put(R.id.color_yellow, R.color.yellow);
        put(R.id.color_aqua, R.color.aqua); put(R.id.color_blue, R.color.blue); put(R.id.color_sky_blue, R.color.sky_blue); put(R.id.color_pale_cyan, R.color.pale_cyan);
        put(R.id.color_green, R.color.green);
    }};

    public static int getColor(String color){
        final String color_ = color.toLowerCase();
        final String regex = "[a-zA-Z]+_[a-zA-Z]+_(\\w+)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(color_);
        String color_name;
        if(matcher.find()) {
            color_name = matcher.group(1);
            switch (color_name) {
                case "black":
                    return R.color.black;
                case "maroon":
                    return R.color.maroon;
                case "red":
                    return R.color.red;
                case "magenta":
                    return R.color.magenta;
                case "pink":
                    return R.color.pink;
                case "orange":
                    return R.color.orange;
                case "yellow":
                    return R.color.yellow;
                case "lime":
                    return R.color.lime;
                case "aqua":
                    return R.color.aqua;
                case "blue":
                    return R.color.blue;
                case "sky_blue":
                    return R.color.sky_blue;
                case "pale_cyan":
                    return R.color.pale_cyan;
                case "green":
                    return R.color.green;
                default:
                    return R.color.black;
            }
        }
        else{
            return R.color.black;
        }
    }

}
