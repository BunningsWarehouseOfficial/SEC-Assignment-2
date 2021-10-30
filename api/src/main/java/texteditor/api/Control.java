package texteditor.api;

//Implemented by main application
public interface Control
{
    void registerTextUpdater(TextUpdater callback);
    void registerSelectionUpdater(SelectionUpdater callback);

    void registerTextUpdateHandler(TextUpdateHandler callback);
    void registerFunctionKeyHandler(FunctionKeyHandler callback);
    void registerButtonPressHandler(ButtonPressHandler callback);
}
