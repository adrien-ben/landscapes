package com.adrien.games.landscapes.ui.controls;

import com.adrien.games.landscapes.ui.UIControl;
import com.adrienben.games.bagl.core.math.MathUtils;

/**
 * A slider is a ui control that has value that is contained
 * between min and max values
 *
 * @author adrien
 */
public class Slider extends UIControl {

    private final float min;
    private final float max;
    private final float step;
    private float value;

    public Slider(final String id, final String label, final float x, final float y, final float width, final float height, final float min,
                  final float max, final float step, final float defaultValue) {
        super(id, label, x, y, width, height);
        this.min = min;
        this.max = max;
        this.step = step;
        this.value = defaultValue;
    }

    /**
     * Sets the value of the slider and make sure that it is contained
     * between the min and max values
     *
     * @param value The value to set
     */
    public void setValue(float value) {
        this.value = MathUtils.clamp(value, this.min, this.max);
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
