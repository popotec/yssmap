package com.broadenway.ureasolution.dto;

import java.math.BigDecimal;

public class MapBound {

    private double westBound;
    private double southBound;
    private double eastBound;
    private double northBound;

    private MapBound() {
    }

    private MapBound(double westBound, double southBound, double eastBound,
        double northBound) {
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

    public boolean isCover(double latitude, double longitude) {
        return isBetweenLatitude(latitude) && isBetweenLongitude(longitude);
    }

    private boolean isBetweenLatitude(double convertLatitude) {
        return convertLatitude >= southBound && convertLatitude <= northBound;
    }

    private boolean isBetweenLongitude(double convertLongitude) {
        return convertLongitude >= westBound && convertLongitude <= eastBound;
    }
}
