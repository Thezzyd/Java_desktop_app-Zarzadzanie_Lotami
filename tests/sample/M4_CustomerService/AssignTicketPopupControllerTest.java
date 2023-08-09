package sample.M4_CustomerService;

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
 * Test class for AssignTicketPopupController
 * @author Daniel Czyz
 * @version 1.0.1 29/05/2021
 */
@RunWith(JUnitParamsRunner.class)
public class AssignTicketPopupControllerTest {

    private  AssignTicketPopupController assignTicketPopupController;

    @Before
    public void setUp() throws Exception {
        assignTicketPopupController = new AssignTicketPopupController();

        try{

            DatabaseConnection databaseConnection = new DatabaseConnection();
            Connection connection = databaseConnection.getConnection();

            String insertSql = "INSERT INTO flight_schedule(id_flight_schedule, departure_place, departure_date, departure_time, destination, arrival_date, arrival_time, price)" +
                    " VALUES (9999,'testCity','2022-01-01','12:00:00','testCity2','2022-01-01','15:00:00','299.00')";

            Statement statement = connection.createStatement();
            statement.executeUpdate(insertSql);

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @After
    public void tearDown() throws Exception {
        try{

            DatabaseConnection databaseConnection = new DatabaseConnection();
            Connection connection = databaseConnection.getConnection();

            String deleteSql = "DELETE FROM flight_schedule WHERE id_flight_schedule = 9999";

            Statement statement = connection.createStatement();
            statement.executeUpdate(deleteSql);

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @Test
    @Parameters({
            "9999, Jan, Kowalski, janK98@gmail.com, 123 123 123",
            "9999, Robert, Malinowski, fggK98@gmail.com, 123123143",
            "9999, Marek, Nazwisko, dfdfdf@gmail.com, 666555444",
            "9999, John, Ddddddd, er456gg@gmail.com, 999 999 999",
            "9999, Stefan, Super, ghghgh99o9@wp.com, 767676761"

    })
    public void insertTicketValidTest(int flightScheduleId, String firstname, String lastname, String email, String phoneNumber){
        assignTicketPopupController.insertTicket(flightScheduleId, firstname, lastname, email, phoneNumber);

        try{
            DatabaseConnection databaseConnection = new DatabaseConnection();
            Connection connection = databaseConnection.getConnection();

            String selectSql = "SELECT * FROM ticket WHERE id_flight_schedule = ? AND firstname = ? AND lastname = ? AND email = ? AND phone_number = ?";

            PreparedStatement statement = connection.prepareStatement(selectSql);
            statement.setInt(1, flightScheduleId);
            statement.setString(2, firstname);
            statement.setString(3, lastname);
            statement.setString(4, email);
            statement.setString(5, phoneNumber);
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
            "9999, Jan5, Kowalski, janK98@gmail.com, 123 123 123",
            "9999, Robert, 6Malinowski, fggK98gmail.com, 123123143",
            "9999, Marek, Nazwisko, dfdfdf@gmail, 66655544",
            "9999, John, Ddddddd, er456gg@gmail.com, 999 99 999",
            "9999, Stefan, Super, ghghgh99o9wp.com, 767676761",
            "9999, Stefan, Super, ghghgh99o9wp.com, 76767676155"

    })
    public void insertTicketInvalidTest(int flightScheduleId, String firstname, String lastname, String email, String phoneNumber){
        assignTicketPopupController.insertTicket(flightScheduleId, firstname, lastname, email, phoneNumber);

        try{
            DatabaseConnection databaseConnection = new DatabaseConnection();
            Connection connection = databaseConnection.getConnection();

            String selectSql = "SELECT * FROM ticket WHERE id_flight_schedule = ? AND firstname = ? AND lastname = ? AND email = ? AND phone_number = ?";

            PreparedStatement statement = connection.prepareStatement(selectSql);
            statement.setInt(1, flightScheduleId);
            statement.setString(2, firstname);
            statement.setString(3, lastname);
            statement.setString(4, email);
            statement.setString(5, phoneNumber);
            ResultSet resultSet = statement.executeQuery();

            assertTrue(resultSet.next());

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            fail();
        }
    }
}