package com.adrien.games.landscapes.ui.controllers;

import com.adrien.games.landscapes.ui.UIController;
import com.adrien.games.landscapes.ui.controls.Slider;
import com.adrienben.games.bagl.engine.Configuration;
import com.adrienben.games.bagl.engine.Input;

/**
 * The slider controller is responsible for checking id the slider control
 * is interacted with. If so it is responsible to compute and set the new
 * value and to notify the listener
 *
 * @author adrien
 */
public class SliderController extends UIController {

    private final Slider slider;
    private final SliderListener listener;

    public SliderController(final Slider slider, final SliderListener listener) {
        super(slider);
        this.slider = slider;
        this.listener = listener;
    }

    @Override
    protected void onClick() {
        final var mousePosition = Input.getMousePosition();
        final float normalizedMouseX = mousePosition.x() / Configuration.getInstance().getXResolution();
        final float sliderPercentage = (normalizedMouseX - this.slider.getX()) / slider.getWidth();
        final float exactValue = this.slider.getMin() + (this.slider.getMax() - this.slider.getMin()) * sliderPercentage;
        final float step = this.slider.getStep();
        final float remaining = exactValue % step;
        final float newValue = remaining < step / 2 ? exactValue - remaining : exactValue + step - remaining;
        this.slider.setValue(newValue);
        this.listener.onChange(newValue);
    }

}
