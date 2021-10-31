package texteditor;

import javafx.scene.control.TextArea;

import java.io.*;
import java.nio.charset.Charset;

public class FileIO
{
    public String load(File file, String encoding) throws IOException
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

    public void save(File file, String encoding, TextArea textArea) throws IOException
    {
        try(PrintWriter pw = new PrintWriter(file, encoding))
        {
            String text = textArea.getText();
            pw.printf("%s", text);
        }
    }
}
