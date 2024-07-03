package entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Hotel extends BaseEntity {
    private String name;
    private String phone;
    private String email;
    private int stars;
    private String addressLine;
    private String country;
    private String city;
    private String district;
    private List<String> amenities;
    private List<String> boardTypes;
    private List<Season> seasons;

    public List<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public List<String> getBoardTypes() {
        return boardTypes;
    }

    public void setBoardTypes(List<String> boardTypes) {
        this.boardTypes = boardTypes;
    }

    public void removeSeasonById(int seasonId) {
        if (seasons != null) {
            Iterator<Season> iterator = seasons.iterator();
            while (iterator.hasNext()) {
                Season season = iterator.next();
                if (Objects.equals(season.getId(), seasonId)) {
                    iterator.remove();
                    return;
                }
            }
        }
    }

    public void addSeason(Season newSeason) {
        if (seasons == null) {
            seasons = new ArrayList<>();
        }
        seasons.add(newSeason);
    }

    public void updateSeason(Season updatedSeason) {
        if (seasons != null) {
            for (int i = 0; i < seasons.size(); i++) {
                Season season = seasons.get(i);
                if (Objects.equals(season.getId(), updatedSeason.getId())) {
                    seasons.set(i, updatedSeason);
                    return;
                }
            }
        }
    }

    public Season matchSeasonById(int seasonId) {
        if (seasons != null) {
            for (Season season : seasons) {
                if (Objects.equals(season.getId(), seasonId)) {
                    return season;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", stars=" + stars +
                ", addressLine='" + addressLine + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", amenities=" + amenities +
                ", boardTypes=" + boardTypes +
                '}';
    }
}
