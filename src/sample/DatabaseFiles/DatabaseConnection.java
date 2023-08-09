package sample.DatabaseFiles;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Solves connection with database issues
 * @author Daniel Czyz
 * @version 1.0.1 29/05/2021
 */
public class DatabaseConnection {
    private Connection databaseLink;

    /**
     * Creates a connection with database named "zl" using "root" username with no password
     * @return ready connection object to be used
     */
    public Connection getConnection(){
        String databaseName = "zl";
        String databaseUser = "root";
        String databasePassword = "";
        String url = "jdbc:mysql://localhost:3306/" + databaseName + "?autoReconnect=true&useSSL=false";

        try{
            Class.forName("com.mysql.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

        return databaseLink;
    }

    /**
     * Creates a connection with custom database
     * @param databaseName database name
     * @param databaseUser username used to create connection
     * @param databasePassword password to access database
     * @return ready connection object to be used
     */
    public Connection getConnection( String databaseName, String databaseUser, String databasePassword){
        String url = "jdbc:mysql://localhost:3306/" + databaseName + "?autoReconnect=true&useSSL=false";

        try{
            Class.forName("com.mysql.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

        return databaseLink;
    }
}


