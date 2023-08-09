package sample.tables;

public class TicketTable {
    String id_ticket;
    String id_flight_schedule;
    String firstname;
    String lastname;
    String phone_number;
    String email;

    public TicketTable(String id_ticket, String id_flight_schedule, String firstname, String lastname, String phone_number, String email) {
        this.id_ticket = id_ticket;
        this.id_flight_schedule = id_flight_schedule;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone_number = phone_number;
        this.email = email;
    }

    public String getId_ticket() {
        return id_ticket;
    }

    public void setId_ticket(String id_ticket) {
        this.id_ticket = id_ticket;
    }

    public String getId_flight_schedule() {
        return id_flight_schedule;
    }

    public void setId_flight_schedule(String id_flight_schedule) {
        this.id_flight_schedule = id_flight_schedule;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}