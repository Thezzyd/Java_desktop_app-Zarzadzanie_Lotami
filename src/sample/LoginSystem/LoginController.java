package sample.LoginSystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.security.crypto.bcrypt.BCrypt;
import sample.DatabaseFiles.DatabaseConnection;


import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.sql.Connection;

/**
 * Controller class for Login screen window
 * @author Daniel Czyz
 * @version 1.0.1 29/05/2021
 */
public class LoginController implements Initializable {

    @FXML
    private Button quitButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private ImageView brandingImageView;
    @FXML
    private ImageView keyImageView;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField enterPasswordField;

    /**
     * Content of this method is executed when instance of LoginController class is loaded
     * @param location location
     * @param resource resource
     */
    @Override
    public void initialize(URL location, ResourceBundle resource){
        Image brandingImage = new Image(getClass().getResourceAsStream("/images/newBrandingIMG.png"));
        brandingImageView.setImage(brandingImage);

        Image keyImage = new Image(getClass().getResourceAsStream("/images/newPasswordIconIMG.png"));
        keyImageView.setImage(keyImage);
    }

    /**
     * Checks if username and password fields was filled, if they were validation method is executed. Method assigned to button "Login" in Login screen section
     */
    public void loginButtonOnAction(){
        if(!usernameTextField.getText().isBlank() && !enterPasswordField.getText().isBlank()){

        /*    int id = 1;
            String firstname = "admin";
            String lastname = "admin";
            String username = "admin";
            String position = "Administrator";
            int[] privileges = getPositionPrivileges(1);

            UserSession userSession = UserSession.getInstance(id, username, firstname, lastname, position, new int[]{1,1,1,1} );*/
            //  parentSceneOnValidation();

            validateLogin();
        }else{
            loginMessageLabel.setText("Please enter username and password...");
        }
    }

    /**
     * Closes application. Method assigned to button in Login screen section
     */
    public void quitButtonOnAction(){
        Stage stage = (Stage) quitButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Selects all privileges assigned to a given position in system
     * @param positionId position id in database
     * @return ResultSet object containing privileges data pulled from database
     */
    public ResultSet selectPrivileges(int positionId){
        DatabaseConnection connectionNow = new DatabaseConnection();
        Connection connectDB = connectionNow.getConnection();

        String getPrivileges = "SELECT * FROM privilege WHERE id_position = ?";
        try {
            PreparedStatement preparedStatementGetPrivileges = connectDB.prepareStatement(getPrivileges);
            preparedStatementGetPrivileges.setInt(1, positionId);
            ResultSet queryResultPrivileges = preparedStatementGetPrivileges.executeQuery();

            return queryResultPrivileges;

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return null;
        }
    }

    /**
     * Saves data returned from selectPrivileges method to integer array where first index means first module and so on...
     * @param positionId position id in database
     * @return array of integers holding data to which module given position have access
     */
    public int[] getPositionPrivileges(int positionId){
        int[] privileges = new int[4]; //4 bo mamy 4 moduły, więc maxymalnie mogą wystąpić 4 pozycje
        try {
            ResultSet privilegesSelected = selectPrivileges(positionId);

            while (privilegesSelected.next()) {
                if (privilegesSelected.getString("module").equals("Account manager"))
                    privileges[0] = privilegesSelected.getInt("access");
                if (privilegesSelected.getString("module").equals("Technical efficiency"))
                    privileges[1] = privilegesSelected.getInt("access");
                if (privilegesSelected.getString("module").equals("Air traffic"))
                    privileges[2] = privilegesSelected.getInt("access");
                if (privilegesSelected.getString("module").equals("Customer service"))
                    privileges[3] = privilegesSelected.getInt("access");
            }
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return privileges;
    }


    /**
     * Selects data assigned to a given user
     * @param username unique username of user in database
     * @return ResultSet object containing user data pulled from database
     */
    public ResultSet selectUser(String username){
        DatabaseConnection connectionNow = new DatabaseConnection();
        Connection connectDB = connectionNow.getConnection();

        try {
            String verifyLogin = "SELECT *  FROM user WHERE username = ?";
            PreparedStatement preparedStatementVerifyLogin = connectDB.prepareStatement(verifyLogin);
            preparedStatementVerifyLogin.setString(1, username);
            // preparedStatementVerifyLogin.setString(2, enterPasswordField.getText());
            ResultSet queryResult = preparedStatementVerifyLogin.executeQuery();
            return queryResult;

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return null;
        }
    }

    /**
     * Checks whether given username and password combination matches data in database, if it does parentSceneOnValidation method is executed
     */
    public void validateLogin(){
        try{
            ResultSet user =  selectUser(usernameTextField.getText());

            while(user.next()){

                if(BCrypt.checkpw(enterPasswordField.getText(),user.getString("password"))){
                    loginMessageLabel.setText("Witaj " +user.getString("firstname")+ " " +user.getString("lastname"));

                    int id = user.getInt("id_user");
                    String firstname = user.getString("firstname");
                    String lastname = user.getString("lastname");
                    String username = user.getString("username");
                    String position = user.getString("id_position");
                    int[] privileges = getPositionPrivileges(user.getInt("id_position"));

                    UserSession userSession = UserSession.getInstance(id, username, firstname, lastname, position, privileges);

                    parentSceneOnValidation();
                }else{
                    loginMessageLabel.setText("Invalid password!");
                }
                return;
            }
            loginMessageLabel.setText("Invalid username!");

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    /**
     * Loads new window called "ParentScene"
     */
    public void parentSceneOnValidation()
    {
        try{
            Stage loginStage = (Stage) quitButton.getScene().getWindow();
            loginStage.close();

            Parent root = FXMLLoader.load(getClass().getResource("/sample/ParentScene/ModuleParent.fxml"));
            Stage home_Stage = new Stage();
            home_Stage.initStyle(StageStyle.DECORATED);
            home_Stage.setScene(new Scene(root, 1150, 700));
            home_Stage.setMinHeight(700+40);
            home_Stage.setMinWidth(1150+20);

            Image img = new Image(getClass().getResourceAsStream("/images/logo_blue.png"));
            home_Stage.getIcons().add(img);
            home_Stage.setTitle("Flight management");

            home_Stage.show();

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

}
