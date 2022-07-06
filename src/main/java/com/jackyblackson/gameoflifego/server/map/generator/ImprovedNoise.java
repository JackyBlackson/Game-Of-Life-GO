package com.jackyblackson.gameoflifego.server.map.generator;

import com.jackyblackson.gameoflifego.server.info.WorldInfo;
import java.util.Random;

public final class ImprovedNoise {
    static public double noise(double x, double y, double z, double scale) {
        //System.out.println("Pos : " + x + " " + y + " " + z);
        x /= scale;
        y /= scale;
        z /= scale;
        int X = (int)Math.floor(x) & 255,                  // FIND UNIT CUBE THAT
                Y = (int)Math.floor(y) & 255,                  // CONTAINS POINT.
                Z = (int)Math.floor(z) & 255;
        x -= Math.floor(x);                                // FIND RELATIVE X,Y,Z
        y -= Math.floor(y);                                // OF POINT IN CUBE.
        z -= Math.floor(z);
        double u = fade(x),                                // COMPUTE FADE CURVES
                v = fade(y),                                // FOR EACH OF X,Y,Z.
                w = fade(z);
        int A = p[X  ]+Y, AA = p[A]+Z, AB = p[A+1]+Z,      // HASH COORDINATES OF
                B = p[X+1]+Y, BA = p[B]+Z, BB = p[B+1]+Z;      // THE 8 CUBE CORNERS,

        return (lerp(w, lerp(v, lerp(u, grad(p[AA  ], x  , y  , z   ),  // AND ADD
                                grad(p[BA  ], x-1, y  , z   )), // BLENDED
                        lerp(u, grad(p[AB  ], x  , y-1, z   ),  // RESULTS
                                grad(p[BB  ], x-1, y-1, z   ))),// FROM  8
                lerp(v, lerp(u, grad(p[AA+1], x  , y  , z-1 ),  // CORNERS
                                grad(p[BA+1], x-1, y  , z-1 )), // OF CUBE
                        lerp(u, grad(p[AB+1], x  , y-1, z-1 ),
                                grad(p[BB+1], x-1, y-1, z-1 ))))
                + 1.0d) / 2.0d;
    }
    static double fade(double t) { return t * t * t * (t * (t * 6 - 15) + 10); }
    static double lerp(double t, double a, double b) { return a + t * (b - a); }
    static double grad(int hash, double x, double y, double z) {
        int h = hash & 15;                      // CONVERT LO 4 BITS OF HASH CODE
        double u = h<8 ? x : y,                 // INTO 12 GRADIENT DIRECTIONS.
                v = h<4 ? y : h==12||h==14 ? x : z;
        return ((h&1) == 0 ? u : -u) + ((h&2) == 0 ? v : -v);
    }
    static final int p[] = genSeededPermutation(WorldInfo.SeedOfWorld);

    private static int[] genSeededPermutation(long seed){
        int[] p = new int[512];
        Random rand = new Random(seed);
        for(int i = 0; i < p.length; i++){
            int a = rand.nextInt() % 256;
            p[i] = (a > 0) ? a : -a;
        }
        return p;
    }

}
