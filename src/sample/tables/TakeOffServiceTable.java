package sample.tables;

public class TakeOffServiceTable {

    String id_flight;
    String sold_tickets;
    String aircraft;
    String id_aircraft;
    String departure_time;
    String departure_date;
    String status;

    public TakeOffServiceTable(String id_flight, String sold_tickets, String aircraft, String id_aircraft, String departure_time, String departure_date, String status) {
        this.id_flight = id_flight;
        this.sold_tickets = sold_tickets;
        this.aircraft = aircraft;
        this.id_aircraft = id_aircraft;
        this.departure_time = departure_time;
        this.departure_date = departure_date;
        this.status = status;
    }

    public String getId_flight() {
        return id_flight;
    }

    public void setId_flight(String id_flight) {
        this.id_flight = id_flight;
    }

    public String getSold_tickets() {
        return sold_tickets;
    }

    public void setSold_tickets(String sold_tickets) {
        this.sold_tickets = sold_tickets;
    }

    public String getAircraft() {
        return aircraft;
    }

    public void setAircraft(String aircraft) {
        this.aircraft = aircraft;
    }

    public String getId_aircraft() {
        return id_aircraft;
    }

    public void setId_aircraft(String id_aircraft) {
        this.id_aircraft = id_aircraft;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getDeparture_date() {
        return departure_date;
    }

    public void setDeparture_date(String departure_date) {
        this.departure_date = departure_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}