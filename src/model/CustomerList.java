package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class CustomerList {
    private final ObservableMap<Integer, Customer> customers;
    public CustomerList(){
        this.customers = FXCollections.observableHashMap();
    }
    public void addCustomer(Customer customer){
        customers.put(customer.getId(), customer);
    }
    public void deleteCustomer(Customer customer){
        customers.remove(customer.getId());
    }
    public void updateCustomer(Customer customer){
        if (customers.containsKey(customer.getId())) {
            customers.replace(customer.getId(), customer);
        } else {
            customers.put(customer.getId(), customer);
        }
    }
    public Customer lookupCustomer(int id){
        return customers.get(id);
    }

    public ObservableList<Customer> getCustomerObservableList() {
        ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();
        customerObservableList.addAll(customers.values());
        return customerObservableList;
    }
}
