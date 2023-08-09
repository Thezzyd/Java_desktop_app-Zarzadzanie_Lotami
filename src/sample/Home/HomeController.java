package sample.Home;

import sample.DatabaseFiles.DatabaseConnection;
import sample.LoginSystem.UserSession;
import sample.ParentScene.ModuleParent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller class for Home screen loaded into Parent screen
 * @author Daniel Czyz
 * @version 1.0.1 29/05/2021
 */
public class HomeController extends ModuleParent {

    /**
     * Content of this method is executed when instance of HomeController class is loaded
     * @param location location
     * @param resource resource
     */
    @Override
    public void initialize(URL location, ResourceBundle resource){
        rootPane.setOpacity(0);
        makeFadeInTransition(rootPane);

        fillHourBtnObjArray();
        getPresentDate();
        loadWorkSchedule();
    }

    /**
     * Selects work schedule for given user, week and year (week and year are evaluated automatically)
     * @param userId user id in database
     * @return object containing work schedule data pulled from database
     */
    public ResultSet selectWorkSchedule(int userId){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String workScheduleSelect = "SELECT * FROM work_schedule WHERE id_user= ? AND year= ? AND week= ?";

        try {
            PreparedStatement preparedStatementWorkScheduleSelect = connectDB.prepareStatement(workScheduleSelect);
            preparedStatementWorkScheduleSelect.setInt(1, userId);
            preparedStatementWorkScheduleSelect.setInt(2, actuallyOperatingYear);
            preparedStatementWorkScheduleSelect.setInt(3, actuallyOperatingWeek);
            ResultSet queryResult = preparedStatementWorkScheduleSelect.executeQuery();

            return queryResult;

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            return null;
        }
    }

    /**
     * Loads work schedule for logged in user, lunched in initialize method.
     */
    public void loadWorkSchedule(){
        resetHourButtonsOnAction();

        try {

            int userId = UserSession.getInstance(0,"","","","",new int[]{}).getId();

            ResultSet workSchedule = selectWorkSchedule(userId);

            while(workSchedule.next())
            {
                readWeekDayHoursToButtons(workSchedule, true);
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
     * Changes week for shown work schedule in lunched application from current to next in order. Method assigned to button "Next week" in application In Home->Work schedule section
     */
    public void nextWeekOnAction(){
        actuallyOperatingWeek += 1;
        if (actuallyOperatingWeek >= 53) {
            actuallyOperatingYear += 1;
            actuallyOperatingWeek = 1;
        }
        setScheduleDataLabels(actuallyOperatingYear,actuallyOperatingWeek);
        loadWorkSchedule();
        scheduleDataPicker.setValue(null);
    }

    /**
     * Changes week for shown work schedule in lunched application from current to previous in order. Method assigned to button "Previous week" in application In Home->Work schedule section
     */
    public void previousWeekOnAction(){
        actuallyOperatingWeek -= 1;
        if (actuallyOperatingWeek <= 0) {
            actuallyOperatingYear -= 1;
            actuallyOperatingWeek = 52;
        }
        setScheduleDataLabels(actuallyOperatingYear,actuallyOperatingWeek);
        loadWorkSchedule();
        scheduleDataPicker.setValue(null);
    }

    /**
     * Checks how many weeks is in picked year from DataPicker in Home->Work schedule section, and saves result to public variables of this class
     */
    public void getNumberOfWeekFromPickedDataOnAction(){
        LocalDate date = scheduleDataPicker.getValue();

        if(date != null) {
            Locale locale = Locale.UK;
            int weekOfYear = date.get(WeekFields.of(locale).weekOfWeekBasedYear());
            int year = date.getYear();

            actuallyOperatingWeek = weekOfYear;
            actuallyOperatingYear = year;

            setScheduleDataLabels(year, weekOfYear);
            loadWorkSchedule();
        }
    }

}
