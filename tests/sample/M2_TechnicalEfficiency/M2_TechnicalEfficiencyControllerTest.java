package sample.M2_TechnicalEfficiency;

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
 * Test class for M2_TechnicalEfficiencyController
 * @author Daniel Czyz
 * @version 1.0.1 29/05/2021
 */
@RunWith(JUnitParamsRunner.class)
public class M2_TechnicalEfficiencyControllerTest {

    private M2_TechnicalEfficiencyController m2_technicalEfficiencyController;

    @Before
    public void setUp() throws Exception {
        m2_technicalEfficiencyController = new M2_TechnicalEfficiencyController();

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String sqlInsert = "INSERT INTO aircraft(id_aircraft, brand_name, model, engine_number, seats_number) VALUES (9999,'testBrand','testModel','TEST_ENGINE_NUMBER_01','100')";

        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(sqlInsert);

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            fail();
        }
    }

    @After
    public void tearUp() throws Exception{
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String sqlDelete = "DELETE FROM aircraft WHERE id_aircraft = 9999";

        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(sqlDelete);

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            fail();
        }
    }

    @Test
    @Parameters({
            "9999",
            "9999",
            "9999"
    })
    public void deleteAircraft(int aircraftId) {
        m2_technicalEfficiencyController.deleteAircraft(aircraftId);

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String sqlSelect = "SELECT * FROM aircraft WHERE id_aircraft = ?";

        try{
            PreparedStatement statement = connectDB.prepareStatement(sqlSelect);
            statement.setInt(1, aircraftId);
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
            "9999, Ryanair, 787 Dreamliner, G189X001, 186, available",
            "9999, EasyJet, 777 X , 123Ha-Xb34, 300, available",
            "9999, American Airlines, Airbus219, GaH-987, 20, available",
            "9999, Delta Airlines, testModel-X0, HK100X, 500, in_flight",
            "9999, Jet2, superr, 99SuPER1Z, 1000, in_flight"
    })
    public void updateAircraftValidTest(int aircraftId, String brandName, String model, String engineNumber, String seatsNumber, String status) {
        m2_technicalEfficiencyController.updateAircraft(aircraftId, brandName, model ,engineNumber, seatsNumber, status);

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String sqlSelect = "SELECT * FROM aircraft WHERE id_aircraft = ? AND brand_name = ? AND engine_number = ? AND model = ? AND seats_number = ? AND status = ?";

        try{
            PreparedStatement statement = connectDB.prepareStatement(sqlSelect);
            statement.setInt(1, aircraftId);
            statement.setString(2, brandName);
            statement.setString(3, engineNumber);
            statement.setString(4, model);
            statement.setString(5, seatsNumber);
            statement.setString(6, status);
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
            "9999, Ryanair, 787 Dreamliner, G189X001, 18f6, available",
            "9999, EasyJet, 777 X , 123Ha-Xb34, 300o, available",
            "9999, American Airlines, Airbus219, GaH-987, 23453453453453453453450, available",
            "9999, Delta Airlines, testModel-X0, HK100X, i500, in_flight",
            "9999, Jet2, superr, , 1000, in_flight",
            "9999, Jet2, superr, , 1000, tak",
            "9999, Jet2, superr, , 1000, 4566"
    })
    public void updateAircraftInvalidTest(int aircraftId, String brandName, String model, String engineNumber, String seatsNumber, String status) {
        m2_technicalEfficiencyController.updateAircraft(aircraftId, brandName, model ,engineNumber, seatsNumber, status);

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String sqlSelect = "SELECT * FROM aircraft WHERE id_aircraft = ? AND brand_name = ? AND engine_number = ? AND model = ? AND seats_number = ? AND status = ?";

        try{
            PreparedStatement statement = connectDB.prepareStatement(sqlSelect);
            statement.setInt(1, aircraftId);
            statement.setString(2, brandName);
            statement.setString(3, engineNumber);
            statement.setString(4, model);
            statement.setString(5, seatsNumber);
            statement.setString(6, status);
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
            "9999, operational, true",
            "9999, not_operational, false",
            "9999, for_check_up, true",
            "9999, operational, false"

    })
    public void updateTaskValidTest(int aircraftId, String technicalCondition, String isTanked) {
        m2_technicalEfficiencyController.updateTask(aircraftId, technicalCondition, isTanked);

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String sqlSelect = "SELECT * FROM aircraft WHERE id_aircraft = ? AND technical_condition = ? AND is_tanked = ?";

        try{
            PreparedStatement statement = connectDB.prepareStatement(sqlSelect);
            statement.setInt(1, aircraftId);
            statement.setString(2, technicalCondition);
            statement.setString(3, isTanked);
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
            "9999, operationall, true",
            "9999, not_operational, tak",
            "9999, for_check_up, yes",
            "9999, 45, false",
            "9999, cosInnego, false"

    })
    public void updateTaskInvalidTest(int aircraftId, String technicalCondition, String isTanked) {
        m2_technicalEfficiencyController.updateTask(aircraftId, technicalCondition, isTanked);

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String sqlSelect = "SELECT * FROM aircraft WHERE id_aircraft = ? AND technical_condition = ? AND is_tanked = ?";

        try{
            PreparedStatement statement = connectDB.prepareStatement(sqlSelect);
            statement.setInt(1, aircraftId);
            statement.setString(2, technicalCondition);
            statement.setString(3, isTanked);
            ResultSet resultSet = statement.executeQuery();

            assertTrue(resultSet.next());

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            fail();
        }

    }
}