package com.jackyblackson.gameoflifego.server.info;

import java.io.*;
import java.util.Properties;

import static com.jackyblackson.gameoflifego.server.logger.Logger.Log;

public class Utility {
    /**
     * @function updateProperties更新配置文件(但是写入会乱，抛弃)
     */

    public static Properties updateProperties(String filename, String key, String value) throws Exception {
        Properties pro = new Properties();
        String enConding = "UTF-8";
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), enConding));
            pro.load(br);
            OutputStreamWriter oStreamWriter = new OutputStreamWriter(new FileOutputStream(filename), enConding);
            pro.setProperty(key, value);
            // 以适合使用 load 方法加载到 Properties 表中的格式，
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流
            pro.list(System.out);
            pro.store(oStreamWriter, "Update '" + key + "' value");
            br.close();
            oStreamWriter.close();

        }
        catch (Exception e) {
            e.printStackTrace();
            Log( Importance.SEVERE, "更新Properties错误"+key);
        }
        return pro;
    }
}
