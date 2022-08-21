package com.miui.gallery.provider.peoplecover;

import android.database.Cursor;
import android.graphics.RectF;

/* loaded from: classes2.dex */
public abstract class BaseStrategy {
    public static final String[] PROJECTION = {"faceXScale", "faceYScale", "faceWScale", "faceHScale", "leftEyeXScale", "leftEyeYScale", "RightEyeXScale", "RightEyeYScale", "photo_id", "photo_server_id", "serverId", "exifOrientation", "exifImageWidth", "exifImageLength", "microthumbfile", "thumbnailFile", "localFile"};
    public int mWeight;

    public abstract boolean isValid(Cursor cursor);

    public boolean isXYReversed(int i) {
        return i == 5 || i == 6 || i == 7 || i == 8;
    }

    public void onFinish() {
    }

    public BaseStrategy(int i) {
        this.mWeight = i;
    }

    public int getWeight() {
        return this.mWeight;
    }

    public RectF getFacePositionRect(Cursor cursor) {
        float f = cursor.getFloat(0);
        float f2 = cursor.getFloat(1);
        return new RectF(f, f2, cursor.getFloat(2) + f, cursor.getFloat(3) + f2);
    }
}
