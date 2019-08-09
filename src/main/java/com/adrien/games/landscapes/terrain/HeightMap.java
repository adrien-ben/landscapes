package com.adrien.games.landscapes.terrain;


import com.adrienben.games.bagl.core.math.Noise;

/**
 * Height map
 * <p>
 * 2D map of heights build using Perlin noise algorithm
 *
 * @author adrien
 */
public class HeightMap {

    /**
     * The width of the map
     */
    private final int width;

    /**
     * The depth of the map
     */
    private final int depth;

    /**
     * The scale of heights
     */
    private final int scale;

    /**
     * The heights of the map
     */
    private final float[] heights;

    /**
     * Construct the height map
     * <p>
     * Heights will be generated in the range [0; 1] then scaled by scale parameter
     *
     * @param parameters The parameters of the height map.
     */
    public HeightMap(final HeightMapParameters parameters) {
        this.width = parameters.getWidth();
        this.depth = parameters.getDepth();
        this.scale = parameters.getScale();
        this.heights = new float[this.width * this.depth];
        for (int i = 0; i < this.width * this.depth; i++) {
            final int x = i / this.depth;
            final int z = i % this.depth;
            final float noise = Noise.perlin(x * parameters.getFrequency(), 0, z * parameters.getFrequency(), parameters.getOctaves(),
                    parameters.getPersistence());
            this.heights[i] = (float) Math.pow(noise, parameters.getExponent()) * this.scale;
        }
    }

    /**
     * Get the height at given coordinates
     *
     * @param x The x coordinate
     * @param z The z coordinate
     * @return The height a these coordinates
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

    public int getScale() {
        return scale;
    }

}
