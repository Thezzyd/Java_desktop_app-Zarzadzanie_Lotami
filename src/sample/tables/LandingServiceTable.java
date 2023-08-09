package sample.tables;

public class LandingServiceTable {
    String id_flight;
    String arrival_time;
    String aircraft;
    String id_aircraft;
    String aircraft_status;

    public LandingServiceTable(String id_flight, String arrival_time, String aircraft, String id_aircraft, String aircraft_status) {
        this.id_flight = id_flight;
        this.arrival_time = arrival_time;
        this.aircraft = aircraft;
        this.id_aircraft = id_aircraft;
        this.aircraft_status = aircraft_status;
    }

    public String getId_flight() {
        return id_flight;
    }

    public void setId_flight(String id_flight) {
        this.id_flight = id_flight;
    }

    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
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

    public String getAircraft_status() {
        return aircraft_status;
    }

    public void setAircraft_status(String aircraft_status) {
        this.aircraft_status = aircraft_status;
    }
}