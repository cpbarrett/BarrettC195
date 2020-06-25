package Model;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class CustomerDatabaseModel {
    private static CustomerList customerList;
    private static LocationList locationList;
    private static boolean connected = false;
    private static Connection connect;
    public static void createCustomerList(){
        customerList = new CustomerList();
        locationList = new LocationList();
    }
    public static CustomerList getCustomerList(){
        return customerList;
    }
    public static LocationList getLocationList(){
        return locationList;
    }
    public static boolean isConnected(){
        return connected;
    }
    public static void getConnected() {
        MysqlDataSource dataSource = new MysqlDataSource();
        Properties properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream("src/ResourceBundle/db.properties");
            properties.load(fileInputStream);
            dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
            dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));

            Class.forName(properties.getProperty("MYSQL_DB_DRIVER_CLASS"));
            System.out.println("Connecting to database...");
            connect = dataSource.getConnection();
            ResultSet rs = connect.createStatement().executeQuery(
                    "select customer.customerId, customer.customerName, address.address, city.city, country.country, address.postalCode, address.phone from customer\n" +
                            "inner join address on address.addressId = customer.addressId\n" +
                            "inner join city on city.cityId = address.cityId\n" +
                            "inner join country on city.countryId = country.countryId;"
            );
            while(rs.next()){
                CustomerDatabaseModel.getCustomerList().addCustomer(new Customer(
                        Integer.parseInt(rs.getString("customerId")),
                        rs.getString("customerName"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("country"),
                        Integer.parseInt(rs.getString("postalCode")),
                        rs.getString("phone")
                ));
            }
            rs = connect.createStatement().executeQuery(
                    "select city.cityId, city.city, country.country from city\n" +
                            "inner join country on city.countryId = country.countryId;"
            );
            while (rs.next()){
                locationList.addCity(new City(
                        Integer.parseInt(rs.getString("cityId")),
                        rs.getString("city"),
                        rs.getString("country")
                ));
            }
            System.out.println("Connection Successful!");
            connected = true;
            Customer sample = new Customer(customerList.getAllCustomers().size()+1,"Jose Cuervo",
                    "123 Calle","Mexico City", "Mexico", 10000, "000-0000");
            insertNewCustomer(sample);
            fileInputStream.close();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
    public static void insertNewCustomer(Customer customer){
        customerList.addCustomer(customer);
        try {
            final String insertAddress = "INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy) VALUES\n" +
                    "('" + customer.getAddress() + "' , ' ', " + locationList.lookupCity(customer.getCity()).getCityId() + ", " +
                    customer.getPostalCode() + ", '" + customer.getPhoneNumber() + "', now(), 'U07Stq', 'U07Stq');";
            PreparedStatement ps = connect.prepareStatement(insertAddress);
            ps.execute();
            ps = connect.prepareStatement("SELECT LAST_INSERT_ID() FROM address");
            ResultSet rs = ps.executeQuery();
            rs.next();
            final String insertCustomer = "INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdateBy) VALUES\n" +
            "('" + customer.getCustomerName() + "', " + Integer.parseInt(rs.getString(1)) + ", 1, now(), 'U07Stq', 'U07Stq');";
            ps = connect.prepareStatement(insertCustomer);
            ps.execute();
        } catch (SQLException ex) { //logging?
            ex.printStackTrace();
        }
    }
    public static void deleteCustomerDB(Customer customer){
        customerList.deleteCustomer(customer);
        final String sqlDelete = "DELETE FROM customer WHERE customerId = " + customer.getId() + ";";
        try {
            PreparedStatement ps = connect.prepareStatement(sqlDelete);
            ps.execute();
        } catch (SQLException ex) { //logging?
            ex.printStackTrace();
        }
    }
    public static void updateCustomerDB(Customer customer){
        customerList.updateCustomer(customer.getId()-1, customer);
        try {
            String sql = "UPDATE address SET " +
                    "address = '" + customer.getAddress() + "', " +
                    "cityId = " + locationList.lookupCity(customer.getCity()).getCityId() + ", " +
                    "postalCode = " + customer.getPostalCode() + ", " +
                    "phone = '" + customer.getPhoneNumber() + "', " +
                    "lastUpdateBy = 'U07Stq'" +
                   "WHERE addressId = " + customer.getId() + ";";
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.execute();
            ps = connect.prepareStatement("SELECT LAST_INSERT_ID() FROM address");
            ResultSet rs = ps.executeQuery();
            rs.next();
            sql = "UPDATE customer SET " +
                    "customerName = '" + customer.getCustomerName() + "', " +
                    "addressId = " + Integer.parseInt(rs.getString(1)) + ", " +
                    "active = 1, createDate = now(), createdBy = 'U07Stq', lastUpdateBy = 'U07Stq'" +
                    "WHERE customerId = " + customer.getId() + ";";
            ps = connect.prepareStatement(sql);
            ps.execute();
        } catch (SQLException ex) { //logging?
            ex.printStackTrace();
        }
    }
}
