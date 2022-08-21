package com.miui.gallery.gallerywidget.db;

import android.content.ContentValues;
import android.database.Cursor;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.dao.base.TableColumn;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class RecommendWidgetDBEntity extends Entity implements Comparable<RecommendWidgetDBEntity> {
    public String mDescription;
    public int mPicHeight;
    public long mPicId;
    public float mPicRotation;
    public String mPicSha1;
    public int mPicWidth;
    public String mTitle;
    public long mUpdateTime;
    public String mUsedCardIdList;
    public String mUsedPicSha1List;
    public int mWidgetId;

    @Override // java.lang.Comparable
    public int compareTo(RecommendWidgetDBEntity recommendWidgetDBEntity) {
        return 0;
    }

    public RecommendWidgetDBEntity() {
    }

    public int getWidgetId() {
        return this.mWidgetId;
    }

    public String getUsedCardIdList() {
        return this.mUsedCardIdList;
    }

    public String getUsedPicSha1List() {
        return this.mUsedPicSha1List;
    }

    public String getPicSha1() {
        return this.mPicSha1;
    }

    public long getUpdateTime() {
        return this.mUpdateTime;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public List<TableColumn> getTableColumns() {
        ArrayList arrayList = new ArrayList();
        Entity.addColumn(arrayList, "widgetId", "INTEGER");
        Entity.addColumn(arrayList, "usedCardIdList", "TEXT");
        Entity.addColumn(arrayList, "usedPicSha1List", "TEXT");
        Entity.addColumn(arrayList, "picId", "TEXT");
        Entity.addColumn(arrayList, "picSha1", "TEXT");
        Entity.addColumn(arrayList, "picWidth", "INTEGER");
        Entity.addColumn(arrayList, "picHeight", "INTEGER");
        Entity.addColumn(arrayList, "picRotation", "INTEGER");
        Entity.addColumn(arrayList, "title", "TEXT");
        Entity.addColumn(arrayList, "description", "TEXT");
        Entity.addColumn(arrayList, "updateTime", "INTEGER");
        return arrayList;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onInitFromCursor(Cursor cursor) {
        this.mWidgetId = Entity.getInt(cursor, "widgetId");
        this.mUsedCardIdList = Entity.getString(cursor, "usedCardIdList");
        this.mUsedPicSha1List = Entity.getString(cursor, "usedPicSha1List");
        this.mPicId = Entity.getLong(cursor, "picId");
        this.mPicSha1 = Entity.getString(cursor, "picSha1");
        this.mPicWidth = Entity.getInt(cursor, "picWidth");
        this.mPicHeight = Entity.getInt(cursor, "picHeight");
        this.mPicRotation = Entity.getFloat(cursor, "picRotation");
        this.mTitle = Entity.getString(cursor, "title");
        this.mDescription = Entity.getString(cursor, "description");
        this.mUpdateTime = Entity.getLong(cursor, "updateTime");
    }

    @Override // com.miui.gallery.dao.base.Entity
    public synchronized void onConvertToContents(ContentValues contentValues) {
        contentValues.put("widgetId", Integer.valueOf(this.mWidgetId));
        contentValues.put("usedCardIdList", this.mUsedCardIdList);
        contentValues.put("usedPicSha1List", this.mUsedPicSha1List);
        contentValues.put("picId", Long.valueOf(this.mPicId));
        contentValues.put("picSha1", this.mPicSha1);
        contentValues.put("picWidth", Integer.valueOf(this.mPicWidth));
        contentValues.put("picHeight", Integer.valueOf(this.mPicHeight));
        contentValues.put("picRotation", Float.valueOf(this.mPicRotation));
        contentValues.put("title", this.mTitle);
        contentValues.put("description", this.mDescription);
        contentValues.put("updateTime", Long.valueOf(this.mUpdateTime));
    }

    @Override // com.miui.gallery.dao.base.Entity
    public String[] getUniqueConstraints() {
        return new String[]{"widgetId"};
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof RecommendWidgetDBEntity) && this.mWidgetId == ((RecommendWidgetDBEntity) obj).mWidgetId;
    }

    public int hashCode() {
        return this.mWidgetId;
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public String mDescription;
        public int mPicHeight;
        public long mPicId;
        public float mPicRotation;
        public String mPicSha1;
        public int mPicWidth;
        public String mTitle;
        public long mUpdateTime;
        public String mUsedCardIdList;
        public String mUsedPicSha1List;
        public int mWidgetId;

        public Builder setWidgetId(int i) {
            this.mWidgetId = i;
            return this;
        }

        public Builder setUsedCardIdList(String str) {
            this.mUsedCardIdList = str;
            return this;
        }

        public Builder setUsedPicSha1List(String str) {
            this.mUsedPicSha1List = str;
            return this;
        }

        public Builder setPicId(long j) {
            this.mPicId = j;
            return this;
        }

        public Builder setPicSha1(String str) {
            this.mPicSha1 = str;
            return this;
        }

        public Builder setPicWidth(int i) {
            this.mPicWidth = i;
            return this;
        }

        public Builder setPicHeight(int i) {
            this.mPicHeight = i;
            return this;
        }

        public Builder setPicRotation(float f) {
            this.mPicRotation = f;
            return this;
        }

        public Builder setTitle(String str) {
            this.mTitle = str;
            return this;
        }

        public Builder setDescription(String str) {
            this.mDescription = str;
            return this;
        }

        public Builder setUpdateTime(long j) {
            this.mUpdateTime = j;
            return this;
        }

        public RecommendWidgetDBEntity build() {
            RecommendWidgetDBEntity recommendWidgetDBEntity = new RecommendWidgetDBEntity();
            recommendWidgetDBEntity.mId = recommendWidgetDBEntity.mWidgetId = this.mWidgetId;
            recommendWidgetDBEntity.mUsedCardIdList = this.mUsedCardIdList;
            recommendWidgetDBEntity.mUsedPicSha1List = this.mUsedPicSha1List;
            recommendWidgetDBEntity.mPicId = this.mPicId;
            recommendWidgetDBEntity.mPicSha1 = this.mPicSha1;
            recommendWidgetDBEntity.mPicWidth = this.mPicWidth;
            recommendWidgetDBEntity.mPicHeight = this.mPicHeight;
            recommendWidgetDBEntity.mPicRotation = this.mPicRotation;
            recommendWidgetDBEntity.mTitle = this.mTitle;
            recommendWidgetDBEntity.mDescription = this.mDescription;
            recommendWidgetDBEntity.mUpdateTime = this.mUpdateTime;
            return recommendWidgetDBEntity;
        }
    }
}
