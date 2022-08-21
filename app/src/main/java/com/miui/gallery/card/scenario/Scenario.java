package com.miui.gallery.card.scenario;

import android.database.Cursor;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.card.Card;
import com.miui.gallery.cloudcontrol.strategies.AssistantScenarioStrategy;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.util.BaseMiscUtil;
import com.xiaomi.stat.b;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public abstract class Scenario implements Comparable<Scenario> {
    public static final String[] BASE_PROJECTION = {"cloud._id"};
    public static int sDefaultMaxImageCount = 500;
    public static int sDefaultMinImageCount = 20;
    public static int sDefaultSelectedMaxImageCount = 80;
    public static int sDefaultSelectedMinImageCount = 6;
    public final String TAG = getClass().getSimpleName();
    public int mHolidayId;
    public int mMaxImageCount;
    public int mMaxSelectedImageCount;
    public int mMinImageCount;
    public int mMinSelectedImageCount;
    public int mPriority;
    public int mScenarioId;
    public List<Integer> mTagIdList;

    public abstract String generateDescription(Record record, List<MediaFeatureItem> list);

    public abstract String generateTitle(Record record, List<MediaFeatureItem> list);

    public abstract long getEndTime();

    public abstract String getLocation();

    public abstract String getPeopleId();

    public abstract String getPrimaryKey();

    public abstract String getSecondary();

    public abstract long getStartTime();

    public abstract long getTargetTime();

    public abstract String getTertiaryKey();

    public boolean isDeletable() {
        return true;
    }

    public abstract List<Long> loadMediaItem();

    public abstract void onFillScenarioRule(AssistantScenarioStrategy.ScenarioRule scenarioRule);

    public abstract boolean onPrepare(List<Record> list, List<Card> list2);

    public int getScenarioId() {
        return this.mScenarioId;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public int getMinImageCount() {
        int i = this.mMinImageCount;
        return i > 0 ? i : sDefaultMinImageCount;
    }

    public int getMaxImageCount() {
        int i = this.mMaxImageCount;
        return i > 0 ? i : sDefaultMaxImageCount;
    }

    public int getMinSelectedImageCount() {
        int i = this.mMinSelectedImageCount;
        return i > 0 ? i : sDefaultSelectedMinImageCount;
    }

    public int getMaxSelectedImageCount() {
        int i = this.mMaxSelectedImageCount;
        return i > 0 ? i : sDefaultSelectedMaxImageCount;
    }

    public boolean prepare(List<Record> list, List<Card> list2) {
        if (isRecentlyTriggered(list, 86400000L) || isCardRecentlyCreated(list2, 86400000L)) {
            return false;
        }
        return onPrepare(list, list2);
    }

    public List<Record> findRecords() {
        return GalleryEntityManager.getInstance().query(Record.class, String.format("%s = %s AND %s > %s", "scenarioId", String.valueOf(getScenarioId()), b.j, String.valueOf(DateUtils.getCurrentTimeMillis() - 15552000000L)), null, "time ASC", null);
    }

    public List<Card> findCards() {
        GalleryEntityManager galleryEntityManager = GalleryEntityManager.getInstance();
        return galleryEntityManager.query(Card.class, "ignored = 0 AND " + String.format("%s = %s AND %s > %s", "scenarioId", String.valueOf(getScenarioId()), "createTime", String.valueOf(DateUtils.getCurrentTimeMillis() - 15552000000L)), null, "createTime ASC", null);
    }

    public String getName() {
        return getClass().getSimpleName();
    }

    public List<Long> getMediaIdsFromCursor(Cursor cursor) {
        int maxImageCount = getMaxImageCount();
        ArrayList arrayList = new ArrayList();
        if (cursor != null) {
            int count = cursor.getCount();
            if (count <= maxImageCount) {
                return cursorToImageIdList(cursor);
            }
            float f = count / maxImageCount;
            while (cursor.moveToNext()) {
                if (Math.random() * f < 1.0d) {
                    arrayList.add(Long.valueOf(cursor.getLong(0)));
                }
            }
        }
        if (arrayList.size() > maxImageCount) {
            int size = arrayList.size() - maxImageCount;
            while (true) {
                int i = size - 1;
                if (size <= 0) {
                    break;
                }
                arrayList.remove((int) ((arrayList.size() - 1) * Math.random()));
                size = i;
            }
        }
        return arrayList;
    }

    public List<Long> cursorToImageIdList(Cursor cursor) {
        ArrayList arrayList = new ArrayList();
        if (cursor == null || !cursor.moveToFirst()) {
            return arrayList;
        }
        do {
            arrayList.add(Long.valueOf(cursor.getLong(0)));
        } while (cursor.moveToNext());
        return arrayList;
    }

    public boolean isRecentlyTriggered(List<Record> list, long j) {
        Record record = null;
        if (BaseMiscUtil.isValid(list)) {
            for (Record record2 : list) {
                if (record == null || record2.getTime() > record.getTime()) {
                    record = record2;
                }
            }
        }
        return record != null && DateUtils.getCurrentTimeMillis() - j < record.getTime();
    }

    public boolean isCardRecentlyCreated(List<Card> list, long j) {
        if (BaseMiscUtil.isValid(list)) {
            Collections.sort(list);
            return DateUtils.getCurrentTimeMillis() - j < list.get(0).getCreateTime();
        }
        return false;
    }

    public static void setDefaultMinImageCount(int i) {
        sDefaultMinImageCount = i;
    }

    public static void setDefaultMaxImageCount(int i) {
        sDefaultMaxImageCount = i;
    }

    public static void setDefaultSelectedMinImageCount(int i) {
        sDefaultSelectedMinImageCount = i;
    }

    public static void setDefaultSelectedMaxImageCount(int i) {
        sDefaultSelectedMaxImageCount = i;
    }

    @Override // java.lang.Comparable
    public int compareTo(Scenario scenario) {
        return Integer.compare(scenario.mPriority, this.mPriority);
    }

    public String toString() {
        return "Scenario{TAG='" + this.TAG + CoreConstants.SINGLE_QUOTE_CHAR + ", mScenarioId=" + this.mScenarioId + ", mHolidayId=" + this.mHolidayId + ", mPriority=" + this.mPriority + ", mMinImageCount=" + this.mMinImageCount + ", mMaxImageCount=" + this.mMaxImageCount + ", mMinSelectedImageCount=" + this.mMinSelectedImageCount + ", mMaxSelectedImageCount=" + this.mMaxSelectedImageCount + ", mTagIdList=" + this.mTagIdList + '}';
    }
}
