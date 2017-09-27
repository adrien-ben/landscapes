package com.adrien.games.landscapes.ui;

import com.adrien.games.bagl.utils.MathUtils;

/**
 * A slider is a ui control that has value that is contained
 * between min and max values.
 */
public class Slider {

    /**
     * The id of the slider.
     */
    private final String id;

    /**
     * The x position of the slider.
     */
    private final float x;

    /**
     * The y position of the slider.
     */
    private final float y;

    /**
     * The width of the slider.
     */
    private final float width;

    /**
     * The height of the slider.
     */
    private final float height;

    /**
     * The minimum value.
     */
    private final float min;

    /**
     * The maximum value.
     */
    private final float max;

    /**
     * The step used to increment/decrement the value.
     */
    private final float step;

    /**
     * The current value.
     */
    private float value;

    public Slider(final String id, final float x, final float y, final float width, final float height, final float min,
                  final float max, final float step, final float defaultValue) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.min = min;
        this.max = max;
        this.step = step;
        this.value = defaultValue;
    }

    /**
     * Adds the step of the slider to its current value.
     */
    public void increment() {
        this.setValue(this.value + this.step);
    }

    /**
     * Subs the step of the slider to the current value.
     */
    public void decrement() {
        this.setValue(this.value - this.step);
    }

    /**
     * Sets the value of the slider and make sure that it is contained
     * between the min and max values.
     *
     * @param value The value to set.
     */
    public void setValue(float value) {
        this.value = MathUtils.clamp(value, this.min, this.max);
    }

    public String getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public float getStep() {
        return step;
    }

    public float getValue() {
        return value;
    }

}
