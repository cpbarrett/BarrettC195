package Model;

public class City {
    int cityId;
    String cityName;
    String country;

    public City(int cityId, String cityName, String country){
        this.cityId = cityId;
        this.cityName = cityName;
        this.country = country;
    }
    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
