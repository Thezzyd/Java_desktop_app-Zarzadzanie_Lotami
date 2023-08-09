package sample.M3_AirTraffic;

import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.DatabaseFiles.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.time.temporal.ChronoField;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MINUTES;


/**
 * Controller class for third module popup screen window - Air Traffic
 * @author Daniel Czyz
 * @version 1.0.1 29/05/2021
 */
public class AddFlightPopupController {

    @FXML
    private Label addFlightInfoLabel;
    @FXML
    private Button closeAddFlightButton;


    @FXML
    private TextField departurePlaceTextField;
    @FXML
    private DatePicker departureDateTextField;
    @FXML
    private JFXTimePicker departureTimeTextField;
    @FXML
    private TextField destinationTextField;
    @FXML
    private DatePicker arrivalDateTextField;
    @FXML
    private JFXTimePicker arrivalTimeTextField;
    @FXML
    private TextField priceTextField;

    private M3_AirTrafficController parentM3;

    /**
     * Closes movable popup window which allows user to insert new flight to database. Method assigned to a button "Add"
     */
    public void closeAddFlightPopup(){
        Stage addPositionStage = (Stage) closeAddFlightButton.getScene().getWindow();
        addPositionStage.close();
        parentM3.addFlightPopup = false;
    }

    /**
     * Checks if given departure date and time is earlier then arrival date and time
     * @param departureDate flight departure date
     * @param arrivalDate flight arrival date
     * @param departureTime flight departure time
     * @param arrivalTime flight arrival time
     * @return true if departure date and time is earlier then arrival date and time else returns false
     */
    public boolean isDepartureDateEarlierThenArrivalDate(String departureDate, String arrivalDate, String departureTime, String arrivalTime){
        try {
            LocalDate dd = LocalDate.parse(departureDate);
            LocalDate ad = LocalDate.parse(arrivalDate);
            LocalTime dt = LocalTime.parse(departureTime);
            LocalTime at = LocalTime.parse(arrivalTime);

            if(DAYS.between(dd,ad) > 0)
                return true;
            else if (DAYS.between(dd,ad) < 0)
                return false;
            else{
                if(MINUTES.between(dt,at) > 0)
                    return true;
                else
                    return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return false;
        }
    }

    /**
     * Inserts new flight record to database
     * @param departurePlace flight departure place
     * @param departureDate flight departure date
     * @param departureTime flight departure time
     * @param destination flight destination place
     * @param arrivalDate flight arrival date
     * @param arrivalTime flight arrival time
     * @param price flight price for a single ticket
     * @return method result message as string
     */
    public String insertFlight(String departurePlace, String departureDate, String departureTime, String destination, String arrivalDate, String arrivalTime, String price) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String flightScheduleInsert = "INSERT INTO flight_schedule(departure_place, departure_date, departure_time, destination, arrival_date, arrival_time, price) VALUES (?,?,?,?,?,?,?)";

        boolean isDeparturePlaceValid = departurePlace.matches("^[a-zA-Z\\u0080-\\u024F\\s\\/\\-\\)\\(\\`\\.\\\"\\']+$");
        boolean isDestinationValid = destination.matches("^[a-zA-Z\\u0080-\\u024F\\s\\/\\-\\)\\(\\`\\.\\\"\\']+$");
        boolean isPriceValid = price.matches("[0-9]{1,13}(\\.[0-9]*)?");


        if (departurePlace.equals("") || departureDate.equals("") || departureTime.equals("") || destination.equals("") || arrivalDate.equals("") || arrivalTime.equals("") || price.equals("") ) {
            return "ERROR: Please fill all fields!";
        }else if(!isDeparturePlaceValid ){
            return "ERROR: Wrong data format given in departure place field!";
        }else if(!isDestinationValid ){
            return "ERROR: Wrong data format given in destination field!";
        }else if(!isPriceValid ){
            return "ERROR: Wrong data format given in price field!";
        }else {
            try {
                if(!isDepartureDateEarlierThenArrivalDate(departureDate, arrivalDate, departureTime, arrivalTime)){
                    return "ERROR: Arrival data-time can not be before departure data-time!";
                }

                PreparedStatement preparedStatementFlightScheduleInsert = connectDB.prepareStatement(flightScheduleInsert);
                preparedStatementFlightScheduleInsert.setString(1, departurePlace);
                preparedStatementFlightScheduleInsert.setString(2, departureDate);
                preparedStatementFlightScheduleInsert.setString(3, departureTime);
                preparedStatementFlightScheduleInsert.setString(4, destination);
                preparedStatementFlightScheduleInsert.setString(5, arrivalDate);
                preparedStatementFlightScheduleInsert.setString(6, arrivalTime);
                preparedStatementFlightScheduleInsert.setString(7, price);
                preparedStatementFlightScheduleInsert.executeUpdate();

                return "Aircraft added successfully!";

            } catch (SQLException e) {
                System.out.println(departurePlace+" "+departureDate+" "+departureTime+" "+destination+" "+arrivalDate+" "+arrivalTime+" "+price);
                return "ERROR: Wrong datatype given!";
            } catch (RuntimeException e) {
                return "ERROR: Please fill all fields!";
            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
                return "ERROR: Something went wrong!";
            }
        }
    }

    /**
     * Gathers data from text fields, then calls insertFlight method
     */
    public void addFlightOnAction() {
        String departurePlace = departurePlaceTextField.getText();
        String departureDate = ""; if(departureDateTextField.getValue() != null) departureDate = departureDateTextField.getValue().toString(); else departureDate ="";
        String departureTime = ""; if(departureTimeTextField.getValue() != null) departureTime = departureTimeTextField.getValue().toString(); else departureTime ="";
        String destination = destinationTextField.getText();
        String arrivalDate = ""; if(arrivalDateTextField.getValue() != null) arrivalDate = arrivalDateTextField.getValue().toString(); else arrivalDate ="";
        String arrivalTime = ""; if(arrivalTimeTextField.getValue() != null) arrivalTime = arrivalTimeTextField.getValue().toString(); else arrivalTime ="";
        String price = priceTextField.getText();

        String methodeResultMessage = "";

        methodeResultMessage = insertFlight(departurePlace, departureDate, departureTime, destination, arrivalDate, arrivalTime, price);

        if(methodeResultMessage.contains("ERROR")) addFlightInfoLabel.setTextFill(Color.web("#bf1c37"));
        else{
            addFlightInfoLabel.setTextFill(Color.web("#3072ff"));
            departurePlaceTextField.setText("");
            departureDateTextField.setValue(null);
            departureTimeTextField.setValue(null);
            arrivalDateTextField.setValue(null);
            arrivalTimeTextField.setValue(null);
            destinationTextField.setText("");
            priceTextField.setText("");
        }
        addFlightInfoLabel.setText(methodeResultMessage);

        parentM3.refreshFlightScheduleOnAction();
    }

    /**
     * Saves instance of parent controller object to private variable (parent means scene controller from where AddFlightController was called)
     * @param parentModule third module parent controller - Air Traffic instance
     */
    public void receiveParentModuleObject(M3_AirTrafficController parentModule){
        parentM3 = parentModule;
    }

}