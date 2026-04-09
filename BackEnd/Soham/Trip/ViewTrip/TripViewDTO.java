package BackEnd.Soham.Trip.ViewTrip;

import java.util.ArrayList;
import java.util.List;

public class TripViewDTO {
    private int tripID;
    private String creatorEmail;
    private String destination;
    private int budget;
    private String startDate;
    private String endDate;
    private List<DayViewDTO> days = new ArrayList<>();

    // Getters and Setters
    public int getTripID() { return tripID; }
    public void setTripID(int tripID) { this.tripID = tripID; }
    public String getCreatorEmail() { return creatorEmail; }
    public void setCreatorEmail(String creatorEmail) { this.creatorEmail = creatorEmail; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public int getBudget() { return budget; }
    public void setBudget(int budget) { this.budget = budget; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public List<DayViewDTO> getDays() { return days; }
    public void setDays(List<DayViewDTO> days) { this.days = days; }

    public static class DayViewDTO {
        private int dayID;
        private String date;
        private List<EventViewDTO> events = new ArrayList<>();

        public int getDayID() { return dayID; }
        public void setDayID(int dayID) { this.dayID = dayID; }
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public List<EventViewDTO> getEvents() { return events; }
        public void setEvents(List<EventViewDTO> events) { this.events = events; }
    }

    public static class EventViewDTO {
        private int eventID;
        private String time;
        private String type;
        private String description;

        public int getEventID() { return eventID; }
        public void setEventID(int eventID) { this.eventID = eventID; }
        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
