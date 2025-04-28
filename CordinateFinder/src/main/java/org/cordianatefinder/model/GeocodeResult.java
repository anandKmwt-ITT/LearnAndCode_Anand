package org.cordianatefinder.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeocodeResult {
    @JsonProperty("lat")
    private String latitude;

    @JsonProperty("lon")
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
