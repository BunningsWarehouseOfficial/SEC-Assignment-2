package edu.curtin.krados.comp3003.assignment2;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Entry point for the bus timetabling app. It can be invoked with the command-line parameter 
 * --locale=[value].
 */
public class BusTimetableApp extends Application
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
        if(localeString != null)
        {
            System.out.println("specified locale");
            System.out.println(localeString);
            Locale locale = Locale.forLanguageTag(localeString);
            bundle = ResourceBundle.getBundle("bundle", locale);
        }
        else
        {
            bundle = ResourceBundle.getBundle("bundle");
        }

        //TODO: Use locale var
        var entries = FXCollections.<TimetableEntry>observableArrayList();
        FileIO fileIO = new FileIO();
        LoadSaveUI loadSaveUI = new LoadSaveUI(stage, entries, fileIO, bundle);
        AddUI addUI = new AddUI(entries, bundle);
        new MainUI(stage, entries, loadSaveUI, addUI, bundle).display();
    }
}
