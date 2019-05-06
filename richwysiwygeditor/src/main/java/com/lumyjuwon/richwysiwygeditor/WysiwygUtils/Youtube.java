package com.lumyjuwon.richwysiwygeditor.WysiwygUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Youtube {

    public static String getVideoId(String url){
        final String[] patterns = {"https://www.youtube.com/watch\\?v=(\\S+)&list", "https://www.youtube.com/watch\\?v=(\\S+)\\??",
                "https://www.youtube.com/watch\\?time_continue=\\d+&v=(\\S+)&list", "https://www.youtube.com/watch\\?time_continue=\\d+&v=(\\S+)\\??",
                "https://youtu.be/(\\S+)\\?list", "https://youtu.be/(\\S+)\\??"};
        Pattern p;
        Matcher m;
        for(String pattern : patterns) {
            p = Pattern.compile(pattern);
            m = p.matcher(url);
            if(m.find()) {
                return m.group(1);
            }
        }
        return "error";
    }

}
