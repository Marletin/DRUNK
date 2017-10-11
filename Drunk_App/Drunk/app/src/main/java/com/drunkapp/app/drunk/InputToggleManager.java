package com.drunkapp.app.drunk;

import android.view.inputmethod.InputMethodManager;

/**
 * Created by jonas on 25.09.2017.
 */

public class InputToggleManager {

    void toggleInputShown(InputMethodManager imm)
    {
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

    void toggleInputHidden(InputMethodManager imm)
    {
        imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
