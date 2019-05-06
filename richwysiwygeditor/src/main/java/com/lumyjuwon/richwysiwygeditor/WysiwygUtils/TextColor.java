package com.lumyjuwon.richwysiwygeditor.WysiwygUtils;

import com.lumyjuwon.richwysiwygeditor.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextColor {

    public static int getColor(String color){
        String color_ = color.toLowerCase();
        String regex = "[a-zA-Z]+_[a-zA-Z]+_(\\w+)";

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
