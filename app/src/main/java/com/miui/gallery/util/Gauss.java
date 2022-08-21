package com.miui.gallery.util;

import com.miui.gallery.search.statistics.SearchStatUtils;

/* loaded from: classes2.dex */
public class Gauss {
    public final double mCoefficient;
    public final double mExpDiv;
    public final double mMean;
    public final double mVariance;

    public Gauss(double d, double d2) {
        if (d2 < SearchStatUtils.POW) {
            throw new IllegalArgumentException("variance can't be negative");
        }
        this.mMean = d;
        this.mVariance = d2;
        double d3 = d2 * 2.0d;
        this.mCoefficient = 1.0d / Math.sqrt(3.141592653589793d * d3);
        this.mExpDiv = d3;
    }

    public double value(double d) {
        return this.mCoefficient * Math.exp((-Math.pow(d - this.mMean, 2.0d)) / this.mExpDiv);
    }
}
