package com.attendance.system.util;

public class LocationUtils {
    
    private static final double EARTH_RADIUS = 6371000; // Earth radius in meters

    /**
     * Calculate the distance between two points on the Earth's surface using the Haversine formula
     * 
     * @param lat1 Latitude of point 1
     * @param lon1 Longitude of point 1
     * @param lat2 Latitude of point 2
     * @param lon2 Longitude of point 2
     * @return Distance in meters
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + 
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * 
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return EARTH_RADIUS * c; // Distance in meters
    }
    
    /**
     * Check if a location is within the allowed radius
     * 
     * @param centerLat Center latitude
     * @param centerLon Center longitude
     * @param targetLat Target latitude
     * @param targetLon Target longitude
     * @param allowedRadius Allowed radius in meters
     * @return true if within radius, false otherwise
     */
    public static boolean isWithinRadius(double centerLat, double centerLon, 
                                        double targetLat, double targetLon, 
                                        int allowedRadius) {
        double distance = calculateDistance(centerLat, centerLon, targetLat, targetLon);
        return distance <= allowedRadius;
    }
}