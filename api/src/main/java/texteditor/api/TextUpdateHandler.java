package texteditor.api;

public interface TextUpdateHandler
{
    void textChanged(String oldText, String newText);
}
