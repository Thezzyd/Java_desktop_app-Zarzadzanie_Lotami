package sample.M1_AccountManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.DatabaseFiles.DatabaseConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

/**
 * Controller class for first module popup screen window - Account Manager
 * @author Daniel Czyz
 * @version 1.0.1 29/05/2021
 */
public class AddPositionPopupController extends M1_AccountManagerController {

    @FXML
    private Label addPositionInfoLabel;
    @FXML
    private Button closeAddPositionButton;
    @FXML
    private TextField positionNameTextField;
    @FXML
    private CheckBox M1_CheckBox, M2_CheckBox, M3_CheckBox, M4_CheckBox;

    private M1_AccountManagerController parentM1;

    /**
     * Content of this method is executed when instance of AddPositionPopupController class is loaded
     * @param location location
     * @param resource resource
     */
    @Override
    public void initialize(URL location, ResourceBundle resource){

    }

    /**
     * Closes movable popup window which allows user to insert new position to database. Method assigned to a button "Add"
     */
    public void closeAddPositionPopup(){
        parentM1.addPositionPopup = false;
        Stage addPositionStage = (Stage) closeAddPositionButton.getScene().getWindow();
        addPositionStage.close();
    }

    /**
     * Inserts privileges for existing position to database
     * @param positionId position id
     * @param m1Privilege first module privilege (0 or 1)
     * @param m2Privilege second module privilege (0 or 1)
     * @param m3Privilege third module privilege (0 or 1)
     * @param m4Privilege fourth module privilege (0 or 1)
     * @return method result message as string
     */
    public String insertPrivileges(int positionId, int m1Privilege, int m2Privilege, int m3Privilege, int m4Privilege){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        try {
            if(m1Privilege <0 || m1Privilege >1) {
                return "ERROR: Wrong privilege value given!";
            }else if(m2Privilege <0 || m2Privilege >1){
                return "ERROR: Wrong privilege value given!";
            }else if(m3Privilege <0 || m3Privilege >1){
                return "ERROR: Wrong privilege value given!";
            }else if(m4Privilege <0 || m4Privilege >1){
                return "ERROR: Wrong privilege value given!";
            }else{

                String insertPrivilegeM1 = "INSERT INTO privilege (id_position, module, access) VALUES (?, 'Account manager', ?)";
                String insertPrivilegeM2 = "INSERT INTO privilege (id_position, module, access) VALUES (?, 'Technical efficiency', ?)";
                String insertPrivilegeM3 = "INSERT INTO privilege (id_position, module, access) VALUES (?, 'Air traffic', ?)";
                String insertPrivilegeM4 = "INSERT INTO privilege (id_position, module, access) VALUES (?, 'Customer service', ?)";

                PreparedStatement preparedStatementInsertPrivilege = connectDB.prepareStatement(insertPrivilegeM1);
                preparedStatementInsertPrivilege.setInt(1, positionId);
                preparedStatementInsertPrivilege.setInt(2, m1Privilege);
                preparedStatementInsertPrivilege.executeUpdate();

                preparedStatementInsertPrivilege = connectDB.prepareStatement(insertPrivilegeM2);
                preparedStatementInsertPrivilege.setInt(1, positionId);
                preparedStatementInsertPrivilege.setInt(2, m2Privilege);
                preparedStatementInsertPrivilege.executeUpdate();

                preparedStatementInsertPrivilege = connectDB.prepareStatement(insertPrivilegeM3);
                preparedStatementInsertPrivilege.setInt(1, positionId);
                preparedStatementInsertPrivilege.setInt(2, m3Privilege);
                preparedStatementInsertPrivilege.executeUpdate();

                preparedStatementInsertPrivilege = connectDB.prepareStatement(insertPrivilegeM4);
                preparedStatementInsertPrivilege.setInt(1, positionId);
                preparedStatementInsertPrivilege.setInt(2, m4Privilege);
                preparedStatementInsertPrivilege.executeUpdate();
                return "Success!";
            }

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return "ERROR: Something went wrong!";
        }
    }

    /**
     * Inserts new position record to database
     * @param positionName position name
     * @return method result message as string
     */
    public String insertPosition(String positionName) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        boolean isPositionNameValid = positionName.matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$");

        try {

            if(positionName.equals("")){
                return "ERROR: Position name can not be empty!";
            }else if(!isPositionNameValid){
                return "ERROR: Wrong data format given in position name field!";
            }else {
                String insertFields = "INSERT INTO zl.position(position_name) VALUES (?)";
                PreparedStatement preparedStatementInsertPosition = connectDB.prepareStatement(insertFields);
                preparedStatementInsertPosition.setString(1, positionName);
                preparedStatementInsertPosition.executeUpdate();
                return "Position added successfully!";
            }
        }catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
            return "ERROR: Given position name is already taken";
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return "ERROR: Something went wrong";
        }
    }

    /**
     * Gets position id from database basing on given unique position name
     * @param positionName position name
     * @return position id
     */
    public int getPositionId(String positionName){
        int positionId = 0;
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        try {
            String selectPosition = "SELECT * FROM position WHERE position_name= ?";
            PreparedStatement preparedStatementSelectPosition = connectDB.prepareStatement(selectPosition);
            preparedStatementSelectPosition.setString(1, positionName);
            ResultSet queryResult = preparedStatementSelectPosition.executeQuery();

            while(queryResult.next()){
                positionId = queryResult.getInt("id_position");
                break;
            }

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return positionId;
    }

    /**
     * Gathers data from text fields and check boxes, then calls insertPosition method
     */
    public void addPositionOnAction(){
        int id_position = 0, m1Privilege = 0, m2Privilege = 0, m3Privilege = 0, m4Privilege = 0;

        String positionName = positionNameTextField.getText();
        if(M1_CheckBox.isSelected())
            m1Privilege = 1;
        if(M2_CheckBox.isSelected())
            m2Privilege = 1;
        if(M3_CheckBox.isSelected())
            m3Privilege = 1;
        if(M4_CheckBox.isSelected())
            m4Privilege = 1;

        try{
            String methodResultMessage = insertPosition(positionName);
            if(methodResultMessage.contains("ERROR")) {
                addPositionInfoLabel.setTextFill(Color.web("#bf1c37"));
                addPositionInfoLabel.setText(methodResultMessage);
                return;
            }else {
                addPositionInfoLabel.setTextFill(Color.web("#3072ff"));
                positionNameTextField.setText("");
                M1_CheckBox.setSelected(false);
                M2_CheckBox.setSelected(false);
                M3_CheckBox.setSelected(false);
                M4_CheckBox.setSelected(false);
            }
            addPositionInfoLabel.setText(methodResultMessage);

            id_position = getPositionId(positionName);

            methodResultMessage = insertPrivileges(id_position, m1Privilege, m2Privilege, m3Privilege, m4Privilege);
            if(methodResultMessage.contains("ERROR")){
                addPositionInfoLabel.setTextFill(Color.web("#bf1c37"));
                addPositionInfoLabel.setText(methodResultMessage);
            }

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
        parentM1.refreshPositionAndPrivilegesOnAction();
    }

    /**
     * Saves instance of parent controller object to private variable (parent means scene controller from where AddPositionController was called)
     * @param parentModule first module parent controller - Account Manager instance
     */
    public void receiveParentModuleObject(M1_AccountManagerController parentModule){
        parentM1 = parentModule;
    }

}
