package com.adrien.games.landscapes.ui;

import com.adrien.games.bagl.rendering.shape.UIRenderer;
import com.adrien.games.bagl.rendering.text.Font;
import com.adrien.games.bagl.rendering.text.TextRenderer;
import com.adrien.games.landscapes.rendering.SliderRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * UI manager.
 * <p>
 * This class updates a renderer all added controls.
 */
public class UI {

    /**
     * The slider renderer.
     */
    private final SliderRenderer sliderRenderer;

    /**
     * The renderer's font.
     */
    private Font font;

    /**
     * The slider controllers.
     */
    private final List<SliderController> sliderControllers;

    /**
     * The sliders.
     */
    private final List<Slider> sliders;

    /**
     * Creates the ui manager.
     *
     * @param uiRenderer The renderer to use to render ui.
     */
    public UI(final UIRenderer uiRenderer, final TextRenderer textRenderer, final Font font) {
        this.sliderRenderer = new SliderRenderer(uiRenderer, textRenderer);
        this.font = font;
        this.sliderControllers = new ArrayList<>();
        this.sliders = new ArrayList<>();
    }

    /**
     * Adds a slider to the manager.
     * <p>
     * When added a slider is automatically assigned a controller.
     *
     * @param slider   The slider to add.
     * @param listener The slider listener.
     */
    public void addSlider(final Slider slider, final SliderListener listener) {
        this.sliderControllers.add(new SliderController(slider, listener));
        this.sliders.add(slider);
    }

    /**
     * Updates all controllers.
     */
    public void update() {
        this.sliderControllers.forEach(SliderController::update);
    }

    /**
     * Renders all sliders.
     */
    public void render() {
        this.sliders.forEach(slider -> this.sliderRenderer.render(slider, this.font));
    }

}
