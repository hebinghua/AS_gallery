package com.miui.gallery.assistant.jni.score;

/* loaded from: classes.dex */
public class FaceScore {
    public static final int EXPRESSION_ANGRY = 2;
    public static final int EXPRESSION_DISGUST = 6;
    public static final int EXPRESSION_FEAR = 5;
    public static final int EXPRESSION_HAPPY = 1;
    public static final int EXPRESSION_NEUTRAL = 0;
    public static final int EXPRESSION_SAD = 3;
    public static final int EXPRESSION_SURPRISED = 4;
    private int expression;
    private double ht;
    private int identity;
    private double rotx;
    private double roty;
    private double rotz;
    private double smileness;
    private double wd;
    private double x;
    private double y;

    public FaceScore(int i, double d, double d2, double d3, double d4, double d5, int i2, double d6, double d7, double d8) {
        this.expression = i;
        this.x = d;
        this.y = d2;
        this.wd = d3;
        this.ht = d4;
        this.smileness = d5;
        this.identity = i2;
        this.rotx = d6;
        this.roty = d7;
        this.rotz = d8;
    }

    public int getExpression() {
        return this.expression;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getWd() {
        return this.wd;
    }

    public double getHt() {
        return this.ht;
    }

    public double getSmileness() {
        return this.smileness;
    }

    public int getIdentity() {
        return this.identity;
    }

    public double getRotx() {
        return this.rotx;
    }

    public double getRoty() {
        return this.roty;
    }

    public double getRotz() {
        return this.rotz;
    }

    public String toString() {
        return "FaceScore{expression=" + this.expression + ", x=" + this.x + ", y=" + this.y + ", wd=" + this.wd + ", ht=" + this.ht + ", smileness=" + this.smileness + ", identity=" + this.identity + ", rotx=" + this.rotx + ", roty=" + this.roty + ", rotz=" + this.rotz + '}';
    }
}
