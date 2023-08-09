package sample.M1_AccountManager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.DatabaseFiles.DatabaseConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import org.springframework.security.crypto.bcrypt.*;

/**
 * Controller class for first module popup screen window - Account Manager
 * @author Daniel Czyz
 * @version 1.0.1 29/05/2021
 */
public class AddUserPopupController extends M1_AccountManagerController implements Initializable {

    @FXML
    private Label addUserInfoLabel;
    @FXML
    private Button closeAddUserButton;

    @FXML
    private PasswordField setPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField firstnameTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private ComboBox positionComboBox;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField phoneNumberTextField;


    private M1_AccountManagerController parentM1;

    /**
     * Content of this method is executed when instance of AddUserPopupController class is loaded
     * @param location location
     * @param resource resource
     */
    @Override
    public void initialize(URL location, ResourceBundle resource){
        //   parentM1.addUserPopup = true;
        positionComboBox.setItems(initPositionComboBox());
    }

    /**
     * Closes movable popup window which allows user to insert new user account to database. Method assigned to a button "Add"
     */
    public void closeAddUserPopup(){
        parentM1.addUserPopup = false;
        Stage addPositionStage = (Stage) closeAddUserButton.getScene().getWindow();
        addPositionStage.close();
    }

    /**
     * Checks if given passwords match and if passwords are not empty
     */
    public void addUserOnAction() {
        if(setPasswordField.getText().equals(confirmPasswordField.getText())){
            if(positionComboBox.getValue() == null){
                addUserInfoLabel.setTextFill(Color.web("#bf1c37"));
                addUserInfoLabel.setText("ERROR: Please fill all fields!");
            }else
                registerUser();

        }else{
            addUserInfoLabel.setTextFill(Color.web("#bf1c37"));
            addUserInfoLabel.setText("ERROR: Passwords does not match!");
        }
    }

    /**
     * Inserts new user account record to database
     * @param firstname user firstname
     * @param lastname user lastname
     * @param position position assigned to a user
     * @param username user username
     * @param password user password
     * @param email user email
     * @param phoneNumber user phone number
     * @return method result message as string
     */
    public String insertUser(String firstname, String lastname, String position, String username, String password, String email, String phoneNumber){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        boolean isFirstnameValid = firstname.matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$");
        boolean isLastnameValid = lastname.matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$");
        boolean isUsernameValid = username.matches("^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");
        boolean isPasswordValid = password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#@$!%*?&-])[A-Za-z\\d@#$!%*?&-]{8,}$");
        boolean isEmailValid = email.matches("^(.+)@(.+)$");
        boolean isPhoneNumberValid = phoneNumber.matches("^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{3}$");

        String insertUser = "INSERT INTO user(firstname, lastname, id_position, username, password, email, phone_number) VALUES (?,?,?,?,?,?,?)";

        try{
            if(firstname.equals("") || lastname.equals("") || position.equals("") || username.equals("") || password.equals("") || email.equals("") || phoneNumber.equals("")) {
                return "ERROR: Please fill all fields!";
            }else if(!isFirstnameValid ){
                return "ERROR: Wrong data format given in firstname field!";
            }else if(!isLastnameValid ){
                return "ERROR: Wrong data format given in lastname field!";
            }else if(!isUsernameValid ){
                return "ERROR: Wrong data format given in username field! (8-20 chars, not allowed at beginning and end '_' and '.')";
            }else if(!isPasswordValid ){
                return "ERROR: Wrong data format given in password field! ( chars > 8, min. 1 uppercase, min. 1 number, min. 1 special sign)";
            }else if(!isEmailValid ){
                return "ERROR: Wrong data format given in email field!";
            }else if(!isPhoneNumberValid ){
                return "ERROR: Wrong data format given in phone number field!";
            }else {
                PreparedStatement preparedStatementInsertUser = connectDB.prepareStatement(insertUser);
                preparedStatementInsertUser.setString(1, firstname);
                preparedStatementInsertUser.setString(2, lastname);
                preparedStatementInsertUser.setString(3, position);
                preparedStatementInsertUser.setString(4, username);
                preparedStatementInsertUser.setString(5, BCrypt.hashpw(password, BCrypt.gensalt(10)));
                preparedStatementInsertUser.setString(6, email);
                preparedStatementInsertUser.setString(7, phoneNumber);
                preparedStatementInsertUser.executeUpdate();
                return "User registered successfully!";
            }
        }catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
            return "ERROR: Given username is already taken!";
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return "ERROR: Something went wrong!";
        }
    }

    /**
     * Selects position data
     * @param position unique position name in database
     * @return object containing data pulled from database
     */
    public ResultSet selectPosition(String position){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String id_positionSelect = "SELECT id_position FROM position Where position_name = ?";
        try {
            PreparedStatement preparedStatementSelectPositionId = connectDB.prepareStatement(id_positionSelect);
            preparedStatementSelectPositionId.setString(1, position);
            ResultSet queryResult = preparedStatementSelectPositionId.executeQuery();
            return queryResult;

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return null;
        }
    }

    /**
     * Gathers data from text fields, then calls insertUser method
     */
    public void registerUser(){
        String firstname = firstnameTextField.getText();
        String lastname = lastnameTextField.getText();
        String position = positionComboBox.getValue().toString();
        String username = usernameTextField.getText();
        String password = setPasswordField.getText();
        String email = emailTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();


        try{

            ResultSet positionSelect = selectPosition(position);

            while(positionSelect.next()){
                position = positionSelect.getString("id_position");
                break;
            }
            String methodResultMessage = insertUser(firstname, lastname, position, username, password, email, phoneNumber);

            if(methodResultMessage.contains("ERROR")) addUserInfoLabel.setTextFill(Color.web("#bf1c37"));
            else{
                addUserInfoLabel.setTextFill(Color.web("#3072ff"));
                resetInputFields();
            }
            addUserInfoLabel.setText(methodResultMessage);

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
        parentM1.refreshUserOnAction();
    }

    /**
     * Saves instance of parent controller object to private variable (parent means scene controller from where AddUserController was called)
     * @param parentModule first module parent controller - Account Manager instance
     */
    public void receiveParentModuleObject(M1_AccountManagerController parentModule){
        parentM1 = parentModule;
    }

    /**
     * Clears all input fields on window
     */
    public void resetInputFields(){
        firstnameTextField.setText("");
        lastnameTextField.setText("");
        positionComboBox.valueProperty().set(null);
        usernameTextField.setText("");
        setPasswordField.setText("");
        confirmPasswordField.setText("");
        phoneNumberTextField.setText("");
        emailTextField.setText("");
    }
}
