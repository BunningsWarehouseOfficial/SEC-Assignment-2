package texteditor;

import javafx.scene.control.TextArea;

import java.io.*;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

//TODO: docs
/**
 * Performs the reading/parsing and writing of the CSV files containing timetable entries.
 */
public class FileIO
{
    //TODO: docs
    /**
     * Loads a bus timetable from a given CSV file.
     */
    public String load(File file, String encoding, ResourceBundle bundle) throws IOException
    {
        StringBuilder text = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new FileReader(file, Charset.forName(encoding))))
        {
            String line;
            while((line = br.readLine()) != null)
            {
                text.append(line);
                text.append("\n");
            }
        }
        return text.toString();
    }

    //TODO: docs
    /**
     * Writes a bus timetable to a given CSV file.
     */
    public void save(File file, String encoding, TextArea textArea) throws IOException
    {
        try(PrintWriter pw = new PrintWriter(file, encoding))
        {
            String text = textArea.getText();
            pw.printf("%s", text);
        }
    }
}
