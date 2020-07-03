package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CustomerList {
    private final ObservableList<Customer> customerObservableList;
    public CustomerList(){
        this.customerObservableList = FXCollections.observableArrayList();
    }
    public void addCustomer(Customer customer){
        customerObservableList.add(customer);
    }
    public void deleteCustomer(Customer customer){
        customerObservableList.remove(customer);
    }
    public void updateCustomer(int id, Customer customer){
        if (customerObservableList.get(id).getId()-1 == id) {
            customerObservableList.set(id, customer);
        } else {
            customerObservableList.add(customer);
        }
    }
    public Customer lookupCustomer(int id){
        return customerObservableList.get(id);
    }

    public ObservableList<Customer> getAllCustomers() {
        return customerObservableList;
    }
}
