package edu.curtin.krados.comp3003.assignment2;

//TODO: Move to not be in original src directory
//
//TODO: Change project structure to allow "texteditor.DatePlugin"
//Implemented by each plugin
public class API
{
    //TODO: No mention of GUI objects

    //Implemented by each plugin
    public interface EditorPlugin
    {
        void start(Control api);
    }

    //Implemented by main application
    public interface Control
    {
        void registerTextUpdater(TextUpdater callback);
        void registerSelectionUpdater(SelectionUpdater callback);

        void registerTextUpdateHandler(TextUpdateHandler callback);
        void registerFunctionKeyHandler(FunctionKeyHandler callback);
        void registerButtonPressHandler(ButtonPressHandler callback);
    }

    //Classes optionally implemented by plugins
    public interface SelectionUpdater
    {
        String updateSelection();
    }
    public interface TextUpdater
    {
        String updateText();
        int updateCaretPosition();
    }
    public interface TextUpdateHandler
    {
        void textChanged(String newText);
    }
    public interface FunctionKeyHandler
    {
        //TODO: Is there a 'key' class to use instead of string?
        void functionKeyPressed(String functionKey);
    }
    public interface ButtonPressHandler
    {
        void buttonPressed(String btnName);
    }
}
