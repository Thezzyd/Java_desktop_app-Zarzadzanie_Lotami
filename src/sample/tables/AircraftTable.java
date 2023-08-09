package sample.tables;

public class AircraftTable {

    String id, brand_name, model, engine_number, seats_number, technical_condition, is_tanked, status, aircraft_task;

    public AircraftTable(String id, String brand_name, String model, String engine_number, String seats_number, String technical_condition, String is_tanked, String status, String aircraft_task) {
        this.id = id;
        this.brand_name = brand_name;
        this.model = model;
        this.engine_number = engine_number;
        this.seats_number = seats_number;
        this.technical_condition = technical_condition;
        this.is_tanked = is_tanked;
        this.status = status;
        this.aircraft_task = aircraft_task;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getEngine_number() {
        return engine_number;
    }

    public void setEngine_number(String engine_number) {
        this.engine_number = engine_number;
    }

    public String getSeats_number() {
        return seats_number;
    }

    public void setSeats_number(String seats_number) {
        this.seats_number = seats_number;
    }

    public String getTechnical_condition() {
        return technical_condition;
    }

    public void setTechnical_condition(String technical_condition) {
        this.technical_condition = technical_condition;
    }

    public String getIs_tanked() {
        return is_tanked;
    }

    public void setIs_tanked(String is_tanked) {
        this.is_tanked = is_tanked;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAircraft_task() {
        return aircraft_task;
    }

    public void setAircraft_task(String aircraft_task) {
        this.aircraft_task = aircraft_task;
    }
}