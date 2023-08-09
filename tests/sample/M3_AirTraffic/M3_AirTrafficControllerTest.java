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
 * Test class for M3_AirTrafficController
 * @author Daniel Czyz
 * @version 1.0.1 29/05/2021
 */
@RunWith(JUnitParamsRunner.class)
public class M3_AirTrafficControllerTest {

    private M3_AirTrafficController m3_airTrafficController;

    @Before
    public void setUp() throws Exception {
        m3_airTrafficController = new M3_AirTrafficController();

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String insertFlight = "INSERT INTO flight_schedule(id_flight_schedule, departure_place, departure_date, departure_time, destination, arrival_date, arrival_time, price)" +
                " VALUES (9999,'testCity','2022-01-01','12:00:00','otherTestCity','2022-01-01','15:00:00','299.00')";

        String insertAircraft = "INSERT INTO aircraft(id_aircraft, brand_name, model, engine_number, seats_number) VALUES (9999,'testBrandName','testModel','TEST_ENGINE_NUMBER_01', '100')";

        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(insertFlight);

            statement = connectDB.createStatement();
            statement.executeUpdate(insertAircraft);

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @After
    public void tearDown() throws Exception {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String deleteFlight = "DELETE FROM flight_schedule WHERE id_flight_schedule = 9999";
        String deleteAircraft = "DELETE FROM aircraft WHERE id_aircraft = 9999";

        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(deleteFlight);

            statement = connectDB.createStatement();
            statement.executeUpdate(deleteAircraft);

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }


    @Test
    @Parameters({
            "9999",
            "9999",
            "9999"

    })
    public void deleteFlight(int flightScheduleId) {
        m3_airTrafficController.deleteFlight(flightScheduleId);

        try{
            DatabaseConnection databaseConnection = new DatabaseConnection();
            Connection connection = databaseConnection.getConnection();
            String selectSql = "SELECT * FROM flight_schedule WHERE id_flight_schedule = ?";

            PreparedStatement statement = connection.prepareStatement(selectSql);
            statement.setInt(1, flightScheduleId);
            ResultSet resultSet = statement.executeQuery();

            assertFalse(resultSet.next());

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            fail();
        }
    }

    @Test
    @Parameters({
            "9999, Rzeszow, 2022-01-03, 15:00:00, Warsaw, 2022-01-03, 17:00:00, 229.00",
            "9999, Rzeszow, 2022-07-21, 15:00:00, Bristol, 2022-07-21, 17:35:10, 220.33",
            "9999, Rzeszow, 2022-06-19, 15:10:00, London, 2022-06-19, 17:55:00, 340.00",
            "9999, Rzeszow, 2022-05-23, 06:00:00, Paris, 2022-05-23, 08:30:00, 420.50",
            "9999, Rzeszow, 2022-04-07, 10:00:00, Los Angeles, 2022-04-07, 23:59:59, 2100.99",
            "9999, Rzeszow, 2022-01-03, 08:30:00, Krakow, 2022-01-03, 10:00:00, 100"

    })
    public void updateFlightValidTest(int flightScheduleId, String departurePlace, String departureDate, String departureTime, String destination, String arrivalDate, String arrivalTime, String price) {
        m3_airTrafficController.updateFlight(flightScheduleId, departurePlace, departureDate, departureTime, destination, arrivalDate, arrivalTime, price);

        try{
            DatabaseConnection databaseConnection = new DatabaseConnection();
            Connection connection = databaseConnection.getConnection();
            String selectSql = "SELECT * FROM flight_schedule WHERE id_flight_schedule = ? AND departure_place = ? AND destination = ? AND price = ? AND departure_date=? AND departure_time =? " +
                    "AND arrival_date = ? AND arrival_time =?";

            PreparedStatement statement = connection.prepareStatement(selectSql);
            statement.setInt(1, flightScheduleId);
            statement.setString(2, departurePlace);
            statement.setString(3, destination);
            statement.setString(4, price);
            statement.setString(5, departureDate);
            statement.setString(6, departureTime);
            statement.setString(7, arrivalDate);
            statement.setString(8, arrivalTime);
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
            "9999, 46, 2022-01-03, 15:00:00, Warsaw, 2022-01-03, 17:00:00, 229.00",
            "9999, Rzeszow10, 2022-07-21, 15:00:00, Bristol, 2022-07-21, 17:35:10, 220.33",
            "9999, Rzeszow, 2022-06-19-09, 15:10:00, London, 2022-06-19, 17:55:00, 340.00",
            "9999, Rzeszow, 2022-05-23, 1236:00:780, Paris, 2022-05-23, 08:30:00, 420.50",
            "9999, Rzeszow, 2022-04-07, 10:00:00, Los5 Angeles, 2022-04-07, 23:59:59, 2100.99",
            "9999, Rzeszow, 2022-01-03, 08:30:00, Krakow, -2022-01-03, 10:00:00, 100",
            "9999, Rzeszow, 2022-01-03, 08:30:00, Krakow, 2022-01-03, 10:00:00, fgfg",
            "9999, Rzeszow, 2022-01-03, 08:30:00, Krakow, 2022-01-03, 10:00:00, 45v6"

    })
    public void updateFlightInvalidTest(int flightScheduleId, String departurePlace, String departureDate, String departureTime, String destination, String arrivalDate, String arrivalTime, String price) {
        m3_airTrafficController.updateFlight(flightScheduleId, departurePlace, departureDate, departureTime, destination, arrivalDate, arrivalTime, price);

        try{
            DatabaseConnection databaseConnection = new DatabaseConnection();
            Connection connection = databaseConnection.getConnection();
            String selectSql = "SELECT * FROM flight_schedule WHERE id_flight_schedule = ? AND departure_place = ? AND destination = ? AND price = ? AND departure_date=? AND departure_time =? " +
                    "AND arrival_date = ? AND arrival_time =?";

            PreparedStatement statement = connection.prepareStatement(selectSql);
            statement.setInt(1, flightScheduleId);
            statement.setString(2, departurePlace);
            statement.setString(3, destination);
            statement.setString(4, price);
            statement.setString(5, departureDate);
            statement.setString(6, departureTime);
            statement.setString(7, arrivalDate);
            statement.setString(8, arrivalTime);
            ResultSet resultSet = statement.executeQuery();

            assertTrue(resultSet.next());

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            fail();
        }

    }

    @Test
    @Parameters({
            "9999, 9999, available",
            "9999, 9999, in_flight"

    })
    public void updateTakeOffFlightAndTakeOffAircraftValidTest(int aircraftId, int flightId, String status) {
        m3_airTrafficController.updateTakeOffFlight(aircraftId, flightId);
        m3_airTrafficController.updateTakeOffAircraft(aircraftId, status);

        try{
            DatabaseConnection databaseConnection = new DatabaseConnection();
            Connection connection = databaseConnection.getConnection();
            String selectSqlFlight = "SELECT * FROM flight_schedule WHERE id_flight_schedule = ? AND id_aircraft = ?";

            PreparedStatement statement = connection.prepareStatement(selectSqlFlight);
            statement.setInt(1, flightId);
            statement.setInt(2, aircraftId);
            ResultSet resultSet = statement.executeQuery();

            assertTrue(resultSet.next());

            String selectSqlAircraft = "SELECT * FROM aircraft WHERE id_aircraft = ? AND status = ?";

            statement = connection.prepareStatement(selectSqlAircraft);
            statement.setInt(1, aircraftId);
            statement.setString(2, status);
            resultSet = statement.executeQuery();

            assertTrue(resultSet.next());

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            fail();
        }
    }

    @Test(expected = AssertionError.class)
    @Parameters({
            "9999, 9999, available5",
            "9999, 9999, in flight",
            "9999, 9999, in cos innego",
            "9999, 9999, 444"

    })
    public void updateTakeOffFlightAndTakeOffAircraftInvalidTest(int aircraftId, int flightId, String status) {
        m3_airTrafficController.updateTakeOffFlight(aircraftId, flightId);
        m3_airTrafficController.updateTakeOffAircraft(aircraftId, status);

        try{
            DatabaseConnection databaseConnection = new DatabaseConnection();
            Connection connection = databaseConnection.getConnection();
            String selectSqlFlight = "SELECT * FROM flight_schedule WHERE id_flight_schedule = ? AND id_aircraft = ?";

            PreparedStatement statement = connection.prepareStatement(selectSqlFlight);
            statement.setInt(1, flightId);
            statement.setInt(2, aircraftId);
            ResultSet resultSet = statement.executeQuery();

            assertTrue(resultSet.next());

            String selectSqlAircraft = "SELECT * FROM aircraft WHERE id_aircraft = ? AND status = ?";

            statement = connection.prepareStatement(selectSqlAircraft);
            statement.setInt(1, aircraftId);
            statement.setString(2, status);
            resultSet = statement.executeQuery();

            assertTrue(resultSet.next());

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            fail();
        }
    }

    @Test
    @Parameters({
            "9999, available"

    })
    public void updateLandingFlightValidTest(int aircraftId, String status) {
        m3_airTrafficController.updateLandingFlight(aircraftId, status);

        try{
            DatabaseConnection databaseConnection = new DatabaseConnection();
            Connection connection = databaseConnection.getConnection();
            String selectSql = "SELECT * FROM aircraft WHERE id_aircraft = ? AND status = ?";

            PreparedStatement statement = connection.prepareStatement(selectSql);
            statement.setInt(1, aircraftId);
            statement.setString(2, status);
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
            "9999, available5",
            "9999, in flight",
            "9999, in cos innego",
            "9999, 444"

    })
    public void updateLandingFlightInvalidTest(int aircraftId, String status) {
        m3_airTrafficController.updateLandingFlight(aircraftId, status);

        try{
            DatabaseConnection databaseConnection = new DatabaseConnection();
            Connection connection = databaseConnection.getConnection();
            String selectSql = "SELECT * FROM aircraft WHERE id_aircraft = ? AND status = ?";

            PreparedStatement statement = connection.prepareStatement(selectSql);
            statement.setInt(1, aircraftId);
            statement.setString(2, status);
            ResultSet resultSet = statement.executeQuery();

            assertTrue(resultSet.next());

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            fail();
        }
    }

}