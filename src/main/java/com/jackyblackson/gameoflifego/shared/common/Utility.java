package com.jackyblackson.gameoflifego.shared.common;

import java.io.*;
import java.util.Properties;

import static com.jackyblackson.gameoflifego.shared.logger.Logger.Log;

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

    public static int getHexValue(char ch){
        if(ch >= '0' && ch <= '9'){
            return Integer.parseInt(String.valueOf(ch));
        }
        if ( (ch >= 'a'  && ch <= 'f') || (ch >= 'A' && ch <= 'F')) {
            switch (ch) {
                case 'a':
                case 'A':
                    //这里不用break是因为执行了return以后就不会再往下执行了
                    return 10;
                case 'b':
                case 'B':
                    return 11;
                case 'c':
                case 'C':
                    return 12;
                case 'd':
                case 'D':
                    return 13;
                case 'e':
                case 'E':
                    return 14;
                case 'f':
                case 'F':
                    return 15;
            }
        }
	/* -1 我习惯用它表示出错，在调用的地方检测它
		在后面可以弄一个异常类来抛出，这里简单用-1检测
	*/
        return -1;
    }
}
