package texteditor;

import javafx.application.Platform;
import javafx.scene.control.*;
import texteditor.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Control implements texteditor.api.Control
{
    private TextArea textArea;
    private ToolBar toolBar;
    private ResourceBundle bundle;

    private List<TextUpdateHandler> textUpdateHandlers;
    private List<FunctionKeyHandler> functionKeyHandlers;

    public Control(TextArea textArea, ToolBar toolBar, ResourceBundle bundle)
    {
        this.textArea = textArea;
        this.toolBar = toolBar;
        this.bundle = bundle;
        textUpdateHandlers = new ArrayList<>();
        functionKeyHandlers = new ArrayList<>();
    }

    public List<TextUpdateHandler> getTextUpdateHandlers()
    {
        return textUpdateHandlers;
    }
    public List<FunctionKeyHandler> getFunctionKeyHandlers()
    {
        return functionKeyHandlers;
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
    public void updateTextSelection(int start, int end)
    {
        Platform.runLater(() -> {
            textArea.selectRange(start, end);
            textArea.requestFocus();
        });
    }

    @Override
    public void promptUser(String prompt)
    {
        new Alert(Alert.AlertType.INFORMATION, prompt, ButtonType.OK).showAndWait();
    }
    @Override
    public String requestUserTextInput(String prompt)
    {
        var dialog = new TextInputDialog();
        dialog.setTitle(bundle.getString("plugin_prompt"));
        dialog.setHeaderText(prompt);
        return dialog.showAndWait().orElse(null);
    }

    @Override
    public void registerTextUpdateHandler(TextUpdateHandler callback)
    {
        textUpdateHandlers.add(callback);
    }
    @Override
    public void registerFunctionKeyHandler(FunctionKeyHandler callback)
    {
        functionKeyHandlers.add(callback);
    }
    @Override
    public void registerButtonPressHandler(ButtonPressHandler callback)
    {
        Button newButton = new Button(callback.getButtonName());
        Platform.runLater(() -> toolBar.getItems().add(newButton));
        newButton.setOnAction(event -> callback.buttonPressed(bundle));
    }
}
