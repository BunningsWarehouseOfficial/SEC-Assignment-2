package texteditor;

import texteditor.api.ButtonPressHandler;
import texteditor.api.Control;
import texteditor.api.EditorPlugin;

import java.text.Normalizer;
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
        String prompt     = "Enter your (case sensitive) search term."; //TODO: i18n?
        String searchTerm = Normalizer.normalize(api.requestUserTextInput(prompt), Normalizer.Form.NFKC);
        String text       = api.getText();
        int caretPosition = api.getCaretPosition();

        String searchSpace = Normalizer.normalize(text.substring(caretPosition), Normalizer.Form.NFKC);
        int start = searchSpace.indexOf(searchTerm); //Search for the term and return its position
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
