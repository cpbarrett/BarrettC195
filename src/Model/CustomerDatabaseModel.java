package Model;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import main.AlertUser;
import main.Main;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.time.Instant;
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
            fileInputStream.close();
            rs.close();
            Main.setAlarm();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
    public static void makeNewAppointment(Appointment appointment){
        int customerId = appointment.getAssociatedCustomer().getId();
        try{
            final String insertAppointment = "INSERT INTO appointment VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = connect.prepareStatement(insertAppointment);
            ps.setInt(1, appointmentTotal++);
            ps.setInt(2, customerId);
            ps.setInt(3, 1);
            ps.setString(4, appointment.getTitle());
            ps.setString(5, appointment.getDescription());
            ps.setString(6, appointment.getLocation());
            ps.setString(7, appointment.getContact());
            ps.setString(8, appointment.getType());
            ps.setString(9, appointment.getUrl());
            ps.setString(10, appointment.getStartTime());
            ps.setString(11, appointment.getEndTime());
            ps.setTimestamp(12, Timestamp.from(Instant.now()));
            ps.setString(13, "U07Stq");
            ps.setTimestamp(14, Timestamp.from(Instant.now()));
            ps.setString(15, "U07Stq");
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        customerList.lookupCustomer(customerId-1).addAppointment(appointment);
    }
    public static void updateAppointment(Appointment appointment) {
        try {
            final String update = "UPDATE appointment SET title = ?, description = ?, location = ?, contact = ?, type = ?, url = ?, start = ?, end = ?, lastUpdate = ?, lastUpdateBy = ? WHERE appointmentId = ?";
            PreparedStatement ps = connect.prepareStatement(update);
            ps.setString(1,appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getContact());
            ps.setString(5, appointment.getType());
            ps.setString(6, appointment.getUrl());
            ps.setTimestamp(7, Timestamp.valueOf(appointment.getStartTime()));
            ps.setTimestamp(8, Timestamp.valueOf(appointment.getEndTime()));
            ps.setTimestamp(9, Timestamp.from(Instant.now()));
            ps.setString(10, "U07Stq");
            ps.setInt(11, appointment.getId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

