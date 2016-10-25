package com.huake.zhnews.util;

/**
 * Created by Werb on 2016/8/30.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 */
public class StringUtils {

    /**
     * 分隔 html 网页解析出图片地址
     *
     * @param body html
     * @return img url
     */
    public static String getImageUrl(String body) {

        //a0,b0的意义是判断只有符合这种html网页的格式才往下走，写的太死
        int a0 = body.indexOf("<body>");
        if (a0 == -1) return null;
        int b0 = body.indexOf("<img", a0);
        if (b0 == -1) return null;
        int c0 = body.indexOf("http:", b0);
        if (c0 == -1) return null;

        //常见的三种图片格式都会考虑，逻辑结构太乱
        int d0 = body.indexOf(".jpg", c0) + ".jpg".length();
        if (d0 == -1) {
            d0 = body.indexOf(".png", c0) + ".png".length();
            if(d0 == -1){
                d0 = body.indexOf(".gif", c0) + ".gif".length();
                if(d0 == -1){
                    return null;//都解析不了我也就没办法了
                }else {
                    return body.substring(c0, d0);
                }
            }else {
                return body.substring(c0, d0);
            }
        }else {
            return body.substring(c0, d0);
        }
    }
}
