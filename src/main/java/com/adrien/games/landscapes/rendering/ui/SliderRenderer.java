package com.adrien.games.landscapes.rendering.ui;

import com.adrien.games.landscapes.ui.controls.Slider;
import com.adrienben.games.bagl.core.Color;
import com.adrienben.games.bagl.engine.rendering.shape.UIRenderer;
import com.adrienben.games.bagl.engine.rendering.text.Font;
import com.adrienben.games.bagl.engine.rendering.text.Text;
import com.adrienben.games.bagl.engine.rendering.text.TextRenderer;
import com.adrienben.games.bagl.opengl.BlendMode;
import com.adrienben.games.bagl.opengl.OpenGL;
import org.lwjgl.opengl.GL11;

/**
 * Renders sliders
 *
 * @author adrien
 */
public class SliderRenderer {

    private static final Color BACK_COLOR = new Color(1.0f, 1.0f, 1.0f, 0.3f);
    private static final Color FILL_COLOR = new Color(1.0f, 0.0f, 0.0f, 0.5f);

    private final UIRenderer uiRenderer;
    private final TextRenderer textRenderer;

    public SliderRenderer(final UIRenderer uiRenderer, final TextRenderer textRenderer) {
        this.uiRenderer = uiRenderer;
        this.textRenderer = textRenderer;
    }

    /**
     * Render a slider
     *
     * @param slider The slider to render
     * @param font   The font to use to render text
     */
    public void render(final Slider slider, final Font font) {
        final var textContent = slider.getLabel() + " : " + slider.getValue() + " [" + slider.getMin() + "; " + slider.getMax() + "]";
        final var text = Text.create(textContent, font, slider.getX(), slider.getY() + slider.getHeight(), slider.getHeight() * 1.6f, Color.WHITE);
        this.textRenderer.render(text);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        OpenGL.setBlendMode(BlendMode.TRANSPARENCY);
        this.uiRenderer.renderBox(slider.getX(), slider.getY(), slider.getWidth(), slider.getHeight(), BACK_COLOR);
        final float range = slider.getMax() - slider.getMin();
        final float advance = slider.getValue() - slider.getMin();
        final float position = advance / range;
        this.uiRenderer.renderBox(slider.getX(), slider.getY(), slider.getWidth() * position, slider.getHeight(), FILL_COLOR);
    }

}
