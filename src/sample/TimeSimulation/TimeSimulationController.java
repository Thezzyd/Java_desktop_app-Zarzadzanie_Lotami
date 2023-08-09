package sample.TimeSimulation;

import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import sample.DatabaseFiles.DatabaseConnection;
import sample.ParentScene.ModuleParent;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * Controller class for Time simulation window
 * @author Daniel Czyz
 * @version 1.0.1 29/05/2021
 */
public class TimeSimulationController extends ModuleParent {

    @FXML
    private Label infoLabel_CurrentTime;
    @FXML
    private DatePicker currentDate_timeSimulation;
    @FXML
    private JFXTimePicker currentTime_timeSimulation;

    @FXML
    private Label infoLabel_timeSimulation;

    /**
     * Content of this method is executed when instance of TimeSimulationController class is loaded
     * @param location location
     * @param resource resource
     */
    @Override
    public void initialize(URL location, ResourceBundle resource){
        rootPane.setOpacity(0);
        makeFadeInTransition(rootPane);

        initAndLoadData_CurrentTime();
    }

    /**
     * Loads currently set time from database and writes into date picker and time picker value fields in Time simulation window
     */
    public void initAndLoadData_CurrentTime(){
        refreshCurrentTime();
        infoLabel_CurrentTime.setText("Current time in system: "+currentLocalDate+" "+currentLocalTime);
        currentDate_timeSimulation.setValue(currentLocalDate);
        currentTime_timeSimulation.setValue(currentLocalTime);
    }

    /**
     * Updates time in database by deleting existing data row and then inserting new one with new data and time
     * @param date date to be set as current
     * @param time time to be set as current
     * @return operation result message as string
     */
    public String updateTime(String date, String time){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String currentTimeDelete = "DELETE FROM time_simulation";
        String currentTimeUpdate = "INSERT INTO time_simulation(date, time) VALUES (?,?)";

        try{
            Statement statementTimeDelete = connectDB.createStatement();
            statementTimeDelete.executeUpdate(currentTimeDelete);

            PreparedStatement preparedStatementCurrentTimeUpdate = connectDB.prepareStatement(currentTimeUpdate);
            preparedStatementCurrentTimeUpdate.setString(1, date);
            preparedStatementCurrentTimeUpdate.setString(2, time);
            preparedStatementCurrentTimeUpdate.executeUpdate();

            return "Successfully changed time in the system!";
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return "ERROR: something went wrong!";
        }
    }


    /**
     * Sets chosen date from date Picker and time from time picker as current. Method assigned to button "Assign" in Time simulation screen
     */
    public void updateTimeOnAction(){

        String date;
        String time ;
        String methodResultMessage = "";
        try{
            date = currentDate_timeSimulation.getValue().toString();
        }catch(Exception e){
            date = currentLocalDate.toString();
        }

        try{
            time = currentTime_timeSimulation.getValue().toString();
        }catch(Exception e){
            time = currentLocalTime.toString();
        }

        methodResultMessage = updateTime(date, time);
        System.out.println(methodResultMessage+" "+date+" "+time);
        if(methodResultMessage.contains("ERROR")) infoLabel_timeSimulation.setTextFill(Color.web("#bf1c37"));
        else infoLabel_timeSimulation.setTextFill(Color.web("#3072ff"));
        infoLabel_timeSimulation.setText(methodResultMessage);

        initAndLoadData_CurrentTime();
    }

}

