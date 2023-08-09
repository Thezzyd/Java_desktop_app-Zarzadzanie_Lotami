package sample.M4_CustomerService;

import com.zarzadzanieLotami.PDFgeneratorZL.PDF;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import sample.DatabaseFiles.DatabaseConnection;
import sample.ParentScene.ModuleParent;
import sample.tables.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;
import java.util.ResourceBundle;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * Controller class for fourth module screen window - Customer Service
 * @author Daniel Czyz
 * @version 1.0.1 30/05/2021
 */
public class M4_CustomerServiceController extends ModuleParent {

    @FXML
    public TableView<FlightsTable> customer_service_table;
    @FXML
    private TableColumn<FlightsTable, String> col_id_customer_service;
    @FXML
    private TableColumn<FlightsTable, String> col_departure_date_customer_service;
    @FXML
    private TableColumn<FlightsTable, String> col_departure_time_customer_service;
    @FXML
    private TableColumn<FlightsTable, String> col_departure_place_customer_service;
    @FXML
    private TableColumn<FlightsTable, String> col_destination_customer_service;
    @FXML
    private TableColumn<FlightsTable, String> col_arrival_date_customer_service;
    @FXML
    private TableColumn<FlightsTable, String> col_arrival_time_customer_service;
    @FXML
    private TableColumn<FlightsTable, String> col_price_customer_service;
    @FXML
    private TableColumn<FlightsTable, String> col_sold_tickets_customer_service;

    @FXML
    public TableView<TicketTable> ticket_table;
    @FXML
    private TableColumn<TicketTable, String> col_id_ticket;
    @FXML
    private TableColumn<TicketTable, String> col_flight_ticket;
    @FXML
    private TableColumn<TicketTable, String> col_firstname_ticket;
    @FXML
    private TableColumn<TicketTable, String> col_lastname_ticket;
    @FXML
    private TableColumn<TicketTable, String> col_phone_number_ticket;
    @FXML
    private TableColumn<TicketTable, String> col_email_ticket;

    @FXML
    private Label queryInfoLabel_customer_service;
    @FXML
    private Label queryInfoLabel_Ticket;
    @FXML
    private TextField listOfFlightsSearchBox;
    @FXML
    private TextField assignedTicketsSearchBox;

    public static ObservableList<FlightsTable> flights_table_data ;
    public static ObservableList<TicketTable> ticket_table_data ;

    public boolean assignTicketPopup = false;

    public static ObservableList<File> pdfs ;

    /**
     * Content of this method is executed when instance of M4_CustomerServiceController class is loaded
     * @param location location
     * @param resource resource
     */
    @Override
    public void initialize(URL location, ResourceBundle resource){
        rootPane.setOpacity(0);
        makeFadeInTransition(rootPane);
        initAndLoadData_TableFlights();
        initAndLoadData_TableTicket();

        pdfs  = FXCollections.observableArrayList();

    }

    /**
     * Sets customer service table view in lunched application. Defines which columns can be updated and which not
     */
    public void seCustomerServiceTableViewColumns(){
        col_id_customer_service.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_departure_date_customer_service.setCellValueFactory(new PropertyValueFactory<>("departure_date"));
        col_departure_time_customer_service.setCellValueFactory(new PropertyValueFactory<>("departure_time"));
        col_departure_place_customer_service.setCellValueFactory(new PropertyValueFactory<>("departure_place"));
        col_destination_customer_service.setCellValueFactory(new PropertyValueFactory<>("destination"));
        col_arrival_date_customer_service.setCellValueFactory(new PropertyValueFactory<>("arrival_date"));
        col_arrival_time_customer_service.setCellValueFactory(new PropertyValueFactory<>("arrival_time"));
        col_price_customer_service.setCellValueFactory(new PropertyValueFactory<>("price"));
        col_sold_tickets_customer_service.setCellValueFactory(new PropertyValueFactory<>("sold_tickets"));
    }

    /**
     * Selects flights information with current total amount of sold tickets
     * @return object containing flights data pulled from database
     */
    public ResultSet selectFlightScheduleTicket(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String sqlSelect = "SELECT *, count(id_ticket) AS sold_tickets FROM flight_schedule Left join ticket on flight_schedule.id_flight_schedule = ticket.id_flight_schedule group by flight_schedule.id_flight_schedule;";

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
     * Fills customer service table view with data pulled from database
     */
    public void initAndLoadData_TableFlights(){
        flights_table_data = FXCollections.observableArrayList();
        seCustomerServiceTableViewColumns();

        try {
            ResultSet selectResult = selectFlightScheduleTicket();

            while(selectResult.next())
            {
                flights_table_data.add(new FlightsTable(selectResult.getString("id_flight_schedule"),
                        selectResult.getString("departure_place"), selectResult.getString("departure_date"), selectResult.getString("departure_time"),
                        selectResult.getString("destination"), selectResult.getString("arrival_date"), selectResult.getString("arrival_time"),
                        selectResult.getString("price"), selectResult.getString("sold_tickets")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        customer_service_table.setItems(searchableFlightsTableView());
    }

    /**
     * Allows to filter customer service table view by given phrase
     * @return filtered content of customer service table view as list
     */
    public SortedList<FlightsTable> searchableFlightsTableView(){
        FilteredList<FlightsTable> filteredData = new FilteredList<>(flights_table_data, b -> true);

        listOfFlightsSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(flight ->{
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if(String.valueOf(flight.getId().toLowerCase()).indexOf(lowerCaseFilter) != -1){
                    return true;
                } else if(flight.getArrival_date().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(flight.getArrival_time().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(flight.getDeparture_date().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(flight.getDeparture_time().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(flight.getDeparture_place().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(flight.getDestination().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(flight.getPrice().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(flight.getSold_tickets().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else return false;

            });
        });
        SortedList<FlightsTable> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(customer_service_table.comparatorProperty());
        return sortedData;
    }

    /**
     * Sets ticket table view in lunched application. Defines which columns can be updated and which not
     */
    public void seTicketTableViewColumns(){
        col_id_ticket.setCellValueFactory(new PropertyValueFactory<>("id_ticket"));
        col_flight_ticket.setCellValueFactory(new PropertyValueFactory<>("id_flight_schedule"));
        col_firstname_ticket.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        col_lastname_ticket.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        col_phone_number_ticket.setCellValueFactory(new PropertyValueFactory<>("phone_number"));
        col_email_ticket.setCellValueFactory(new PropertyValueFactory<>("email"));

        col_firstname_ticket.setCellFactory(TextFieldTableCell.forTableColumn());
        col_firstname_ticket.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setFirstname(e.getNewValue());
        });

        col_lastname_ticket.setCellFactory(TextFieldTableCell.forTableColumn());
        col_lastname_ticket.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setLastname(e.getNewValue());
        });

        col_phone_number_ticket.setCellFactory(TextFieldTableCell.forTableColumn());
        col_phone_number_ticket.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setPhone_number(e.getNewValue());
        });

        col_email_ticket.setCellFactory(TextFieldTableCell.forTableColumn());
        col_email_ticket.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setEmail(e.getNewValue());
        });

        ticket_table.setEditable(true);
    }

    /**
     * Selects all tickets assigned to flights
     * @return object containing ticket data pulled from database
     */
    public ResultSet selectTicketFlightSchedule(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String sqlSelect = "SELECT * FROM ticket,flight_schedule WHERE ticket.id_flight_schedule = flight_schedule.id_flight_schedule;";

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
     * Fills ticket table view with data pulled from database
     */
    public void initAndLoadData_TableTicket(){
        ticket_table_data = FXCollections.observableArrayList();
        seTicketTableViewColumns();

        try {
            ResultSet selectResult = selectTicketFlightSchedule();

            while(selectResult.next())
            {
                ticket_table_data.add(new TicketTable(selectResult.getString("id_ticket"),
                        selectResult.getString("id_flight_schedule")+". "+selectResult.getString("departure_place")+
                                " "+selectResult.getString("departure_date")+" "+selectResult.getString("departure_time")+" => "
                                +selectResult.getString("destination")+" "+selectResult.getString("arrival_date")+" "
                                +selectResult.getString("arrival_time")+" (price: "+selectResult.getString("price")+")", selectResult.getString("firstname"),
                        selectResult.getString("lastname"), selectResult.getString("phone_number"), selectResult.getString("email")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        ticket_table.setItems(searchableTicketTableView());
    }

    /**
     * Allows to filter ticket table view by given phrase
     * @return filtered content of ticket table view as list
     */
    public SortedList<TicketTable> searchableTicketTableView(){
        FilteredList<TicketTable> filteredData = new FilteredList<>(ticket_table_data, b -> true);

        assignedTicketsSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ticket ->{
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if(String.valueOf(ticket.getId_ticket().toLowerCase()).indexOf(lowerCaseFilter) != -1){
                    return true;
                } else if(ticket.getFirstname().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(ticket.getLastname().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(ticket.getEmail().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(ticket.getPhone_number().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(ticket.getId_flight_schedule().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else return false;

            });
        });
        SortedList<TicketTable> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(ticket_table.comparatorProperty());
        return sortedData;
    }

    /**
     * Deletes specific ticket from database
     * @param ticketId ticket id
     * @return method result message as string
     */
    public String deleteTicket(int ticketId){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String ticketDelete = "DELETE FROM ticket WHERE id_ticket= ?";

        try{
            PreparedStatement preparedStatementTicketDelete = connectDB.prepareStatement(ticketDelete);
            preparedStatementTicketDelete.setInt(1, ticketId);
            preparedStatementTicketDelete.executeUpdate();

            return "Successfully deleted ticket with id number: "+ ticketId;

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return "ERROR: something went wrong!";
        }
    }

    /**
     * Passes ticket id from selected row in ticket table view to deleteTicket method and calls it. Method assigned to button "Delete"
     * in Flights section, fourth module - Customer Service
     */
    public void deleteTicketOnAction(){
        ObservableList<TicketTable> ticketTables = ticket_table.getSelectionModel().getSelectedItems();
        for(TicketTable ticketTable : ticketTables){

            String methodResultMessage = deleteTicket(Integer.parseInt(ticketTable.getId_ticket()));
            initAndLoadData_TableTicket();

            if(methodResultMessage.contains("ERROR")) queryInfoLabel_Ticket.setTextFill(Color.web("#bf1c37"));
            else queryInfoLabel_Ticket.setTextFill(Color.web("#3072ff"));
            queryInfoLabel_Ticket.setText(methodResultMessage);
            break;
        }
    }

    /**
     * Updates specific ticket in database
     * @param ticketId ticket id
     * @param firstname ticket owner firstname
     * @param lastname ticket owner lastname
     * @param email ticket owner email
     * @param phoneNumber ticket owner phone number
     * @return method result message as string
     */
    public String updateTicket(int ticketId, String firstname, String lastname, String email, String phoneNumber){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        boolean isFirstnameValid = firstname.matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$");
        boolean isLastnameValid = lastname.matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$");
        boolean isEmailValid = email.matches("^(.+)@(.+)$");
        boolean isPhoneNumberValid = phoneNumber.matches("^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{3}$");

        String ticketUpdate = "UPDATE ticket SET firstname = ?, lastname = ?, phone_number = ?, email = ? WHERE id_ticket= ?";

        try{

            if(firstname.equals("") || lastname.equals("") || email.equals("") || phoneNumber.equals("")) {
                return "ERROR: all fields must be filled!";
            }else if(!isFirstnameValid ){
                return "ERROR: Wrong data format given in firstname field!";
            }else if(!isLastnameValid ){
                return "ERROR: Wrong data format given in lastname field!";
            }else if(!isEmailValid ){
                return "ERROR: Wrong data format given in email field!";
            }else if(!isPhoneNumberValid ){
                return "ERROR: Wrong data format given in phone number field!";
            }else{
                PreparedStatement preparedStatementTicketUpdate = connectDB.prepareStatement(ticketUpdate);
                preparedStatementTicketUpdate.setString(1, firstname);
                preparedStatementTicketUpdate.setString(2, lastname);
                preparedStatementTicketUpdate.setString(3, phoneNumber);
                preparedStatementTicketUpdate.setString(4, email);
                preparedStatementTicketUpdate.setInt(5, ticketId);
                preparedStatementTicketUpdate.executeUpdate();

                return "Successfully updated ticket with id number: "+ ticketId;
            }
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return "ERROR: Something went wrong!";
        }
    }

    /**
     * Passes ticket id from selected row in ticket table view to updateTicket method and calls it. Method assigned to button "Update"
     * in Flights section, fourth module - Customer Service
     */
    public void updateTicketOnAction(){
        ObservableList<TicketTable> ticketTables = ticket_table.getSelectionModel().getSelectedItems();
        for(TicketTable ticketTable : ticketTables){
            System.out.println("weszloo");
            String methodResultMessage = updateTicket(Integer.parseInt(ticketTable.getId_ticket()), ticketTable.getFirstname(), ticketTable.getLastname(), ticketTable.getEmail(), ticketTable.getPhone_number());

            if(methodResultMessage.contains("ERROR")) queryInfoLabel_Ticket.setTextFill(Color.web("#bf1c37"));
            else {
                queryInfoLabel_Ticket.setTextFill(Color.web("#3072ff"));
                initAndLoadData_TableTicket();
            }
            queryInfoLabel_Ticket.setText(methodResultMessage);
        }
    }

    /**
     * Checks if there is still possibility to assign customer to a ticket basing on current time and time to flight departure. Time to departure must be greater then 30 minutes
     * @return true is there is still possibility to assign a ticket to customer, if not returns false
     */
    public boolean isTimeLeftToAssignTicket() {
        //Select co klikam i decyduje czy puszczam dalej...
        refreshCurrentTime();
        ObservableList<FlightsTable> flightTables = customer_service_table.getSelectionModel().getSelectedItems();
        for (FlightsTable flightTable : flightTables) {

            LocalDate flightDepartureDate = LocalDate.parse(flightTable.getDeparture_date());
            LocalTime flightDepartureTime = LocalTime.parse(flightTable.getDeparture_time());

            System.out.println(flightDepartureDate + " ...... " + currentLocalDate);

            long daysToFlight = DAYS.between(currentLocalDate,flightDepartureDate);
            long minutesToFlight = MINUTES.between(currentLocalTime, flightDepartureTime);
            minutesToFlight = minutesToFlight + (daysToFlight * 1440);

            if (minutesToFlight < 30) {
                // jeżeli zostało 30 minut do odlotu, na lot nie można już zakupić biletu...
                return false;
            }
        }
        return true;
    }

    /**
     * Opens movable popup window in which you can insert new ticket for specific flight record to database
     */
    public void assignTicketOnAction() {
        if (!isTimeLeftToAssignTicket()) {
            queryInfoLabel_customer_service.setTextFill(Color.web("#bf1c37"));
            queryInfoLabel_customer_service.setText("ERROR: It is too late to buy ticket for this flight! (Less then 30 minutes left to departure)");
        } else {

            if (!assignTicketPopup) {
                ObservableList<FlightsTable> flightsTables = customer_service_table.getSelectionModel().getSelectedItems();
                for (FlightsTable flightTable : flightsTables) {

                    FXMLLoader root = openMovablePopupWindow("AssignTicketPopup.fxml");
                    AssignTicketPopupController controller = root.getController();
                    controller.setFlightId(flightTable.getId() + ". " + flightTable.getDeparture_place() + " " + flightTable.getDeparture_date() + " " + flightTable.getDeparture_time() + " => " +
                            flightTable.getDestination() + " " + flightTable.getArrival_date() + " " + flightTable.getArrival_time() + " (price: " + flightTable.getPrice() + ")", flightTable.getId());

                    controller.receiveParentModuleObject(this);
                    assignTicketPopup = true;
                }

                if (flightsTables.isEmpty()) {
                    queryInfoLabel_customer_service.setTextFill(Color.web("#bf1c37"));
                    queryInfoLabel_customer_service.setText("ERROR: please select a flight first");
                }
            }
        }
    }

    /**
     * Refreshes customer service table view pulling fresh data from database
     */
    public void refreshFlightsOnAction(){
        initAndLoadData_TableFlights();
        initAndLoadData_TableTicket();
        queryInfoLabel_customer_service.setText("");
        queryInfoLabel_Ticket.setText("");
        assignedTicketsSearchBox.setText("");
        listOfFlightsSearchBox.setText("");
    }

    /**
     * Selects data of specific ticket
     * @param id_ticket ticket id
     * @return object containing ticket data pulled from database
     */
    public ResultSet getTicketData(int id_ticket){

        try{
            String ticketSelect = "SELECT * FROM ticket WHERE id_ticket = ?";

            DatabaseConnection databaseConnection = new DatabaseConnection();
            Connection connection = databaseConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(ticketSelect);
            preparedStatement.setInt(1, id_ticket);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet;

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return null;
        }
    }

    /**
     * Selects data of specific flight
     * @param id_flight flight id
     * @return object containing flight data pulled from database
     */
    public ResultSet getFlightData(int id_flight){
        try{
            String flightSelect = "SELECT * FROM flight_schedule WHERE id_flight_schedule = ?";

            DatabaseConnection databaseConnection = new DatabaseConnection();
            Connection connection = databaseConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(flightSelect);
            preparedStatement.setInt(1, id_flight);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet;

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return null;
        }
    }

    /**
     * Generates pdf for selected ticket in ticket table view. File is saved automatically in "myDocuments/zl_pdfs" and opened in default browser for pdf files set in system
     * @param filePath path where to save file with file name
     * @param ticketTable selected ticket in ticket table view
     */
    public void generateTicketPDFOnAction(String filePath, TicketTable ticketTable){

        try{
            ResultSet ticketResultSet = getTicketData(Integer.parseInt(ticketTable.getId_ticket()));
            ticketResultSet.next();

            ResultSet flightResultSet = getFlightData(ticketResultSet.getInt("id_flight_schedule"));
            flightResultSet.next();

            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
            System.out.println(formatter.format(flightResultSet.getDouble("price")));
            String price = formatter.format(flightResultSet.getDouble("price"));

            PDF pdf = new PDF(getClass().getResourceAsStream("/images/Ticket_template_3.pdf"));

            pdf.addBlackLineOfTextToPDF(16,290,232, ""+flightResultSet.getString("departure_place"));
            pdf.addBlackLineOfTextToPDF(16,290,202, ""+flightResultSet.getString("departure_date"));
            pdf.addBlackLineOfTextToPDF(16,290,172, ""+flightResultSet.getString("departure_time"));
            pdf.addBlackLineOfTextToPDF(16,290,142, ""+flightResultSet.getString("destination"));
            pdf.addBlackLineOfTextToPDF(16,290,112, ""+flightResultSet.getString("arrival_date"));
            pdf.addBlackLineOfTextToPDF(16,290,84,  ""+flightResultSet.getString("arrival_time"));
            pdf.addBlackLineOfTextToPDF(16,290,52,  ""+price);

            pdf.addBlackLineOfTextToPDF(16,650,191, ""+ticketResultSet.getString("firstname"));
            pdf.addBlackLineOfTextToPDF(16,650,161, ""+ticketResultSet.getString("lastname"));
            pdf.addBlackLineOfTextToPDF(16,650,131, ""+ticketResultSet.getString("email"));
            pdf.addBlackLineOfTextToPDF(16,650,104, ""+ticketResultSet.getString("phone_number"));

            pdf.saveAndCloseDocument(filePath);
            //  pdfs.add(new File(filePath));
            //    File pdfToDelete = new File(filePath);
            //   pdfToDelete.deleteOnExit();
            Desktop.getDesktop().browse(new URL( "file:///"+filePath).toURI());

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    /**
     * Checks where on computer is located my Documents directory, sets name for new pdf file then calls generateTicketPDFOnAction method
     */
    public void fileChooser(){
        ObservableList<TicketTable> ticketTables = ticket_table.getSelectionModel().getSelectedItems();

        //  ObservableList<TicketTable> ticketTables = ticket_table.getItems();
        for(TicketTable ticketTable : ticketTables){

            try {
            /*   Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialFileName("Ticket_with_id_"+ticketTable.getId_ticket());
                FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
                fileChooser.getExtensionFilters().add(extensionFilter);

                File file = fileChooser.showSaveDialog(stage);

                if (file != null) {
                    final String filePath = file.getAbsolutePath();
                    generateTicketPDFOnAction(filePath, ticketTable);
                    Desktop.getDesktop().browse(new URL( "file:///"+filePath).toURI());

                }*/
                System.out.println(Desktop.isDesktopSupported() );
                String filePath = new JFileChooser().getFileSystemView().getDefaultDirectory().toString()+"\\zl_pdfs";
                Path path = Paths.get(filePath);
                Files.createDirectories(path);

                generateTicketPDFOnAction(filePath+"\\ticket_"+ticketTable.getId_ticket()+".pdf", ticketTable);


            }catch(Exception e){
                e.printStackTrace();
                e.getCause();
            }
            break;
        }
    }

}