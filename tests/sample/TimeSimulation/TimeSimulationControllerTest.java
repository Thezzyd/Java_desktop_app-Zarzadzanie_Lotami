package sample.TimeSimulation;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import sample.DatabaseFiles.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.Assert.*;

/**
 * Test class for TimeSimulationController
 * @author Daniel Czyz
 * @version 1.0.1 29/05/2021
 */
@RunWith(JUnitParamsRunner.class)
public class TimeSimulationControllerTest {

    private TimeSimulationController timeSimulationController;

    @Before
    public void setUp() throws Exception {
        timeSimulationController = new TimeSimulationController();
    }

    @Test
    @Parameters({
            "2022-08-21, 10:00:00",
            "2022-12-28, 12:15:30",
            "2030-01-01, 23:30:00",
            "2000-11-21, 00:01:00"
    })
    public void insertTimeValidTest(String date, String time) {
        timeSimulationController.updateTime(date, time);

        String selectSql = "SELECT * FROM time_simulation WHERE date = ? AND time = ? ";

        try {
            DatabaseConnection databaseConnection = new DatabaseConnection();
            Connection connection = databaseConnection.getConnection();

            PreparedStatement statement = connection.prepareStatement(selectSql);
            statement.setString(1, date);
            statement.setString(2, time);
            ResultSet resultSet = statement.executeQuery();

            assertTrue(resultSet.next());

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            fail();
        }
    }

    @Test(expected = AssertionError.class)
    @Parameters({
            "20220821A, 10:00:00",
            "2022-26-11, 12:15:30",
            "2030-60-28, 23:30:00",
            "2000-11-21, 33:44:220",
            "TAK, 33:44:220"
    })
    public void insertTimeInvalidTest(String date, String time) {
        timeSimulationController.updateTime(date, time);

        String selectSql = "SELECT * FROM time_simulation WHERE date = ? AND time = ? ";

        try {
            DatabaseConnection databaseConnection = new DatabaseConnection();
            Connection connection = databaseConnection.getConnection();

            PreparedStatement statement = connection.prepareStatement(selectSql);
            statement.setString(1, date);
            statement.setString(2, time);
            ResultSet resultSet = statement.executeQuery();

            assertTrue(resultSet.next());

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            fail();
        }
    }

}