package com.adrien.games.landscapes.rendering;

import com.adrien.games.bagl.core.Color;
import com.adrien.games.bagl.core.Engine;
import com.adrien.games.bagl.core.math.Vector2;
import com.adrien.games.bagl.rendering.BlendMode;
import com.adrien.games.bagl.rendering.shape.UIRenderer;
import com.adrien.games.bagl.rendering.text.Font;
import com.adrien.games.bagl.rendering.text.TextRenderer;
import com.adrien.games.landscapes.ui.Slider;
import org.lwjgl.opengl.GL11;

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

    /**
     * The text renderer.
     */
    private TextRenderer textRenderer;

    public SliderRenderer(final UIRenderer uiRenderer, final TextRenderer textRenderer) {
        this.uiRenderer = uiRenderer;
        this.textRenderer = textRenderer;
    }

    /**
     * Renders a slider.
     *
     * @param slider The slider to render.
     * @param font   The font to use to render text.
     */
    public void render(final Slider slider, final Font font) {
        final String text = slider.getId() + " : " + slider.getValue() + " [" + slider.getMin() + "; " + slider.getMax() + "]";
        this.textRenderer.render(text, font, new Vector2(slider.getX(), slider.getY() + slider.getHeight()), slider.getHeight() * 1.6f, Color.WHITE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        Engine.setBlendMode(BlendMode.TRANSPARENCY);
        this.uiRenderer.renderBox(slider.getX(), slider.getY(), slider.getWidth(), slider.getHeight(), BACK_COLOR);
        final float range = slider.getMax() - slider.getMin();
        final float advance = slider.getValue() - slider.getMin();
        final float position = advance / range;
        this.uiRenderer.renderBox(slider.getX(), slider.getY(), slider.getWidth() * position, slider.getHeight(), FILL_COLOR);
    }

}
