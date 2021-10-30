package texteditor;

import texteditor.api.Control;
import texteditor.api.EditorPlugin;
import texteditor.api.TextUpdater;

import java.util.ResourceBundle;

public class DatePlugin implements EditorPlugin
{
    public DatePlugin() { }

    @Override
    public void start(Control api)
    {
        api.registerTextUpdater(new TextUpdater() {
            @Override
            public String getUpdatedText(ResourceBundle bundle)
            {
                return null;
            }

            @Override
            public int getNewCaretPosition()
            {
                return 0;
            }
        });
    }

    @Override
    public String getDisplayName()
    {
        return "Insert Date"; //TODO: i18n?
    }
}
