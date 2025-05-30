package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Brandon Jones
 * - Uses JDBC Driver to establish a connection to the database.
 */
public abstract class JDBC {

    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static final String password = "Passw0rd!"; // Password
    public static Connection connection;  // Connection Interface

    /**
     *
     * @return - a newly created connection to the database
     */
    public static Connection openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     *  - Closes the currently open connection to the database
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed");
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     *
     * @return - The currently open connection to the database
     */
    public static Connection getConnection() {
            return connection;
    }

}
