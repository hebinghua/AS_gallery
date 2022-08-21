package com.miui.gallery.util.photoview;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import com.miui.gallery.app.activity.GalleryActivity;
import com.miui.gallery.util.BaseMiscUtil;
import miuix.appcompat.app.ActionBar;

/* loaded from: classes2.dex */
public class ItemViewInfo implements Parcelable {
    public static final Parcelable.Creator<ItemViewInfo> CREATOR = new Parcelable.Creator<ItemViewInfo>() { // from class: com.miui.gallery.util.photoview.ItemViewInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public ItemViewInfo mo1743createFromParcel(Parcel parcel) {
            return new ItemViewInfo(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public ItemViewInfo[] mo1744newArray(int i) {
            return new ItemViewInfo[i];
        }
    };
    public int mAdapterPos;
    public float mCoverHeightRatio;
    public float mCoverWidthRatio;
    public int mHeight;
    public long mItemId;
    public float mViewRadius;
    public int mWidth;
    public int mX;
    public int mY;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public ItemViewInfo(int i, int i2, int i3, int i4, int i5) {
        this.mItemId = -1L;
        this.mAdapterPos = i;
        this.mX = i2;
        this.mY = i3;
        this.mWidth = i4;
        this.mHeight = i5;
    }

    public ItemViewInfo(int i, int i2, int i3, int i4, int i5, int i6) {
        this.mItemId = -1L;
        this.mAdapterPos = i;
        this.mX = i2;
        this.mY = i3;
        this.mWidth = i4;
        this.mHeight = i5;
        this.mViewRadius = i6;
    }

    public ItemViewInfo(int i, int i2, int i3, int i4, int i5, float f, float f2) {
        this.mItemId = -1L;
        this.mAdapterPos = i;
        this.mX = i2;
        this.mY = i3;
        this.mWidth = i4;
        this.mHeight = i5;
        this.mCoverWidthRatio = f;
        this.mCoverHeightRatio = f2;
    }

    public ItemViewInfo(long j, int i, int i2, int i3, int i4, float f, float f2) {
        this.mItemId = -1L;
        this.mItemId = j;
        this.mX = i;
        this.mY = i2;
        this.mWidth = i3;
        this.mHeight = i4;
        this.mCoverWidthRatio = f;
        this.mCoverHeightRatio = f2;
    }

    public ItemViewInfo(Parcel parcel) {
        this.mItemId = -1L;
        this.mAdapterPos = parcel.readInt();
        this.mX = parcel.readInt();
        this.mY = parcel.readInt();
        this.mWidth = parcel.readInt();
        this.mHeight = parcel.readInt();
        this.mCoverWidthRatio = parcel.readFloat();
        this.mCoverHeightRatio = parcel.readFloat();
        this.mItemId = parcel.readLong();
    }

    public boolean isLocationValid() {
        return (this.mX == -1 || this.mY == -1) ? false : true;
    }

    public int getX() {
        return this.mX;
    }

    public int getY() {
        return this.mY;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getPosition() {
        return this.mAdapterPos;
    }

    public float getCoverWidthRatio() {
        return this.mCoverWidthRatio;
    }

    public float getCoverHeightRatio() {
        return this.mCoverHeightRatio;
    }

    public float getViewRadius() {
        return this.mViewRadius;
    }

    public long getItemId() {
        return this.mItemId;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mAdapterPos);
        parcel.writeInt(this.mX);
        parcel.writeInt(this.mY);
        parcel.writeInt(this.mWidth);
        parcel.writeInt(this.mHeight);
        parcel.writeFloat(this.mCoverWidthRatio);
        parcel.writeFloat(this.mCoverHeightRatio);
        parcel.writeLong(this.mItemId);
    }

    public static GalleryActivity getActivityFromView(View view) {
        if (view != null) {
            for (Context context = view.getContext(); context instanceof ContextWrapper; context = ((ContextWrapper) context).getBaseContext()) {
                if (context instanceof GalleryActivity) {
                    return (GalleryActivity) context;
                }
            }
            return null;
        }
        return null;
    }

    public static ItemViewInfo getImageInfo(View view, int i) {
        ActionBar appCompatActionBar;
        int height;
        if (view == null) {
            return null;
        }
        int[] iArr = new int[2];
        int width = view.getWidth();
        int height2 = view.getHeight();
        view.getLocationInWindow(iArr);
        Rect rect = new Rect();
        view.getLocalVisibleRect(rect);
        int i2 = rect.left;
        float f = i2 != 0 ? (i2 * 1.0f) / width : 0.0f;
        int i3 = rect.top;
        float f2 = i3 != 0 ? (i3 * 1.0f) / height2 : 0.0f;
        int i4 = rect.right;
        if (i4 != width) {
            f = ((i4 - width) * 1.0f) / width;
        }
        float f3 = f;
        int i5 = rect.bottom;
        if (i5 != height2) {
            f2 = ((i5 - height2) * 1.0f) / height2;
        }
        GalleryActivity activityFromView = getActivityFromView(view);
        float f4 = (activityFromView == null || (appCompatActionBar = activityFromView.getAppCompatActionBar()) == null || iArr[1] >= (height = appCompatActionBar.getHeight() + 0) || height >= iArr[1] + height2) ? 0.0f : ((height - iArr[1]) * 1.0f) / height2;
        if ((f2 <= 0.0f && !BaseMiscUtil.floatEquals(f2, 0.0f)) || f2 >= f4) {
            f4 = f2;
        }
        return new ItemViewInfo(i, iArr[0], iArr[1], width, height2, f3, f4);
    }

    public static ItemViewInfo getImageInfo(View view, long j) {
        ActionBar appCompatActionBar;
        int height;
        if (view == null) {
            return null;
        }
        int[] iArr = new int[2];
        int width = view.getWidth();
        int height2 = view.getHeight();
        view.getLocationInWindow(iArr);
        Rect rect = new Rect();
        view.getLocalVisibleRect(rect);
        int i = rect.left;
        float f = i != 0 ? (i * 1.0f) / width : 0.0f;
        int i2 = rect.top;
        float f2 = i2 != 0 ? (i2 * 1.0f) / height2 : 0.0f;
        int i3 = rect.right;
        if (i3 != width) {
            f = ((i3 - width) * 1.0f) / width;
        }
        float f3 = f;
        int i4 = rect.bottom;
        if (i4 != height2) {
            f2 = ((i4 - height2) * 1.0f) / height2;
        }
        GalleryActivity activityFromView = getActivityFromView(view);
        float f4 = (activityFromView == null || (appCompatActionBar = activityFromView.getAppCompatActionBar()) == null || iArr[1] >= (height = appCompatActionBar.getHeight() + 0) || height >= iArr[1] + height2) ? 0.0f : ((height - iArr[1]) * 1.0f) / height2;
        return new ItemViewInfo(j, iArr[0], iArr[1], width, height2, f3, ((f2 > 0.0f || BaseMiscUtil.floatEquals(f2, 0.0f)) && f2 < f4) ? f4 : f2);
    }

    public static ItemViewInfo getImageInfo(int i, int i2, View view, int i3) {
        if (view == null) {
            return null;
        }
        return new ItemViewInfo(i3, i, i2, view.getWidth(), view.getHeight());
    }

    public String toString() {
        return "ItemViewInfo{mX=" + this.mX + ", mY=" + this.mY + ", mWidth=" + this.mWidth + ", mHeight=" + this.mHeight + ", mAdapterPos=" + this.mAdapterPos + ", mCoverWidthRatio=" + this.mCoverWidthRatio + ", mCoverHeightRatio=" + this.mCoverHeightRatio + ", mItemId=" + this.mItemId + '}';
    }
}
