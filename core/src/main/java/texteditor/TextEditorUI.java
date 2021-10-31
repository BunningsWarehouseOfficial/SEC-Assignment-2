package texteditor;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import texteditor.api.EditorPlugin;
import texteditor.api.FunctionKeyHandler;
import texteditor.api.HotkeyHandler;
import texteditor.api.TextUpdateHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class TextEditorUI extends Application
{
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    private final String keymapFile = "keymap";

    private TextArea textArea = new TextArea();
    private FileIO fileIO;
    private LoadSaveUI loadSaveUI;
    private ExtensionsUI extensionsUI;
    private ToolBar toolBar;
    private Control api;
    
    @Override
    public void start(Stage stage)
    {
        //Retrieving locale for internationalisation
        ResourceBundle bundle;
        var localeString = getParameters().getNamed().get("locale");
        if(localeString != null) //E.g. If localeString == 'hr-HR'
        {
            Locale locale = Locale.forLanguageTag(localeString);
            bundle = ResourceBundle.getBundle("bundle", locale);
        }
        else
        {
            bundle = ResourceBundle.getBundle("bundle");
        }

        //Preparing required objects
        fileIO = new FileIO();
        loadSaveUI = new LoadSaveUI(stage, textArea, fileIO, bundle);

        stage.setTitle(bundle.getString("main_title"));
        stage.setMinWidth(800);

        // Create toolbar
        Button loadBtn = new Button(bundle.getString("load_btn"));
        Button saveBtn = new Button(bundle.getString("save_btn"));
        Button loadPluginScriptBtn = new Button(bundle.getString("load_extension_btn"));
        toolBar = new ToolBar(loadBtn, saveBtn, loadPluginScriptBtn);

        // Subtle user experience tweaks
        toolBar.setFocusTraversable(false);
        toolBar.getItems().forEach(btn -> btn.setFocusTraversable(false));
        textArea.setStyle("-fx-font-family: 'monospace'"); // Set the font
        
        // Add the main parts of the UI to the window.
        BorderPane mainBox = new BorderPane();
        mainBox.setTop(toolBar);
        mainBox.setCenter(textArea);
        Scene scene = new Scene(mainBox);        
        
        // Button event handlers.
        loadBtn.setOnAction(event -> loadSaveUI.load());
        saveBtn.setOnAction(event -> loadSaveUI.save());
        loadPluginScriptBtn.setOnAction(event -> extensionsUI.showExtensions());

        //Set up control object for interacting with plugins/scripts via API
        api = new Control(textArea, toolBar, bundle);

        extensionsUI = new ExtensionsUI(stage, textArea, fileIO, api, bundle);

        //Text modification event handler
        textArea.textProperty().addListener((object, oldValue, newValue) -> 
        {
            for (TextUpdateHandler h : api.getTextUpdateHandlers())
            {
                h.textChanged(oldValue, newValue);
            }
        });
        
        textArea.setText(bundle.getString("default_text"));

        //Parse 'keymap' hotkeys file with custom JavaCC DSL
        try
        {
            InputStream stream = new FileInputStream(keymapFile);
            TextEditorParser.parse(keymapFile, api, stream);
        }
        catch (IOException | ParseException e)
        {
            System.out.println(String.format(bundle.getString("parser_error") + " %s - %s", e.getClass().getName(),
                    e.getMessage()));
            new Alert(Alert.AlertType.ERROR,
                    String.format(bundle.getString("parser_error") + " %s - %s", e.getClass().getName(),
                    e.getMessage()), ButtonType.CLOSE).showAndWait();
        } //TODO: InvocationTargetException for incorrect DSL syntax?

        //Global key press handler.
        scene.setOnKeyPressed(keyEvent -> 
        {
            KeyCode key = keyEvent.getCode();
            boolean ctrl = keyEvent.isControlDown();
            boolean shift = keyEvent.isShiftDown();
            boolean alt = keyEvent.isAltDown();
        
            if (key.isFunctionKey())
            {
                //Function key press handler
                for (FunctionKeyHandler h : api.getFunctionKeyHandlers())
                {
                    h.functionKeyPressed(key.getName());
                }
            }
            else
            {
                List<String> cKeys = new LinkedList<>();
                if (ctrl)
                {
                    cKeys.add("ctrl");
                }
                if (alt)
                {
                    cKeys.add("alt");
                }
                if (shift)
                {
                    cKeys.add("shift");
                }

                for (HotkeyHandler h : api.getHotkeyHandlers())
                {
                    h.hotkeyPressed(cKeys, key.getName());
                }
            }
        });
        
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }
}
