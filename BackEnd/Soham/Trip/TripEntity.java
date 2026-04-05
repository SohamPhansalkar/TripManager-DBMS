package BackEnd.Soham.Trip;

public class TripEntity {
    private String creatorEmail;
    private String destination;
    private Integer budget;
    private String startDate;
    private String endDate;
    
    public TripEntity(String creatorEmail, String destination, Integer budget, String startDate, String endDate) {
        this.creatorEmail = creatorEmail;
        this.destination = destination;
        this.budget = budget;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getCreatorEmail() { return creatorEmail; }
    public void setCreatorEmail(String creatorEmail) { this.creatorEmail = creatorEmail; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public Integer getBudget() { return budget; }
    public void setBudget(Integer budget) { this.budget = budget; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
}
