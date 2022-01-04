package com.broadenway.ureasolution.dto;

import java.math.BigDecimal;

public class MapBound {

    private Double westBound;
    private Double southBound;
    private Double eastBound;
    private Double northBound;

    private MapBound() {
    }

    private MapBound(Double westBound, Double southBound, Double eastBound,
        Double northBound) {
        this.westBound = westBound;
        this.southBound = southBound;
        this.eastBound = eastBound;
        this.northBound = northBound;
    }

    public static MapBound from(String westBound, String southBound, String eastBound,
        String northBound) {
        return new MapBound(Double.parseDouble(westBound), Double.parseDouble(southBound),
            Double.parseDouble(eastBound), Double.parseDouble(northBound));
    }

    public boolean isCover(Double latitude, Double longitude) {
        return isBetweenLatitude(latitude) && isBetweenLongitude(longitude);
    }

    private boolean isBetweenLatitude(Double convertLatitude) {
        return convertLatitude >= southBound && convertLatitude <= northBound;
    }

    private boolean isBetweenLongitude(Double convertLongitude) {
        return convertLongitude >= westBound && convertLongitude <= eastBound;
    }
}
