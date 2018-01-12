package com.adrien.games.landscapes.ui.controllers;

import com.adrien.games.landscapes.ui.UIController;
import com.adrien.games.landscapes.ui.controls.CheckBox;

/**
 * Check box controller
 *
 * @author adrien
 */
public class CheckBoxController extends UIController {

    private CheckBox checkBox;

    private CheckBoxListener listener;

    public CheckBoxController(final CheckBox checkBox, final CheckBoxListener listener) {
        super(checkBox);
        this.checkBox = checkBox;
        this.listener = listener;
    }

    @Override
    protected void onClick() {
        this.listener.onChange(this.checkBox.toggle());
    }

}
