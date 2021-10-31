package texteditor;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import texteditor.api.*;

import java.util.List;
import java.util.ResourceBundle;

public class Control implements texteditor.api.Control
{
    private TextArea textArea;
    private ToolBar toolBar;
    private ResourceBundle bundle;

    private List<SelectionUpdater> selectionUpdaters;
    private List<TextUpdateHandler> textUpdateHandlers;
    private List<FunctionKeyHandler> functionKeyHandlers;

    public Control(TextArea textArea, ToolBar toolBar, ResourceBundle bundle)
    {
        this.textArea = textArea;
        this.toolBar = toolBar;
        this.bundle = bundle;
    }

    @Override
    public String getLocaleString()
    {
        //FIXME?
        return null;
    }

    @Override
    public String getText()
    {
        return textArea.getText();
    }
    @Override
    public void updateText(String newText)
    {
        Platform.runLater(() -> {
            textArea.setText(newText);
            textArea.requestFocus();
        });
    }

    @Override
    public int getCaretPosition()
    {
        return textArea.getCaretPosition();
    }
    @Override
    public void updateCaretPosition(int newPosition)
    {
        Platform.runLater(() -> {
            textArea.positionCaret(newPosition);
            textArea.requestFocus();
        });
    }

    @Override
    public void registerUserTextInputCollector(UserTextInputCollector callback)
    {

    }

    @Override
    public void registerTextUpdateHandler(TextUpdateHandler callback)
    {

    }
    @Override
    public void registerFunctionKeyHandler(FunctionKeyHandler callback)
    {

    }
    @Override
    public void registerButtonPressHandler(ButtonPressHandler callback)
    {
        Button newButton = new Button(callback.getButtonName());
        Platform.runLater(() -> toolBar.getItems().add(newButton));
        newButton.setOnAction(event -> callback.buttonPressed(bundle));
    }
}
