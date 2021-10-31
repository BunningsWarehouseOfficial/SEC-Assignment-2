package texteditor;

import texteditor.api.ButtonPressHandler;
import texteditor.api.Control;
import texteditor.api.EditorPlugin;

import java.util.ResourceBundle;

public class FindPlugin implements EditorPlugin
{
    @Override
    public void start(Control api)
    {
        api.registerButtonPressHandler(new ButtonPressHandler()
        {
            @Override
            public String getButtonName()
            {
                return "Find"; //TODO: i18n?
            }

            @Override
            public void buttonPressed(ResourceBundle bundle)
            {
                find(api);
            }
        });

        api.registerFunctionKeyHandler(functionKey -> {
            if (functionKey.equals("F3"))
            {
                find(api);
            }
        });
    }

    @Override
    public String getDisplayName()
    {
        return "Finder"; //TODO: i18n?;
    }

    private void find(Control api)
    {
        String prompt     = "Enter your search term"; //TODO: i18n?
        String searchTerm = api.requestUserTextInput(prompt);
        String text       = api.getText();
        int caretPosition = api.getCaretPosition();

        //TODO: normalisation? check announcements/notes/discord

        String searchSpace = text.substring(caretPosition);
        int start = searchSpace.indexOf(searchTerm);
        if (start != -1)
        {
            start += caretPosition;
            int end = start + searchTerm.length();
            api.updateTextSelection(start, end);
        }
        else
        {
            api.promptUser("Search term could not be found after caret position."); //TODO: i18n?
        }
    }
}
