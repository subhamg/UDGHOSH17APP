package com.iitk.udghosh.models;

public class Event {

    public String eventListName;
    private int eventListImage;
    private String className;
    private int eventBackground;
    public String Data;

    public Event() {

        // Default constructor required for calls to DataSnapshot.getValue(Event.class)
    }

    public Event(String eventName,int eventImage, String className, int eventBackground)
    {
        this.eventListImage = eventImage;
        this.eventListName = eventName;
        this.className = className;
        this.eventBackground = eventBackground;
    }
    public String getEventName()
    {
        return eventListName;
    }
    public int getEventImage()
    {
        return eventListImage;
    }
    public String getClassName() { return className;}
    public int getEventBackground() { return eventBackground; }

}