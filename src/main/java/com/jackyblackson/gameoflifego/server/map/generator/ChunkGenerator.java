package com.jackyblackson.gameoflifego.server.map.generator;

import com.jackyblackson.gameoflifego.server.info.Importance;
import com.jackyblackson.gameoflifego.server.info.Pos;
import com.jackyblackson.gameoflifego.server.info.WorldInfo;
import com.jackyblackson.gameoflifego.server.map.chunk.Chunk;
import com.jackyblackson.gameoflifego.server.tiles.*;

import static com.jackyblackson.gameoflifego.server.logger.Logger.Log;

public class ChunkGenerator {
    public static Chunk generate(Pos chunkPos) {
        Chunk c = new Chunk(chunkPos);

        //generating using berlin noise
        improvedGenerating(c);

        //featuring process
        basicFeaturing(c);

        Log(Importance.INFO, "Generating chunks at " + chunkPos + " (Chunk Coordinate)");
        return c;
    }

    /**
     * This method is implemented to generate chunks with only vacuum(i.e. void) and asteroid(i.e. stone) using berlin noise with multi octaves.
     * This is an improved version of basic chunk generation algorithms.
     *
     * @param c The chunk that need to generate
     */
    private static void improvedGenerating(Chunk c) {
        Pos chunkPos = c.getChunkPos();
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                //get world position of the block that is now being generating
                Pos worldPos = Pos.getWorldPos(chunkPos, new Pos((double) x, (double) y));
                //get noise with multi-octave improved noise (Berlin noise)
                double randRes = WorldInfo.Octave1Amp * ImprovedNoise.noise(worldPos.getX(), worldPos.getY(), 0.0d, WorldInfo.NoiseScale) +
                        WorldInfo.Octave2Amp * ImprovedNoise.noise(worldPos.getX(), worldPos.getY(), 1.0d, WorldInfo.NoiseScale / 2.0d) +
                        WorldInfo.Octave3Amp * ImprovedNoise.noise(worldPos.getX(), worldPos.getY(), 2.0d, WorldInfo.NoiseScale / 4.0d);

                //if it is bigger than the threshold to summon asteroid, then place one at the position
                //otherwise, it will be placed by vacuum.
                if (randRes >= WorldInfo.AsteroidThreshold +
                        WorldInfo.NoiseDynamics *
                                (ImprovedNoise.noise(worldPos.getX(), worldPos.getY(), 3.0d, WorldInfo.NoiseScale * WorldInfo.DynamicsScale) + WorldInfo.DynamicsShift)
                ) {
                    c.setTileAt(x, y, new Asteroid(Pos.getWorldPos(chunkPos, new Pos((double) x, (double) y))));
                } else {
                    c.setTileAt(x, y, new Vacuum(Pos.getWorldPos(chunkPos, new Pos((double) x, (double) y))));
                }
            }
        }
    }

    /**
     * This is a basic solution of the Featuring process that intend to generate resources for players to create cells
     * However, this is a simple solution that may need to be replaced sometime after.
     *
     * @param c The chunk that need to be featured.
     */
    private static void basicFeaturing(Chunk c) {
        //Summon Carbon
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                Pos worldPos = Pos.getWorldPos(c.getChunkPos(), new Pos((double) x, (double) y));
                double noise = ImprovedNoise.noise(worldPos.getX(), worldPos.getY(), 1000.0d, WorldInfo.CarbonGenScale);
                if (noise >= WorldInfo.CarbonGenThreshold) {
                    if (c.getTileAt(x, y) instanceof Asteroid) {
                        c.setTileAt(x, y, new Carbon(worldPos));
                        //Log(Importance.DEBUG, "Featuring: Generated a Carbon");
                    }
                }
            }
        }

        //Summon Oxygen
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                Pos worldPos = Pos.getWorldPos(c.getChunkPos(), new Pos((double) x, (double) y));
                double noise = ImprovedNoise.noise(worldPos.getX(), worldPos.getY(), 2000.0d, WorldInfo.OxygenGenScale);
                if (noise >= WorldInfo.OxygenGenThreshold) {
                    if (c.getTileAt(x, y) instanceof Asteroid) {
                        c.setTileAt(x, y, new Oxygen(worldPos));
                        //Log(Importance.DEBUG, "Featuring: Generated a Oxygen");
                    }
                }
            }
        }

        //Summon Water
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                Pos worldPos = Pos.getWorldPos(c.getChunkPos(), new Pos((double) x, (double) y));
                double noise = ImprovedNoise.noise(worldPos.getX(), worldPos.getY(), 3000.0d, WorldInfo.WaterGenScale);
                if (noise >= WorldInfo.WaterGenThreshold) {
                    if (c.getTileAt(x, y) instanceof Asteroid) {
                        c.setTileAt(x, y, new Water(worldPos));
                        //Log(Importance.DEBUG, "Featuring: Generated a Water");
                    }
                }
            }
        }

    }
}
