package model;

public class City {
    private final int cityId;
    private final String cityName;
    private final String country;

    public City(int cityId, String cityName, String country){
        this.cityId = cityId;
        this.cityName = cityName;
        this.country = country;
    }
    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCountry() {
        return country;
    }

}
