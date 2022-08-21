package com.miui.gallery.card.scenario;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.dao.base.TableColumn;
import com.miui.gallery.util.GsonUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.b;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class Record extends Entity {
    public long mEndTime;
    public String mLocation;
    public List<Long> mMediaIds;
    public String mPeopleId;
    public String mPrimaryKey;
    public int mScenarioId;
    public String mSecondaryKey;
    public long mStartTime;
    public int mState;
    public long mTargetTime;
    public String mTertiaryKey;
    public long mTime;

    public Record() {
    }

    public Record(Scenario scenario, List<Long> list) {
        this.mTime = DateUtils.getCurrentTimeMillis();
        this.mMediaIds = list;
        this.mState = 1;
        if (scenario != null) {
            this.mScenarioId = scenario.getScenarioId();
            this.mStartTime = scenario.getStartTime();
            this.mEndTime = scenario.getEndTime();
            this.mTargetTime = scenario.getTargetTime() <= 0 ? this.mStartTime : scenario.getTargetTime();
            this.mPeopleId = scenario.getPeopleId();
            this.mLocation = scenario.getLocation();
            this.mPrimaryKey = scenario.getPrimaryKey();
            this.mSecondaryKey = scenario.getSecondary();
            this.mTertiaryKey = scenario.getTertiaryKey();
        }
    }

    public int getScenarioId() {
        return this.mScenarioId;
    }

    public long getStartTime() {
        return this.mStartTime;
    }

    public long getEndTime() {
        return this.mEndTime;
    }

    public String getPeopleId() {
        return this.mPeopleId;
    }

    public String getLocation() {
        return this.mLocation;
    }

    public String getPrimaryKey() {
        return this.mPrimaryKey;
    }

    public String getSecondaryKey() {
        return this.mSecondaryKey;
    }

    public long getTime() {
        return this.mTime;
    }

    public int getState() {
        return this.mState;
    }

    public void setState(int i) {
        this.mState = i;
    }

    public List<Long> getMediaIds() {
        return this.mMediaIds;
    }

    public long getTargetTime() {
        long j = this.mTargetTime;
        return j <= 0 ? this.mStartTime : j;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public List<TableColumn> getTableColumns() {
        ArrayList arrayList = new ArrayList();
        Entity.addColumn(arrayList, "scenarioId", "INTEGER");
        Entity.addColumn(arrayList, "scenarioStartTime", "INTEGER");
        Entity.addColumn(arrayList, "scenarioEndTime", "INTEGER");
        Entity.addColumn(arrayList, "scenarioPeopleId", "TEXT");
        Entity.addColumn(arrayList, "scenarioLocation", "TEXT");
        Entity.addColumn(arrayList, "scenarioPrimaryKey", "TEXT");
        Entity.addColumn(arrayList, "scenarioSecondaryKey", "TEXT");
        Entity.addColumn(arrayList, "scenarioTertiaryKey", "TEXT");
        Entity.addColumn(arrayList, b.j, "INTEGER");
        Entity.addColumn(arrayList, "state", "INTEGER");
        Entity.addColumn(arrayList, "medias", "TEXT");
        Entity.addColumn(arrayList, "scenarioTargetTime", "INTEGER");
        return arrayList;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onInitFromCursor(Cursor cursor) {
        this.mScenarioId = Entity.getInt(cursor, "scenarioId");
        this.mStartTime = Entity.getLong(cursor, "scenarioStartTime");
        this.mEndTime = Entity.getLong(cursor, "scenarioEndTime");
        this.mPeopleId = Entity.getString(cursor, "scenarioPeopleId");
        this.mLocation = Entity.getString(cursor, "scenarioLocation");
        this.mPrimaryKey = Entity.getString(cursor, "scenarioPrimaryKey");
        this.mSecondaryKey = Entity.getString(cursor, "scenarioSecondaryKey");
        this.mTertiaryKey = Entity.getString(cursor, "scenarioTertiaryKey");
        this.mTime = Entity.getLong(cursor, b.j);
        this.mState = Entity.getInt(cursor, "state");
        try {
            this.mMediaIds = GsonUtils.getArray(Entity.getString(cursor, "medias"), Long.class);
        } catch (Exception unused) {
            DefaultLogger.e("Record", "Get media array of scenario %d from cursor error", Integer.valueOf(this.mScenarioId));
        }
        long j = Entity.getLong(cursor, "scenarioTargetTime");
        this.mTargetTime = j;
        if (j <= 0) {
            j = this.mStartTime;
        }
        this.mTargetTime = j;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onConvertToContents(ContentValues contentValues) {
        contentValues.put("scenarioId", Integer.valueOf(this.mScenarioId));
        contentValues.put("scenarioStartTime", Long.valueOf(this.mStartTime));
        contentValues.put("scenarioEndTime", Long.valueOf(this.mEndTime));
        String str = this.mPeopleId;
        String str2 = "";
        if (str == null) {
            str = str2;
        }
        contentValues.put("scenarioPeopleId", str);
        String str3 = this.mLocation;
        if (str3 == null) {
            str3 = str2;
        }
        contentValues.put("scenarioLocation", str3);
        String str4 = this.mPrimaryKey;
        if (str4 == null) {
            str4 = str2;
        }
        contentValues.put("scenarioPrimaryKey", str4);
        String str5 = this.mSecondaryKey;
        if (str5 == null) {
            str5 = str2;
        }
        contentValues.put("scenarioSecondaryKey", str5);
        String str6 = this.mTertiaryKey;
        if (str6 != null) {
            str2 = str6;
        }
        contentValues.put("scenarioTertiaryKey", str2);
        contentValues.put(b.j, Long.valueOf(this.mTime));
        contentValues.put("state", Integer.valueOf(this.mState));
        contentValues.put("medias", GsonUtils.toString(this.mMediaIds));
        contentValues.put("scenarioTargetTime", Long.valueOf(this.mTargetTime));
    }

    @Override // com.miui.gallery.dao.base.Entity
    public String[] getUniqueConstraints() {
        return new String[]{"scenarioId"};
    }

    public UniqueKey getUniqueKey() {
        return new UniqueKey(getScenarioId(), getStartTime(), getEndTime(), getTargetTime(), getPeopleId(), getLocation(), getPrimaryKey(), getSecondaryKey(), getPrimaryKey());
    }

    /* loaded from: classes.dex */
    public static class UniqueKey {
        public final long mEndTime;
        public final String mLocation;
        public final String mPeopleId;
        public final String mPrimaryKey;
        public final int mScenarioId;
        public final String mSecondaryKey;
        public final long mStartTime;
        public final long mTargetTime;
        public final String mTertiaryKey;

        public UniqueKey(int i, long j, long j2, long j3, String str, String str2, String str3, String str4, String str5) {
            this.mScenarioId = i;
            this.mStartTime = j;
            this.mEndTime = j2;
            this.mTargetTime = j3;
            this.mPeopleId = str;
            this.mLocation = str2;
            this.mPrimaryKey = str3;
            this.mSecondaryKey = str4;
            this.mTertiaryKey = str5;
        }

        public int getScenarioId() {
            return this.mScenarioId;
        }

        public long getStartTime() {
            return this.mStartTime;
        }

        public String getLocation() {
            return this.mLocation;
        }

        public String getPrimaryKey() {
            return this.mPrimaryKey;
        }

        public long getTargetTime() {
            long j = this.mTargetTime;
            return j <= 0 ? this.mStartTime : j;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof UniqueKey)) {
                return false;
            }
            UniqueKey uniqueKey = (UniqueKey) obj;
            if (this.mScenarioId == uniqueKey.mScenarioId) {
                if (this.mStartTime == uniqueKey.mStartTime) {
                    return true;
                }
                long j = this.mTargetTime;
                if (j > 0 && j == uniqueKey.mTargetTime) {
                    return true;
                }
                if (!TextUtils.isEmpty(this.mLocation) && !TextUtils.isEmpty(uniqueKey.mLocation) && TextUtils.equals(this.mLocation, uniqueKey.mLocation)) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            long j = this.mStartTime;
            long j2 = this.mTargetTime;
            return (((this.mScenarioId * 31) + ((int) (j ^ (j >>> 32)))) * 31) + ((int) (j2 ^ (j2 >>> 32)));
        }
    }
}
