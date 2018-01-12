package com.adrien.games.landscapes.rendering.ui;

import com.adrien.games.bagl.core.Color;
import com.adrien.games.bagl.core.Engine;
import com.adrien.games.bagl.core.math.Vector2;
import com.adrien.games.bagl.rendering.BlendMode;
import com.adrien.games.bagl.rendering.shape.UIRenderer;
import com.adrien.games.bagl.rendering.text.Font;
import com.adrien.games.bagl.rendering.text.TextRenderer;
import com.adrien.games.landscapes.ui.controls.CheckBox;
import org.lwjgl.opengl.GL11;

/**
 * Check box renderer
 *
 * @author adrien
 */
public class CheckBoxRenderer {

    /** The back color of the slider */
    private static final Color BACK_COLOR = new Color(1.0f, 1.0f, 1.0f, 0.3f);

    /** The color of the slider's selected portion part */
    private static final Color FILL_COLOR = new Color(1.0f, 0.0f, 0.0f, 0.5f);

    /** The ui renderer */
    private final UIRenderer uiRenderer;

    /** The text renderer */
    private final TextRenderer textRenderer;

    public CheckBoxRenderer(final UIRenderer uiRenderer, final TextRenderer textRenderer) {
        this.uiRenderer = uiRenderer;
        this.textRenderer = textRenderer;
    }

    /**
     * Render a checkbox
     *
     * @param checkBox The check box to render
     * @param font     The font to use to render text
     */
    public void render(final CheckBox checkBox, final Font font) {
        this.textRenderer
                .render(checkBox.getLabel(), font, new Vector2(checkBox.getX() + checkBox.getWidth(), checkBox.getY()), checkBox.getHeight() * 1f,
                        Color.WHITE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        Engine.setBlendMode(BlendMode.TRANSPARENCY);
        this.uiRenderer.renderBox(checkBox.getX(), checkBox.getY(), checkBox.getWidth(), checkBox.getHeight(), BACK_COLOR);
        if (checkBox.isChecked()) {
            final float leftMargin = checkBox.getWidth() * 0.05f;
            final float bottomMargin = checkBox.getHeight() * 0.05f;
            this.uiRenderer.renderBox(checkBox.getX() + leftMargin, checkBox.getY() + bottomMargin, checkBox.getWidth() - leftMargin * 2,
                    checkBox.getHeight() - bottomMargin * 2, FILL_COLOR);
        }
    }

}
