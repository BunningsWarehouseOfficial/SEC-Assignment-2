package edu.curtin.krados.comp3003.assignment2;


/**
 * Thrown in the event of a parsing error while reading a timetable CSV file.
 */
public class TimetableFormatException extends Exception
{
    public TimetableFormatException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
