package com.example.terramodusvtl.service;


public class DistanceCalcService {
    public static final double EARTH_RADIUS = 6371; //  km
    public static double LONGITUDE_TERRA_MODUS = 33.927442;
    public static double LATITUDE_TERRA_MODUS = -6.933320;


    public static double haversine(double lat, double lon) {

        double phi1 = Math.toRadians(LATITUDE_TERRA_MODUS);
        double phi2 = Math.toRadians(lat);
        double deltaPhi = Math.toRadians(lat - LATITUDE_TERRA_MODUS);
        double deltaLambda = Math.toRadians(lon - LONGITUDE_TERRA_MODUS);

        double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2) + Math.cos(phi1) * Math.cos(phi2)
                * Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double lambda1 = Math.toRadians(LONGITUDE_TERRA_MODUS);
        double lambda2 = Math.toRadians(lon);
        double sinPhi1 = Math.sin(phi1);
        double cosPhi1 = Math.cos(phi1);
        double sinPhi2 = Math.sin(phi2);
        double cosPhi2 = Math.cos(phi2);

        double cosDeltaLambda = Math.cos(deltaLambda);
        double sinDeltaLambda = Math.sin(deltaLambda);

        double y = Math.sqrt(Math.pow(cosPhi2 * sinDeltaLambda, 2)
                + Math.pow(cosPhi1 * sinPhi2 - sinPhi1 * cosPhi2 * cosDeltaLambda, 2));
        double x = sinPhi1 * sinPhi2 + cosPhi1 * cosPhi2 * cosDeltaLambda;

        double bearing = Math.atan2(y, x);
        double sinSigma = Math.sqrt(Math.pow(cosPhi2 * sinDeltaLambda, 2)
                + Math.pow(cosPhi1 * sinPhi2 - sinPhi1 * cosPhi2 * cosDeltaLambda, 2)) / c;
        double cosSigma = sinPhi1 * sinPhi2 + cosPhi1 * cosPhi2 * cosDeltaLambda / c;
        double sigma = Math.atan2(sinSigma, cosSigma);
        double sinAlpha = cosPhi1 * cosPhi2 * Math.sin(deltaLambda) / Math.sin(sigma);
        double alpha = Math.asin(sinAlpha);
        double cos2Alpha = Math.pow(Math.cos(alpha), 2);
        double cos2SigmaM = Math.cos(sigma) - 2 * sinPhi1 * sinPhi2 / cos2Alpha;
        double C = cos2Alpha * EARTH_RADIUS / (1 - Math.pow(sinAlpha, 2));
        double d = C * sigma;

        return d;
    }

}
