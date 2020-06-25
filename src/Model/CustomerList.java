package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CustomerList {
    private ObservableList<Customer> customerObservableList;
    public CustomerList(){
        this.customerObservableList = FXCollections.observableArrayList();
    }
    public boolean addCustomer(Customer customer){
        return customerObservableList.add(customer);
    }
    public boolean deleteCustomer(Customer customer){
        if (customerObservableList.contains(customer)){
            customerObservableList.remove(customer);
            return true;
        } else {
            return false;
        }
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
    public ObservableList<Customer> lookupCustomer(String name){
        ObservableList<Customer> matchingCustomers = FXCollections.observableArrayList();
        for(Customer match : customerObservableList){
            if (match.getCustomerName().toLowerCase().contains(name.toLowerCase())){
                matchingCustomers.add(match);
            }
        }
        return matchingCustomers;
    }
    public ObservableList<Customer> getAllCustomers() {
        return customerObservableList;
    }
}
