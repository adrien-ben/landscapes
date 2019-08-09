package com.adrien.games.landscapes.ui;

/**
 * Base UI control
 *
 * @author adrien
 */
public abstract class UIControl {

    private final String id;
    private String label;
    private final float x;
    private final float y;
    private final float width;
    private final float height;

    /**
     * Create a new UI control
     * <p>
     * Positions and dimensions must be specified in screen space (ranging
     * from 0 to 1 where (0,0) is the bottom left corner of the screen)
     *
     * @param id     Identifier of the control
     * @param x      X position of the control
     * @param y      Y position of the control
     * @param width  Width of the control
     * @param height Height of the control
     */
    protected UIControl(final String id, final String label, final float x, final float y, final float width, final float height) {
        this.id = id;
        this.label = label;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
