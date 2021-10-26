package edu.curtin.krados.comp3003.assignment2;

import javafx.scene.control.TextArea;

import java.io.*;
import java.util.ResourceBundle;

/**
 * Performs the reading/parsing and writing of the CSV files containing timetable entries.
 */
public class FileIO
{
    /**
     * Loads a bus timetable from a given CSV file.
     */
    public String load(File file, ResourceBundle bundle) throws IOException, TimetableFormatException_Old
    {
        StringBuilder text = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new FileReader(file)))
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
    
    /**
     * Writes a bus timetable to a given CSV file.
     */
    public void save(File file, TextArea textArea) throws IOException
    {
        try(PrintWriter pw = new PrintWriter(file))
        {
            String text = textArea.getText();
            pw.printf("%s", text);
        }
    }
}
