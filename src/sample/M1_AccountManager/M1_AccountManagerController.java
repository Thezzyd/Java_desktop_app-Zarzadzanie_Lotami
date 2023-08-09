package sample.M1_AccountManager;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.converter.DefaultStringConverter;
import org.springframework.security.crypto.bcrypt.BCrypt;
import sample.DatabaseFiles.DatabaseConnection;
import sample.ParentScene.ModuleParent;
import sample.tables.PositionTable;
import sample.tables.PrivilegeTable;
import sample.tables.UserTable;
import java.sql.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;

/**
 * Controller class for first module screen window - Account Manager
 * @author Daniel Czyz
 * @version 1.0.1 30/05/2021
 */
public class M1_AccountManagerController extends ModuleParent {

    @FXML
    public TableView<UserTable> user_table;
    @FXML
    private TableColumn<UserTable, String> col_id_User;
    @FXML
    private TableColumn<UserTable, String> col_firstname_User;
    @FXML
    private TableColumn<UserTable, String> col_lastname_User;
    @FXML
    private TableColumn<UserTable, String> col_id_position_User;
    @FXML
    private TableColumn<UserTable, String> col_username_User;
    @FXML
    private TableColumn<UserTable, String> col_password_User;
    @FXML
    private TableColumn<UserTable, String> col_email_User;
    @FXML
    private TableColumn<UserTable, String> col_phone_number_User;

    @FXML
    private Label queryInfoLabel_User;
    @FXML
    private Label queryInfoLabel_Position;

    @FXML
    private TextField userSearchBox;

    @FXML
    public TableView<PositionTable> position_table;
    @FXML
    private TableColumn<PositionTable, String> col_id_position_Position;
    @FXML
    private TableColumn<PositionTable, String> col_position_name_Position;

    @FXML
    public TableView<PrivilegeTable> privilege_table;
    @FXML
    private TableColumn<PrivilegeTable, String> col_module_Privilege;
    @FXML
    private TableColumn<PrivilegeTable, String> col_access_Privilege;

    @FXML
    private Label workScheduleInfoLabel;
    @FXML
    private TextField saveTemplateTextField;
    @FXML
    private TextField positionSearchBox;

    @FXML
    private ComboBox scheduleWorkerComboBox;
    @FXML
    public ComboBox workScheduleTemplateComboBox;

    public static ObservableList<UserTable> user_table_data ;
    public static ObservableList<PositionTable> position_table_data ;
    public static ObservableList<PrivilegeTable> privilege_table_data ;

    public static ObservableList<String> positionIdComboBox_data ;
    public static ObservableList<String> positionNameComboBox_data ;

    public static ObservableList<String> workScheduleTemplateComboBox_data ;

    public static ObservableList<String> scheduleWorkerComboBox_data ;
    public static ObservableList<String> scheduleWorkerIdComboBox_data ;

    public boolean addUserPopup = false;
    public boolean addPositionPopup = false;

    /**
     * Content of this method is executed when instance of M1_AccountManagerController class is loaded
     * @param location location
     * @param resource resource
     */
    @Override
    public void initialize(URL location, ResourceBundle resource){
        rootPane.setOpacity(0);
        makeFadeInTransition(rootPane);

        initAndLoadData_TableUser();
        initAndLoadData_TablePosition();

        initScheduleWorkerComboBox();
        fillHourBtnObjArray();
        getPresentDate();
        initWorkScheduleTemplateComboBox();
    }

    /**
     * Selects all work schedule templates from database
     * @return object containing work schedule template data pulled from database
     */
    public ResultSet selectWorkScheduleTemplate(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String sqlSelect = "SELECT template_name FROM work_schedule_template";

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
     * Fills work schedule template combo box with data pulled from database
     */
    public void initWorkScheduleTemplateComboBox(){
        workScheduleTemplateComboBox_data = FXCollections.observableArrayList();

        try {
            ResultSet selectWT = selectWorkScheduleTemplate();

            while(selectWT.next())
            {
                workScheduleTemplateComboBox_data.add(selectWT.getString("template_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }

        workScheduleTemplateComboBox.setItems(workScheduleTemplateComboBox_data);
    }

    /**
     * Deletes given template of work schedule
     * @param template_name work schedule template name
     */
    public void deleteWorkScheduleTemplate(String template_name){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String workScheduleDelete = "DELETE FROM work_schedule_template WHERE template_name= ?";

        try {
            PreparedStatement preparedStatementWorkSchedule = connectDB.prepareStatement(workScheduleDelete);
            preparedStatementWorkSchedule.setString(1, template_name);
            preparedStatementWorkSchedule.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            workScheduleInfoLabel.setTextFill(Color.web("#bf1c37"));
            workScheduleInfoLabel.setText("ERROR: Something went wrong!");
        }
    }

    /**
     * Passes which work schedule template was selected from combo box ten calls deleteWorkScheduleTemplate method
     */
    public void deleteWorkScheduleTemplate(){
        deleteWorkScheduleTemplate(workScheduleTemplateComboBox.getValue().toString());
        if(workScheduleTemplateComboBox.getValue() != null) {
            workScheduleInfoLabel.setTextFill(Color.web("#3072ff"));
            workScheduleInfoLabel.setText("Work schedule template with name: '" + workScheduleTemplateComboBox.getValue() + "' successfully deleted!");
        }else{
            workScheduleInfoLabel.setTextFill(Color.web("#bf1c37"));
            workScheduleInfoLabel.setText("ERROR: No template was selected!");
        }
        initWorkScheduleTemplateComboBox();
    }

    /**
     * Selects specific work schedule template by passing unique template name
     * @param templateName work schedule template name
     * @return object containing work schedule template data pulled from database
     */
    public ResultSet selectWorkScheduleTemplate(String templateName ){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String workScheduleSelect = "SELECT * FROM work_schedule_template WHERE template_name= ?";
        try{
            PreparedStatement preparedStatementWorkScheduleSelect = connectDB.prepareStatement(workScheduleSelect);
            preparedStatementWorkScheduleSelect.setString(1, templateName);
            ResultSet queryResult = preparedStatementWorkScheduleSelect.executeQuery();
            return queryResult;

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return null;
        }
    }

    /**
     * Passes data pulled from database about specific work schedule template work hours to buttons to represent this data visually on screen
     */
    public void loadWorkScheduleFromTemplate(){
        try {
            ResultSet selectWST = selectWorkScheduleTemplate(workScheduleTemplateComboBox.getValue().toString());

            while(selectWST.next())
            {
                readWeekDayHoursToButtons(selectWST, false);
                refreshHourButtonsOnAction();
                break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    /**
     * Inserts new work schedule template record to database
     * @param template_name work schedule template name
     * @param mondayHours string containing monday work hours
     * @param tuesdayHours string containing tuesday work hours
     * @param wednesdayHours string containing wednesday work hours
     * @param thursdayHours string containing thursday work hours
     * @param fridayHours string containing friday work hours
     * @param saturdayHours string containing saturday work hours
     * @param sundayHours string containing sunday work hours
     */
    public void insertWorkScheduleTemplate(String template_name, String mondayHours, String tuesdayHours, String wednesdayHours, String thursdayHours,
                                           String fridayHours, String saturdayHours, String sundayHours){

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String workScheduleInsert = "INSERT INTO work_schedule_template (template_name, monday_hours, tuesday_hours, wednesday_hours, thursday_hours, friday_hours, saturday_hours, sunday_hours) " +
                "VALUES (?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement preparedStatementWorkScheduleInsert = connectDB.prepareStatement(workScheduleInsert);
            preparedStatementWorkScheduleInsert.setString(1,template_name);
            preparedStatementWorkScheduleInsert.setString(2,mondayHours);
            preparedStatementWorkScheduleInsert.setString(3,tuesdayHours);
            preparedStatementWorkScheduleInsert.setString(4,wednesdayHours);
            preparedStatementWorkScheduleInsert.setString(5,thursdayHours);
            preparedStatementWorkScheduleInsert.setString(6,fridayHours);
            preparedStatementWorkScheduleInsert.setString(7,saturdayHours);
            preparedStatementWorkScheduleInsert.setString(8,sundayHours);
            preparedStatementWorkScheduleInsert.executeUpdate();

        }catch(MySQLIntegrityConstraintViolationException e){
            if(workScheduleInfoLabel!= null){
                workScheduleInfoLabel.setTextFill(Color.web("#bf1c37"));
                workScheduleInfoLabel.setText("ERROR: Template with this name already exists!");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            e.getCause();
            workScheduleInfoLabel.setTextFill(Color.web("#bf1c37"));
            workScheduleInfoLabel.setText("ERROR: Something went wrong!");
        }
    }

    /**
     * Saves currently marked hours on work schedule visualised on screen in app as work schedule template in database
     */
    public void saveWorkScheduleAsTemplate(){
        String mondayHours= "", tuesdayHours= "", wednesdayHours= "", thursdayHours= "", fridayHours= "", saturdayHours= "", sundayHours= "";

        String template_name = saveTemplateTextField.getText();

        if(template_name == "" || template_name==null) {
            workScheduleInfoLabel.setTextFill(Color.web("#bf1c37"));
            workScheduleInfoLabel.setText("ERROR: You need to provide name for template!");
        }else {

            for (int i = 0; i <= 23; i++) {
                for (int j = 0; j <= 6; j++) {
                    if (hourBtnArray[i][j] == 1 || hourBtnArray[i][j] == 2) {
                        switch (j) {
                            case 0: mondayHours += i + ",";break;
                            case 1: tuesdayHours += i + ",";break;
                            case 2: wednesdayHours += i + ",";break;
                            case 3: thursdayHours += i + ",";break;
                            case 4: fridayHours += i + ",";break;
                            case 5: saturdayHours += i + ",";break;
                            case 6: sundayHours += i + ",";break;
                        }
                    }
                }
            }
            insertWorkScheduleTemplate(template_name, mondayHours, tuesdayHours, wednesdayHours, thursdayHours, fridayHours, saturdayHours, sundayHours);
            workScheduleInfoLabel.setTextFill(Color.web("#3072ff"));
            workScheduleInfoLabel.setText("Successfully saved schedule as template");
        }
        initWorkScheduleTemplateComboBox();
    }

    /**
     * Selects work schedule for specific user and date
     * @param userId user id
     * @param year year
     * @param week week of the year
     * @return object containing work schedule data pulled from database
     */
    public ResultSet selectWorkSchedule(int userId, int year, int week){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String workScheduleSelect = "SELECT * FROM work_schedule WHERE id_user= ? AND year= ? AND week= ?";
        try{
            PreparedStatement preparedStatementWorkScheduleSelect = connectDB.prepareStatement(workScheduleSelect);
            preparedStatementWorkScheduleSelect.setInt(1, userId);
            preparedStatementWorkScheduleSelect.setInt(2, year);
            preparedStatementWorkScheduleSelect.setInt(3, week);
            ResultSet queryResult = preparedStatementWorkScheduleSelect.executeQuery();
            return queryResult;

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return null;
        }
    }

    /**
     * Loads work schedule on screen by passing parameters and calling selectWorkSchedule method then representing visually pulled data from database
     */
    public void loadWorkSchedule(){
        resetHourButtonsOnAction();

        try {
            int worker = scheduleWorkerComboBox.getItems().indexOf(scheduleWorkerComboBox.getValue());
            String userId = scheduleWorkerIdComboBox_data.get(worker);

            ResultSet selectWS = selectWorkSchedule(Integer.parseInt(userId), actuallyOperatingYear, actuallyOperatingWeek);

            while(selectWS.next())
            {
                readWeekDayHoursToButtons(selectWS, true);
                refreshHourButtonsOnAction();
                break;
            }

        }catch(RuntimeException e){
            // Do Nothing
        }
        catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    /**
     * Deletes previously set work schedule for specific user and date
     * @param userId user id
     * @param year year
     * @param week week of the year
     * @return true if operation was executed successfully, if not returns false
     */
    public boolean deleteWorkSchedule(int userId, int year, int week ){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String workScheduleDelete = "DELETE FROM work_schedule WHERE id_user= ? AND year= ? AND week= ?";

        try{
            PreparedStatement preparedStatementWorkScheduleDelete = connectDB.prepareStatement(workScheduleDelete);
            preparedStatementWorkScheduleDelete.setInt(1,userId);
            preparedStatementWorkScheduleDelete.setInt(2,actuallyOperatingYear);
            preparedStatementWorkScheduleDelete.setInt(3,actuallyOperatingWeek);
            preparedStatementWorkScheduleDelete.executeUpdate();
            return true;

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return false;
        }
    }

    /**
     * Inserts new work schedule record to database for specific user and date
     * @param userId user id
     * @param mondayHours string containing monday work hours
     * @param tuesdayHours string containing tuesday work hours
     * @param wednesdayHours string containing wednesday work hours
     * @param thursdayHours string containing thursday work hours
     * @param fridayHours string containing friday work hours
     * @param saturdayHours string containing saturday work hours
     * @param sundayHours string containing sunday work hours
     */
    public void insertWorkSchedule(int userId, String mondayHours, String tuesdayHours, String wednesdayHours, String thursdayHours, String fridayHours,
                                   String saturdayHours, String sundayHours){

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        try{
            deleteWorkSchedule(userId,actuallyOperatingYear,actuallyOperatingWeek);

            String workScheduleInsert = "INSERT INTO work_schedule (id_user, year, week, monday_hours, tuesday_hours, wednesday_hours, thursday_hours, friday_hours, saturday_hours, sunday_hours) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement preparedStatementWorkScheduleInsert = connectDB.prepareStatement(workScheduleInsert);
            preparedStatementWorkScheduleInsert.setInt(1, userId);
            preparedStatementWorkScheduleInsert.setInt(2, actuallyOperatingYear);
            preparedStatementWorkScheduleInsert.setInt(3, actuallyOperatingWeek);
            preparedStatementWorkScheduleInsert.setString(4, mondayHours);
            preparedStatementWorkScheduleInsert.setString(5, tuesdayHours);
            preparedStatementWorkScheduleInsert.setString(6, wednesdayHours);
            preparedStatementWorkScheduleInsert.setString(7, thursdayHours);
            preparedStatementWorkScheduleInsert.setString(8, fridayHours);
            preparedStatementWorkScheduleInsert.setString(9, saturdayHours);
            preparedStatementWorkScheduleInsert.setString(10, sundayHours);
            preparedStatementWorkScheduleInsert.executeUpdate();

        }catch(RuntimeException e){
            if(workScheduleInfoLabel!=null) {
                workScheduleInfoLabel.setTextFill(Color.web("#bf1c37"));
                workScheduleInfoLabel.setText("ERROR: No worker selected!");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            e.getCause();
            workScheduleInfoLabel.setTextFill(Color.web("#bf1c37"));
            workScheduleInfoLabel.setText("ERROR: Something went wrong!");
        }
    }

    /**
     * Data about work schedule for specific user and data is visually represented on screen by changing background color of specific buttons representing hours
     */
    public void assignWorkSchedule(){
        String mondayDate = dateOfDayLabelMonday.getText();
        String tuesdayDate = dateOfDayLabelTuesday.getText();
        String wednesdayDate = dateOfDayLabelWednesday.getText();
        String thursdayDate = dateOfDayLabelThursday.getText();
        String fridayDate = dateOfDayLabelFriday.getText();
        String saturdayDate = dateOfDayLabelSaturday.getText();
        String sundayDate = dateOfDayLabelSunday.getText();

        int worker = scheduleWorkerComboBox.getItems().indexOf(scheduleWorkerComboBox.getValue());
        int userId = Integer.parseInt(scheduleWorkerIdComboBox_data.get(worker));

        String mondayHours= "", tuesdayHours= "", wednesdayHours= "", thursdayHours= "", fridayHours= "", saturdayHours= "", sundayHours= "";

        for(int i = 0; i <= 23; i++)
        {
            for(int j = 0; j <= 6; j++)
            {
                if( hourBtnArray[i][j] == 1 || hourBtnArray[i][j] == 2) {

                    switch(j) {
                        case 0: mondayHours += i+","; break;
                        case 1: tuesdayHours += i+","; break;
                        case 2: wednesdayHours += i+","; break;
                        case 3: thursdayHours += i+","; break;
                        case 4: fridayHours += i+","; break;
                        case 5: saturdayHours += i+","; break;
                        case 6: sundayHours += i+","; break;
                    }
                }
            }
        }
        insertWorkSchedule(userId, mondayHours, tuesdayHours, wednesdayHours, thursdayHours, fridayHours, saturdayHours, sundayHours);
        workScheduleInfoLabel.setTextFill(Color.web("#3072ff"));
        workScheduleInfoLabel.setText("Work schedule for week: "+dateOfDayLabelMonday.getText()+" - "+dateOfDayLabelSunday.getText()+" successfully assigned to user with id:"+userId);

        for(int i = 0; i <= 23; i++)
        {
            for(int j = 0; j <= 6; j++)
            {
                if( hourBtnArray[i][j] == 1) {
                    hourBtnArray[i][j] = 2;
                    hourBtnObjArray[i][j].setStyle("-fx-background-color: #00ffe5");
                }
            }
        }
    }

    /**
     * Basing on data picker selection, method checks which week of the year it is
     */
    public void getNumberOfWeekFromPickedDataOnAction(){
        LocalDate date = scheduleDataPicker.getValue();
        Locale locale = Locale.UK;
        int weekOfYear = date.get(WeekFields.of(locale).weekOfWeekBasedYear()) ;
        int year = date.getYear();

        actuallyOperatingWeek = weekOfYear;
        actuallyOperatingYear = year;

        setScheduleDataLabels(year,weekOfYear);
        loadWorkSchedule();
        workScheduleInfoLabel.setText("");
    }

    /**
     * Changes week for shown work schedule in lunched application from current to next in order. Method assigned to button "Next week" in application In Account Manager->Work schedule section
     */
    public void nextWeekOnAction(){
        actuallyOperatingWeek += 1;
        if (actuallyOperatingWeek >= 53) {
            actuallyOperatingYear += 1;
            actuallyOperatingWeek = 1;
        }
        setScheduleDataLabels(actuallyOperatingYear,actuallyOperatingWeek);
        loadWorkSchedule();
        workScheduleInfoLabel.setText("");
    }

    /**
     * Changes week for shown work schedule in lunched application from current to previous in order. Method assigned to button "Previous week" in application In Account Manager->Work schedule section
     */
    public void previousWeekOnAction(){
        actuallyOperatingWeek -= 1;
        if (actuallyOperatingWeek <= 0) {
            actuallyOperatingYear -= 1;
            actuallyOperatingWeek = 52;
        }
        setScheduleDataLabels(actuallyOperatingYear,actuallyOperatingWeek);
        loadWorkSchedule();
        workScheduleInfoLabel.setText("");
    }

    /**
     * Fills combo box on screen with all possible workers in system, so work schedule could be assigned to a specific person
     */
    public void initScheduleWorkerComboBox(){
        scheduleWorkerComboBox_data = FXCollections.observableArrayList();
        scheduleWorkerIdComboBox_data = FXCollections.observableArrayList();

        try {
            ResultSet userSelect = selectUser();

            while(userSelect.next())
            {
                scheduleWorkerIdComboBox_data.add(userSelect.getString("id_user"));
                scheduleWorkerComboBox_data.add(userSelect.getString("id_user")+"."+userSelect.getString("firstname")
                        +" "+userSelect.getString("lastname")+" ("+userSelect.getString("position_name")+")");
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        scheduleWorkerComboBox.setItems(scheduleWorkerComboBox_data);
    }

    /**
     * Fills combo box used when updating user position in table view with all possible positions to set pulled from database
     * @return lis of all positions pulled from database
     */
    public ObservableList<String> initPositionComboBox(){
        positionIdComboBox_data = FXCollections.observableArrayList();
        positionNameComboBox_data = FXCollections.observableArrayList();

        try {

            ResultSet positionSelcect = selectPosition();

            while(positionSelcect.next())
            {
                positionIdComboBox_data.add(positionSelcect.getString("id_position"));
                positionNameComboBox_data.add(positionSelcect.getString("position_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return positionNameComboBox_data;
    }

    /**
     * Sets user table view in lunched application. Defines which columns can be updated and which not
     */
    public void setUserTableViewColumns(){
        col_id_User.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_firstname_User.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        col_lastname_User.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        col_id_position_User.setCellValueFactory(new PropertyValueFactory<>("position"));
        col_username_User.setCellValueFactory(new PropertyValueFactory<>("username"));
        col_password_User.setCellValueFactory(new PropertyValueFactory<>("password"));
        col_email_User.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_phone_number_User.setCellValueFactory(new PropertyValueFactory<>("phone_number"));

        col_firstname_User.setCellFactory(TextFieldTableCell.forTableColumn());
        col_firstname_User.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setFirstname(e.getNewValue());
        });

        col_lastname_User.setCellFactory(TextFieldTableCell.forTableColumn());
        col_lastname_User.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setLastname(e.getNewValue());
        });

        col_id_position_User.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), initPositionComboBox()));
        col_id_position_User.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setPosition(e.getNewValue());
        });

        col_username_User.setCellFactory(TextFieldTableCell.forTableColumn());
        col_username_User.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setUsername(e.getNewValue());
        });

        col_password_User.setCellFactory(TextFieldTableCell.forTableColumn());
        col_password_User.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setPassword(e.getNewValue());
        });

        col_email_User.setCellFactory(TextFieldTableCell.forTableColumn());
        col_email_User.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setEmail(e.getNewValue());
        });

        col_phone_number_User.setCellFactory(TextFieldTableCell.forTableColumn());
        col_phone_number_User.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setPhone_number(e.getNewValue());
        });
        user_table.setEditable(true);
    }

    /**
     * Selects all users and assigned to them positions data from database
     * @return object containing users and assigned to them positions data pulled from database
     */
    public ResultSet selectUser(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String sqlSelect = "SELECT * FROM user, position WHERE user.id_position = position.id_position";
        try {
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
     * Selects specific user data pulled from database
     * @param idUser user id
     * @return object containing user data pulled from database
     */
    public ResultSet selectUser(int idUser){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String sqlSelect = "SELECT * FROM user WHERE id_user = ?";
        try {
            PreparedStatement statement = connectDB.prepareStatement(sqlSelect);
            statement.setInt(1, idUser);
            ResultSet queryResult = statement.executeQuery();
            return  queryResult;

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return null;
        }
    }

    /**
     * Fills user table view with data pulled from database
     */
    public void initAndLoadData_TableUser(){
        user_table_data = FXCollections.observableArrayList();
        setUserTableViewColumns();

        try {

            ResultSet selectUser = selectUser();

            while(selectUser.next())
            {
                user_table_data.add(new UserTable(selectUser.getString("id_user"), selectUser.getString("firstname"),
                        selectUser.getString("lastname"), selectUser.getString("position_name"),
                        selectUser.getString("username"), selectUser.getString("password"),
                        selectUser.getString("email"), selectUser.getString("phone_number")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        user_table.setItems(searchableUserTableView());
    }

    /**
     * Allows to filter user table view by given phrase
     * @return filtered content of user table view as list
     */
    public SortedList<UserTable> searchableUserTableView(){
        FilteredList<UserTable> filteredData = new FilteredList<>(user_table_data, b -> true);

        userSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user ->{
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if(String.valueOf(user.getId().toLowerCase()).indexOf(lowerCaseFilter) != -1){
                    return true;
                } else if(user.getEmail().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(user.getFirstname().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(user.getUsername().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(user.getPassword().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(user.getLastname().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(user.getPhone_number().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(user.getPosition().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else return false;

            });
        });

        SortedList<UserTable> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(user_table.comparatorProperty());

        return sortedData;
    }

    /**
     * Sets position table view in lunched application. Defines which columns can be updated and which not
     */
    public void setPositionTableViewColumns(){
        col_id_position_Position.setCellValueFactory(new PropertyValueFactory<>("id_position"));
        col_position_name_Position.setCellValueFactory(new PropertyValueFactory<>("position_name"));

        col_position_name_Position.setCellFactory(TextFieldTableCell.forTableColumn());
        col_position_name_Position.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setPosition_name(e.getNewValue());
        });

        position_table.setEditable(true);
    }

    /**
     * Selects all positions data from database
     * @return object containing positions data pulled from database
     */
    public ResultSet selectPosition(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String sqlSelect = "SELECT * FROM position";

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
     * Fills position table view with data pulled from database
     */
    public void initAndLoadData_TablePosition(){
        position_table_data = FXCollections.observableArrayList();
        setPositionTableViewColumns();

        try {
            ResultSet position = selectPosition();

            while(position.next())
            {
                position_table_data.add(new PositionTable(position.getString("id_position"), position.getString("position_name")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }

        position_table.setItems(searchablePositionTableView());

        position_table.setOnMouseClicked(e->{initAndLoadData_TablePrivileges();});

    }

    /**
     * Allows to filter position table view by given phrase
     * @return filtered content of position table view as list
     */
    public SortedList<PositionTable> searchablePositionTableView(){
        FilteredList<PositionTable> filteredData = new FilteredList<>(position_table_data, b -> true);

        positionSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(position ->{
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if(String.valueOf(position.getId_position().toLowerCase()).indexOf(lowerCaseFilter) != -1){
                    return true;
                } else if(position.getPosition_name().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }else return false;

            });
        });

        SortedList<PositionTable> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(position_table.comparatorProperty());
        return sortedData;
    }


    /**
     * Sets privilege table view in lunched application. Defines which columns can be updated and which not
     */
    public void setPrivilegeTableViewColumns(){
        col_module_Privilege.setCellValueFactory(new PropertyValueFactory<>("module"));
        col_access_Privilege.setCellValueFactory(new PropertyValueFactory<>("access"));

        col_access_Privilege.setCellFactory(TextFieldTableCell.forTableColumn());
        col_access_Privilege.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setAccess(e.getNewValue());
        });

        privilege_table.setEditable(true);
    }

    /**
     * Selects all privileges assigned to specific position
     * @param idPosition position id
     * @return object containing privileges data pulled from database
     */
    public  ResultSet selectPrivileges(int idPosition){

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String privilegeSelect = "SELECT * FROM privilege WHERE id_position= ?";
        try {
            PreparedStatement preparedStatementPrivilegeSelect = connectDB.prepareStatement(privilegeSelect);
            preparedStatementPrivilegeSelect.setInt(1, idPosition);
            ResultSet queryResult = preparedStatementPrivilegeSelect.executeQuery();
            return queryResult;

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return null;
        }
    }

    /**
     * Fills privilege table view with data pulled from database
     */
    public void initAndLoadData_TablePrivileges(){

        privilege_table_data = FXCollections.observableArrayList();
        privilege_table_data.clear();
        privilege_table.setItems(privilege_table_data);

        for(PositionTable positionTable : position_table.getSelectionModel().getSelectedItems()){

            privilege_table_data = FXCollections.observableArrayList();
            setPrivilegeTableViewColumns();
            privilege_table_data.clear();

            try {
                ResultSet privileges = selectPrivileges(Integer.parseInt(positionTable.getId_position()));

                while(privileges.next())
                {
                    privilege_table_data.add(new PrivilegeTable(privileges.getString("id_position"), privileges.getString("module"), privileges.getString("access")));
                }

            } catch (SQLException e) {
                e.printStackTrace();
                e.getCause();
            }

            privilege_table.setItems(privilege_table_data);

        }
    }

    /**
     * Deletes specific user from database
     * @param userId user id
     * @return method result message as string
     */
    public String deleteUser(int userId){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String userDelete = "DELETE FROM user WHERE id_user= ?";

        try{
            if(userId == 1){
                return "ERROR: Administrator account can not be deleted!";
            }else {
                PreparedStatement preparedStatementUserDelete = connectDB.prepareStatement(userDelete);
                preparedStatementUserDelete.setInt(1, userId);
                preparedStatementUserDelete.executeUpdate();
                return "Successfully deleted user with id number: "+userId;
            }
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return "ERROR: something went wrong!";
        }
    }

    /**
     * Passes user id from selected item in user table view to deleteUser method and calls it. Method assigned to button "Delete" in Account list section, first module - Account Manager
     */
    public void deleteUserOnAction(){
        ObservableList<UserTable> userTables = user_table.getSelectionModel().getSelectedItems();
        for(UserTable userTable : userTables){

            String resultMessage = deleteUser(Integer.parseInt(userTable.getId()));
            if(resultMessage.contains("ERROR")){
                queryInfoLabel_User.setTextFill(Color.web("#bf1c37"));
            }else {
                queryInfoLabel_User.setTextFill(Color.web("#3072ff"));
            }
            queryInfoLabel_User.setText(resultMessage);

            initAndLoadData_TableUser();
            break;
        }
    }

    /**
     * Deletes specific position from database
     * @param positionId position id
     * @return method result message as string
     */
    public String deletePosition(int positionId){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String positionDelete = "DELETE FROM position WHERE id_position= ?";

        if(positionId == 1){
            return "ERROR: Administrator position can not be deleted!";
        }else {
            try {
                PreparedStatement preparedStatementPositionDelete = connectDB.prepareStatement(positionDelete);
                preparedStatementPositionDelete.setInt(1, positionId);
                preparedStatementPositionDelete.executeUpdate();
                return "Successfully deleted position with id number: " + positionId;

            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
                return "ERROR: Something went wrong!";
            }
        }
    }

    /**
     * Passes position id from selected item in position table view to deletePosition method and calls it. Method assigned to button "Delete" in Positions list section, first module - Account Manager
     */
    public void deletePositionOnAction(){
        ObservableList<PositionTable> positionTables = position_table.getSelectionModel().getSelectedItems();
        ObservableList<PrivilegeTable> privilegeTables = privilege_table.getSelectionModel().getSelectedItems();

        if(!privilegeTables.isEmpty()){

            for (PrivilegeTable privilegeTable : privilegeTables) {
                queryInfoLabel_Position.setTextFill(Color.web("#bf1c37"));
                queryInfoLabel_Position.setText("ERROR: You can not delete a privilege row!");
                break;
            }

        }else {

            for (PositionTable positionTable : positionTables) {
                String resultMessage = deletePosition(Integer.parseInt(positionTable.getId_position()));
                if(resultMessage.contains("ERROR")) {
                    queryInfoLabel_Position.setTextFill(Color.web("#bf1c37"));
                }else{
                    queryInfoLabel_Position.setTextFill(Color.web("#3072ff"));
                }
                queryInfoLabel_Position.setText(resultMessage);

                break;
            }
        }
        initAndLoadData_TablePosition();
        initAndLoadData_TablePrivileges();
    }

    /**
     * Refreshes user table view pulling fresh data from database
     */
    public void refreshUserOnAction(){
        initAndLoadData_TableUser();
        queryInfoLabel_User.setText("");
        userSearchBox.setText("");
    }

    /**
     * Refreshes position and privilege table view pulling fresh data from database
     */
    public void refreshPositionAndPrivilegesOnAction(){
        initAndLoadData_TablePosition();
        initAndLoadData_TablePrivileges();
        queryInfoLabel_Position.setText("");
        positionSearchBox.setText("");
    }

    /**
     * Updates specific user in database
     * @param firstname user firstname
     * @param lastname user lastname
     * @param id_position user position id
     * @param username user username
     * @param password user password
     * @param email user email
     * @param phoneNumber user phone number
     * @param userId user id
     * @return method result message as string
     */
    public String updateUser(String firstname, String lastname, int id_position, String username, String password, String email, String phoneNumber, int userId){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        boolean isFirstnameValid = firstname.matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$");
        boolean isLastnameValid = lastname.matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$");
        boolean isUsernameValid = username.matches("^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");
        boolean isPasswordValid = password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#@$!%*?&-])[A-Za-z\\d@#$!%*?&-]{8,}$");
        boolean isEmailValid = email.matches("^(.+)@(.+)$");
        boolean isPhoneNumberValid = phoneNumber.matches("^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{3}$");

        String userUpdate = "UPDATE user SET firstname = ?, lastname = ?, id_position = ?, username = ?, password = ?, email = ?, phone_number = ? WHERE id_user= ?";

        try{
            ResultSet rsUser = selectUser(userId);
            rsUser.next();

            if(firstname.equals("") || lastname.equals("") || id_position == 0 || username.equals("") || password.equals("") || email.equals("") || phoneNumber.equals("")) {
                return "ERROR: all fields must be filled!";
            }else if(!isFirstnameValid ){
                return "ERROR: Wrong data format given in firstname field!";
            }else if(!isLastnameValid ){
                return "ERROR: Wrong data format given in lastname field!";
            }else if(!isUsernameValid ){
                return "ERROR: Wrong data format given in username field! (8-20 chars, not allowed at beginning and end '_' and '.')";
            }else if(!isPasswordValid && !rsUser.getString("password").equals(password)){
                return "ERROR: Wrong data format given in password field! (min. eight chars, min. one letter, min. one number)";
            }else if(!isEmailValid ){
                return "ERROR: Wrong data format given in email field!";
            }else if(!isPhoneNumberValid ){
                return "ERROR: Wrong data format given in phone number field!";
            }else {
                PreparedStatement preparedStatementUserUpdate = connectDB.prepareStatement(userUpdate);
                preparedStatementUserUpdate.setString(1, firstname);
                preparedStatementUserUpdate.setString(2, lastname);
                preparedStatementUserUpdate.setInt(3, id_position);
                preparedStatementUserUpdate.setString(4, username);
                preparedStatementUserUpdate.setString(5,  BCrypt.hashpw(password, BCrypt.gensalt(10)));
                preparedStatementUserUpdate.setString(6, email);
                preparedStatementUserUpdate.setString(7, phoneNumber);
                preparedStatementUserUpdate.setInt(8, userId);
                preparedStatementUserUpdate.executeUpdate();
                return "Successfully updated user with id number: " + userId;
            }
        }catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
            return "ERROR: given username is already taken";
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return "ERROR: Something went wrong";
        }
    }

    /**
     * Selects specific position data from database by passing unique position name
     * @param position position name
     * @return object containing position data pulled from database
     */
    public ResultSet selectPosition(String position){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String positionSelect  = "SELECT * FROM position WHERE position_name= ?";
        try {
            PreparedStatement preparedStatementPositionSelect = connectDB.prepareStatement(positionSelect);
            preparedStatementPositionSelect.setString(1, position);
            ResultSet queryResult = preparedStatementPositionSelect.executeQuery();
            return queryResult;

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return null;
        }
    }

    /**
     * Passes selected user from user table view to updateUser and calls it
     */
    public void updateUserOnAction(){

        ObservableList<UserTable> userTables = user_table.getSelectionModel().getSelectedItems();
        for(UserTable userTable : userTables){
            int id_position= 0;

            ResultSet position = selectPosition( userTable.getPosition());

            try{
                while(position.next())
                {
                    id_position = position.getInt("id_position");
                    break;
                }

                String firstname = userTable.getFirstname();
                String lastname = userTable.getLastname();
                String username = userTable.getUsername();
                String password = userTable.getPassword();
                String email = userTable.getEmail();
                String phoneNumber = userTable.getPhone_number();
                int userId = Integer.parseInt(userTable.getId());

                String methodResultMessage = updateUser(firstname, lastname, id_position, username, password, email, phoneNumber, userId);
                if(methodResultMessage.contains("ERROR")) queryInfoLabel_User.setTextFill(Color.web("#bf1c37"));
                else {
                    queryInfoLabel_User.setTextFill(Color.web("#3072ff"));
                    initAndLoadData_TableUser();
                }
                queryInfoLabel_User.setText(methodResultMessage);
                break;

            } catch (SQLException e) {
                e.printStackTrace();
                e.getCause();
            }
        }
    }

    /**
     * Updates privileges assigned to a specific position
     * @param module module name (one of: Account Manager, Technical Efficiency, Air Traffic, Customer Service)
     * @param access access value (1 or 0)
     * @param positionId position id
     * @return method result message as string
     */
    public String updatePrivilege(String module, String access, int positionId ){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String privilegeUpdate = "UPDATE privilege SET access = ? WHERE id_position= ? AND module= ?";

        try {
            if(access.equals("")) {
                return "ERROR: access field can not be empty!";
            }else if(!access.equals("1") && !access.equals("0")){
                return "ERROR: You can set access value only as 0 or 1!";
            }else if(positionId == 1){
                return "ERROR: Administrator position can not be changed!";
            }else {
                PreparedStatement preparedStatementPrivilegeUpdate = connectDB.prepareStatement(privilegeUpdate);
                preparedStatementPrivilegeUpdate.setString(1, access);
                preparedStatementPrivilegeUpdate.setInt(2, positionId);
                preparedStatementPrivilegeUpdate.setString(3, module);
                preparedStatementPrivilegeUpdate.executeUpdate();
                return "Successfully updated privilege of position with id number: " + positionId;
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
            return "ERROR: Something went wrong!";
        }
    }

    /**
     * Updates specific position in database
     * @param positionName position name
     * @param positionId position id
     * @return method result message as string
     */
    public String updatePosition(String positionName, int positionId){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        boolean isPositionNameValid = positionName.matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$");

        String positionUpdate = "UPDATE position SET position_name = ? WHERE id_position= ?";

        try {
            if (positionName.equals("")) {
                return"ERROR: Position name can not be empty!";
            }else if(!isPositionNameValid){
                return "ERROR: Wrong data format given in position name field!";
            }else if(positionId == 1){
                return "ERROR: Administrator position can not be changed!";
            }else {
                PreparedStatement preparedStatementPositionUpdate = connectDB.prepareStatement(positionUpdate);
                preparedStatementPositionUpdate.setString(1, positionName);
                preparedStatementPositionUpdate.setInt(2, positionId);
                preparedStatementPositionUpdate.executeUpdate();
                return "Successfully updated position with id number: " + positionId;
            }
        } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
            return "ERROR: given position already exists!";
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
            return "ERROR: Something went wrong!";
        }
    }

    /**
     * Passes selected position data from position table view to updatePosition method and calls it
     */
    public void updatePositionOnAction(){
        ObservableList<PositionTable> positionTables = position_table.getSelectionModel().getSelectedItems();
        ObservableList<PrivilegeTable> privilegeTables = privilege_table.getSelectionModel().getSelectedItems();

        if(!privilegeTables.isEmpty()) {
            for (PrivilegeTable privilegeTable : privilegeTables) {
                String module = privilegeTable.getModule();
                String access = privilegeTable.getAccess();
                int positionId = Integer.parseInt(privilegeTable.getId_position());

                String methodResultMessage = updatePrivilege(module, access, positionId);
                if(methodResultMessage.contains("ERROR")) queryInfoLabel_Position.setTextFill(Color.web("#bf1c37"));
                else {
                    queryInfoLabel_Position.setTextFill(Color.web("#3072ff"));
                    initAndLoadData_TablePosition();
                    initAndLoadData_TablePrivileges();
                }
                queryInfoLabel_Position.setText(methodResultMessage);
                break;
            }
        }else{
            for (PositionTable positionTable : positionTables) {
                String positionName = positionTable.getPosition_name();
                int positionId = Integer.parseInt(positionTable.getId_position());

                String methodResultMessage = updatePosition(positionName, positionId);

                if(methodResultMessage.contains("ERROR")) queryInfoLabel_Position.setTextFill(Color.web("#bf1c37"));
                else {
                    queryInfoLabel_Position.setTextFill(Color.web("#3072ff"));
                    initAndLoadData_TablePosition();
                    initAndLoadData_TablePrivileges();
                }
                queryInfoLabel_Position.setText(methodResultMessage);
                break;
            }
        }
    }

    /**
     * When hour button in work schedule section is pressed, its being saved to array as one value of 1 - marked, 2 - confirmed, 0 - neutral, changing at the same time background color of the button
     * @param event source button that was clicked
     */
    public void hourButtonOnAction(javafx.event.ActionEvent event) {
        //  Button btn = (Button)event.getSource();
        Node node = (Node) event.getTarget();
        try {
            Integer row = GridPane.getRowIndex(node);
            Integer col = GridPane.getColumnIndex(node);

            row = row - 2;

            if(row == null)
                row = 0;
            if(col == null)
                col = 0;

            System.out.println(row+" "+col);
            if( hourBtnArray[row][col] == 0) {
                node.setStyle("-fx-background-color: #bdffdf");
                hourBtnArray[row][col] = 1;
            }else{
                node.setStyle("-fx-background-color: white");
                hourBtnArray[row][col] = 0;
            }
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    /**
     * Opens movable popup window in which you can insert new user record to database
     */
    public void addUserOnAction(){
        if(!addUserPopup) {
            FXMLLoader root = openMovablePopupWindow("AddUserPopup.fxml");
            AddUserPopupController controller = root.getController();
            controller.receiveParentModuleObject(this);
            addUserPopup = true;
        }
    }

    /**
     * Opens movable popup window in which you can insert new position record to database
     */
    public void addPositionOnAction() {
        if(!addPositionPopup){
            FXMLLoader root = openMovablePopupWindow("AddPositionPopup.fxml");
            AddPositionPopupController controller = root.getController();
            controller.receiveParentModuleObject(this);
            addPositionPopup = true;
        }
    }

}
