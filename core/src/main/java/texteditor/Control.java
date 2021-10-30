package texteditor;

import texteditor.api.*;

import java.util.List;

public class Control implements texteditor.api.Control
{
    private List<TextUpdater> textUpdaters;
    private List<SelectionUpdater> selectionUpdaters;
    private List<TextUpdateHandler> textUpdateHandlers;
    private List<FunctionKeyHandler> functionKeyHandlers;
    private List<ButtonPressHandler> buttonPressHandlers;

    public Control()
    {

    }

    @Override
    public void registerTextUpdater(TextUpdater callback)
    {

    }
    @Override
    public void registerSelectionUpdater(SelectionUpdater callback)
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

    }
}
