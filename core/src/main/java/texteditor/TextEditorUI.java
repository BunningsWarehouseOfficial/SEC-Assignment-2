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

import java.io.File;
import java.io.IOException;
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
    
    private TextArea textArea = new TextArea();
    private LoadSaveUI loadSaveUI;
    private List<EditorPlugin> plugins;
    private ToolBar toolBar;
    private Control api;
    
    @Override
    public void start(Stage stage)
    {
        //Retrieving locale for internationalisation
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

        //Preparing required objects
        loadSaveUI = new LoadSaveUI(stage, textArea, new FileIO(), bundle);
        plugins = new ArrayList<>();

        stage.setTitle(bundle.getString("main_title"));
        stage.setMinWidth(800);

        // Create toolbar
        Button loadBtn = new Button(bundle.getString("load_btn"));
        Button saveBtn = new Button(bundle.getString("save_btn"));
        Button loadPluginScriptBtn = new Button(bundle.getString("load_plugin_script_btn"));
        Button btn1 = new Button("Button1"); //TODO: string i18n
        Button btn3 = new Button("Button3"); //TODO: string i18n
        toolBar = new ToolBar(loadBtn, saveBtn, loadPluginScriptBtn, btn1, btn3);

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
        loadPluginScriptBtn.setOnAction(event -> showPluginsExtensions(bundle));
        btn1.setOnAction(event -> showDialog1());
        btn3.setOnAction(event -> toolBar.getItems().add(new Button("ButtonN"))); //TODO: string i18n
        
        // TextArea event handlers & caret positioning.
        textArea.textProperty().addListener((object, oldValue, newValue) -> 
        {
            System.out.println("caret position is " + textArea.getCaretPosition() + 
                               "; text is\n---\n" + newValue + "\n---\n");
        });
        
        textArea.setText("This is some\ndemonstration text\nTry pressing F1, ctrl+b, ctrl+shift+b or alt+b.");
        
        // Example global keypress handler.
        scene.setOnKeyPressed(keyEvent -> 
        {
            // See the documentation for the KeyCode class to see all the available keys.
            
            KeyCode key = keyEvent.getCode();
            boolean ctrl = keyEvent.isControlDown();
            boolean shift = keyEvent.isShiftDown();
            boolean alt = keyEvent.isAltDown();
        
            if(key == KeyCode.F1)
            {
                new Alert(Alert.AlertType.INFORMATION, "F1", ButtonType.OK).showAndWait(); //TODO: string i18n
            }
            else if(ctrl && shift && key == KeyCode.B)
            {
                new Alert(Alert.AlertType.INFORMATION, "ctrl+shift+b", ButtonType.OK).showAndWait(); //TODO: string i18n
            }
            else if(ctrl && key == KeyCode.B)
            {
                new Alert(Alert.AlertType.INFORMATION, "ctrl+b", ButtonType.OK).showAndWait(); //TODO: string i18n
            }
            else if(alt && key == KeyCode.B)
            {
                new Alert(Alert.AlertType.INFORMATION, "alt+b", ButtonType.OK).showAndWait(); //TODO: string i18n
            }
        });

        //Set up control object for interacting with plugins/scripts via API
        api = new Control(textArea, toolBar, bundle);
        
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }
    
    private void showDialog1()
    {

    }

    //TODO: below functions in new class? LoadPluginsScriptsUI?
    private void showPluginsExtensions(ResourceBundle bundle)
    {
        ObservableList<String> list = FXCollections.observableArrayList(); //TODO: not string, plugin class?
        ListView<String> listView = new ListView<>(list);

        Button addPluginBtn = new Button(bundle.getString("load_plugin_btn"));
        Button addScriptBtn = new Button(bundle.getString("load_script_btn"));
        ToolBar toolBar = new ToolBar(addPluginBtn, addScriptBtn);

        addPluginBtn.setOnAction(event -> loadPlugin(bundle, plugins, list));
        addScriptBtn.setOnAction(event -> System.out.println("add script"));
        
        BorderPane box = new BorderPane();
        box.setTop(toolBar);
        box.setCenter(listView);
        
        Dialog dialog = new Dialog();
        dialog.setTitle(bundle.getString("load_plugins_scripts_title"));
        dialog.setHeaderText(bundle.getString("load_plugins_scripts_header"));
        dialog.getDialogPane().setContent(box);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }

    private void loadPlugin(ResourceBundle bundle, List<EditorPlugin> plugins, ObservableList<String> list)
    {
        var dialog = new TextInputDialog();
        dialog.setTitle(bundle.getString("load_plugin_title"));
        dialog.setHeaderText(bundle.getString("load_plugin_header"));

        var inputStr = dialog.showAndWait().orElse(null);
        if(inputStr != null)
        {
            //TODO: Finish
            try
            {
                Class<?> cls = Class.forName(inputStr);
                //TODO: Check to make sure that the class inherits from EditorPlugin
                Constructor<?> constructor = cls.getConstructor();
                Method nameMethod = cls.getMethod("getDisplayName");
                if (Modifier.isStatic(nameMethod.getModifiers()))
                {
                    throw new IllegalArgumentException("Couldn't find non-static getDisplayName() method");
                } //TODO: i18n
                Method startMethod = cls.getMethod("start", texteditor.api.Control.class);
                if (Modifier.isStatic(startMethod.getModifiers()))
                {
                    throw new IllegalArgumentException("Couldn't find non-static start() method");
                } //TODO: i18n

                //Start the plugin (registers callbacks)
                EditorPlugin newPlugin = (EditorPlugin)constructor.newInstance();
                System.out.println("1");
                String displayName = (String)nameMethod.invoke(newPlugin);
                System.out.println("2");
                startMethod.invoke(newPlugin, api);
                System.out.println("3");
//                Button newButton = new Button(displayName);
//                toolBar.getItems().add(newButton);
//                newButton.setOnAction(event -> ); //TODO: Remove from here? not all have button callbacks

                //Update the plugins list
                plugins.add(newPlugin);
                list.add(displayName);

                new Alert(Alert.AlertType.INFORMATION,
                        String.format(bundle.getString("load_plugin_success") + "\n%s", inputStr),
                        ButtonType.OK).showAndWait();
            }
            catch (InvocationTargetException e)
            {
                new Alert(Alert.AlertType.ERROR,
                        String.format(bundle.getString("load_plugin_error") + " %s - %s",
                        e.getCause().getClass().getName(), e.getCause().getMessage()), ButtonType.CLOSE).showAndWait();
            }
            catch (ReflectiveOperationException | IllegalArgumentException |
                    NoClassDefFoundError e)
            {
                new Alert(Alert.AlertType.ERROR,
                        String.format(bundle.getString("load_plugin_error") + " %s - %s", e.getClass().getName(),
                        e.getMessage()), ButtonType.CLOSE).showAndWait();
            }
        }
    }

//    private void loadScript(ResourceBundle bundle)
//    {
//        FileChooser fileDialog = new FileChooser();
//
//        fileDialog.setTitle(bundle.getString("load_title"));
//
//        File f = fileDialog.showOpenDialog(stage);
//        if(f != null)
//        {
//            String encoding = getEncoding();
//            if(encoding != null)
//            {
//                try
//                {
//                    textArea.setText(fileIO.load(f, encoding, bundle));
//                }
//                catch(IOException e)
//                {
//                    new Alert(
//                            Alert.AlertType.ERROR,
//                            String.format(bundle.getString("load_error") + " %s - %s", e.getClass().getName(), e.getMessage()),
//                            ButtonType.CLOSE
//                    ).showAndWait();
//                }
//            }
//        }
//    }
}
