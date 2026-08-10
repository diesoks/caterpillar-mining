package com.caterpillar.mining.backend.shared.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * GeoCoordinate value object.
 * <p>
 * Represents a valid geographic coordinate composed of a latitude and a longitude.
 * Latitude must be within [-90, 90] and longitude must be within [-180, 180].
 * </p>
 *
 * @param latitude  the latitude component, in degrees, within [-90, 90]
 * @param longitude the longitude component, in degrees, within [-180, 180]
 * @author Diego Vilca
 */
@Embeddable
public record GeoCoordinate(double latitude, double longitude) {

    /**
     * Validates the coordinate ranges.
     *
     * @throws IllegalArgumentException if latitude or longitude are out of range
     */
    public GeoCoordinate {
        if (latitude < -90.0 || latitude > 90.0) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90 degrees.");
        }
        if (longitude < -180.0 || longitude > 180.0) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180 degrees.");
        }
    }
}
