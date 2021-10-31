package texteditor.api;

import java.util.ResourceBundle;

//Implemented by each plugin
public interface EditorPlugin
{
    void start(Control api);
    String getDisplayName(Control api);
}
