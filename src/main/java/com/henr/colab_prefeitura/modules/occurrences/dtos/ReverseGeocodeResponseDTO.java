package com.henr.colab_prefeitura.modules.occurrences.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReverseGeocodeResponseDTO {
    public Address address;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Address {
        public String state;
        public String country;

        @JsonProperty("city")
        public String city;

        @JsonProperty("town")
        public String town;

        @JsonProperty("municipality")
        public String municipality;
    }
}
