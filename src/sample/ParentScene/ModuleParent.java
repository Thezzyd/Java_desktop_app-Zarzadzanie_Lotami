package sample.ParentScene;


import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
//import org.apache.pdfbox.io.IOUtils;
import sample.DatabaseFiles.DatabaseConnection;
import sample.LoginSystem.UserSession;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;


/**
 * Controller class for Parent screen window
 * @author Daniel Czyz
 * @version 1.0.1 29/05/2021
 */
public class ModuleParent implements Initializable {

    @FXML
    public AnchorPane containerPane;
    @FXML
    private Label moduleNameLabel;

    @FXML
    private ImageView logoImageView;
    @FXML
    private Button logoutButton;
    @FXML
    private Button quitButton;
    @FXML
    private Button homeButton;
    @FXML
    private Button accountManagerButton;
    @FXML
    private Button technicalEfficiencyButton;
    @FXML
    private Button airTrafficButton;
    @FXML
    private Button customerServiceButton;
    @FXML
    private Button timeSimulationButton;

  //  @FXML
  //  public Label parentLogLabel;

    @FXML
    public AnchorPane rootPane;
    @FXML
    public AnchorPane parentRootPane;

    public double xOffset=0;
    public double yOffset=0;

    public LocalDate currentLocalDate;
    public LocalTime currentLocalTime;

    @FXML
    public Label dateOfDayLabelMonday, dateOfDayLabelTuesday, dateOfDayLabelWednesday, dateOfDayLabelThursday, dateOfDayLabelFriday, dateOfDayLabelSaturday, dateOfDayLabelSunday;

    @FXML
    public Button Btn_0_0, Btn_1_0, Btn_2_0, Btn_3_0, Btn_4_0, Btn_5_0, Btn_6_0, Btn_7_0, Btn_8_0, Btn_9_0, Btn_10_0, Btn_11_0, Btn_12_0, Btn_13_0, Btn_14_0, Btn_15_0, Btn_16_0, Btn_17_0, Btn_18_0, Btn_19_0, Btn_20_0, Btn_21_0, Btn_22_0, Btn_23_0;
    @FXML
    public Button Btn_0_1, Btn_1_1, Btn_2_1, Btn_3_1, Btn_4_1, Btn_5_1, Btn_6_1, Btn_7_1, Btn_8_1, Btn_9_1, Btn_10_1, Btn_11_1, Btn_12_1, Btn_13_1, Btn_14_1, Btn_15_1, Btn_16_1, Btn_17_1, Btn_18_1, Btn_19_1, Btn_20_1, Btn_21_1, Btn_22_1, Btn_23_1;
    @FXML
    public Button Btn_0_2, Btn_1_2, Btn_2_2, Btn_3_2, Btn_4_2, Btn_5_2, Btn_6_2, Btn_7_2, Btn_8_2, Btn_9_2, Btn_10_2, Btn_11_2, Btn_12_2, Btn_13_2, Btn_14_2, Btn_15_2, Btn_16_2, Btn_17_2, Btn_18_2, Btn_19_2, Btn_20_2, Btn_21_2, Btn_22_2, Btn_23_2;
    @FXML
    public Button Btn_0_3, Btn_1_3, Btn_2_3, Btn_3_3, Btn_4_3, Btn_5_3, Btn_6_3, Btn_7_3, Btn_8_3, Btn_9_3, Btn_10_3, Btn_11_3, Btn_12_3, Btn_13_3, Btn_14_3, Btn_15_3, Btn_16_3, Btn_17_3, Btn_18_3, Btn_19_3, Btn_20_3, Btn_21_3, Btn_22_3, Btn_23_3;
    @FXML
    public Button Btn_0_4, Btn_1_4, Btn_2_4, Btn_3_4, Btn_4_4, Btn_5_4, Btn_6_4, Btn_7_4, Btn_8_4, Btn_9_4, Btn_10_4, Btn_11_4, Btn_12_4, Btn_13_4, Btn_14_4, Btn_15_4, Btn_16_4, Btn_17_4, Btn_18_4, Btn_19_4, Btn_20_4, Btn_21_4, Btn_22_4, Btn_23_4;
    @FXML
    public Button Btn_0_5, Btn_1_5, Btn_2_5, Btn_3_5, Btn_4_5, Btn_5_5, Btn_6_5, Btn_7_5, Btn_8_5, Btn_9_5, Btn_10_5, Btn_11_5, Btn_12_5, Btn_13_5, Btn_14_5, Btn_15_5, Btn_16_5, Btn_17_5, Btn_18_5, Btn_19_5, Btn_20_5, Btn_21_5, Btn_22_5, Btn_23_5;
    @FXML
    public Button Btn_0_6, Btn_1_6, Btn_2_6, Btn_3_6, Btn_4_6, Btn_5_6, Btn_6_6, Btn_7_6, Btn_8_6, Btn_9_6, Btn_10_6, Btn_11_6, Btn_12_6, Btn_13_6, Btn_14_6, Btn_15_6, Btn_16_6, Btn_17_6, Btn_18_6, Btn_19_6, Btn_20_6, Btn_21_6, Btn_22_6, Btn_23_6;

    public Button[][] hourBtnObjArray = new Button[24][7];
    public int[][] hourBtnArray = new int[24][7];
    public int actuallyOperatingWeek;
    public int actuallyOperatingYear;

    @FXML
    public DatePicker scheduleDataPicker;

    /**
     * Content of this method is executed when instance of ModuleParent class is loaded
     * @param location location
     * @param resource resource
     */
    @Override
    public void initialize(URL location, ResourceBundle resource){
        Image logoImage = new Image(getClass().getResourceAsStream("/images/logo_blue.png"));
        logoImageView.setImage(logoImage);

        parentRootPane.setOpacity(0);
        makeFadeInTransition(parentRootPane);

        loadNextScene("/sample/Home/Home.fxml");
        moduleNameLabel.setText("Home");
    }

    /**
     * Fills array representing work schedule with proper values ( 1 - marked, 2 - confirmed, 0 - neutral) based on data pulled from database
     * @param queryResult work schedule data for certain user, week and year
     * @param isConfirmed is data from database is confirmed (from work schedule table) or not (from work schedule template table)
     */
    public void readWeekDayHoursToButtons(ResultSet queryResult, boolean isConfirmed){
        try {
            int btnValue = 0;

            if(!isConfirmed)
                btnValue = 1;
            else
                btnValue = 2;

            List<String> monday_hours = Arrays.asList(queryResult.getString("monday_hours").split(","));
            List<String> tuesday_hours = Arrays.asList(queryResult.getString("tuesday_hours").split(","));
            List<String> wednesday_hours = Arrays.asList(queryResult.getString("wednesday_hours").split(","));
            List<String> thursday_hours = Arrays.asList(queryResult.getString("thursday_hours").split(","));
            List<String> friday_hours = Arrays.asList(queryResult.getString("friday_hours").split(","));
            List<String> saturday_hours = Arrays.asList(queryResult.getString("saturday_hours").split(","));
            List<String> sunday_hours = Arrays.asList(queryResult.getString("sunday_hours").split(","));

            for(int i = 0; i <= 23; i++)
            {
                for(int j = 0; j <= 6; j++)
                {
                    switch(j) {
                        case 0:  if(monday_hours.contains(String.valueOf(i))) hourBtnArray[i][j] = btnValue; else hourBtnArray[i][j] = 0; break;
                        case 1:  if(tuesday_hours.contains(String.valueOf(i))) hourBtnArray[i][j] = btnValue; else hourBtnArray[i][j] = 0; break;
                        case 2:  if(wednesday_hours.contains(String.valueOf(i))) hourBtnArray[i][j] = btnValue; else hourBtnArray[i][j] = 0; break;
                        case 3:  if(thursday_hours.contains(String.valueOf(i))) hourBtnArray[i][j] = btnValue; else hourBtnArray[i][j] = 0; break;
                        case 4:  if(friday_hours.contains(String.valueOf(i))) hourBtnArray[i][j] = btnValue; else hourBtnArray[i][j] = 0; break;
                        case 5:  if(saturday_hours.contains(String.valueOf(i))) hourBtnArray[i][j] = btnValue; else hourBtnArray[i][j] = 0; break;
                        case 6:  if(sunday_hours.contains(String.valueOf(i))) hourBtnArray[i][j] = btnValue; else hourBtnArray[i][j] = 0; break;
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Allows currently logged user to logout (clear User Session data) and open login screen window.
     */
    public void logoutButtonOnAction() {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
//
        try {
            UserSession userSession = UserSession.getInstance(0,"","","","", new int[]{});
            userSession.cleanUserSession();

            Parent root = FXMLLoader.load(getClass().getResource("/sample/LoginSystem/Login.fxml"));
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.setScene(new Scene(root, 600, 400));
            loginStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    /**
     * Selects date and time from database as current realtime
     * @return result data of current date and time
     */
    public ResultSet selectDateTime(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String currentTimeSelect = "SELECT * FROM time_simulation LIMIT 1";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(currentTimeSelect);
            return queryResult;

        }catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
            return null;
        }
    }

    /**
     * Assign time and date pulled from database to public variables representing current date and time
     */
    public void refreshCurrentTime(){
        ResultSet dateTime = selectDateTime();
        try{
            while(dateTime.next())
                {
                    DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-d");
                    currentLocalDate = LocalDate.parse(dateTime.getString("date"), formatterDate);
                    currentLocalTime = LocalTime.parse(dateTime.getString("time"));
                }

        }catch(Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    /**
     * Loads Home scene screen into currently opened Parent screen window. Method assigned to button "Home" in Parent screen window.
     */
    public void loadSceneHomeOnAction(){
        makeFadeOutAndLoadNextScene("/sample/Home/Home.fxml", rootPane);
        moduleNameLabel.setText("Home");
    }

    /**
     * Loads Time simulation scene screen into currently opened Parent screen window. Method assigned to button "Time simulation" in Parent screen window.
     */
    public void loadSceneTimeSimulationOnAction(){
        makeFadeOutAndLoadNextScene("/sample/TimeSimulation/TimeSimulation.fxml", rootPane);
        moduleNameLabel.setText("Time Simulation");
    }

    /**
     * Loads first module scene screen - Account Manager, into currently opened Parent screen window. Method assigned to button "Account Manager" in Parent screen window.
     */
    public void loadSceneM1_AccountManagerOnAction(){
        int[] userPrivileges = UserSession.getInstance(0,"","","","", new int[]{}).getPrivileges();

        if(userPrivileges[0] == 1) {
            makeFadeOutAndLoadNextScene("/sample/M1_AccountManager/M1_AccountManager.fxml", rootPane);
            moduleNameLabel.setText("Account Manager Module");
        }else{
            makeFadeOutAndLoadNextScene("/sample/NoPermissionScene/NoPermission.fxml", rootPane);
            moduleNameLabel.setText("Account Manager Module");
        }
    }

    /**
     * Loads second module scene screen - Technical Efficiency, into currently opened Parent screen window. Method assigned to button "Technical Efficiency" in Parent screen window.
     */
    public void loadSceneM2_TechnicalEfficiencyOnAction(){

        int[] userPrivileges = UserSession.getInstance(0,"","","","", new int[]{}).getPrivileges();
        moduleNameLabel.setText("Technical Efficiency Module");

        if(userPrivileges[1] == 1) {
            makeFadeOutAndLoadNextScene("/sample/M2_TechnicalEfficiency/M2_TechnicalEfficiency.fxml", rootPane);
        }else{
            makeFadeOutAndLoadNextScene("/sample/NoPermissionScene/NoPermission.fxml", rootPane);
        }
    }

    /**
     * Loads third module scene screen - Air Traffic, into currently opened Parent screen window. Method assigned to button "Air Traffic" in Parent screen window.
     */
    public void loadSceneM3_AirTrafficOnAction(){

        int[] userPrivileges = UserSession.getInstance(0,"","","","", new int[]{}).getPrivileges();
        moduleNameLabel.setText("Air Traffic Module");

        if(userPrivileges[2] == 1) {
            makeFadeOutAndLoadNextScene("/sample/M3_AirTraffic/M3_AirTraffic.fxml", rootPane);
        }else{
            makeFadeOutAndLoadNextScene("/sample/NoPermissionScene/NoPermission.fxml", rootPane);
        }
    }

    /**
     * Loads fourth module scene screen - Customer Service, into currently opened Parent screen window. Method assigned to button "Customer Service" in Parent screen window.
     */
    public void loadSceneM4_CustomerServiceOnAction(){

        int[] userPrivileges = UserSession.getInstance(0,"","","","", new int[]{}).getPrivileges();
        moduleNameLabel.setText("Customer Service Module");

        if(userPrivileges[3] == 1) {
            makeFadeOutAndLoadNextScene("/sample/M4_CustomerService/M4_CustomerService.fxml", rootPane);
        }else{
            makeFadeOutAndLoadNextScene("/sample/NoPermissionScene/NoPermission.fxml", rootPane);
        }
    }

    /**
     * Closes application. Method  assigned to button "Quit" in Parent screen window.
     */
    public void quitButtonOnAction() {
        Stage stage = (Stage) quitButton.getScene().getWindow();
        stage.close();
        Platform.exit();
    }

    /**
     * Loads scene into Parent scene screen window
     * @param path path to fxml file
     */
    public void loadNextScene(String path){

        try{
            Pane view = new FXMLLoader().load(getClass().getResource(path));
            containerPane.getChildren().add(view);
            containerPane.setTopAnchor( view, 0.00 );
            containerPane.setRightAnchor( view, 0.00 );
            containerPane.setBottomAnchor( view, 0.00 );
            containerPane.setLeftAnchor( view, 0.00 );
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    /**
     * Gets current year and week basing on current date in system and saves data to public variables "actuallyOperatingYear" and "actuallyOperatingWeek"
     */
    public void getPresentDate(){
        java.util.Date date = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        int year = cal.get(Calendar.YEAR);

        actuallyOperatingWeek = weekOfYear;
        actuallyOperatingYear = year;

        setScheduleDataLabels(year,weekOfYear);
    }

    /**
     * Sets labels text above work schedule which days of year are now displayed
     * @param year year
     * @param weekOfYear week of year
     */
    public void setScheduleDataLabels(int year, int weekOfYear){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        dateOfDayLabelMonday.setText(sdf.format(cal.getTime()));
        cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        dateOfDayLabelTuesday.setText(sdf.format(cal.getTime()));
        cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        dateOfDayLabelWednesday.setText(sdf.format(cal.getTime()));
        cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        dateOfDayLabelThursday.setText(sdf.format(cal.getTime()));
        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        dateOfDayLabelFriday.setText(sdf.format(cal.getTime()));
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        dateOfDayLabelSaturday.setText(sdf.format(cal.getTime()));
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        dateOfDayLabelSunday.setText(sdf.format(cal.getTime()));
    }

    /**
     * Putting buttons of work schedule to an array in order, so they could properly represent specific hour.
     */
    public void fillHourBtnObjArray(){
        hourBtnObjArray[0][0] =Btn_0_0;hourBtnObjArray[1][0] =Btn_1_0;hourBtnObjArray[2][0] =Btn_2_0;hourBtnObjArray[3][0] =Btn_3_0;hourBtnObjArray[4][0] =Btn_4_0;hourBtnObjArray[5][0] =Btn_5_0;hourBtnObjArray[6][0] =Btn_6_0;hourBtnObjArray[7][0]=Btn_7_0;hourBtnObjArray[8][0]=Btn_8_0;hourBtnObjArray[9][0]=Btn_9_0;
        hourBtnObjArray[10][0]=Btn_10_0;hourBtnObjArray[11][0]=Btn_11_0;hourBtnObjArray[12][0]=Btn_12_0;hourBtnObjArray[13][0]=Btn_13_0;hourBtnObjArray[14][0]=Btn_14_0;hourBtnObjArray[15][0]=Btn_15_0;hourBtnObjArray[16][0]=Btn_16_0;hourBtnObjArray[17][0]=Btn_17_0;hourBtnObjArray[18][0]=Btn_18_0;hourBtnObjArray[19][0]=Btn_19_0;
        hourBtnObjArray[20][0]=Btn_20_0;hourBtnObjArray[21][0]=Btn_21_0;hourBtnObjArray[22][0]=Btn_22_0;hourBtnObjArray[23][0]=Btn_23_0;

        hourBtnObjArray[0][1] =Btn_0_1;hourBtnObjArray[1][1] =Btn_1_1;hourBtnObjArray[2][1] =Btn_2_1;hourBtnObjArray[3][1] =Btn_3_1;hourBtnObjArray[4][1] =Btn_4_1;hourBtnObjArray[5][1] =Btn_5_1;hourBtnObjArray[6][1] =Btn_6_1;hourBtnObjArray[7][1]=Btn_7_1;hourBtnObjArray[8][1]=Btn_8_1;hourBtnObjArray[9][1]=Btn_9_1;
        hourBtnObjArray[10][1]=Btn_10_1;hourBtnObjArray[11][1]=Btn_11_1;hourBtnObjArray[12][1]=Btn_12_1;hourBtnObjArray[13][1]=Btn_13_1;hourBtnObjArray[14][1]=Btn_14_1;hourBtnObjArray[15][1]=Btn_15_1;hourBtnObjArray[16][1]=Btn_16_1;hourBtnObjArray[17][1]=Btn_17_1;hourBtnObjArray[18][1]=Btn_18_1;hourBtnObjArray[19][1]=Btn_19_1;
        hourBtnObjArray[20][1]=Btn_20_1;hourBtnObjArray[21][1]=Btn_21_1;hourBtnObjArray[22][1]=Btn_22_1;hourBtnObjArray[23][1]=Btn_23_1;

        hourBtnObjArray[0][2]=Btn_0_2;hourBtnObjArray[1][2]=Btn_1_2;hourBtnObjArray[2][2]=Btn_2_2;hourBtnObjArray[3][2]=Btn_3_2;hourBtnObjArray[4][2]=Btn_4_2;hourBtnObjArray[5][2]=Btn_5_2;hourBtnObjArray[6][2]=Btn_6_2;hourBtnObjArray[7][2]=Btn_7_2;hourBtnObjArray[8][2]=Btn_8_2;hourBtnObjArray[9][2]=Btn_9_2;
        hourBtnObjArray[10][2]=Btn_10_2;hourBtnObjArray[11][2]=Btn_11_2;hourBtnObjArray[12][2]=Btn_12_2;hourBtnObjArray[13][2]=Btn_13_2;hourBtnObjArray[14][2]=Btn_14_2;hourBtnObjArray[15][2]=Btn_15_2;hourBtnObjArray[16][2]=Btn_16_2;hourBtnObjArray[17][2]=Btn_17_2;hourBtnObjArray[18][2]=Btn_18_2;hourBtnObjArray[19][2]=Btn_19_2;
        hourBtnObjArray[20][2]=Btn_20_2;hourBtnObjArray[21][2]=Btn_21_2;hourBtnObjArray[22][2]=Btn_22_2;hourBtnObjArray[23][2]=Btn_23_2;

        hourBtnObjArray[0][3]=Btn_0_3;hourBtnObjArray[1][3]=Btn_1_3;hourBtnObjArray[2][3]=Btn_2_3;hourBtnObjArray[3][3]=Btn_3_3;hourBtnObjArray[4][3]=Btn_4_3;hourBtnObjArray[5][3]=Btn_5_3;hourBtnObjArray[6][3]=Btn_6_3;hourBtnObjArray[7][3]=Btn_7_3;hourBtnObjArray[8][3]=Btn_8_3;hourBtnObjArray[9][3]=Btn_9_3;
        hourBtnObjArray[10][3]=Btn_10_3;hourBtnObjArray[11][3]=Btn_11_3;hourBtnObjArray[12][3]=Btn_12_3;hourBtnObjArray[13][3]=Btn_13_3;hourBtnObjArray[14][3]=Btn_14_3;hourBtnObjArray[15][3]=Btn_15_3;hourBtnObjArray[16][3]=Btn_16_3;hourBtnObjArray[17][3]=Btn_17_3;hourBtnObjArray[18][3]=Btn_18_3;hourBtnObjArray[19][3]=Btn_19_3;
        hourBtnObjArray[20][3]=Btn_20_3;hourBtnObjArray[21][3]=Btn_21_3;hourBtnObjArray[22][3]=Btn_22_3;hourBtnObjArray[23][3]=Btn_23_3;

        hourBtnObjArray[0][4]=Btn_0_4;hourBtnObjArray[1][4]=Btn_1_4;hourBtnObjArray[2][4]=Btn_2_4;hourBtnObjArray[3][4]=Btn_3_4;hourBtnObjArray[4][4]=Btn_4_4;hourBtnObjArray[5][4]=Btn_5_4;hourBtnObjArray[6][4]=Btn_6_4;hourBtnObjArray[7][4]=Btn_7_4;hourBtnObjArray[8][4]=Btn_8_4;hourBtnObjArray[9][4]=Btn_9_4;
        hourBtnObjArray[10][4]=Btn_10_4;hourBtnObjArray[11][4]=Btn_11_4;hourBtnObjArray[12][4]=Btn_12_4;hourBtnObjArray[13][4]=Btn_13_4;hourBtnObjArray[14][4]=Btn_14_4;hourBtnObjArray[15][4]=Btn_15_4;hourBtnObjArray[16][4]=Btn_16_4;hourBtnObjArray[17][4]=Btn_17_4;hourBtnObjArray[18][4]=Btn_18_4;hourBtnObjArray[19][4]=Btn_19_4;
        hourBtnObjArray[20][4]=Btn_20_4;hourBtnObjArray[21][4]=Btn_21_4;hourBtnObjArray[22][4]=Btn_22_4;hourBtnObjArray[23][4]=Btn_23_4;

        hourBtnObjArray[0][5]=Btn_0_5;hourBtnObjArray[1][5]=Btn_1_5;hourBtnObjArray[2][5]=Btn_2_5;hourBtnObjArray[3][5]=Btn_3_5;hourBtnObjArray[4][5]=Btn_4_5;hourBtnObjArray[5][5]=Btn_5_5;hourBtnObjArray[6][5]=Btn_6_5;hourBtnObjArray[7][5]=Btn_7_5;hourBtnObjArray[8][5]=Btn_8_5;hourBtnObjArray[9][5]=Btn_9_5;
        hourBtnObjArray[10][5]=Btn_10_5;hourBtnObjArray[11][5]=Btn_11_5;hourBtnObjArray[12][5]=Btn_12_5;hourBtnObjArray[13][5]=Btn_13_5;hourBtnObjArray[14][5]=Btn_14_5;hourBtnObjArray[15][5]=Btn_15_5;hourBtnObjArray[16][5]=Btn_16_5;hourBtnObjArray[17][5]=Btn_17_5;hourBtnObjArray[18][5]=Btn_18_5;hourBtnObjArray[19][5]=Btn_19_5;
        hourBtnObjArray[20][5]=Btn_20_5;hourBtnObjArray[21][5]=Btn_21_5;hourBtnObjArray[22][5]=Btn_22_5;hourBtnObjArray[23][5]=Btn_23_5;

        hourBtnObjArray[0][6]=Btn_0_6;hourBtnObjArray[1][6]=Btn_1_6;hourBtnObjArray[2][6]=Btn_2_6;hourBtnObjArray[3][6]=Btn_3_6;hourBtnObjArray[4][6]=Btn_4_6;hourBtnObjArray[5][6]=Btn_5_6;hourBtnObjArray[6][6]=Btn_6_6;hourBtnObjArray[7][6]=Btn_7_6;hourBtnObjArray[8][6]=Btn_8_6;hourBtnObjArray[9][6]=Btn_9_6;
        hourBtnObjArray[10][6]=Btn_10_6;hourBtnObjArray[11][6]=Btn_11_6;hourBtnObjArray[12][6]=Btn_12_6;hourBtnObjArray[13][6]=Btn_13_6;hourBtnObjArray[14][6]=Btn_14_6;hourBtnObjArray[15][6]=Btn_15_6;hourBtnObjArray[16][6]=Btn_16_6;hourBtnObjArray[17][6]=Btn_17_6;hourBtnObjArray[18][6]=Btn_18_6;hourBtnObjArray[19][6]=Btn_19_6;
        hourBtnObjArray[20][6]=Btn_20_6;hourBtnObjArray[21][6]=Btn_21_6;hourBtnObjArray[22][6]=Btn_22_6;hourBtnObjArray[23][6]=Btn_23_6;
    }

    /**
     * Clears array of values for all buttons, which makes every button neutral (white color, not marked or confirmed)
     */
    public void resetHourButtonsOnAction(){
        for(int i = 0; i <= 23; i++)
        {
            for(int j = 0; j <= 6; j++)
            {
                hourBtnArray[i][j] = 0;
                hourBtnObjArray[i][j].setStyle("-fx-background-color: white");
            }
        }
    }

    /**
     * Refresh style for all buttons basing on their value (0 - neutral - white bg, 1 - marked - light green bg, 2 - confirmed - dark sea bg)
     */
    public void refreshHourButtonsOnAction(){
        for(int i = 0; i <= 23; i++)
        {
            for(int j = 0; j <= 6; j++)
            {
                if(hourBtnArray[i][j] == 0){
                    hourBtnObjArray[i][j].setStyle("-fx-background-color: white");
                }else if(hourBtnArray[i][j] == 1){
                    hourBtnObjArray[i][j].setStyle("-fx-background-color:  #bdffdf");
                }else{
                    hourBtnObjArray[i][j].setStyle("-fx-background-color: #00ffe5");
                }
            }
        }
    }

    /**
     * Fade in transition. This method is called when new scene is loaded into Parent screen window
     * @param pane pane which should have fading in effect
     */
    public void makeFadeInTransition(AnchorPane pane){
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(300));
        fadeTransition.setNode(pane);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    /**
     * Fade out transition. This method is called when new scene is loaded into Parent screen window and current scene begin to fade out
     * @param pane pane which should have fading out effect
     */
    public void makeFadeOutAndLoadNextScene(String pathToScene, AnchorPane pane){
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(300));
        fadeTransition.setNode(pane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);

        fadeTransition.setOnFinished((ActionEvent event) -> {
            loadNextScene(pathToScene);
        });
        fadeTransition.play();
    }

    /**
     * Opens new window, which can be moved while is grabbed.
     * @param path path to fxml file
     * @return reference object which allows us to intercept instance of opened window controller and call its methods
     */
    public FXMLLoader openMovablePopupWindow(String path){
        FXMLLoader root = null;
        try {
            root = new FXMLLoader(getClass().getResource(path));
            Stage popup = new Stage();
            popup.initStyle(StageStyle.UNDECORATED);
         //   File logoFile = new File("images/logo_blue.png");
            Image img = new Image(getClass().getResourceAsStream("/images/logo_blue.png"));
            popup.getIcons().add(img);

            Scene scene = new Scene(root.load());

            scene.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    xOffset = mouseEvent.getSceneX();
                    yOffset = mouseEvent.getSceneY();
                }
            });

            scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    popup.setX(mouseEvent.getScreenX() - xOffset);
                    popup.setY(mouseEvent.getScreenY() - yOffset);

                }
            });

            popup.setScene(scene);
            popup.show();

        } catch (IOException e) {
            e.printStackTrace();
            e.getCause();
        }
        return root;
    }

    /**
     * Clears all data from database , leaving only administrator position and user account
     */
    public void emptyDatabaseData(){

        try{

            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();
            String deleteDataFromTable = "";

            deleteDataFromTable = "DELETE FROM position WHERE id_position <> 1 ";
            PreparedStatement preparedStatementLoadDataFromFile = connectDB.prepareStatement(deleteDataFromTable);
            preparedStatementLoadDataFromFile.executeUpdate();

            deleteDataFromTable = "DELETE FROM privilege WHERE id_position <> 1 ";
            preparedStatementLoadDataFromFile = connectDB.prepareStatement(deleteDataFromTable);
            preparedStatementLoadDataFromFile.executeUpdate();

            deleteDataFromTable = "DELETE FROM user WHERE id_user <> 1";
            preparedStatementLoadDataFromFile = connectDB.prepareStatement(deleteDataFromTable);
            preparedStatementLoadDataFromFile.executeUpdate();

            deleteDataFromTable = "DELETE FROM aircraft ";
            preparedStatementLoadDataFromFile = connectDB.prepareStatement(deleteDataFromTable);
            preparedStatementLoadDataFromFile.executeUpdate();

            deleteDataFromTable = "DELETE FROM flight_schedule ";
            preparedStatementLoadDataFromFile = connectDB.prepareStatement(deleteDataFromTable);
            preparedStatementLoadDataFromFile.executeUpdate();

            deleteDataFromTable = "DELETE FROM ticket ";
            preparedStatementLoadDataFromFile = connectDB.prepareStatement(deleteDataFromTable);
            preparedStatementLoadDataFromFile.executeUpdate();

            deleteDataFromTable = "DELETE FROM work_schedule ";
            preparedStatementLoadDataFromFile = connectDB.prepareStatement(deleteDataFromTable);
            preparedStatementLoadDataFromFile.executeUpdate();

            deleteDataFromTable = "DELETE FROM work_schedule_template ";
            preparedStatementLoadDataFromFile = connectDB.prepareStatement(deleteDataFromTable);
            preparedStatementLoadDataFromFile.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    /**
     * Gets filepath to directory containing currently running jar file (working after application is build into separate jar file)
     * @return filepath to directory containing currently running jar file as string
     */
    public String getJarFilePath(){
        try {

            return new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getPath();

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return null;
    }

    /**
     * Imports database sql file populated with sample data in it.
     */
    public void databaseDumpPopulated(){

        String user = "root";

        String absolutePathSqlFile = getJarFilePath()+"\\zlPopulated.sql" ;

        if(absolutePathSqlFile.contains("file:\\")){
            absolutePathSqlFile = absolutePathSqlFile.replace("file:\\", "");
        }

        try {

            String[] executeCmd = new String[]{"C:\\xampp\\mysql\\bin\\mysql", "-u" + user, "-e", " source " + absolutePathSqlFile};

            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();

            if (processComplete == 0) {
                JOptionPane.showMessageDialog(null, "Database was loaded successfully");

            } else {
                JOptionPane.showMessageDialog(null, "Could not load database !!!");
            }

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    /**
     * Imports raw database sql file. Contains only administrator user account and position
     */
    public void databaseDumpRaw(){

        String user = "root";

        String absolutePathSqlFile = getJarFilePath()+"\\zlRaw.sql" ;

        if(absolutePathSqlFile.contains("file:\\")){
            absolutePathSqlFile = absolutePathSqlFile.replace("file:\\", "");
        }

        try {

            String[] executeCmd = new String[]{"C:\\xampp\\mysql\\bin\\mysql", "-u" + user, "-e", " source " + absolutePathSqlFile};

            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();

            if (processComplete == 0) {
                JOptionPane.showMessageDialog(null, "Database was loaded successfully");

            } else {
                JOptionPane.showMessageDialog(null, "Could not load database !!!");
            }

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }
/*
    public Boolean databaseDumpPopulated(String user, String mysqlExePath){

        String absolutePathSqlFile = getJarFilePath()+"\\zlPopulated.sql" ;

        if(absolutePathSqlFile.contains("file:\\")){
            absolutePathSqlFile = absolutePathSqlFile.replace("file:\\", "");
        }

        try {

            String[] executeCmd = new String[]{mysqlExePath, "-u" + user, "-e", " source " + absolutePathSqlFile};

            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();

            if (processComplete == 0) {
                System.out.println("Database created successfully");
                return true;

            } else {
                System.out.println("Could not create database !!!");
                return false;
            }

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

        return false;
    }*/



}
