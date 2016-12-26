package ru.startandroid.retrofit.Model.routes;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Flight {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("itineraryDTO")
    @Expose
    private ItineraryDTO itineraryDTO;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItineraryDTO getItineraryDTO() {
        return itineraryDTO;
    }

    public void setItineraryDTO(ItineraryDTO itineraryDTO) {
        this.itineraryDTO = itineraryDTO;
    }

}