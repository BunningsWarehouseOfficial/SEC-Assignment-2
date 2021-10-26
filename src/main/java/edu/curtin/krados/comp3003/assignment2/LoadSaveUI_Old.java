package edu.curtin.krados.comp3003.assignment2;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;


/** 
 * Controls those parts of the user interface relating to loading/saving timetable CSV files. For 
 * both loading and saving, we first display a 'file chooser' window, and then a second dialog box
 * to select the file encoding.
 */
public class LoadSaveUI_Old
{
    private static final int SPACING = 8;
    
    private Stage stage;
    private ObservableList<TimetableEntry_Old> entries;
    private FileIO_Old fileIO;
    private FileChooser fileDialog = new FileChooser();
    private Dialog<String> encodingDialog;
    private ResourceBundle bundle;
    
    public LoadSaveUI_Old(Stage stage, ObservableList<TimetableEntry_Old> entries, FileIO_Old fileIO, ResourceBundle bundle)
    {
        this.stage = stage;
        this.entries = entries;
        this.fileIO = fileIO;
        this.bundle = bundle;
    }
    
    /**
     * Internal method for displaying the encoding dialog and retrieving the name of the chosen 
     * encoding.
     */
    private String getEncoding()
    {
        if(encodingDialog == null)
        {
            var encodingComboBox = new ComboBox<String>();
            var content = new FlowPane();
            encodingDialog = new Dialog<>();
            encodingDialog.setTitle(bundle.getString("load_save_title"));
            encodingDialog.getDialogPane().setContent(content);
            encodingDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);        
            encodingDialog.setResultConverter(
                btn -> (btn == ButtonType.OK) ? encodingComboBox.getValue() : null);
            
            content.setHgap(SPACING);
            content.getChildren().setAll(new Label(bundle.getString("encoding_label")), encodingComboBox);
            
            encodingComboBox.getItems().setAll("UTF-8", "UTF-16", "UTF-32");
            encodingComboBox.setValue("UTF-8");
        }        
        return encodingDialog.showAndWait().orElse(null);
    }

    /**
     * Asks the user to choose a file to load, then an encoding, then loads the file contents and 
     * updates the timetable.
     */
    public void load()
    {
        fileDialog.setTitle(bundle.getString("load_title"));
    
        File f = fileDialog.showOpenDialog(stage);
        if(f != null)
        {
            String encoding = getEncoding();
            if(encoding != null)
            {
                // FIXME: encoding is not actually used yet. We currently just use the default encoding, whatever the user selects.
            
                try
                {
                    entries.setAll(fileIO.load(f, bundle));
                }
                catch(IOException | TimetableFormatException_Old e)
                {
                    new Alert(
                        Alert.AlertType.ERROR, 
                        String.format(bundle.getString("load_error") + " %s - %s", e.getClass().getName(), e.getMessage()),
                        ButtonType.CLOSE
                    ).showAndWait();
                }                
            }
        }
    }
    
    /**
     * Asks the user to choose a filename to save the timetable under, then an encoding, then 
     * saves the timetable contents to the chosen file in the chosen encoding.
     */
    public void save()
    {
        fileDialog.setTitle(bundle.getString("save_title"));
        File f = fileDialog.showSaveDialog(stage);
        if(f != null)
        {
            String encoding = getEncoding();
            if(encoding != null)
            {
                // FIXME: encoding is not actually used yet. We currently just save to the default encoding, whatever the user selects.
                
                try
                {
                    fileIO.save(f, entries);
                }
                catch(IOException e)
                {
                    new Alert(
                        Alert.AlertType.ERROR, 
                        String.format(bundle.getString("save_error") + " %s - %s", e.getClass().getName(), e.getMessage()),
                        ButtonType.CLOSE
                    ).showAndWait();
                }
            }
        }
    }
}
