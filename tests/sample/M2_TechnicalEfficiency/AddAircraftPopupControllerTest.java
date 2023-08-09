package sample.M2_TechnicalEfficiency;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
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
 * Test class for AddAircraftPopupController
 * @author Daniel Czyz
 * @version 1.0.1 29/05/2021
 */
@RunWith(JUnitParamsRunner.class)
public class AddAircraftPopupControllerTest {

    private AddAircraftPopupController addAircraftPopupController;

    @Before
    public void setUp() throws Exception {
        addAircraftPopupController = new AddAircraftPopupController();

    }


    @Test
    @Parameters({
            "Ryanair, 787 Dreamliner, G189X001, 186",
            "EasyJet, 777 X , 123Ha-Xb34, 300",
            "American Airlines, Airbus219, GaH-987, 20",
            "Delta Airlines, testModel-X0, HK100X, 500",
            "Jet2, superr, 99SuPER1Z, 1000"
    })
    public void insertAircraftValidTest(String brandName, String model, String engineNumber, String seatsNumber) {
        addAircraftPopupController.insertAircraft(brandName,model, engineNumber,seatsNumber);

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String sqlSelect = "SELECT * FROM aircraft WHERE brand_name = ? AND model = ? AND engine_number = ? AND seats_number = ?";
        String sqlDelete = "DELETE FROM aircraft WHERE id_aircraft = ?";

        try{
            PreparedStatement statement = connectDB.prepareStatement(sqlSelect);
            statement.setString(1, brandName);
            statement.setString(2, model);
            statement.setString(3, engineNumber);
            statement.setString(4, seatsNumber);
            ResultSet resultSet = statement.executeQuery();

            assertTrue(resultSet.next());

            statement = connectDB.prepareStatement(sqlDelete);
            statement.setInt(1, resultSet.getInt("id_aircraft"));
            statement.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            fail();
        }

    }


    @Test(expected = AssertionError.class)
    @Parameters({
            "Ryanair, 787 Dreamliner, G189X001, *186",
            "EasyJet, 777 X , ffs-Xb34, 3j00",
            "American Airlines, Airbus219, GaH-987, 4090909909090909990909090",
            "Delta Airlines, df-X0, HK100X, 50^0",
            "Jet2, superr, d, 1000O"
    })
    public void insertAircraftInvalidTest(String brandName, String model, String engineNumber, String seatsNumber) {
        addAircraftPopupController.insertAircraft(brandName,model, engineNumber,seatsNumber);

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String sqlSelect = "SELECT * FROM aircraft WHERE brand_name = ? AND model = ? AND engine_number = ? AND seats_number = ?";
        String sqlDelete = "DELETE FROM aircraft WHERE id_aircraft = ?";

        try{
            PreparedStatement statement = connectDB.prepareStatement(sqlSelect);
            statement.setString(1, brandName);
            statement.setString(2, model);
            statement.setString(3, engineNumber);
            statement.setString(4, seatsNumber);
            ResultSet resultSet = statement.executeQuery();

            assertTrue(resultSet.next());

            statement = connectDB.prepareStatement(sqlDelete);
            statement.setInt(1, resultSet.getInt("id_aircraft"));
            statement.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
            fail();
        }

    }
}