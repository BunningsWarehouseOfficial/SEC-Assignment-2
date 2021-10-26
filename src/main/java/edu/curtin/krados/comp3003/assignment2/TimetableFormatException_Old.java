package edu.curtin.krados.comp3003.assignment2;


/**
 * Thrown in the event of a parsing error while reading a timetable CSV file.
 */
public class TimetableFormatException_Old extends Exception
{
    public TimetableFormatException_Old(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
