package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CustomerDatabaseModel {
    private static CustomerList customerList;
    public static void createCustomerList(){
        customerList = new CustomerList();
    }
    public static CustomerList getCustomerList(){
        return customerList;
    }
    public static Connection getConnected() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:myslq", "test", "test");

        return connection;
    }
}

