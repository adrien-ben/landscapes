package com.adrien.games.landscapes.ui;

import com.adrien.games.bagl.rendering.shape.UIRenderer;
import com.adrien.games.bagl.rendering.text.Font;
import com.adrien.games.bagl.rendering.text.TextRenderer;
import com.adrien.games.landscapes.rendering.ui.CheckBoxRenderer;
import com.adrien.games.landscapes.rendering.ui.SliderRenderer;
import com.adrien.games.landscapes.ui.controllers.CheckBoxController;
import com.adrien.games.landscapes.ui.controllers.CheckBoxListener;
import com.adrien.games.landscapes.ui.controllers.SliderController;
import com.adrien.games.landscapes.ui.controllers.SliderListener;
import com.adrien.games.landscapes.ui.controls.CheckBox;
import com.adrien.games.landscapes.ui.controls.Slider;

import java.util.ArrayList;
import java.util.List;

/**
 * UI manager
 * <p>
 * This class updates a renderer all added controls
 *
 * @author adrien
 */
public class UI {

    /** The slider renderer */
    private final SliderRenderer sliderRenderer;

    /** The checkbox renderer */
    private final CheckBoxRenderer checkBoxRenderer;

    /** The renderer's font */
    private final Font font;

    /** The sliders */
    private final List<Slider> sliders;

    /** The checkboxes */
    private final List<CheckBox> checkBoxes;

    /** UI controllers */
    private final List<UIController> controllers;

    /**
     * Create the ui manager
     *
     * @param uiRenderer The renderer to use to render ui
     */
    public UI(final UIRenderer uiRenderer, final TextRenderer textRenderer, final Font font) {
        this.sliderRenderer = new SliderRenderer(uiRenderer, textRenderer);
        this.checkBoxRenderer = new CheckBoxRenderer(uiRenderer, textRenderer);
        this.font = font;
        this.sliders = new ArrayList<>();
        this.checkBoxes = new ArrayList<>();
        this.controllers = new ArrayList<>();
    }

    /**
     * Add a slider to the manager
     * <p>
     * When added a slider is automatically assigned a controller
     *
     * @param slider   The slider to add
     * @param listener The slider listener
     */
    public void add(final Slider slider, final SliderListener listener) {
        this.controllers.add(new SliderController(slider, listener));
        this.sliders.add(slider);
    }

    /**
     * Add a checkbox the the manager
     * <p>
     * When added a slider is automatically assigned a controller
     *
     * @param checkBox The checkbox to add
     * @param listener The checkbox listener
     */
    public void add(final CheckBox checkBox, final CheckBoxListener listener) {
        this.controllers.add(new CheckBoxController(checkBox, listener));
        this.checkBoxes.add(checkBox);
    }

    /**
     * Update all controllers
     */
    public void update() {
        this.controllers.forEach(UIController::update);
    }

    /**
     * Render all sliders
     */
    public void render() {
        this.sliders.forEach(slider -> this.sliderRenderer.render(slider, this.font));
        this.checkBoxes.forEach(checkBox -> this.checkBoxRenderer.render(checkBox, this.font));
    }

}
