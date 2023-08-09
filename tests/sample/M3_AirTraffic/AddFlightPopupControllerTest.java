package sample.M3_AirTraffic;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import sample.DatabaseFiles.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.Assert.*;

/**
 * Test class for AddFlightPopupController
 * @author Daniel Czyz
 * @version 1.0.1 29/05/2021
 */
@RunWith(JUnitParamsRunner.class)
public class AddFlightPopupControllerTest {

    private AddFlightPopupController addFlightPopupController;

    @Before
    public void setUp() throws Exception {
        addFlightPopupController = new AddFlightPopupController();
    }


    @Test
    @Parameters({
            "Rzeszow, 2022-01-03, 15:00:00, Warsaw, 2022-01-03, 17:00:00, 229.00",
            "Rzeszow, 2022-07-21, 15:00:00, Bristol, 2022-07-21, 17:35:10, 220.33",
            "Rzeszow, 2022-06-19, 15:10:00, London, 2022-06-19, 17:55:00, 340.00",
            "Rzeszow, 2022-05-23, 06:00:00, Paris, 2022-05-23, 08:30:00, 420.50",
            "Rzeszow, 2022-04-07, 10:00:00, Los Angeles, 2022-04-07, 23:59:59, 2100.99",
            "Rzeszow, 2022-01-03, 08:30:00, Krakow, 2022-01-03, 10:00:00, 100"

    })
    public void insertFlightValidTest(String departurePlace, String departureDate, String departureTime, String destination, String arrivalDate, String arrivalTime, String price) {
        addFlightPopupController.insertFlight(departurePlace, departureDate, departureTime, destination, arrivalDate, arrivalTime,price);

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String sqlSelect = "SELECT * FROM flight_schedule WHERE departure_place = ? AND departure_date = ? AND destination = ? AND price = ? AND departure_time = ? AND arrival_time = ?";

        try{
            PreparedStatement statement = connectDB.prepareStatement(sqlSelect);
            statement.setString(1, departurePlace);
            statement.setString(2, departureDate);
            statement.setString(3, destination);
            statement.setString(4, price);
            statement.setString(5, departureTime);
            statement.setString(6, arrivalTime);
            ResultSet resultSet = statement.executeQuery();

            assertTrue(resultSet.next());

            String sqlDelete = "DELETE FROM flight_schedule WHERE id_flight_schedule = ?";
            PreparedStatement statement2 = connectDB.prepareStatement(sqlDelete);
            statement2.setInt(1, resultSet.getInt("id_flight_schedule"));
            statement2.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            fail();
        }
    }

    @Test(expected = AssertionError.class)
    @Parameters({
            "46, 2022-01-03, 15:00:00, Warsaw, 2022-01-03, 17:00:00, 229.00",
            "Rzeszow10, 2022-07-21, 15:00:00, Bristol, 2022-07-21, 17:35:10, 220.33",
            "Rzeszow, 2022-06-19-09, 15:10:00, London, 2022-06-19, 17:55:00, 340.00",
            "Rzeszow, 2022-05-23, 1236:00:780, Paris, 2022-05-23, 08:30:00, 420.50",
            "Rzeszow, 2022-04-07, 10:00:00, Los5 Angeles, 2022-04-07, 23:59:59, 2100.99",
            "Rzeszow, 2022-01-03, 08:30:00, Krakow, -2022-01-03, 10:00:00, 100",
            "Rzeszow, 2022-01-03, 08:30:00, Krakow, 2022-01-03, 10:00:00, fgfg",
            "Rzeszow, 2022-01-03, 08:30:00, Krakow, 2022-01-03, 10:00:00, 45v6"

    })
    public void insertFlightInalidTest(String departurePlace, String departureDate, String departureTime, String destination, String arrivalDate, String arrivalTime, String price) {
        addFlightPopupController.insertFlight(departurePlace, departureDate, departureTime, destination, arrivalDate, arrivalTime,price);

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String sqlSelect = "SELECT * FROM flight_schedule WHERE departure_place = ? AND departure_date = ? AND destination = ? AND price = ? AND departure_time = ? AND arrival_time = ?";

        try{
            PreparedStatement statement = connectDB.prepareStatement(sqlSelect);
            statement.setString(1, departurePlace);
            statement.setString(2, departureDate);
            statement.setString(3, destination);
            statement.setString(4, price);
            statement.setString(5, departureTime);
            statement.setString(6, arrivalTime);
            ResultSet resultSet = statement.executeQuery();

            assertTrue(resultSet.next());

            String sqlDelete = "DELETE FROM flight_schedule WHERE id_flight_schedule = ?";
            PreparedStatement statement2 = connectDB.prepareStatement(sqlDelete);
            statement2.setInt(1, resultSet.getInt("id_flight_schedule"));
            statement2.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            fail();
        }
    }
}