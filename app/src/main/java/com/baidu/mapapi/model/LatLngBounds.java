package com.baidu.mapapi.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.search.statistics.SearchStatUtils;
import java.util.List;

/* loaded from: classes.dex */
public final class LatLngBounds implements Parcelable {
    public static final Parcelable.Creator<LatLngBounds> CREATOR = new b();
    public final LatLng northeast;
    public final LatLng southwest;

    /* loaded from: classes.dex */
    public static final class Builder {
        private double a;
        private double b;
        private double c;
        private double d;
        private double e;
        private double f;
        private boolean g = true;

        private void a(LatLng latLng) {
            if (latLng == null) {
                return;
            }
            double d = latLng.latitude;
            double d2 = latLng.longitude;
            if (d < this.a) {
                this.a = d;
            }
            if (d > this.b) {
                this.b = d;
            }
            int i = (d2 > SearchStatUtils.POW ? 1 : (d2 == SearchStatUtils.POW ? 0 : -1));
            if (i >= 0) {
                if (d2 < this.c) {
                    this.c = d2;
                }
                if (d2 > this.d) {
                    this.d = d2;
                    if (this.c == SearchStatUtils.POW) {
                        this.c = d2;
                    }
                }
                if (i != 0) {
                    return;
                }
            } else {
                if (d2 < this.f) {
                    this.f = d2;
                }
                if (d2 <= this.e) {
                    return;
                }
            }
            this.e = d2;
        }

        public LatLngBounds build() {
            double d = this.e;
            if (d != SearchStatUtils.POW || this.f != SearchStatUtils.POW) {
                double d2 = this.d;
                if (d2 == SearchStatUtils.POW && this.c == SearchStatUtils.POW) {
                    this.d = d;
                    this.c = this.f;
                } else {
                    double d3 = this.f + 360.0d;
                    this.c = d3;
                    if (d3 > d2) {
                        this.d = d3;
                        this.c = d2;
                    }
                }
            }
            double d4 = this.d;
            if (d4 > 180.0d) {
                double d5 = d4 - 360.0d;
                this.d = d5;
                double d6 = this.c;
                if (d5 < d6) {
                    this.d = d6;
                    this.c = d5;
                }
            }
            return new LatLngBounds(new LatLng(this.b, this.d), new LatLng(this.a, this.c));
        }

        public Builder include(LatLng latLng) {
            if (latLng == null) {
                return this;
            }
            if (this.g) {
                this.g = false;
                double d = latLng.longitude;
                if (d >= SearchStatUtils.POW) {
                    this.c = d;
                    this.d = d;
                } else {
                    this.f = d;
                    this.e = d;
                }
                double d2 = latLng.latitude;
                this.a = d2;
                this.b = d2;
            }
            a(latLng);
            return this;
        }

        public Builder include(List<LatLng> list) {
            if (list != null && list.size() != 0) {
                if (list.get(0) != null && this.g) {
                    this.g = false;
                    if (list.get(0).longitude >= SearchStatUtils.POW) {
                        double d = list.get(0).longitude;
                        this.c = d;
                        this.d = d;
                    } else {
                        double d2 = list.get(0).longitude;
                        this.f = d2;
                        this.e = d2;
                    }
                    double d3 = list.get(0).latitude;
                    this.a = d3;
                    this.b = d3;
                }
                for (LatLng latLng : list) {
                    a(latLng);
                }
            }
            return this;
        }
    }

    public LatLngBounds(Parcel parcel) {
        this.northeast = (LatLng) parcel.readParcelable(LatLng.class.getClassLoader());
        this.southwest = (LatLng) parcel.readParcelable(LatLng.class.getClassLoader());
    }

    public LatLngBounds(LatLng latLng, LatLng latLng2) {
        this.northeast = latLng;
        this.southwest = latLng2;
    }

    public boolean contains(LatLng latLng) {
        if (latLng == null) {
            return false;
        }
        LatLng latLng2 = this.southwest;
        double d = latLng2.latitude;
        LatLng latLng3 = this.northeast;
        double d2 = latLng3.latitude;
        double d3 = latLng2.longitude;
        double d4 = latLng3.longitude;
        double d5 = latLng.latitude;
        double d6 = latLng.longitude;
        return d5 >= d && d5 <= d2 && d6 >= d3 && d6 <= d4;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public LatLng getCenter() {
        LatLng latLng = this.northeast;
        double d = latLng.latitude;
        LatLng latLng2 = this.southwest;
        double d2 = latLng2.latitude;
        double d3 = ((d - d2) / 2.0d) + d2;
        double d4 = latLng.longitude;
        double d5 = latLng2.longitude;
        return new LatLng(d3, ((d4 - d5) / 2.0d) + d5);
    }

    public String toString() {
        return "southwest: " + this.southwest.latitude + ", " + this.southwest.longitude + "\nnortheast: " + this.northeast.latitude + ", " + this.northeast.longitude;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.northeast, i);
        parcel.writeParcelable(this.southwest, i);
    }
}
