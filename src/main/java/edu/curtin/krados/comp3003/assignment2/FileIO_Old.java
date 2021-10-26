package edu.curtin.krados.comp3003.assignment2;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.*;


/**
 * Performs the reading/parsing and writing of the CSV files containing timetable entries.
 */
public class FileIO_Old
{
    /**
     * Loads a bus timetable from a given CSV file.
     */
    public List<TimetableEntry_Old> load(File file, ResourceBundle bundle) throws IOException, TimetableFormatException_Old
    {
        List<TimetableEntry_Old> entries = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String line;
            while((line = br.readLine()) != null)
            {
                String[] fields = line.split(",");
                if(fields.length == 5)
                {
                    try
                    {
                        String routeId = fields[0];
                        String from = fields[1];
                        String destination = fields[2];
                        LocalTime departureTime = LocalTime.parse(fields[3]);
                        Duration duration = Duration.ofMinutes(Integer.parseInt(fields[4]));
                        
                        entries.add(new TimetableEntry_Old(routeId, from, destination, departureTime, duration));
                    }
                    catch(DateTimeParseException e)
                    {
                        throw new TimetableFormatException_Old(String.format(
                            bundle.getString("departure_error") + " '%s'", fields[3]), e);
                    }
                    catch(NumberFormatException e)
                    {
                        throw new TimetableFormatException_Old(String.format(
                            bundle.getString("duration_error") + " '%s'", fields[4]), e);
                    }                    
                }
            }
        }
        return entries;
    }
    
    /**
     * Writes a bus timetable to a given CSV file.
     */
    public void save(File file, List<TimetableEntry_Old> entries) throws IOException
    {
        try(PrintWriter pw = new PrintWriter(file))
        {
            for(TimetableEntry_Old entry : entries)
            {
                pw.printf("%s,%s,%s,%s,%d\n",
                    entry.getRouteId().replace(",", ""),
                    entry.getFrom().replace(",", ""),
                    entry.getDestination().replace(",", ""),
                    entry.getDepartureTime().toString(),
                    entry.getDuration().toMinutes());
            }
        }
    }
}
