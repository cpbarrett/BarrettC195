package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CustomerList {
    private ObservableList<Customer> customerObservableList;
    public CustomerList(){
        this.customerObservableList = FXCollections.observableArrayList();
    }
    public ObservableList<Customer> getCustomerObservableList() {
        return this.customerObservableList;
    }
    public boolean addCustomer(Customer customer){
        return this.customerObservableList.add(customer);
    }
    public Customer deleteCustomer(int id){
        return this.customerObservableList.remove(id);
    }
    public void updateCustomer(int id, Customer customer){
        this.customerObservableList.add(id, customer);
    }
    public Customer lookupCustomer(int id){
        return this.customerObservableList.get(id);
    }
    public ObservableList<Customer> lookupCustomer(Customer customer){
        ObservableList<Customer> matchingCustomers = FXCollections.observableArrayList();
        for(Customer match : this.customerObservableList){
            if (match.getFirstName().contains(customer.getFirstName()) || match.getLastName().contains(customer.getLastName())){
                matchingCustomers.add(match);
            }
        }
        return matchingCustomers;
    }

}
