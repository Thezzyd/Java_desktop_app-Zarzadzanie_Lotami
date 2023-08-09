package sample.tables;

public class CustomerServiceTable {
    String cs_id, cs_id_flight, cs_NoS, cs_price_list;

    public CustomerServiceTable(String cs_id, String cs_id_flight, String cs_NoS, String cs_price_list) {
        this.cs_id = cs_id;
        this.cs_id_flight = cs_id_flight;
        this.cs_NoS = cs_NoS;
        this.cs_price_list = cs_price_list;
    }

    public String getCs_id() {
        return cs_id;
    }

    public void setCs_id(String cs_id) {
        this.cs_id = cs_id;
    }

    public String getCs_id_flight() {
        return cs_id_flight;
    }

    public void setCs_id_flight(String cs_id_flight) {
        this.cs_id_flight = cs_id_flight;
    }

    public String getCs_NoS() {
        return cs_NoS;
    }

    public void setCs_NoS(String cs_NoS) {
        this.cs_NoS = cs_NoS;
    }

    public String getCs_price_list() {
        return cs_price_list;
    }

    public void setCs_price_list(String cs_price_list) {
        this.cs_price_list = cs_price_list;
    }
}
