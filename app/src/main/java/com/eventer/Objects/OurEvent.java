package com.eventer.Objects;

import java.util.Set;

/**
 * Created by rg on 26-Sep-15.
 */
public class OurEvent {

    private int eventId;

    private String eventName;

    private String eventLocation;

    public OurEvent() {
    }

    public OurEvent(String eventName, String eventLocation) {
        this.eventName = eventName;
        this.eventLocation = eventLocation;
    }


    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }


}
