package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CustomerDatabase {
    private static CustomerList customerList = new CustomerList();
    public static CustomerList getCustomerList(){
        return customerList;
    }
    public static Connection getConnected() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:myslq", "test", "test");

        return connection;
    }
}

