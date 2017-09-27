package com.adrien.games.landscapes.rendering;

import com.adrien.games.bagl.core.Color;
import com.adrien.games.bagl.rendering.shape.UIRenderer;
import com.adrien.games.landscapes.ui.Slider;

/**
 * Renders sliders.
 */
public class SliderRenderer {

    /**
     * The back color of the slider.
     */
    private static final Color BACK_COLOR = new Color(1.0f, 1.0f, 1.0f, 0.3f);

    /**
     * The color of the slider's selected portion part
     */
    private static final Color FILL_COLOR = new Color(1.0f, 0.0f, 0.0f, 0.5f);

    /**
     * The ui renderer.
     */
    private UIRenderer uiRenderer;

    public SliderRenderer(final UIRenderer uiRenderer) {
        this.uiRenderer = uiRenderer;
    }

    /**
     * Renders a slider.
     *
     * @param slider The slider to render.
     */
    public void render(final Slider slider) {
        this.uiRenderer.renderBox(slider.getX(), slider.getY(), slider.getWidth(), slider.getHeight(), BACK_COLOR);
        final float range = slider.getMax() - slider.getMin();
        final float advance = slider.getValue() - slider.getMin();
        final float position = advance / range;
        this.uiRenderer.renderBox(slider.getX(), slider.getY(), slider.getWidth() * position, slider.getHeight(), FILL_COLOR);
    }

}
