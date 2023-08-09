package sample.tables;

public class FlightScheduleTable {
    String id;
    String aircraft;
    String departure_place;
    String departure_date;
    String departure_time;
    String destination;
    String arrival_date;
    String arrival_time;
    String price;

    public FlightScheduleTable(String id, String aircraft, String departure_place, String departure_date, String departure_time, String destination, String arrival_date, String arrival_time, String price) {
        this.id = id;
        this.aircraft = aircraft;
        this.departure_place = departure_place;
        this.departure_date = departure_date;
        this.departure_time = departure_time;
        this.destination = destination;
        this.arrival_date = arrival_date;
        this.arrival_time = arrival_time;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAircraft() {
        return aircraft;
    }

    public void setAircraft(String aircraft) {
        this.aircraft = aircraft;
    }

    public String getDeparture_place() {
        return departure_place;
    }

    public void setDeparture_place(String departure_place) {
        this.departure_place = departure_place;
    }

    public String getDeparture_date() {
        return departure_date;
    }

    public void setDeparture_date(String departure_date) {
        this.departure_date = departure_date;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getArrival_date() {
        return arrival_date;
    }

    public void setArrival_date(String arrival_date) {
        this.arrival_date = arrival_date;
    }

    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}