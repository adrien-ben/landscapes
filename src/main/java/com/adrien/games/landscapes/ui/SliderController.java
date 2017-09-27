package com.adrien.games.landscapes.ui;

import com.adrien.games.bagl.core.Configuration;
import com.adrien.games.bagl.core.Input;
import com.adrien.games.bagl.core.math.Vector2;
import org.lwjgl.glfw.GLFW;

/**
 * The slider controller is responsible for checking id the slider control
 * is interacted with. If so it is responsible to compute and set the new
 * value and to notify the listener.
 */
public class SliderController {

    /**
     * The slider to control.
     */
    private Slider slider;

    /**
     * The listener to notify.
     */
    private SliderListener listener;

    public SliderController(final Slider slider, final SliderListener listener) {
        this.slider = slider;
        this.listener = listener;
    }

    /**
     * Checks if the slider was interacted with and reacts accordingly.
     */
    public void update() {
        if (Input.wasMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
            final Vector2 mousePosition = Input.getMousePosition();
            final Configuration config = Configuration.getInstance();
            final int xResolution = config.getXResolution();
            final int yResolution = config.getYResolution();
            final int left = (int) (this.slider.getX() * xResolution);
            final int right = (int) (left + this.slider.getWidth() * xResolution);
            final int bottom = (int) (this.slider.getY() * yResolution);
            final int top = (int) (bottom + this.slider.getHeight() * yResolution);

            if (mousePosition.getX() >= left && mousePosition.getX() <= right && mousePosition.getY() >= bottom && mousePosition.getY() <= top) {
                final float normalizedMouseX = mousePosition.getX() / xResolution;
                final float sliderPercentage = (normalizedMouseX - this.slider.getX()) / slider.getWidth();
                final float exactValue = this.slider.getMin() + (this.slider.getMax() - this.slider.getMin()) * sliderPercentage;
                final float step = this.slider.getStep();
                final float remaining = exactValue % step;
                final float newValue = remaining < step / 2 ? exactValue - remaining : exactValue + step - remaining;
                this.slider.setValue(newValue);
                this.listener.onChange(newValue);
            }
        }
    }

}
