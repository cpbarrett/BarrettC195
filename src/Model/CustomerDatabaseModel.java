package Model;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Properties;

public class CustomerDatabaseModel {
    private static CustomerList customerList;
    private static LocationList locationList;
    private static int appointmentTotal = 1;
    private static boolean connected = false;
    private static Connection connect;
    public static int getAppointmentTotal(){
        return appointmentTotal;
    }
    public static Connection getConnection(){
        return connect;
    }
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
            rs = connect.createStatement().executeQuery(
                    "SELECT * FROM appointment;"
            );
            while (rs.next()){
                customerList.lookupCustomer(Integer.parseInt(rs.getString("customerId"))-1).addAppointment(
                    (new Appointment(
                            Integer.parseInt(rs.getString("appointmentId")),
                            customerList.lookupCustomer(Integer.parseInt(rs.getString("customerId"))-1),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("location"),
                            rs.getString("contact"),
                            rs.getString("type"),
                            rs.getString("url"),
                            rs.getString("start"),
                            rs.getString("end")
                )));
                appointmentTotal++;
            }
            System.out.println("Connection Successful!");
            connected = true;
//            Customer sample = new Customer(customerList.getAllCustomers().size()+1,"Jose Cuervo",
//                    "123 Calle","Mexico City", "Mexico", 10000, "000-0000");
//            insertNewCustomer(sample);
            fileInputStream.close();
            rs.close();
//            String test = "2019-01-01 00:08:00.0";
//            String result = convertToUtcTime(test);
//            System.out.println(result);
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
    public static void makeNewAppointment(Appointment appointment){
        int customerId = appointment.getAssociatedCustomer().getId();
        try{
            final String insertAppointment = "INSERT INTO appointment VALUES" +
                    "(" + appointmentTotal++ + ", " + customerId + ", 1, '" + appointment.getTitle() + "', '" + appointment.getDescription() + "', '" +
                    appointment.getLocation() + "', '" + appointment.getContact() + "', '" + appointment.getType() + "', '" +
                    appointment.getUrl() + "', now(), now(), now(), 'U07Stq', now(), 'U07Stq');";
            PreparedStatement ps = connect.prepareStatement(insertAppointment);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        customerList.lookupCustomer(customerId-1).addAppointment(appointment);
    }
    public static void deleteAppointment(Appointment appointment){
        try {
            final String deleteAppointment = "DELETE FROM appointment WHERE appointmentId = " + appointment.getId() + ";";
            connect.createStatement().execute(deleteAppointment);
        } catch (SQLException e){
            e.printStackTrace();
        }
        appointment.getAssociatedCustomer().deleteAppointment(appointment);
        appointmentTotal--;
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
            ps.close();
            rs.close();
        } catch (SQLException ex) { //logging?
            ex.printStackTrace();
        }
    }
    public static void deleteCustomerDB(Customer customer){
        customerList.deleteCustomer(customer);
        final String sqlDeleteCustomer = "DELETE FROM customer WHERE customerId = " + customer.getId() + ";";
        final String sqlDeleteAddress = "DELETE FROM address WHERE addressId = " + customer.getId() + ";";
        try {
            connect.createStatement().execute(sqlDeleteCustomer);
            connect.createStatement().execute(sqlDeleteAddress);
        } catch (SQLException ex) { //logging?
            ex.printStackTrace();
        }
    }
    public static void updateCustomerDB(Customer customer){
        customerList.updateCustomer(customer.getId()-1, customer);
        try {
            final String sqlUpdateAddress = "UPDATE address SET " +
                    "address = '" + customer.getAddress() + "', " +
                    "cityId = " + locationList.lookupCity(customer.getCity()).getCityId() + ", " +
                    "postalCode = " + customer.getPostalCode() + ", " +
                    "phone = '" + customer.getPhoneNumber() + "', " +
                    "lastUpdateBy = 'U07Stq'" +
                   "WHERE addressId = " + customer.getId() + ";";
            PreparedStatement ps = connect.prepareStatement(sqlUpdateAddress);
            ps.execute();
            ps = connect.prepareStatement("SELECT LAST_INSERT_ID() FROM address");
            ResultSet rs = ps.executeQuery();
            rs.next();
            final String sqlUpdateCustomer = "UPDATE customer SET " +
                    "customerName = '" + customer.getCustomerName() + "', " +
                    "addressId = " + Integer.parseInt(rs.getString(1)) + ", " +
                    "active = 1, createDate = now(), createdBy = 'U07Stq', lastUpdateBy = 'U07Stq'" +
                    "WHERE customerId = " + customer.getId() + ";";
            ps = connect.prepareStatement(sqlUpdateCustomer);
            ps.execute();
            ps.close();
            rs.close();
        } catch (SQLException ex) { //logging?
            ex.printStackTrace();
        }
    }
}

