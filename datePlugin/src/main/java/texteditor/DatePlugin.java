package texteditor;

import texteditor.api.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

public class DatePlugin implements EditorPlugin
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
                String oldText = api.getText();
                int caretPosition = api.getCaretPosition();

                //Insert datetime into text
                ZonedDateTime datetime = ZonedDateTime.now();
                DateTimeFormatter pattern = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)
                        .withLocale(getBundle(api).getLocale());
                String insertionString = datetime.format(pattern);
                String newText = oldText.substring(0, caretPosition) + insertionString +
                        oldText.substring(caretPosition);
                api.updateText(newText);

                //Move the caret to end of new datetime text
                int insertionLength = insertionString.length();
                api.updateCaretPosition(caretPosition + insertionLength);
            }
        });
    }

    @Override
    public String getDisplayName(Control api)
    {
        return getBundle(api).getString("display_name");
    }

    private ResourceBundle getBundle(Control api)
    {
        //Retrieving locale for internationalisation
        Locale locale = api.getLocale();
        if(locale != null) //E.g. If localeString == 'hr-HR'
        {
            return ResourceBundle.getBundle("datebundle", locale);
        }
        else
        {
            return ResourceBundle.getBundle("datebundle");
        }
    }
}
