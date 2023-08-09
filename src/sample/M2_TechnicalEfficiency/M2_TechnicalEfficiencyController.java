package sample.M2_TechnicalEfficiency;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.util.converter.DefaultStringConverter;
import sample.ParentScene.ModuleParent;
import sample.tables.AircraftTable;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import sample.DatabaseFiles.DatabaseConnection;

/**
 * Controller class for second module screen window - Technical Efficiency
 * @author Daniel Czyz
 * @version 1.0.1 30/05/2021
 */
public class M2_TechnicalEfficiencyController extends ModuleParent {

    @FXML
    private Label queryInfoLabel_Aircraft ;
    @FXML
    private Label queryInfoLabel_Task;
    @FXML
    private TextField aircraftSearchBox;

    @FXML
    public TableView<AircraftTable> aircraft_table;
    @FXML
    private TableColumn<AircraftTable, String> col_id_aircraft;
    @FXML
    private TableColumn<AircraftTable, String> col_name_aircraft;
    @FXML
    private TableColumn<AircraftTable, String> col_model_aircraft;
    @FXML
    private TableColumn<AircraftTable, String> col_number_of_seats_aircraft;
    @FXML
    private TableColumn<AircraftTable, String> col_engine_number_aircraft;
    @FXML
    private TableColumn<AircraftTable, String> col_status_aircraft;
    @FXML
    private TableColumn<AircraftTable, String> col_technical_condition_aircraft;
    @FXML
    private TableColumn<AircraftTable, String> col_is_tanked_aircraft;

    @FXML
    public TableView<AircraftTable> aircraft_checkup_table;
    @FXML
    private TableColumn<AircraftTable, String> col_id_aircraft_task;
    @FXML
    private TableColumn<AircraftTable, String> col_aircraft_task;
    @FXML
    private TableColumn<AircraftTable, String> col_technical_condition_task;
    @FXML
    private TableColumn<AircraftTable, String> col_isTanked_task;

    @FXML
    private TextField aircraftCheckupSearchBox;

    public static ObservableList<String> aircraftTechnicalCondition_data;
    public static ObservableList<String> aircraftIsTanked_data;

    public static ObservableList<AircraftTable> aircraft_table_data ;
    public static ObservableList<AircraftTable> aircraft_checkup_table_data ;

    public boolean addAircraftPopup = false;

    /**
     * Content of this method is executed when instance of M2_TechnicalEfficiencyController class is loaded
     * @param location location
     * @param resource resource
     */
    @Override
    public void initialize(URL location, ResourceBundle resource){
        rootPane.setOpacity(0);
        makeFadeInTransition(rootPane);

        initAndLoadData_TableAircraft();
    }

    /**
     * Sets aircraft table view in lunched application. Defines which columns can be updated and which not
     */
    public void setAircraftTableViewColumns(){
        col_id_aircraft.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_aircraft_task.setCellValueFactory(new PropertyValueFactory<>("aircraft_task"));
        col_name_aircraft.setCellValueFactory(new PropertyValueFactory<>("brand_name"));
        col_model_aircraft.setCellValueFactory(new PropertyValueFactory<>("model"));
        col_technical_condition_aircraft.setCellValueFactory(new PropertyValueFactory<>("technical_condition"));
        col_is_tanked_aircraft.setCellValueFactory(new PropertyValueFactory<>("is_tanked"));
        col_engine_number_aircraft.setCellValueFactory(new PropertyValueFactory<>("engine_number"));
        col_number_of_seats_aircraft.setCellValueFactory(new PropertyValueFactory<>("seats_number"));
        col_technical_condition_task.setCellValueFactory(new PropertyValueFactory<>("technical_condition"));
        col_isTanked_task.setCellValueFactory(new PropertyValueFactory<>("is_tanked"));
        col_status_aircraft.setCellValueFactory(new PropertyValueFactory<>("status"));

        col_name_aircraft.setCellFactory(TextFieldTableCell.forTableColumn());
        col_name_aircraft.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setBrand_name(e.getNewValue());
        });

        col_model_aircraft.setCellFactory(TextFieldTableCell.forTableColumn());
        col_model_aircraft.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setModel(e.getNewValue());
        });

        col_engine_number_aircraft.setCellFactory(TextFieldTableCell.forTableColumn());
        col_engine_number_aircraft.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setEngine_number(e.getNewValue());
        });

        col_number_of_seats_aircraft.setCellFactory(TextFieldTableCell.forTableColumn());
        col_number_of_seats_aircraft.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setSeats_number(e.getNewValue());
        });

        col_technical_condition_task.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), aircraftTechnicalCondition_data));
        col_technical_condition_task.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setTechnical_condition(e.getNewValue());
        });

        col_isTanked_task.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), aircraftIsTanked_data));
        col_isTanked_task.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setIs_tanked(e.getNewValue());
        });

        aircraft_table.setEditable(true);
        aircraft_checkup_table.setEditable(true);

    }

    /**
     * Fills aircraft combobox with possible technical condition states
     */
    public void initTechnicalConditionListForComboBox(){
        aircraftTechnicalCondition_data = FXCollections.observableArrayList();
        aircraftTechnicalCondition_data.add("for_check_up");
        aircraftTechnicalCondition_data.add("not_operational");
        aircraftTechnicalCondition_data.add("operational");
    }

    /**
     * Fills aircraft combobox with true or false statement which defines if aircraft is tanked or not
     */
    public void initIsTankedListForComboBox(){
        aircraftIsTanked_data = FXCollections.observableArrayList();
        aircraftIsTanked_data.add("true");
        aircraftIsTanked_data.add("false");
    }

    /**
     * Allows to filter aircraft table view by given phrase
     * @return filtered content of aircraft table view as list
     */
    public SortedList<AircraftTable> searchableAircraftTableView(){
        FilteredList<AircraftTable> filteredData = new FilteredList<>(aircraft_table_data, b -> true);

        aircraftSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(aircraft ->{
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if(String.valueOf(aircraft.getId().toLowerCase()).indexOf(lowerCaseFilter) != -1){
                    return true;
                } else if(aircraft.getModel().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(aircraft.getBrand_name().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(aircraft.getTechnical_condition().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(aircraft.getStatus().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(aircraft.getIs_tanked().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(aircraft.getEngine_number().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(aircraft.getSeats_number().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else return false;

            });
        });

        SortedList<AircraftTable> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(aircraft_table.comparatorProperty());
        return sortedData;
    }

    /**
     * Allows to filter aircraft checkup table view by given phrase
     * @return filtered content of aircraft checkup table view as list
     */
    public SortedList<AircraftTable> searchableAircraftCheckupTableView(){
        FilteredList<AircraftTable> filteredData = new FilteredList<>(aircraft_checkup_table_data, b -> true);

        aircraftCheckupSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(aircraftCheckup ->{
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if(String.valueOf(aircraftCheckup.getId().toLowerCase()).indexOf(lowerCaseFilter) != -1){
                    return true;
                } else if(aircraftCheckup.getModel().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(aircraftCheckup.getBrand_name().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(aircraftCheckup.getTechnical_condition().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(aircraftCheckup.getIs_tanked().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(aircraftCheckup.getSeats_number().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }else if(aircraftCheckup.getAircraft_task().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else return false;

            });
        });

        SortedList<AircraftTable> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(aircraft_checkup_table.comparatorProperty());
        return sortedData;
    }

    /**
     * Selects all aircraft records from database
     * @return object containing aircraft data pulled from database
     */
    public ResultSet selectAircraft(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String sqlSelect = "SELECT * FROM aircraft";

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
     * Fills aircraft and aircraft checkup table view with data pulled from database
     */
    public void initAndLoadData_TableAircraft(){
        aircraft_table_data = FXCollections.observableArrayList();
        aircraft_checkup_table_data = FXCollections.observableArrayList();
        initTechnicalConditionListForComboBox();
        initIsTankedListForComboBox();
        setAircraftTableViewColumns();

        try {
            ResultSet selectAircraft = selectAircraft();

            while(selectAircraft.next())
            {
                aircraft_table_data.add(new AircraftTable(selectAircraft.getString("id_aircraft"), selectAircraft.getString("brand_name"),
                        selectAircraft.getString("model"), selectAircraft.getString("engine_number"),
                        selectAircraft.getString("seats_number"),selectAircraft.getString("technical_condition") ,
                        selectAircraft.getString("is_tanked"), selectAircraft.getString("status"), "" ));

                if(selectAircraft.getString("technical_condition").equals("for_check_up") ||
                        selectAircraft.getString("technical_condition").equals("not_operational") || selectAircraft.getString("is_tanked").equals("false")) {
                    aircraft_checkup_table_data.add(new AircraftTable(selectAircraft.getString("id_aircraft"), "",
                            "", "", "", selectAircraft.getString("technical_condition"),
                            selectAircraft.getString("is_tanked"), "", selectAircraft.getString("id_aircraft") +
                            ". " + selectAircraft.getString("brand_name") + " " + selectAircraft.getString("model") + " (seats: " + selectAircraft.getString("seats_number") + ")"
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }
        aircraft_table.setItems(searchableAircraftTableView());
        aircraft_checkup_table.setItems(searchableAircraftCheckupTableView());
    }

    /**
     * Deletes specific aircraft from database
     * @param aircraftId aircraft id
     * @return method result message as string
     */
    public String deleteAircraft(int aircraftId){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String aircraftDelete = "DELETE FROM aircraft WHERE id_aircraft= ?";

        try{
            PreparedStatement preparedStatementAircraftDelete = connectDB.prepareStatement(aircraftDelete);
            preparedStatementAircraftDelete.setInt(1, aircraftId);
            preparedStatementAircraftDelete.executeUpdate();

            return "Successfully deleted aircraft with id number: "+ aircraftId;

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return "ERROR: something went wrong!";
        }
    }

    /**
     * Passes aircraft id from selected row in aircraft table view to deleteAircraft method and calls it. Method assigned to button "Delete" in Aircraft section, second module - Technical Efficiency
     */
    public void deleteAircraftOnAction(){
        ObservableList<AircraftTable> aircraftTables = aircraft_table.getSelectionModel().getSelectedItems();
        for(AircraftTable aircraftTable : aircraftTables){

            String methodeResultMessage  = deleteAircraft(Integer.parseInt(aircraftTable.getId()));
            if(methodeResultMessage.contains("ERROR")) queryInfoLabel_Aircraft.setTextFill(Color.web("#bf1c37"));
            else queryInfoLabel_Aircraft.setTextFill(Color.web("#3072ff"));
            queryInfoLabel_Aircraft.setText(methodeResultMessage);
            initAndLoadData_TableAircraft();

            break;
        }
    }

    /**
     * Updates specific aircraft data in database
     * @param aircraftId aircraft id
     * @param brandName aircraft brand name
     * @param model aircraft model
     * @param engineNumber aircraft engine number
     * @param seatsNumber number of seats in aircraft
     * @param status aircraft status (one of: available, in_flight)
     * @return method result message as string
     */
    public String updateAircraft(int aircraftId, String brandName, String model, String engineNumber, String seatsNumber, String status){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        //  boolean isEngineNumberValid = engineNumber.matches("^[A-Z]\\d\\d\\d[a-zA-Z]\\d\\d\\d$");
        boolean isSeatsNumberValid = seatsNumber.matches("[1-9][0-9]*|0 or [1-9]\\d*|0");

        String aircraftUpdate = "UPDATE aircraft SET brand_name = ?, model = ?, engine_number = ?, seats_number = ?, status = ? WHERE id_aircraft= ?";

        try{
            if(brandName.equals("") || model.equals("") || engineNumber.equals("") || seatsNumber.equals("") || status.equals("")) {
                return "ERROR: all fields must be filled!";
                //    }else if(!isEngineNumberValid ){
                //        return "ERROR: Wrong data format given in engine number field!";
            }else if(!isSeatsNumberValid ){
                return "ERROR: Wrong data format given in seats number field!";
            }else{
                PreparedStatement preparedStatementAircraftUpdate = connectDB.prepareStatement(aircraftUpdate);
                preparedStatementAircraftUpdate.setString(1, brandName);
                preparedStatementAircraftUpdate.setString(2, model);
                preparedStatementAircraftUpdate.setString(3, engineNumber);
                preparedStatementAircraftUpdate.setString(4, seatsNumber);
                preparedStatementAircraftUpdate.setString(5, status);
                preparedStatementAircraftUpdate.setInt(6, aircraftId);
                preparedStatementAircraftUpdate.executeUpdate();

                return "Successfully updated user with id number: "+ aircraftId;
            }
        }catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
            return "ERROR: given engine number already exists!";
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return "ERROR: something went wrong!";
        }
    }

    /**
     * Passes aircraft id from selected row in aircraft table view to updateAircraft method and calls it. Method assigned to button "Update" in Aircraft section, second module - Technical Efficiency
     */
    public void updateAircraftOnAction(){
        ObservableList<AircraftTable> aircraftTables = aircraft_table.getSelectionModel().getSelectedItems();
        for(AircraftTable aircraftTable : aircraftTables){

            String methodResultMessage = updateAircraft(Integer.parseInt(aircraftTable.getId()), aircraftTable.getBrand_name(), aircraftTable.getModel(),
                    aircraftTable.getEngine_number(), aircraftTable.getSeats_number(), aircraftTable.getStatus());

            if(methodResultMessage.contains("ERROR")) queryInfoLabel_Aircraft.setTextFill(Color.web("#bf1c37"));
            else {
                queryInfoLabel_Aircraft.setTextFill(Color.web("#3072ff"));
                initAndLoadData_TableAircraft();
            }
            queryInfoLabel_Aircraft.setText(methodResultMessage);
            break;
        }

    }

    /**
     * Refreshes aircraft table view pulling fresh data from database
     */
    public void refreshAircraftListOnAction(){
        initAndLoadData_TableAircraft();
        queryInfoLabel_Aircraft.setText("");
        queryInfoLabel_Task.setText("");
        aircraftSearchBox.setText("");
        aircraftCheckupSearchBox.setText("");
    }

    /**
     * Opens movable popup window in which you can insert new aircraft record to database
     */
    public void addAircraftOnAction() {
        if(!addAircraftPopup){
            FXMLLoader root = openMovablePopupWindow("AddAircraftPopup.fxml");
            AddAircraftPopupController controller = root.getController();
            controller.receiveParentModuleObject(this);
            addAircraftPopup = true;
        }
    }

    /**
     * Updates aircraft technical condition and is tanked data to database
     * @param aircraftId aircraft id
     * @param technicalCondition aircraft technical condition
     * @param isTanked is aircraft tanked
     * @return method result message as string
     */
    public String updateTask(int aircraftId, String technicalCondition, String isTanked){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String aircraftUpdate = "UPDATE aircraft SET technical_condition = ?, is_tanked = ? WHERE id_aircraft= ?";

        try{
            if (technicalCondition == null || isTanked == null || isTanked.equals("") || technicalCondition.equals("")) {
                return "ERROR: all fields must be filled!";

            }else{
                PreparedStatement preparedStatementAircraftUpdate = connectDB.prepareStatement(aircraftUpdate);
                preparedStatementAircraftUpdate.setString(1,technicalCondition);
                preparedStatementAircraftUpdate.setString(2, isTanked);
                preparedStatementAircraftUpdate.setInt(3, aircraftId);
                preparedStatementAircraftUpdate.executeUpdate();

                return "Successfully updated user with id number: "+ aircraftId;
            }

        }catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
            return "ERROR: given engine number already exists";
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return "ERROR: something went wrong!";
        }
    }

    /**
     * Passes aircraft id from selected row in aircraft checkup table view to updateTask method and calls it. Method assigned to button "Update" in Aircraft Checkup section, second module - Technical Efficiency
     */
    public void updateTaskOnAction(){

        ObservableList<AircraftTable> aircraftTables = aircraft_checkup_table.getSelectionModel().getSelectedItems();
        for(AircraftTable aircraftTable : aircraftTables){

            String methodResultMessage = updateTask(Integer.parseInt(aircraftTable.getId()), aircraftTable.getTechnical_condition(), aircraftTable.getIs_tanked());

            if(methodResultMessage.contains("ERROR")) queryInfoLabel_Task.setTextFill(Color.web("#bf1c37"));
            else queryInfoLabel_Task.setTextFill(Color.web("#3072ff"));
            queryInfoLabel_Task.setText(methodResultMessage);

        }
        initAndLoadData_TableAircraft();
    }

}