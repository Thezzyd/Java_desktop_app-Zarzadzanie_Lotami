package sample.M3_AirTraffic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.util.converter.DefaultStringConverter;
import sample.DatabaseFiles.DatabaseConnection;
import sample.ParentScene.ModuleParent;
import sample.tables.*;
import javax.swing.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.ResourceBundle;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * Controller class for third module screen window - Air Traffic
 * @author Daniel Czyz
 * @version 1.0.1 30/05/2021
 */
public class M3_AirTrafficController extends ModuleParent {

    @FXML
    public TableView<FlightScheduleTable> flight_schedule;
    @FXML
    public TableView<LandingServiceTable> landing_service_table;
    @FXML
    public TableView<TakeOffServiceTable> take_off_service_table;

    @FXML
    private TableColumn<FlightScheduleTable, String> col_id_flight_schedule;
    @FXML
    private TableColumn<FlightScheduleTable, String> col_aircraft_flight_schedule;
    @FXML
    private TableColumn<FlightScheduleTable, String> col_departure_date_flight_schedule;
    @FXML
    private TableColumn<FlightScheduleTable, String> col_departure_time_flight_schedule;
    @FXML
    private TableColumn<FlightScheduleTable, String> col_departure_place_flight_schedule;
    @FXML
    private TableColumn<FlightScheduleTable, String> col_destination_flight_schedule;
    @FXML
    private TableColumn<FlightScheduleTable, String> col_arrival_date_flight_schedule;
    @FXML
    private TableColumn<FlightScheduleTable, String> col_arrival_time_flight_schedule;
    @FXML
    private TableColumn<FlightScheduleTable, String> col_price_flight_schedule;
    @FXML
    private TableColumn<TakeOffServiceTable, String> col_departure_time_flight_take_off_service;
    @FXML
    private TableColumn<TakeOffServiceTable, String> col_departure_date_flight_take_off_service;
    @FXML
    private TableColumn<TakeOffServiceTable, String> col_aircraft_flight_take_off_service;
    @FXML
    private TableColumn<TakeOffServiceTable, String> col_sold_tickets_flight_take_off_service;
    @FXML
    private TableColumn<TakeOffServiceTable, String> col_id_flight_take_off_service;
    @FXML
    private TableColumn<TakeOffServiceTable, String> col_status_flight_take_off_service;
    @FXML
    private TableColumn<LandingServiceTable, String> col_id_flight_landing_service;
    @FXML
    private TableColumn<LandingServiceTable, String> col_arrival_time_flight_landing_service;
    @FXML
    private TableColumn<LandingServiceTable, String> col_aircraft_flight_landing_service;
    @FXML
    private TableColumn<LandingServiceTable, String> col_landed_flight_landing_service;

    @FXML
    private Label queryInfoLabel_FlightSchedule;
    @FXML
    private Label queryInfoLabel_landing_service;
    @FXML
    private Label queryInfoLabel_take_off_service;
    @FXML
    private TextField flightScheduleSearchBox;
    @FXML
    private TextField takeOffServiceSearchBox;
    @FXML
    private TextField landingServiceSearchBox;

    public static ObservableList<FlightScheduleTable> flight_schedule_data ;
    public static ObservableList<TakeOffServiceTable> take_off_service_data ;
    public static ObservableList<LandingServiceTable> landing_service_data ;

    public static ObservableList<String> aircraftTakeOffServiceComboBox_data;
    public static ObservableList<String> statusTakeOffAndLandingServiceComboBox_data;
    public static ObservableList<String> aircraftIdLandingServiceComboBox_data;

    public boolean addFlightPopup = false;

    /**
     * Content of this method is executed when instance of M3_AirTrafficController class is loaded
     * @param location location
     * @param resource resource
     */
    @Override
    public void initialize(URL location, ResourceBundle resource){

        rootPane.setOpacity(0);
        makeFadeInTransition(rootPane);
        initAndLoadData_FlightSchedule();
    }

    /**
     * Calculates how many minutes are left for specific flight to departure
     * @param flightDate flight departure date
     * @param flightTime flight departure time
     * @return
     */
    public long minutesTillDeparture(LocalDate flightDate, LocalTime flightTime){
        refreshCurrentTime();
        long daysToFlight = DAYS.between(currentLocalDate,flightDate);
        long minutesToFlight = MINUTES.between(currentLocalTime, flightTime);
        System.out.println(MINUTES.between(currentLocalTime, flightTime));
        return minutesToFlight + (daysToFlight * 1440);
    }

    /**
     * Fills aircraft status combo box
     */
    public void initStatusListForComboBox(){
        statusTakeOffAndLandingServiceComboBox_data = FXCollections.observableArrayList();
        statusTakeOffAndLandingServiceComboBox_data.add("available");
        statusTakeOffAndLandingServiceComboBox_data.add("in_flight");
    }

    /**
     * Selects all aircrafts from database that have: status - available, technical condition - operational, is tanked - true
     * @return object containing aircraft data pulled from database
     */
    public ResultSet selectAircraftOperationalAvailableTanked(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String sqlAircraftSelect = "SELECT DISTINCT aircraft.id_aircraft, brand_name, model, seats_number FROM aircraft WHERE technical_condition = 'operational' AND is_tanked = 'true' AND status = 'available' AND id_aircraft NOT IN (SELECT id_aircraft FROM flight_schedule Where id_aircraft is NOT null )";

        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(sqlAircraftSelect);
            return  queryResult;

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return null;
        }

    }

    /**
     * Fills combo box with all aircrafts that are ready to fly
     */
    public void initAircraftListForComboBox(){
        try {
            ResultSet selectAOAT = selectAircraftOperationalAvailableTanked();
            aircraftTakeOffServiceComboBox_data.add("not assigned");
            while(selectAOAT.next()) {
                aircraftTakeOffServiceComboBox_data.add(selectAOAT.getString("id_aircraft")+". "+selectAOAT.getString("brand_name")+" "+selectAOAT.getString("model")+", seats: "+
                        selectAOAT.getString("seats_number"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    /**
     * Sets take off service table view in lunched application. Defines which columns can be updated and which not
     */
    public void setTakeOffServiceTableViewColumns(){
        col_id_flight_take_off_service.setCellValueFactory(new PropertyValueFactory<>("id_flight"));
        col_sold_tickets_flight_take_off_service.setCellValueFactory(new PropertyValueFactory<>("sold_tickets"));
        col_aircraft_flight_take_off_service.setCellValueFactory(new PropertyValueFactory<>("aircraft"));
        col_departure_date_flight_take_off_service.setCellValueFactory(new PropertyValueFactory<>("departure_date"));
        col_departure_time_flight_take_off_service.setCellValueFactory(new PropertyValueFactory<>("departure_time"));
        col_status_flight_take_off_service.setCellValueFactory(new PropertyValueFactory<>("status"));

        col_aircraft_flight_take_off_service.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), aircraftTakeOffServiceComboBox_data));
        col_aircraft_flight_take_off_service.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setAircraft(e.getNewValue());
        });

        col_status_flight_take_off_service.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), statusTakeOffAndLandingServiceComboBox_data));
        col_status_flight_take_off_service.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setStatus(e.getNewValue());
        });
        take_off_service_table.setEditable(true);
    }

    /**
     * Allows to filter take off service table view by given phrase
     * @return filtered content of take off service table view as list
     */
    public SortedList<TakeOffServiceTable> searchableTakeOffServiceTableView(){
        FilteredList<TakeOffServiceTable> filteredData = new FilteredList<>(take_off_service_data, b -> true);

        takeOffServiceSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(takeOffService ->{
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if(String.valueOf(takeOffService.getId_flight().toLowerCase()).indexOf(lowerCaseFilter) != -1){
                    return true;
                } else if(takeOffService.getId_aircraft() != null && String.valueOf(takeOffService.getId_aircraft().toLowerCase()).indexOf(lowerCaseFilter) != -1){
                    return true;
                } else if(takeOffService.getAircraft().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(takeOffService.getStatus().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(takeOffService.getDeparture_time().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(takeOffService.getSold_tickets().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(takeOffService.getDeparture_date().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else return false;

            });
        });

        SortedList<TakeOffServiceTable> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(take_off_service_table.comparatorProperty());
        return sortedData;
    }

    /**
     * Selects all assigned tickets to specific flight from database
     * @return object containing ticket data pulled from database
     */
    public ResultSet selectFlightScheduleTicketAircraft(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String sqlSelect = "SELECT *, COUNT(ticket.id_ticket) AS sold_tickets FROM flight_schedule LEFT JOIN ticket  ON flight_schedule.id_flight_schedule = ticket.id_flight_schedule"+
                " LEFT JOIN aircraft ON aircraft.id_aircraft = flight_schedule.id_aircraft group by flight_schedule.id_flight_schedule HAVING aircraft.status is null OR aircraft.status = 'available'";

        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(sqlSelect);
            return queryResult;

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return null;
        }
    }

    /**
     * Fills take off service table view with data pulled from database
     */
    public void initAndLoadData_TakeOffService(){
        take_off_service_data = FXCollections.observableArrayList();
        aircraftTakeOffServiceComboBox_data = FXCollections.observableArrayList();
        initStatusListForComboBox();
        initAircraftListForComboBox();
        setTakeOffServiceTableViewColumns();
        String aircraft = "", status = "";

        try {
            ResultSet selectFSTicketAircraft = selectFlightScheduleTicketAircraft();

            while(selectFSTicketAircraft.next())
            {
                long minutesLeft = minutesTillDeparture(LocalDate.parse(selectFSTicketAircraft.getString("departure_date")), LocalTime.parse(selectFSTicketAircraft.getString("departure_time")));
                if( minutesLeft < 30) {
                    if(selectFSTicketAircraft.getInt("sold_tickets") != 0) {
                        if(minutesLeft <= 0){
                            delayFlight(minutesLeft, selectFSTicketAircraft.getInt("id_flight_schedule"),LocalDate.parse(selectFSTicketAircraft.getString("departure_date")),
                                    LocalTime.parse(selectFSTicketAircraft.getString("departure_time")), LocalDate.parse(selectFSTicketAircraft.getString("arrival_date")),
                                    LocalTime.parse(selectFSTicketAircraft.getString("arrival_time")));
                            JOptionPane.showMessageDialog(null, "Flight with id: "+selectFSTicketAircraft.getInt("id_flight_schedule")+" was delayed!");
                        }
                        aircraft = selectFSTicketAircraft.getString("id_aircraft");
                        status = selectFSTicketAircraft.getString("status");
                        if (selectFSTicketAircraft.getString("id_aircraft") == null) {
                            aircraft = "not assigned";
                            status = "-";
                        }else
                            aircraft += ". " + selectFSTicketAircraft.getString("brand_name") + " " + selectFSTicketAircraft.getString("model") + ", seats: "
                                    + selectFSTicketAircraft.getString("seats_number");

                        take_off_service_data.add(new TakeOffServiceTable(selectFSTicketAircraft.getString("id_flight_schedule"),
                                selectFSTicketAircraft.getString("sold_tickets"), aircraft, selectFSTicketAircraft.getString("id_aircraft"),
                                selectFSTicketAircraft.getString("departure_time"), selectFSTicketAircraft.getString("departure_date"), status
                        ));
                    }else{
                        deleteFlight(selectFSTicketAircraft.getInt("id_flight_schedule"));
                        JOptionPane.showMessageDialog(null, "Flight with id: "+selectFSTicketAircraft.getInt("id_flight_schedule")+" was deleted due to lack of sold tickets...");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }
        take_off_service_table.setItems(searchableTakeOffServiceTableView());
    }

    /**
     * If a flight has not been marked as in flight and time of departure for that flight has passed. Departure date time and arrival date time is delayed by 15 minutes
     * @param minutesTillDeparture amount of minutes till flight departure
     * @param id_flight_schedule flight id
     * @param flightDate flight departure date
     * @param flightTime flight departure time
     * @param arrivalDate flight arrival date
     * @param arrivalTime flight arrival time
     */
    public void delayFlight(long minutesTillDeparture, int id_flight_schedule, LocalDate flightDate, LocalTime flightTime, LocalDate arrivalDate, LocalTime arrivalTime){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String flightScheduleUpdate = "UPDATE flight_schedule SET departure_date = ? , departure_time = ?, arrival_date = ?, arrival_time = ? WHERE id_flight_schedule= ?";

        long minutesToAdd = Math.abs(minutesTillDeparture) + 15; //dopełnienie do zera + 15 min zapasu...

        //dodanie dnia do daty dni jeśli rozbieżność dniowa, dodanie opóźnienia oraz przeciągniecie daty/czasu przylotu
        String setDepartureDate = flightDate.plusDays(Math.floorDiv(flightTime.get(ChronoField.MINUTE_OF_DAY) + minutesToAdd, 1440)).toString();
        String setArrivalDate =  arrivalDate.plusDays(Math.floorDiv(flightTime.get(ChronoField.MINUTE_OF_DAY) + minutesToAdd, 1440)).toString();
        String setDepartureTime = flightTime.plusMinutes(minutesToAdd).toString();
        String setArrivalTime = arrivalTime.plusMinutes(minutesToAdd).toString();

        try{
            PreparedStatement preparedStatementFlightScheduleUpdate = connectDB.prepareStatement(flightScheduleUpdate);
            preparedStatementFlightScheduleUpdate.setString(1, setDepartureDate) ;
            preparedStatementFlightScheduleUpdate.setString(2, setDepartureTime);
            preparedStatementFlightScheduleUpdate.setString(3, setArrivalDate);
            preparedStatementFlightScheduleUpdate.setString(4, setArrivalTime);
            preparedStatementFlightScheduleUpdate.setInt(5, id_flight_schedule);
            preparedStatementFlightScheduleUpdate.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    /**
     * Allows to filter landing service table view by given phrase
     * @return filtered content of landing service table view as list
     */
    public SortedList<LandingServiceTable> searchableLandingServiceTableView(){
        FilteredList<LandingServiceTable> filteredData = new FilteredList<>(landing_service_data, b -> true);

        landingServiceSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(landingService ->{
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if(String.valueOf(landingService.getId_flight().toLowerCase()).indexOf(lowerCaseFilter) != -1){
                    return true;
                } else if(String.valueOf(landingService.getId_aircraft().toLowerCase()).indexOf(lowerCaseFilter) != -1){
                    return true;
                } else if(landingService.getAircraft().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(landingService.getAircraft_status().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(landingService.getArrival_time().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else return false;

            });
        });

        SortedList<LandingServiceTable> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(landing_service_table.comparatorProperty());
        return sortedData;
    }

    /**
     * Sets landing service table view in lunched application. Defines which columns can be updated and which not
     */
    public void setLandingTableViewColumns(){
        col_id_flight_landing_service.setCellValueFactory(new PropertyValueFactory<>("id_flight"));
        col_arrival_time_flight_landing_service.setCellValueFactory(new PropertyValueFactory<>("arrival_time"));
        col_aircraft_flight_landing_service.setCellValueFactory(new PropertyValueFactory<>("aircraft"));
        col_landed_flight_landing_service.setCellValueFactory(new PropertyValueFactory<>("aircraft_status"));

        col_landed_flight_landing_service.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), statusTakeOffAndLandingServiceComboBox_data));
        col_landed_flight_landing_service.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setAircraft_status(e.getNewValue());
        });

        landing_service_table.setEditable(true);
    }

    /**
     * Selects all flights and assigned to them aircrafts from database
     * @return object containing flights data pulled from database
     */
    public ResultSet selectFlightScheduleAircraft(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String sqlSelect = "SELECT * FROM flight_schedule, aircraft WHERE aircraft.id_aircraft = flight_schedule.id_aircraft";

        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(sqlSelect);
            return queryResult;

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return null;
        }
    }

    /**
     * Fills landing service table view with data pulled from database
     */
    public void initAndLoadData_LandingService(){
        landing_service_data = FXCollections.observableArrayList();
        aircraftIdLandingServiceComboBox_data= FXCollections.observableArrayList();
        initStatusListForComboBox();
        setLandingTableViewColumns();
        String aircraft = "";

        try {
            ResultSet selectFSAircraft = selectFlightScheduleAircraft();

            while(selectFSAircraft.next())
            {
                aircraft = selectFSAircraft.getString("id_aircraft")+". "+selectFSAircraft.getString("brand_name")+" "+selectFSAircraft.getString("model");

                if(selectFSAircraft.getString("status").equals("in_flight")) {
                    landing_service_data.add(new LandingServiceTable(selectFSAircraft.getString("id_flight_schedule"),
                            selectFSAircraft.getString("arrival_date"), aircraft,selectFSAircraft.getString("id_aircraft") , selectFSAircraft.getString("status")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        landing_service_table.setItems(searchableLandingServiceTableView());
    }

    /**
     * Sets flights schedule table view in lunched application. Defines which columns can be updated and which not
     */
    public void setFlightScheduleTableViewColumns(){
        col_id_flight_schedule.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_aircraft_flight_schedule.setCellValueFactory(new PropertyValueFactory<>("aircraft"));
        col_departure_place_flight_schedule.setCellValueFactory(new PropertyValueFactory<>("departure_place"));
        col_departure_date_flight_schedule.setCellValueFactory(new PropertyValueFactory<>("departure_date"));
        col_departure_time_flight_schedule.setCellValueFactory(new PropertyValueFactory<>("departure_time"));
        col_destination_flight_schedule.setCellValueFactory(new PropertyValueFactory<>("destination"));
        col_arrival_date_flight_schedule.setCellValueFactory(new PropertyValueFactory<>("arrival_date"));
        col_arrival_time_flight_schedule.setCellValueFactory(new PropertyValueFactory<>("arrival_time"));
        col_price_flight_schedule.setCellValueFactory(new PropertyValueFactory<>("price"));

        col_departure_place_flight_schedule.setCellFactory(TextFieldTableCell.forTableColumn());
        col_departure_place_flight_schedule.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setDeparture_place(e.getNewValue());
        });

        col_departure_date_flight_schedule.setCellFactory(TextFieldTableCell.forTableColumn());
        col_departure_date_flight_schedule.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setDeparture_date(e.getNewValue());
        });

        col_departure_time_flight_schedule.setCellFactory(TextFieldTableCell.forTableColumn());
        col_departure_time_flight_schedule.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setDeparture_time(e.getNewValue());
        });

        col_destination_flight_schedule.setCellFactory(TextFieldTableCell.forTableColumn());
        col_destination_flight_schedule.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setDestination(e.getNewValue());
        });

        col_arrival_date_flight_schedule.setCellFactory(TextFieldTableCell.forTableColumn());
        col_arrival_date_flight_schedule.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setArrival_date(e.getNewValue());
        });

        col_arrival_time_flight_schedule.setCellFactory(TextFieldTableCell.forTableColumn());
        col_arrival_time_flight_schedule.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setArrival_time(e.getNewValue());
        });

        col_price_flight_schedule.setCellFactory(TextFieldTableCell.forTableColumn());
        col_price_flight_schedule.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setPrice(e.getNewValue());
        });

        flight_schedule.setEditable(true);
    }

    /**
     * Selects all flights from database
     * @return object containing flights data pulled from database
     */
    public ResultSet selectFlightSchedule(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String sqlSelect = "SELECT * FROM flight_schedule";

        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(sqlSelect);
            return  queryResult;

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return null;
        }
    }

    /**
     * Fills flight schedule table view with data pulled from database
     */
    public void initAndLoadData_FlightSchedule(){
        flight_schedule_data = FXCollections.observableArrayList();
        setFlightScheduleTableViewColumns();
        String aircraft = "";

        try {
            ResultSet selectFlightSchedule = selectFlightSchedule();

            while(selectFlightSchedule.next())
            {
                aircraft = selectFlightSchedule.getString("id_aircraft");
                if(selectFlightSchedule.getString("id_aircraft") == null)
                    aircraft = "not assigned";

                flight_schedule_data.add(new FlightScheduleTable(selectFlightSchedule.getString("id_flight_schedule"), aircraft,
                        selectFlightSchedule.getString("departure_place"), selectFlightSchedule.getString("departure_date"), selectFlightSchedule.getString("departure_time"),
                        selectFlightSchedule.getString("destination"), selectFlightSchedule.getString("arrival_date"), selectFlightSchedule.getString("arrival_time"),
                        selectFlightSchedule.getString("price")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        flight_schedule.setItems(searchableFlightScheduleTableView());
    }

    /**
     * Allows to filter flight schedule table view by given phrase
     * @return filtered content of flight schedule table view as list
     */
    public SortedList<FlightScheduleTable> searchableFlightScheduleTableView(){
        FilteredList<FlightScheduleTable> filteredData = new FilteredList<>(flight_schedule_data, b -> true);

        flightScheduleSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(flight ->{
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if(String.valueOf(flight.getId().toLowerCase()).indexOf(lowerCaseFilter) != -1){
                    return true;
                } else if(flight.getArrival_time().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(flight.getAircraft().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(flight.getDeparture_time().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(flight.getDestination().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(flight.getDeparture_place().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(flight.getPrice().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(flight.getArrival_date().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(flight.getDeparture_date().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else return false;

            });
        });

        SortedList<FlightScheduleTable> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(flight_schedule.comparatorProperty());
        return sortedData;
    }

    /**
     * Opens movable popup window in which you can insert new flight record to database
     */
    public void addFlightOnAction() {
        if(!addFlightPopup){
            FXMLLoader root = openMovablePopupWindow("AddFlightPopup.fxml");
            AddFlightPopupController controller = root.getController();
            controller.receiveParentModuleObject(this);
            addFlightPopup = true;
        }
    }

    /**
     * Refreshes flight schedule table view pulling fresh data from database
     */
    public void refreshFlightScheduleOnAction(){
        initAndLoadData_FlightSchedule();
        queryInfoLabel_FlightSchedule.setText("");
        flightScheduleSearchBox.setText("");
    }

    /**
     * Refreshes take off service table view pulling fresh data from database
     */
    public void refreshTakeOffServiceOnAction(){
        initAndLoadData_TakeOffService();
        queryInfoLabel_take_off_service.setText("");
        takeOffServiceSearchBox.setText("");
    }

    /**
     * Refreshes landing service table view pulling fresh data from database
     */
    public void refreshLandingServiceOnAction(){
        initAndLoadData_LandingService();
        queryInfoLabel_landing_service.setText("");
        landingServiceSearchBox.setText("");
    }

    /**
     * Passes flight id from selected row in flight schedule table view to deleteFlight method and calls it. Method assigned to button "Delete"
     * in Flight schedule section, third module - Air Traffic
     */
    public void deleteFlightOnAction(){
        ObservableList<FlightScheduleTable> flightScheduleTables = flight_schedule.getSelectionModel().getSelectedItems();
        for(FlightScheduleTable flightScheduleTable : flightScheduleTables){

            String methodeResultMessage = deleteFlight(Integer.parseInt(flightScheduleTable.getId()));

            if(methodeResultMessage.contains("ERROR")) queryInfoLabel_FlightSchedule.setTextFill(Color.web("#bf1c37"));
            else queryInfoLabel_FlightSchedule.setTextFill(Color.web("#3072ff"));
            queryInfoLabel_FlightSchedule.setText(methodeResultMessage);

            initAndLoadData_FlightSchedule();
        }
    }

    /**
     * Deletes specific flight from database
     * @param id_flight_schedule flight id
     * @return method result message from database
     */
    public String deleteFlight(int id_flight_schedule){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String flightScheduleDelete = "DELETE FROM flight_schedule WHERE id_flight_schedule= ?";

        try{
            PreparedStatement preparedStatementFlightScheduleDelete = connectDB.prepareStatement(flightScheduleDelete);
            preparedStatementFlightScheduleDelete.setInt(1, id_flight_schedule);
            preparedStatementFlightScheduleDelete.executeUpdate();

            return "Successfully deleted flight with id number: " + id_flight_schedule;

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return "ERROR: something went wrong!";
        }
    }

    /**
     * Updates specific flight in database
     * @param flightId flight id
     * @param departurePlace flight departure place
     * @param departureDate flight departure date
     * @param departureTime flight departure time
     * @param destination flight destination place
     * @param arrivalDate flight arrival date
     * @param arrivalTime flight arrival time
     * @param price flight price for a single ticket
     * @return method result message as string
     */
    public String updateFlight(int flightId, String departurePlace, String departureDate, String departureTime, String destination, String arrivalDate, String arrivalTime, String price){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        boolean isDeparturePlaceValid = departurePlace.matches("^[a-zA-Z\\u0080-\\u024F\\s\\/\\-\\)\\(\\`\\.\\\"\\']+$");
        boolean isDepartureDateValid = departureDate.matches("^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$");
        boolean isDepartureTimeValid = departureTime.matches("^(?:(?:([01]?\\d|2[0-3]):)?([0-5]?\\d):)?([0-5]?\\d)$");
        boolean isDestinationValid = destination.matches("^[a-zA-Z\\u0080-\\u024F\\s\\/\\-\\)\\(\\`\\.\\\"\\']+$");
        boolean isArrivalDateValid = arrivalDate.matches("^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$");
        boolean isArrivalTimeValid = arrivalTime.matches("^(?:(?:([01]?\\d|2[0-3]):)?([0-5]?\\d):)?([0-5]?\\d)$");
        boolean isPriceValid = price.matches("[0-9]{1,13}(\\.[0-9]*)?");

        String flightScheduleUpdate = "UPDATE flight_schedule SET departure_place = ?, departure_date = ? , departure_time = ?, destination = ?, arrival_date = ?, arrival_time = ?, price = ?"+
                " WHERE id_flight_schedule= ?";
        try{
            if(departurePlace.equals("") ||departureDate.equals("")  || departureTime.equals("") ||  destination.equals("") || arrivalDate.equals("") || arrivalTime.equals("") || price.equals("")) {
                return "ERROR: all fields must be filled!";
            }else if(!isDeparturePlaceValid ){
                return "ERROR: Wrong data format given in departure place field!";
            }else if(!isDepartureDateValid ){
                return "ERROR: Wrong data format given in departure date field!";
            }else if(!isDepartureTimeValid ){
                return "ERROR: Wrong data format given in departure time field!";
            }else if(!isDestinationValid ){
                return "ERROR: Wrong data format given in destination field!";
            }else if(!isArrivalDateValid ){
                return "ERROR: Wrong data format given in arrival date field!";
            }else if(!isArrivalTimeValid ){
                return "ERROR: Wrong data format given in arrival time field!";
            }else if(!isPriceValid ) {
                return "ERROR: Wrong data format given in price field!";
            }else{
                PreparedStatement preparedStatementFlightScheduleUpdate = connectDB.prepareStatement(flightScheduleUpdate);
                preparedStatementFlightScheduleUpdate.setString(1, departurePlace);
                preparedStatementFlightScheduleUpdate.setString(2, departureDate);
                preparedStatementFlightScheduleUpdate.setString(3, departureTime);
                preparedStatementFlightScheduleUpdate.setString(4, destination);
                preparedStatementFlightScheduleUpdate.setString(5, arrivalDate);
                preparedStatementFlightScheduleUpdate.setString(6, arrivalTime);
                preparedStatementFlightScheduleUpdate.setString(7, price);
                preparedStatementFlightScheduleUpdate.setInt(8, flightId);
                preparedStatementFlightScheduleUpdate.executeUpdate();

                return "Successfully updated flight with id number: "+ flightId;
            }
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return "ERROR: Something went wrong!";
        }
    }

    /**
     * Passes flight id from selected row in flight schedule table view to updateFlight method and calls it. Method assigned to button "Update"
     * in Flight schedule section, third module - Air Traffic
     */
    public void updateFlightOnAction(){
        ObservableList<FlightScheduleTable> flightScheduleTables = flight_schedule.getSelectionModel().getSelectedItems();
        for(FlightScheduleTable flightScheduleTable : flightScheduleTables){

            String methodeResultMessage =  updateFlight(Integer.parseInt(flightScheduleTable.getId()), flightScheduleTable.getDeparture_place(), flightScheduleTable.getDeparture_date(),
                    flightScheduleTable.getDeparture_time(), flightScheduleTable.getDestination(), flightScheduleTable.getArrival_date(), flightScheduleTable.getArrival_time(), flightScheduleTable.getPrice());

            if(methodeResultMessage.contains("ERROR")) queryInfoLabel_FlightSchedule.setTextFill(Color.web("#bf1c37"));
            else {
                queryInfoLabel_FlightSchedule.setTextFill(Color.web("#3072ff"));
                initAndLoadData_FlightSchedule();
            }
            queryInfoLabel_FlightSchedule.setText(methodeResultMessage);
            break;
        }
    }

    /**
     * Updates specific flight with information which aircraft is assigned
     * @param aircraftId aircraft id
     * @param flightId flight id
     */
    public void updateTakeOffFlight(int aircraftId, int flightId){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String flightUpdate = "UPDATE flight_schedule SET id_aircraft = ? WHERE id_flight_schedule = ?";

        try {

            PreparedStatement preparedStatementUpdate = connectDB.prepareStatement(flightUpdate);
            preparedStatementUpdate.setInt(1, aircraftId);
            preparedStatementUpdate.setInt(2, flightId);
            preparedStatementUpdate.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    /**
     * Updates aircraft status from available to in_flight
     * @param aircraftId aircraft id
     * @param status aircraft status
     */
    public void updateTakeOffAircraft(int aircraftId, String status){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String aircraftUpdate = "UPDATE aircraft SET status = ? WHERE id_aircraft = ?";

        try {

            PreparedStatement preparedStatementUpdate = connectDB.prepareStatement(aircraftUpdate);
            preparedStatementUpdate.setString(1, status);
            preparedStatementUpdate.setInt(2, aircraftId);
            preparedStatementUpdate.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }

    /**
     * Passes flight id from selected row in take off service table view to updateTakeOffFlight method and calls it. Method assigned to button "Update"
     * in Take off service section, third module - Air Traffic
     */
    public void updateTakeOffFlightOnAction(){
        ObservableList<TakeOffServiceTable> takeOffServiceTables = take_off_service_table.getSelectionModel().getSelectedItems();
        for(TakeOffServiceTable takeOffServiceTable : takeOffServiceTables){

            int separatorIndex = takeOffServiceTable.getAircraft().indexOf(".");

            String id_aircraft= "";
            if (separatorIndex != -1)
                id_aircraft= takeOffServiceTable.getAircraft().substring(0 , separatorIndex);
            else
                id_aircraft = null;

            updateTakeOffFlight(Integer.parseInt(id_aircraft),Integer.parseInt(takeOffServiceTable.getId_flight()));

            if(id_aircraft!= null && takeOffServiceTable.getStatus().equals("-")) {
                queryInfoLabel_take_off_service.setTextFill(Color.web("#3072ff"));
                queryInfoLabel_take_off_service.setText("Successfully assigned aircraft to flight with id number: " + takeOffServiceTable.getId_flight());
            }else{
                queryInfoLabel_take_off_service.setTextFill(Color.web("#3072ff"));
                queryInfoLabel_take_off_service.setText("Successfully updated aircraft from flight with id number: " + takeOffServiceTable.getId_flight());
            }

            if(!takeOffServiceTable.getStatus().equals("-")) {
                updateTakeOffAircraft(Integer.parseInt(id_aircraft), takeOffServiceTable.getStatus());
            }
        }
        initAndLoadData_TakeOffService();
    }

    /**
     * Updates aircraft status from in flight to available
     * @param aircraftId aircraft id
     * @param status aircraft status
     * @return method result message as string
     */
    public String updateLandingFlight(int aircraftId, String status){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String aircraftUpdate = "UPDATE aircraft SET status = 'available', technical_condition = 'for_check_up', is_tanked = 'false' WHERE id_aircraft = ?";

        try{
            if(status.equals("available")) {
                PreparedStatement preparedStatementUpdate = connectDB.prepareStatement(aircraftUpdate);
                preparedStatementUpdate.setInt(1, aircraftId);
                preparedStatementUpdate.executeUpdate();

                String flightDelete = "DELETE FROM flight_schedule WHERE id_aircraft = ?";
                PreparedStatement preparedStatementDelete = connectDB.prepareStatement(flightDelete);
                preparedStatementDelete.setInt(1, aircraftId);
                preparedStatementDelete.executeUpdate();

                return "Aircraft with id: " + aircraftId + " successfully marked as landed!";

            }else{
                return "Nothing has changed";
            }

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return "ERROR: Something went wrong";
        }
    }

    /**
     * Passes flight id from selected row in Landing service table view to updateLandingFlight method and calls it. Method assigned to button "Update"
     * in Landing service section, third module - Air Traffic
     */
    public void updateLandingFlightOnAction(){

        ObservableList<LandingServiceTable> landingServiceTables = landing_service_table.getSelectionModel().getSelectedItems();
        for(LandingServiceTable landingServiceTable : landingServiceTables){

            String methodResultMessage =  updateLandingFlight(Integer.parseInt(landingServiceTable.getId_aircraft()), landingServiceTable.getAircraft_status());

            if(methodResultMessage.contains("ERROR")) queryInfoLabel_landing_service.setTextFill(Color.web("#bf1c37"));
            else queryInfoLabel_landing_service.setTextFill(Color.web("#3072ff"));
            queryInfoLabel_landing_service.setText(methodResultMessage);
        }

        initAndLoadData_LandingService();
    }

}