package texteditor;

import texteditor.api.ButtonPressHandler;
import texteditor.api.Control;
import texteditor.api.EditorPlugin;

import java.text.Normalizer;
import java.util.Locale;
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
                return getBundle(api).getString("button_name");
            }

            @Override
            public void buttonPressed()
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
    public String getDisplayName(Control api)
    {
        return getBundle(api).getString("display_name");
    }

    private void find(Control api)
    {
        ResourceBundle bundle = getBundle(api);

        String prompt     = bundle.getString("find_prompt");
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
            api.promptUser(bundle.getString("find_error"));
        }
    }

    private ResourceBundle getBundle(Control api)
    {
        //Retrieving locale for internationalisation
        Locale locale = api.getLocale();
        if(locale != null) //E.g. If localeString == 'hr-HR'
        {
            return ResourceBundle.getBundle("findbundle", locale);
        }
        else
        {
            return ResourceBundle.getBundle("findbundle");
        }
    }
}
