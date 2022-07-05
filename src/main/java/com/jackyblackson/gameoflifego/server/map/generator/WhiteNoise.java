package com.jackyblackson.gameoflifego.server.map.generator;

import com.jackyblackson.gameoflifego.server.info.Importance;

import static com.jackyblackson.gameoflifego.server.logger.Logger.Log;

public class WhiteNoise {
    private static long rot(long x, long b){
            return((x<<b) ^ (x >> (32 - b)));
    }

    private static Long pcghash(long x, long y, long seed){
        for (int l = 0; l < 3; l++){
            x = rot(x^0xcafebabe + y^0xfaceb00c + seed^0xba5eba11, 23);
            x = rot(x^0xdeadbeef + y^0x8badf00d + seed^0x5ca1ab1e, 5);
            x = rot(x^0xca11ab1e + y^0xfacefeed + seed^0xdeadc0de, 17);
        }
        return(x^y^seed);
    }

    public static double noise(long x, long y, long seed){
        //Log(Importance.DEBUG, String.format("The noise generated: [%f]", Double.parseDouble("0." + Math.abs(pcghash(x, y, seed)))));
        return(Double.parseDouble("0." + Math.abs(pcghash(x, y, seed))));
    }
}
