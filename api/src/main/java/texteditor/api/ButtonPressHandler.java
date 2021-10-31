package texteditor.api;

import java.util.ResourceBundle;

public interface ButtonPressHandler
{
    String getButtonName();
    //TODO: Remove bundle from declaration and have in Control implementation with getLocaleBundle() method?
    void buttonPressed(ResourceBundle bundle);
}
