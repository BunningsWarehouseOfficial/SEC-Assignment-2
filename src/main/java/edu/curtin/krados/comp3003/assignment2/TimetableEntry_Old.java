package edu.curtin.krados.comp3003.assignment2;

import java.time.Duration;
import java.time.LocalTime;


/** 
 * Simple model class for representing a (read-only) timetable entry.
 */
public class TimetableEntry_Old
{
    private final String routeId;
    private final String from;
    private final String destination;
    private final LocalTime departureTime;
    private final Duration duration;
    
    public TimetableEntry_Old(String routeId, String from, String destination, LocalTime departureTime, Duration duration)
    {
        this.routeId = routeId;
        this.from = from;
        this.destination = destination;
        this.departureTime = departureTime;
        this.duration = duration;
    }
    
    public String getRouteId()          { return routeId; }
    public String getFrom()             { return from; }
    public String getDestination()      { return destination; }
    public LocalTime getDepartureTime() { return departureTime; }
    public Duration getDuration()       { return duration; }
}
