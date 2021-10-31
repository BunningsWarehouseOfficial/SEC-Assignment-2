package texteditor.api;

import java.util.List;

public interface HotkeyHandler
{
    void hotkeyPressed(List<String> controlKeys, String letterKey);
}
