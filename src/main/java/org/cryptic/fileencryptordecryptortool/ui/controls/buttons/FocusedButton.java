package org.cryptic.fileencryptordecryptortool.ui.controls.buttons;

public class FocusedButton extends javafx.scene.control.Button {
    public FocusedButton() {
        super();
        bindFocusToDefault();
    }

    private void bindFocusToDefault() {
        defaultButtonProperty().bind(focusedProperty());
    }
}
