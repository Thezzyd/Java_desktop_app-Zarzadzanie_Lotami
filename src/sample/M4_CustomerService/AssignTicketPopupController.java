package sample.M4_CustomerService;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.DatabaseFiles.DatabaseConnection;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * Controller class for fourth module popup screen window - Customer Service
 * @author Daniel Czyz
 * @version 1.0.1 29/05/2021
 */
public class AssignTicketPopupController extends M4_CustomerServiceController {

    @FXML
    private Label assignTicketInfoLabel;
    @FXML
    private Button closeAssignTicketButton;

    @FXML
    private TextField flightTextField;
    @FXML
    private TextField firstnameTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField phoneNumberTextField;

    private String flight_id = "";
    private M4_CustomerServiceController parentM4;

    /**
     * Content of this method is executed when instance of AddTicketPopupController class is loaded
     * @param location location
     * @param resource resource
     */
    @Override
    public void initialize(URL location, ResourceBundle resource){

    }

    /**
     * Closes movable popup window which allows user to insert new ticket to database. Method assigned to a button "Add"
     */
    public void closeAddUserPopup(){
        Stage addPositionStage = (Stage) closeAssignTicketButton.getScene().getWindow();
        addPositionStage.close();
        parentM4.assignTicketPopup = false;
    }

    /**
     * Flight information is passed from source class, then flight information text field on screen is filled with that information
     * @param flightInfoString information about flight
     * @param id flight id
     */
    public void setFlightId(String flightInfoString, String id){
        flightTextField.setText(flightInfoString);
        flight_id = id;
    }

    /**
     * Inserts new ticket record to database
     * @param idFlightSchedule flight id
     * @param firstname firstname of ticket owner
     * @param lastname lastname of ticket owner
     * @param email email of ticket owner
     * @param phoneNumber phone number of ticket owner
     * @return method result message as string
     */
    public String insertTicket(int idFlightSchedule, String firstname, String lastname, String email, String phoneNumber){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        boolean isFirstnameValid = firstname.matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$");
        boolean isLastnameValid = lastname.matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$");
        boolean isEmailValid = email.matches("^(.+)@(.+)$");
        boolean isPhoneNumberValid = phoneNumber.matches("^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{3}$");

        String ticketInsert = "INSERT INTO ticket(id_flight_schedule, firstname, lastname, phone_number, email) VALUES (?,?,?,?,?)";

        try {
            if (firstname.equals("") || lastname.equals("") || email.equals("") || phoneNumber.equals("")) {
                return "ERROR: Please fill all fields!";
            }else if(!isFirstnameValid ){
                return "ERROR: Wrong data format given in firstname field!";
            }else if(!isLastnameValid ){
                return "ERROR: Wrong data format given in lastname field!";
            }else if(!isEmailValid ){
                return "ERROR: Wrong data format given in email field!";
            }else if(!isPhoneNumberValid ){
                return "ERROR: Wrong data format given in phone number field!";
            }else {
                PreparedStatement preparedStatementTicketInsert = connectDB.prepareStatement(ticketInsert);
                preparedStatementTicketInsert.setInt(1, idFlightSchedule);
                preparedStatementTicketInsert.setString(2, firstname);
                preparedStatementTicketInsert.setString(3, lastname);
                preparedStatementTicketInsert.setString(4, phoneNumber);
                preparedStatementTicketInsert.setString(5, email);
                preparedStatementTicketInsert.executeUpdate();

                return "Ticket assigned successfully!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
            return "ERROR: Something went wrong!";
        }
    }

    /**
     * Gathers data from text fields, then calls insertTicket method
     */
    public void assignTicketOnAction(){
        if(!isTimeLeftToAssignTicket()){
            assignTicketInfoLabel.setTextFill(Color.web("#bf1c37"));
            assignTicketInfoLabel.setText("ERROR: It is too late to buy ticket for this flight! (Less then 30 minutes left to departure)");
        }else {
            String firstname = firstnameTextField.getText();
            String lastname = lastnameTextField.getText();
            String email = emailTextField.getText();
            String phoneNumber = phoneNumberTextField.getText();

            String methodResultMessage = insertTicket(Integer.parseInt(flight_id), firstname, lastname, email, phoneNumber);

            if(methodResultMessage.contains("ERROR")) assignTicketInfoLabel.setTextFill(Color.web("#bf1c37"));
            else{
                assignTicketInfoLabel.setTextFill(Color.web("#3072ff"));
                firstnameTextField.setText("");
                lastnameTextField.setText("");
                phoneNumberTextField.setText("");
                emailTextField.setText("");
            }
            assignTicketInfoLabel.setText(methodResultMessage);

            parentM4.refreshFlightsOnAction();
        }
    }

    /**
     * Checks if there is still possibility to assign customer to a ticket basing on current time and time to flight departure. Time to departure must be greater then 30 minutes
     * @return true is there is still possibility to assign a ticket to customer, if not returns false
     */
    @Override
    public boolean isTimeLeftToAssignTicket(){

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String sqlSelect = "SELECT * FROM flight_schedule WHERE id_flight_schedule = "+flight_id+" LIMIT 1;";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(sqlSelect);

            while(queryResult.next())
            {
                refreshCurrentTime();
                LocalDate flightDepartureDate = LocalDate.parse(queryResult.getString("departure_date"));
                LocalTime flightDepartureTime = LocalTime.parse(queryResult.getString("departure_time"));

                long daysToFlight = DAYS.between(currentLocalDate,flightDepartureDate);
                long minutesToFlight = MINUTES.between(currentLocalTime, flightDepartureTime);
                minutesToFlight = minutesToFlight + (daysToFlight * 1440);

                if (minutesToFlight < 30) {
                    // jeżeli zostało 30 minut do odlotu, na lot nie można już zakupić biletu...
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }
        return true;
    }

    /**
     * Saves instance of parent controller object to private variable (parent means scene controller from where AddTicketController was called)
     * @param parentModule fourth module parent controller - Customer Service instance
     */
    public void receiveParentModuleObject(M4_CustomerServiceController parentModule){
        parentM4 = parentModule;
    }

}