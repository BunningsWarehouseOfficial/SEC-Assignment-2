package texteditor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.python.util.PythonInterpreter;
import texteditor.api.EditorPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ResourceBundle;

public class ExtensionsUI
{
    private Stage stage;
    private TextArea textArea;
    private FileIO fileIO;
    private Control api;
    private ResourceBundle bundle;
    ObservableList<String> extensionList = FXCollections.observableArrayList();

    public ExtensionsUI(Stage stage, TextArea textArea, FileIO fileIO, Control api, ResourceBundle bundle)
    {
        this.stage = stage;
        this.textArea = textArea;
        this.fileIO = fileIO;
        this.api = api;
        this.bundle = bundle;
    }

    public void showExtensions()
    {
        ListView<String> listView = new ListView<>(extensionList);

        Button addPluginBtn = new Button(bundle.getString("load_plugin_btn"));
        Button addScriptBtn = new Button(bundle.getString("load_script_btn"));
        ToolBar toolBar = new ToolBar(addPluginBtn, addScriptBtn);

        addPluginBtn.setOnAction(event -> loadPlugin());
        addScriptBtn.setOnAction(event -> loadScript());

        BorderPane box = new BorderPane();
        box.setTop(toolBar);
        box.setCenter(listView);

        Dialog dialog = new Dialog();
        dialog.setTitle(bundle.getString("load_extensions_title"));
        dialog.setHeaderText(bundle.getString("load_extensions_header"));
        dialog.getDialogPane().setContent(box);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }

    private void loadPlugin()
    {
        var dialog = new TextInputDialog();
        dialog.setTitle(bundle.getString("load_plugin_title"));
        dialog.setHeaderText(bundle.getString("load_plugin_header"));

        var inputStr = dialog.showAndWait().orElse(null);
        if(inputStr != null)
        {
            try
            {
                Class<?> cls = Class.forName(inputStr);
                //TODO: Check to make sure that the class inherits from EditorPlugin
                Constructor<?> constructor = cls.getConstructor();
                Method nameMethod = cls.getMethod("getDisplayName", texteditor.api.Control.class);
                if (Modifier.isStatic(nameMethod.getModifiers()))
                {
                    throw new IllegalArgumentException(bundle.getString("getdisplayname_method_error"));
                }
                Method startMethod = cls.getMethod("start", texteditor.api.Control.class);
                if (Modifier.isStatic(startMethod.getModifiers()))
                {
                    throw new IllegalArgumentException(bundle.getString("start_method_error"));
                }

                //Start the plugin (registers callbacks)
                EditorPlugin newPlugin = (EditorPlugin)constructor.newInstance();
                String displayName = (String)nameMethod.invoke(newPlugin, api);
                startMethod.invoke(newPlugin, api);

                //Update the plugins list
                extensionList.add(displayName);

                new Alert(Alert.AlertType.INFORMATION,
                        String.format(bundle.getString("load_extension_success") + "\n%s", inputStr),
                        ButtonType.OK).showAndWait();
            }
            catch (InvocationTargetException e)
            {
                new Alert(Alert.AlertType.ERROR,
                        String.format(bundle.getString("load_extension_error") + " %s - %s",
                                e.getCause().getClass().getName(), e.getCause().getMessage()), ButtonType.CLOSE).showAndWait();
            }
            catch (ReflectiveOperationException | IllegalArgumentException |
                    NoClassDefFoundError e)
            {
                new Alert(Alert.AlertType.ERROR,
                        String.format(bundle.getString("load_extension_error") + " %s - %s", e.getClass().getName(),
                                e.getMessage()), ButtonType.CLOSE).showAndWait();
            }
        }
    }

    private void loadScript()
    {
        FileChooser fileDialog = new FileChooser();

        fileDialog.setTitle(bundle.getString("load_title"));

        File f = fileDialog.showOpenDialog(stage);
        if(f != null)
        {
            try
            {
                //Start the script (should register callbacks)
                String pythonScript = fileIO.load(f, "UTF-8");
                PythonInterpreter interpreter = new PythonInterpreter();
                interpreter.set("api", api);
                interpreter.exec(pythonScript);

                //Update the plugins list
                extensionList.add(f.getName());

                new Alert(Alert.AlertType.INFORMATION,
                        String.format(bundle.getString("load_extension_success") + "\n%s", f.getName()),
                        ButtonType.OK).showAndWait();
            }
            catch (IOException e)
            {
                new Alert(Alert.AlertType.ERROR,
                        String.format(bundle.getString("load_extension_error") + " %s - %s", e.getClass().getName(),
                                e.getMessage()), ButtonType.CLOSE).showAndWait();
            }
        }
    }
}
