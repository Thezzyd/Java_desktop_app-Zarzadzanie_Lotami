package sample.M2_TechnicalEfficiency;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.DatabaseFiles.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Controller class for second module popup screen window - Technical Efficiency
 * @author Daniel Czyz
 * @version 1.0.1 29/05/2021
 */
public class AddAircraftPopupController {

    @FXML
    private Label addAircraftInfoLabel;
    @FXML
    private Button closeAddAircraftButton;

    @FXML
    private TextField brandNameTextField;
    @FXML
    private TextField modelTextField;
    @FXML
    private TextField engineNumberTextField;
    @FXML
    private TextField seatsNumberTextField;

    private M2_TechnicalEfficiencyController parentM2;

    /**
     * Closes movable popup window which allows user to insert new aircraft to database. Method assigned to a button "Add"
     */
    public void closeAddAircraftPopup(){
        Stage addPositionStage = (Stage) closeAddAircraftButton.getScene().getWindow();
        addPositionStage.close();
        parentM2.addAircraftPopup = false;
    }

    /**
     * Inserts new aircraft record to database
     * @param brandName aircraft brand name
     * @param model aircraft model
     * @param engineNumber aircraft engine number
     * @param seatsNumber number of seats in aircraft
     * @return method result message as string
     */
    public String insertAircraft(String brandName, String model, String engineNumber, String seatsNumber){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String aircraftInsert = "INSERT INTO aircraft(brand_name, model, engine_number, seats_number) VALUES (?,?,?,?)";

        boolean isSeatsNumberValid = seatsNumber.matches("[1-9][0-9]*|0 or [1-9]\\d*|0");

        try{
            if(brandName.equals("") || model.equals("") || engineNumber.equals("") || seatsNumber.equals("")) {
                return "ERROR: Please fill all fields!";
            }else if(!isSeatsNumberValid ){
                return "ERROR: Wrong data format given in seats number field!";
            }else{
                PreparedStatement preparedStatementAircraftInsert = connectDB.prepareStatement(aircraftInsert);
                preparedStatementAircraftInsert.setString(1,brandName);
                preparedStatementAircraftInsert.setString(2,model);
                preparedStatementAircraftInsert.setString(3,engineNumber);
                preparedStatementAircraftInsert.setString(4,seatsNumber);
                preparedStatementAircraftInsert.executeUpdate();

                return "Aircraft added successfully!";
            }
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return "ERROR: Something went wrong!";
        }
    }

    /**
     * Gathers data from text fields, then calls insertAircraft method
     */
    public void addAircraftOnAction() {
        String brandName = brandNameTextField.getText();
        String model = modelTextField.getText();
        String engineNumber = engineNumberTextField.getText();
        String seatsNumber = seatsNumberTextField.getText();

        String methodResultMessage = insertAircraft(brandName, model, engineNumber, seatsNumber);

        if(methodResultMessage.contains("ERROR")) addAircraftInfoLabel.setTextFill(Color.web("#bf1c37"));
        else {
            addAircraftInfoLabel.setTextFill(Color.web("#3072ff"));
            brandNameTextField.setText("");
            modelTextField.setText("");
            engineNumberTextField.setText("");
            seatsNumberTextField.setText("");
        }
        addAircraftInfoLabel.setText(methodResultMessage);

        parentM2.refreshAircraftListOnAction();

    }

    /**
     * Saves instance of parent controller object to private variable (parent means scene controller from where AddAircraftController was called)
     * @param parentModule second module parent controller - Technical Efficiency instance
     */
    public void receiveParentModuleObject(M2_TechnicalEfficiencyController parentModule){
        parentM2 = parentModule;
    }

}