package com.adrien.games.landscapes.terrain;

import com.adrien.games.bagl.core.math.Noise;

/**
 * Height map.
 * <p>
 * 2D map of heights build using Perlin noise algorithm.
 */
public class HeightMap {

    /**
     * The width of the map.
     */
    private final int width;

    /**
     * The depth of the map.
     */
    private final int depth;

    /**
     * The heights of the map.
     */
    private final float[] heights;

    /**
     * Constructs the height map.
     *
     * @param width       The width of the map.
     * @param depth       The depth of the map.
     * @param frequency   The frequency of the noise generation.
     * @param octaves     The number of octaves used to generate heights.
     * @param persistence The persistence of the noise generator.
     * @param exponent    The exponent used to alter noise generator result.
     */
    public HeightMap(final int width, final int depth, final float frequency, final int octaves, final float persistence, final float exponent) {
        this.width = width;
        this.depth = depth;
        this.heights = new float[this.width * this.depth];
        for (int i = 0; i < this.width * this.depth; i++) {
            final int x = i / this.depth;
            final int z = i % this.depth;
            final float noise = Noise.perlin(x * frequency, 0, z * frequency, octaves, persistence);
            this.heights[i] = (float) Math.pow(noise, exponent);
        }
    }

    /**
     * Gets the height at given coordinates.
     *
     * @param x The x coordinate.
     * @param z The z coordinate.
     * @return The height a these coordinates.
     */
    public float getHeight(final int x, final int z) {
        return this.heights[x * this.depth + z];
    }

    public int getWidth() {
        return width;
    }

    public int getDepth() {
        return depth;
    }

}
