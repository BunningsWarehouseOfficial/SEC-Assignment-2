package texteditor.api;

import java.util.ResourceBundle;

public interface TextUpdater
{
    String getUpdatedText(ResourceBundle bundle);
    int getNewCaretPosition();
}
