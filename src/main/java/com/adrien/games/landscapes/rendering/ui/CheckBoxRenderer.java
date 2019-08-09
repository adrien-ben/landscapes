package com.adrien.games.landscapes.rendering.ui;


import com.adrien.games.landscapes.ui.controls.CheckBox;
import com.adrienben.games.bagl.core.Color;
import com.adrienben.games.bagl.engine.rendering.shape.UIRenderer;
import com.adrienben.games.bagl.engine.rendering.text.Font;
import com.adrienben.games.bagl.engine.rendering.text.Text;
import com.adrienben.games.bagl.engine.rendering.text.TextRenderer;
import com.adrienben.games.bagl.opengl.BlendMode;
import com.adrienben.games.bagl.opengl.OpenGL;
import org.lwjgl.opengl.GL11;

/**
 * Check box renderer
 *
 * @author adrien
 */
public class CheckBoxRenderer {

    private static final Color BACK_COLOR = new Color(1.0f, 1.0f, 1.0f, 0.3f);
    private static final Color FILL_COLOR = new Color(1.0f, 0.0f, 0.0f, 0.5f);

    private final UIRenderer uiRenderer;
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
        final var text = Text.create(checkBox.getLabel(), font, checkBox.getX() + checkBox.getWidth(), checkBox.getY(), checkBox.getHeight(), Color.WHITE);
        this.textRenderer.render(text);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        OpenGL.setBlendMode(BlendMode.TRANSPARENCY);
        this.uiRenderer.renderBox(checkBox.getX(), checkBox.getY(), checkBox.getWidth(), checkBox.getHeight(), BACK_COLOR);
        if (checkBox.isChecked()) {
            final float leftMargin = checkBox.getWidth() * 0.05f;
            final float bottomMargin = checkBox.getHeight() * 0.05f;
            this.uiRenderer.renderBox(checkBox.getX() + leftMargin, checkBox.getY() + bottomMargin, checkBox.getWidth() - leftMargin * 2,
                    checkBox.getHeight() - bottomMargin * 2, FILL_COLOR);
        }
    }

}
