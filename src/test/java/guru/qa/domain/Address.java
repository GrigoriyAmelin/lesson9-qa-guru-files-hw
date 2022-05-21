package guru.qa.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Address {
    public String street;
    public Integer house;

    @SerializedName("favorite_music")
    public List<String> favoriteMusic;
}
