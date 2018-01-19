package com.adrien.games.landscapes.ui;

import com.adrien.games.bagl.core.Configuration;
import com.adrien.games.bagl.core.Input;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public abstract class UIController {

    private final UIControl control;

    protected UIController(final UIControl control) {
        this.control = control;
    }

    public void update() {
        if (Input.wasMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
            final Vector2f mousePosition = Input.getMousePosition();
            final Configuration config = Configuration.getInstance();
            final int xResolution = config.getXResolution();
            final int yResolution = config.getYResolution();
            final int left = (int) (this.control.getX() * xResolution);
            final int right = (int) (left + this.control.getWidth() * xResolution);
            final int bottom = (int) (this.control.getY() * yResolution);
            final int top = (int) (bottom + this.control.getHeight() * yResolution);

            if (mousePosition.x() >= left && mousePosition.x() <= right && mousePosition.y() >= bottom && mousePosition.y() <= top) {
                this.onClick();
            }
        }
    }

    protected abstract void onClick();

}
