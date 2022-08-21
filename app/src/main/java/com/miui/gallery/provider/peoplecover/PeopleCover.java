package com.miui.gallery.provider.peoplecover;

import android.content.ContentValues;
import android.database.Cursor;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.dao.base.TableColumn;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class PeopleCover extends Entity {
    public int mCoverScore;
    public String mCoverServerId;
    public String mPeopleServerId;
    public String mPeopleServerTag;

    public PeopleCover() {
    }

    public PeopleCover(String str, String str2, String str3, int i) {
        this.mPeopleServerId = str;
        this.mCoverServerId = str2;
        this.mPeopleServerTag = str3;
        this.mCoverScore = i;
    }

    public String getCoverId() {
        return this.mCoverServerId;
    }

    public void setCoverId(String str) {
        this.mCoverServerId = str;
    }

    public String getServerTag() {
        return this.mPeopleServerTag;
    }

    public void setServerTag(String str) {
        this.mPeopleServerTag = str;
    }

    public int getCoverScore() {
        return this.mCoverScore;
    }

    public void setCoverScore(int i) {
        this.mCoverScore = i;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public List<TableColumn> getTableColumns() {
        ArrayList arrayList = new ArrayList();
        Entity.addColumn(arrayList, "peopleServerId", "TEXT");
        Entity.addColumn(arrayList, "coverServerId", "TEXT");
        Entity.addColumn(arrayList, "peopleServerTag", "TEXT");
        Entity.addColumn(arrayList, "coverScore", "INTEGER");
        return arrayList;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onInitFromCursor(Cursor cursor) {
        this.mPeopleServerId = Entity.getString(cursor, "peopleServerId");
        this.mCoverServerId = Entity.getString(cursor, "coverServerId");
        this.mPeopleServerTag = Entity.getString(cursor, "peopleServerTag");
        this.mCoverScore = Entity.getInt(cursor, "coverScore");
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onConvertToContents(ContentValues contentValues) {
        contentValues.put("peopleServerId", this.mPeopleServerId);
        contentValues.put("coverServerId", this.mCoverServerId);
        contentValues.put("peopleServerTag", this.mPeopleServerTag);
        contentValues.put("coverScore", Integer.valueOf(this.mCoverScore));
    }

    @Override // com.miui.gallery.dao.base.Entity
    public String[] getUniqueConstraints() {
        return new String[]{"peopleServerId"};
    }
}
