package texteditor;

import texteditor.api.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
                return "Insert Date"; //TODO: i18n?
            }

            @Override
            public void buttonPressed(ResourceBundle bundle)
            {
                String oldText = api.getText();
                int caretPosition = api.getCaretPosition();

                //Insert datetime into text
                ZonedDateTime datetime = ZonedDateTime.now();
                DateTimeFormatter pattern = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)
                        .withLocale(bundle.getLocale()); //TODO: revisit use of bundle vs. Locale vs. locale string
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
    public String getDisplayName()
    {
        return "Date Insertion Button"; //TODO: i18n?
    }
}
