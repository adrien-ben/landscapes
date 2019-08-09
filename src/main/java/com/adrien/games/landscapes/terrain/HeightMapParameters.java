package com.adrien.games.landscapes.terrain;

/**
 * Parameters for the generation of height maps
 * <p>
 * Usage example : HeightMapParameters parameters = HeightMapParameters.create().width(100).height(100).octaves(6);
 * <p>
 * The available parameters are :
 * <ul>
 * <li>width       width of the map. default = 10
 * <li>depth       depth of the map. default = 10
 * <li>scale       scale of the heights. default = 1
 * <li>frequency   frequency of the noise generation. default = 0.01
 * <li>octaves     number of octaves used to generate heights. default = 1
 * <li>persistence persistence of the noise generator. default = 1
 * <li>exponent    exponent used to alter noise generator result. default = 1
 *
 * @author adrien
 */
public class HeightMapParameters {

    /**
     * The width of the map
     */
    private int width = 10;

    /**
     * The depth of the map
     */
    private int depth = 10;

    /**
     * The scale of the height of the map
     */
    private int scale = 1;

    /**
     * The frequency of generation
     */
    private float frequency = 0.01f;

    /**
     * The number of octaves of generation
     */
    private int octaves = 1;

    /**
     * The persistence of the generation
     */
    private float persistence = 1;

    /**
     * The exponent use to alter the generated heights
     */
    private float exponent = 1;

    /**
     * Create a new default instance of the parameters
     *
     * @return An new instance of {@link HeightMapParameters}
     */
    public static HeightMapParameters create() {
        return new HeightMapParameters();
    }

    private HeightMapParameters() {
    }

    public HeightMapParameters width(int width) {
        this.width = width;
        return this;
    }

    public HeightMapParameters depth(int depth) {
        this.depth = depth;
        return this;
    }

    public HeightMapParameters scale(int scale) {
        this.scale = scale;
        return this;
    }

    public HeightMapParameters frequency(float frequency) {
        this.frequency = frequency;
        return this;
    }

    public HeightMapParameters octaves(int octaves) {
        this.octaves = octaves;
        return this;
    }

    public HeightMapParameters persistence(float persistence) {
        this.persistence = persistence;
        return this;
    }

    public HeightMapParameters exponent(float exponent) {
        this.exponent = exponent;
        return this;
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

    public float getFrequency() {
        return frequency;
    }

    public int getOctaves() {
        return octaves;
    }

    public float getPersistence() {
        return persistence;
    }

    public float getExponent() {
        return exponent;
    }

}
