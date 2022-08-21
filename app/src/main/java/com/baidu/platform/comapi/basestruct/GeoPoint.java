package com.baidu.platform.comapi.basestruct;

/* loaded from: classes.dex */
public class GeoPoint {
    private double a;
    private double b;

    public GeoPoint(double d, double d2) {
        this.a = d;
        this.b = d2;
    }

    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == getClass()) {
            GeoPoint geoPoint = (GeoPoint) obj;
            return Math.abs(this.a - geoPoint.a) <= 1.0E-6d && Math.abs(this.b - geoPoint.b) <= 1.0E-6d;
        }
        return false;
    }

    public double getLatitude() {
        return this.a;
    }

    public double getLatitudeE6() {
        return this.a;
    }

    public double getLongitude() {
        return this.b;
    }

    public double getLongitudeE6() {
        return this.b;
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public void setLatitude(double d) {
        this.a = d;
    }

    public void setLatitudeE6(double d) {
        this.a = d;
    }

    public void setLongitude(double d) {
        this.b = d;
    }

    public void setLongitudeE6(double d) {
        this.b = d;
    }

    public String toString() {
        return "GeoPoint: Latitude: " + this.a + ", Longitude: " + this.b;
    }
}
