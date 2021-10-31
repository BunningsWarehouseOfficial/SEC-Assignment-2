package texteditor.api;

import java.util.Locale;

//Implemented by main application
public interface Control
{
    Locale getLocale();

    String getText();
    void updateText(String newText);

    int getCaretPosition();
    void updateCaretPosition(int newPosition);
    void updateTextSelection(int start, int end);

    void promptUser(String prompt);
    String requestUserTextInput(String prompt);

    void registerTextUpdateHandler(TextUpdateHandler callback);
    void registerFunctionKeyHandler(FunctionKeyHandler callback);
    void registerHotkeyHandler(HotkeyHandler callback);
    void registerButtonPressHandler(ButtonPressHandler callback);
}
