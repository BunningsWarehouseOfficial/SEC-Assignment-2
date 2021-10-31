package texteditor.api;

//Implemented by main application
public interface Control
{
    String getLocaleString(); //TODO: revisit

    String getText();
    void updateText(String newText);

    int getCaretPosition();
    void updateCaretPosition(int newPosition);

    //TODO: retrieveSelection() ?
    //TODO: updateSelection() (what parameters? 2 ints?)

    //TODO: refactor to be like above functions and not a callback?
    void registerUserTextInputCollector(UserTextInputCollector callback);

    void registerTextUpdateHandler(TextUpdateHandler callback);
    void registerFunctionKeyHandler(FunctionKeyHandler callback);
    void registerButtonPressHandler(ButtonPressHandler callback);
}
