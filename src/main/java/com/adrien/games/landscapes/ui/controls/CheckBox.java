package com.adrien.games.landscapes.ui.controls;

import com.adrien.games.landscapes.ui.UIControl;

/**
 * Checkbox control
 *
 * @author adrien
 */
public class CheckBox extends UIControl {

    /** Is the value selected */
    private boolean checked;

    public CheckBox(final String id, final String label, final float x, final float y, final float size, final boolean checked) {
        super(id, label, x, y, size, size);
        this.checked = checked;
    }

    /**
     * Toggle the checkbox
     */
    public boolean toggle() {
        return this.checked = !this.checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(final boolean checked) {
        this.checked = checked;
    }

}
