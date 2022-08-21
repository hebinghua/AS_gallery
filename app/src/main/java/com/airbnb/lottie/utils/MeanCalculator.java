package com.airbnb.lottie.utils;

/* loaded from: classes.dex */
public class MeanCalculator {
    public int n;
    public float sum;

    public void add(float f) {
        float f2 = this.sum + f;
        this.sum = f2;
        int i = this.n + 1;
        this.n = i;
        if (i == Integer.MAX_VALUE) {
            this.sum = f2 / 2.0f;
            this.n = i / 2;
        }
    }
}