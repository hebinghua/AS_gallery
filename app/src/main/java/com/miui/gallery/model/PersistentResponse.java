package com.miui.gallery.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.dao.base.TableColumn;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class PersistentResponse extends Entity implements Parcelable {
    public static final Parcelable.Creator<PersistentResponse> CREATOR = new Parcelable.Creator<PersistentResponse>() { // from class: com.miui.gallery.model.PersistentResponse.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public PersistentResponse mo1114createFromParcel(Parcel parcel) {
            return new PersistentResponse(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public PersistentResponse[] mo1115newArray(int i) {
            return new PersistentResponse[i];
        }
    };
    public long mExpireMillis;
    public long mLastVisitTime;
    public String mResponse;
    public long mUpdateTime;
    public String mUrl;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public PersistentResponse() {
    }

    public PersistentResponse(String str, String str2, long j, long j2, long j3) {
        this.mUrl = str;
        this.mResponse = str2;
        this.mUpdateTime = j;
        this.mLastVisitTime = j2;
        this.mExpireMillis = j3;
    }

    public void setLastVisitTime(long j) {
        this.mLastVisitTime = j;
    }

    public String getResponse() {
        return this.mResponse;
    }

    public long getUpdateTime() {
        return this.mUpdateTime;
    }

    public long getExpireMillis() {
        return this.mExpireMillis;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public String[] getUniqueConstraints() {
        return new String[]{MapBundleKey.MapObjKey.OBJ_URL};
    }

    @Override // com.miui.gallery.dao.base.Entity
    public List<TableColumn> getTableColumns() {
        ArrayList arrayList = new ArrayList();
        Entity.addColumn((List<TableColumn>) arrayList, MapBundleKey.MapObjKey.OBJ_URL, "TEXT", true);
        Entity.addColumn(arrayList, "response", "TEXT");
        Entity.addColumn(arrayList, "updateTime", "INTEGER");
        Entity.addColumn(arrayList, "lastVisitTime", "INTEGER");
        Entity.addColumn(arrayList, "expireMillis", "INTEGER");
        return arrayList;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onInitFromCursor(Cursor cursor) {
        this.mUrl = Entity.getString(cursor, MapBundleKey.MapObjKey.OBJ_URL);
        this.mResponse = Entity.getString(cursor, "response");
        this.mUpdateTime = Entity.getLong(cursor, "updateTime");
        this.mLastVisitTime = Entity.getLong(cursor, "lastVisitTime");
        this.mExpireMillis = Entity.getLong(cursor, "expireMillis");
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onConvertToContents(ContentValues contentValues) {
        contentValues.put(MapBundleKey.MapObjKey.OBJ_URL, this.mUrl);
        contentValues.put("response", this.mResponse);
        contentValues.put("updateTime", Long.valueOf(this.mUpdateTime));
        contentValues.put("lastVisitTime", Long.valueOf(this.mLastVisitTime));
        contentValues.put("expireMillis", Long.valueOf(this.mExpireMillis));
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mUrl);
        parcel.writeString(this.mResponse);
        parcel.writeLong(this.mUpdateTime);
        parcel.writeLong(this.mLastVisitTime);
        parcel.writeLong(this.mId);
        parcel.writeLong(this.mExpireMillis);
    }

    public PersistentResponse(Parcel parcel) {
        this.mUrl = parcel.readString();
        this.mResponse = parcel.readString();
        this.mUpdateTime = parcel.readLong();
        this.mLastVisitTime = parcel.readLong();
        this.mId = parcel.readLong();
        this.mExpireMillis = parcel.readLong();
    }
}
