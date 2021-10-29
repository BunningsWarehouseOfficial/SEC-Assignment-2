package texteditor;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Entry point for the bus timetabling app. It can be invoked with the command-line parameter 
 * --locale=[value].
 */
public class BusTimetableApp_Old extends Application
{
    public static void main(String[] args)
    {
        Application.launch(args);
    }
        
    @Override
    public void start(Stage stage)
    {
        ResourceBundle bundle;
        var localeString = getParameters().getNamed().get("locale");
        if(localeString != null)  //E.g. If localeString == 'hr-HR'
        {
            Locale locale = Locale.forLanguageTag(localeString);
            bundle = ResourceBundle.getBundle("bundle", locale);
        }
        else
        {
            bundle = ResourceBundle.getBundle("bundle");
        }

        //TODO: Use locale var
        var entries = FXCollections.<TimetableEntry_Old>observableArrayList();
        FileIO_Old fileIO = new FileIO_Old();
        LoadSaveUI_Old loadSaveUI = new LoadSaveUI_Old(stage, entries, fileIO, bundle);
        AddUI_Old addUI = new AddUI_Old(entries, bundle);
        new MainUI_Old(stage, entries, loadSaveUI, addUI, bundle).display();
    }
}
