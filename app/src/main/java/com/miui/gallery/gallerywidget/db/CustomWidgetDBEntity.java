package com.miui.gallery.gallerywidget.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.dao.base.TableColumn;
import com.miui.gallery.gallerywidget.common.GalleryWidgetUtils;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class CustomWidgetDBEntity extends Entity implements Comparable<CustomWidgetDBEntity> {
    public float mCropLeftTopX;
    public float mCropLeftTopY;
    public float mCropRightBottomX;
    public float mCropRightBottomY;
    public int mCurrentIndex;
    public String mPicCropList;
    public String mPicIDList;
    public String mPicMatrixList;
    public String mPicPath;
    public String mPicPathList;
    public String mPicUriString;
    public String mPicUriStringList;
    public int mWidgetId;
    public int mWidgetSize;

    @Override // java.lang.Comparable
    public int compareTo(CustomWidgetDBEntity customWidgetDBEntity) {
        return 0;
    }

    public CustomWidgetDBEntity() {
    }

    public int getWidgetId() {
        return this.mWidgetId;
    }

    public int getWidgetSize() {
        return this.mWidgetSize;
    }

    public String getPicPath() {
        return this.mPicPath;
    }

    public String getPicCropList() {
        if (TextUtils.isEmpty(this.mPicCropList)) {
            float f = this.mCropRightBottomX;
            if (f > 0.0f) {
                float f2 = this.mCropRightBottomY;
                if (f2 > 0.0f) {
                    this.mPicCropList = GalleryWidgetUtils.getCropInfoString(new float[]{this.mCropLeftTopX, this.mCropLeftTopY, f, f2});
                }
            }
        }
        return this.mPicCropList;
    }

    public String getPicMatrixList() {
        return this.mPicMatrixList;
    }

    public String getPicPathList() {
        if (TextUtils.isEmpty(this.mPicPathList) && !TextUtils.isEmpty(this.mPicPath)) {
            this.mPicPathList = this.mPicPath;
        }
        return this.mPicPathList;
    }

    public String getPicIDList() {
        if (TextUtils.isEmpty(this.mPicIDList) && !TextUtils.isEmpty(this.mPicPath)) {
            this.mPicIDList = String.valueOf(0L);
        }
        return this.mPicIDList;
    }

    public int getCurrentIndex() {
        return this.mCurrentIndex;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public List<TableColumn> getTableColumns() {
        ArrayList arrayList = new ArrayList();
        Entity.addColumn(arrayList, "widgetId", "INTEGER");
        Entity.addColumn(arrayList, "widgetSize", "INTEGER");
        Entity.addColumn(arrayList, "picPath", "TEXT");
        Entity.addColumn(arrayList, "picUriString", "TEXT");
        Entity.addColumn(arrayList, "cropLeftTopX", "INTEGER");
        Entity.addColumn(arrayList, "cropLeftTopY", "INTEGER");
        Entity.addColumn(arrayList, "cropRightBottomX", "INTEGER");
        Entity.addColumn(arrayList, "cropRightBottomY", "INTEGER");
        Entity.addColumn(arrayList, "picCropList", "TEXT");
        Entity.addColumn(arrayList, "picMatrixList", "TEXT");
        Entity.addColumn(arrayList, "picPathList", "TEXT");
        Entity.addColumn(arrayList, "picUriStringList", "TEXT");
        Entity.addColumn(arrayList, "picIDList", "TEXT");
        Entity.addColumn(arrayList, "currentIndex", "INTEGER");
        return arrayList;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onInitFromCursor(Cursor cursor) {
        this.mWidgetId = Entity.getInt(cursor, "widgetId");
        this.mWidgetSize = Entity.getInt(cursor, "widgetSize");
        this.mPicPath = Entity.getString(cursor, "picPath");
        this.mPicUriString = Entity.getString(cursor, "picUriString");
        this.mCropLeftTopX = Entity.getFloat(cursor, "cropLeftTopX");
        this.mCropLeftTopY = Entity.getFloat(cursor, "cropLeftTopY");
        this.mCropRightBottomX = Entity.getFloat(cursor, "cropRightBottomX");
        this.mCropRightBottomY = Entity.getFloat(cursor, "cropRightBottomY");
        this.mPicCropList = Entity.getString(cursor, "picCropList");
        this.mPicMatrixList = Entity.getString(cursor, "picMatrixList");
        this.mPicPathList = Entity.getString(cursor, "picPathList");
        this.mPicUriStringList = Entity.getString(cursor, "picUriStringList");
        this.mPicIDList = Entity.getString(cursor, "picIDList");
        this.mCurrentIndex = Entity.getInt(cursor, "currentIndex");
    }

    @Override // com.miui.gallery.dao.base.Entity
    public synchronized void onConvertToContents(ContentValues contentValues) {
        contentValues.put("widgetId", Integer.valueOf(this.mWidgetId));
        contentValues.put("widgetSize", Integer.valueOf(this.mWidgetSize));
        contentValues.put("picPath", this.mPicPath);
        contentValues.put("picUriString", this.mPicUriString);
        contentValues.put("cropLeftTopX", Float.valueOf(this.mCropLeftTopX));
        contentValues.put("cropLeftTopY", Float.valueOf(this.mCropLeftTopY));
        contentValues.put("cropRightBottomX", Float.valueOf(this.mCropRightBottomX));
        contentValues.put("cropRightBottomY", Float.valueOf(this.mCropRightBottomY));
        contentValues.put("picCropList", this.mPicCropList);
        contentValues.put("picMatrixList", this.mPicMatrixList);
        contentValues.put("picPathList", this.mPicPathList);
        contentValues.put("picUriStringList", this.mPicUriStringList);
        contentValues.put("picIDList", this.mPicIDList);
        contentValues.put("currentIndex", Integer.valueOf(this.mCurrentIndex));
    }

    @Override // com.miui.gallery.dao.base.Entity
    public String[] getUniqueConstraints() {
        return new String[]{"widgetId"};
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof CustomWidgetDBEntity) && this.mWidgetId == ((CustomWidgetDBEntity) obj).mWidgetId;
    }

    public int hashCode() {
        return this.mWidgetId;
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public float mCropLeftTopX;
        public float mCropLeftTopY;
        public float mCropRightBottomX;
        public float mCropRightBottomY;
        public int mCurrentIndex;
        public String mPicCropList;
        public String mPicIDList;
        public String mPicMatrixList;
        public String mPicPath;
        public String mPicPathList;
        public String mPicUriString;
        public String mPicUriStringList;
        public int mWidgetId;
        public int mWidgetSize;

        public Builder setWidgetId(int i) {
            this.mWidgetId = i;
            return this;
        }

        public Builder setWidgetSize(int i) {
            this.mWidgetSize = i;
            return this;
        }

        public Builder setPicPath(String str) {
            this.mPicPath = str;
            return this;
        }

        public Builder setPicCropList(String str) {
            this.mPicCropList = str;
            return this;
        }

        public Builder setPicMatrixList(String str) {
            this.mPicMatrixList = str;
            return this;
        }

        public Builder setPicPathList(String str) {
            this.mPicPathList = str;
            return this;
        }

        public Builder setPicIDList(String str) {
            this.mPicIDList = str;
            return this;
        }

        public Builder setCurrentIndex(int i) {
            this.mCurrentIndex = i;
            return this;
        }

        public Builder setEntity(CustomWidgetDBEntity customWidgetDBEntity) {
            if (customWidgetDBEntity != null) {
                this.mWidgetId = customWidgetDBEntity.mWidgetId;
                this.mWidgetSize = customWidgetDBEntity.mWidgetSize;
                this.mPicPath = customWidgetDBEntity.mPicPath;
                this.mPicUriString = customWidgetDBEntity.mPicUriString;
                this.mCropLeftTopX = customWidgetDBEntity.mCropLeftTopX;
                this.mCropLeftTopY = customWidgetDBEntity.mCropLeftTopY;
                this.mCropRightBottomX = customWidgetDBEntity.mCropRightBottomX;
                this.mCropRightBottomY = customWidgetDBEntity.mCropRightBottomY;
                this.mPicCropList = customWidgetDBEntity.mPicCropList;
                this.mPicMatrixList = customWidgetDBEntity.mPicMatrixList;
                this.mPicPathList = customWidgetDBEntity.mPicPathList;
                this.mPicUriStringList = customWidgetDBEntity.mPicUriStringList;
                this.mPicIDList = customWidgetDBEntity.mPicIDList;
                this.mCurrentIndex = customWidgetDBEntity.mCurrentIndex;
            }
            return this;
        }

        public CustomWidgetDBEntity build() {
            CustomWidgetDBEntity customWidgetDBEntity = new CustomWidgetDBEntity();
            customWidgetDBEntity.mId = customWidgetDBEntity.mWidgetId = this.mWidgetId;
            customWidgetDBEntity.mWidgetSize = this.mWidgetSize;
            customWidgetDBEntity.mPicPath = this.mPicPath;
            customWidgetDBEntity.mPicUriString = this.mPicUriString;
            customWidgetDBEntity.mCropLeftTopX = this.mCropLeftTopX;
            customWidgetDBEntity.mCropLeftTopY = this.mCropLeftTopY;
            customWidgetDBEntity.mCropRightBottomX = this.mCropRightBottomX;
            customWidgetDBEntity.mCropRightBottomY = this.mCropRightBottomY;
            customWidgetDBEntity.mPicCropList = this.mPicCropList;
            customWidgetDBEntity.mPicMatrixList = this.mPicMatrixList;
            customWidgetDBEntity.mPicPathList = this.mPicPathList;
            customWidgetDBEntity.mPicUriStringList = this.mPicUriStringList;
            customWidgetDBEntity.mPicIDList = this.mPicIDList;
            customWidgetDBEntity.mCurrentIndex = this.mCurrentIndex;
            return customWidgetDBEntity;
        }
    }
}
