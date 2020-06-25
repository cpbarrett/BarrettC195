package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LocationList {
    private ObservableList<City> cityList;

    public LocationList() {
        this.cityList = FXCollections.observableArrayList();
    }
    public void addCity(City city){
        cityList.add(city);
    }
    public City lookupCity(String cityName){
        ObservableList<City> match = FXCollections.observableArrayList();
        for (City city : cityList) if(city.getCityName().contains(cityName)) match.add(city);
            return match.get(0);
    }
    public ObservableList<City> getCityList(){
        return cityList;
    }
}
