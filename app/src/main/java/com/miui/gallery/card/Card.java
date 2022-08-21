package com.miui.gallery.card;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import ch.qos.logback.core.CoreConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.card.scenario.Record;
import com.miui.gallery.cloud.card.model.CardInfo;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.dao.base.TableColumn;
import com.miui.gallery.stat.StatHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.GsonUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import org.json.JSONException;

/* loaded from: classes.dex */
public class Card extends Entity implements Comparable<Card> {
    public static final String BASE_BACKUP_CARD_SELECTION;
    public static final String BASE_UI_CARD_SELECTION;
    public static final String BASE_UNSYNC_CARD_SELECTION;
    public static final String BASE_USABLE_CARD_SELECTION;
    public String mActionUrl;
    public List<String> mAllMediaSha1s;
    public int mBaseColor;
    public List<MediaFeatureItem> mCoverMediaFeatureItems;
    public int mCreateBy;
    public long mCreateTime;
    public String mDescription;
    public String mDetailUrl;
    public String mDisplayStatus;
    public HashMap<String, String> mExtras;
    public int mImageResId;
    public Uri mImageUri;
    public boolean mIsDeletable;
    public boolean mIsIgnored;
    public boolean mIsShowVideo;
    public boolean mIsSyncable;
    public boolean mIsTop;
    public int mLocalFlag;
    public CardInfo.OperationInfo mOperationInfo;
    public int mPriority;
    public int mScenarioId;
    public double mScore;
    public List<String> mSelectedMediaSha1s;
    public String mServerId;
    public long mServerTag;
    public String mTitle;
    public int mType;
    public Record.UniqueKey mUniqueKey;
    public long mUpdateTime;
    public long mValidEndTime;
    public long mValidStartTime;

    static {
        String str = "ignored = 0 AND localFlag NOT IN (1,-2,-1,4) AND (" + String.format("%s > %s AND %s < %s", Long.valueOf(System.currentTimeMillis()), "validStart", Long.valueOf(System.currentTimeMillis()), "validEnd") + " OR " + nexExportFormat.TAG_FORMAT_TYPE + "<>2)";
        BASE_USABLE_CARD_SELECTION = str;
        BASE_UI_CARD_SELECTION = str + " AND (displayStatus IS NULL OR " + String.format("%s != '%s'", "displayStatus", "hidden") + ")";
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" AND ");
        sb.append(String.format("%s = '%s'", "displayStatus", "hidden"));
        BASE_BACKUP_CARD_SELECTION = sb.toString();
        BASE_UNSYNC_CARD_SELECTION = "ignored = 0 AND syncable = 1 AND " + String.format("%s = %s OR %s = %s OR %s = %s", "localFlag", 1, "localFlag", 0, "localFlag", 2);
    }

    public Card() {
        this.mImageResId = -1;
    }

    public synchronized void setCoverMediaFeatureItems(List<MediaFeatureItem> list) {
        if (this.mCoverMediaFeatureItems == null) {
            this.mCoverMediaFeatureItems = new ArrayList();
        }
        this.mCoverMediaFeatureItems.clear();
        if (BaseMiscUtil.isValid(list)) {
            this.mCoverMediaFeatureItems.addAll(list);
        }
    }

    public List<MediaFeatureItem> getCoverMediaFeatureItems() {
        if (this.mCoverMediaFeatureItems == null) {
            return null;
        }
        return new ArrayList(this.mCoverMediaFeatureItems);
    }

    public void setCreateTime(long j) {
        this.mCreateTime = j;
    }

    public long getCreateTime() {
        return this.mCreateTime;
    }

    public void setTitle(String str) {
        this.mTitle = str;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setDescription(String str) {
        this.mDescription = str;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public String getDetailUrl() {
        return this.mDetailUrl;
    }

    public String getActionUrl() {
        return this.mActionUrl;
    }

    public Uri getImageUri() {
        return this.mImageUri;
    }

    public int getImageResId(Context context) {
        Uri uri = this.mImageUri;
        if (uri == null) {
            return 0;
        }
        if (this.mImageResId == -1) {
            this.mImageResId = convertUriToResId(context, uri);
        }
        return this.mImageResId;
    }

    public int getBaseColor() {
        return this.mBaseColor;
    }

    public boolean isDeletable() {
        return this.mIsDeletable;
    }

    public boolean isBackup() {
        return "hidden".equals(this.mDisplayStatus);
    }

    public Record.UniqueKey getUniqueKey() {
        return this.mUniqueKey;
    }

    public CardInfo.OperationInfo getOperationInfo() {
        return this.mOperationInfo;
    }

    public synchronized List<String> getAllMediaSha1s() {
        return this.mAllMediaSha1s == null ? null : new ArrayList(this.mAllMediaSha1s);
    }

    public synchronized void setAllMediaSha1s(List<String> list) {
        if (this.mAllMediaSha1s == null) {
            this.mAllMediaSha1s = new ArrayList();
        }
        this.mAllMediaSha1s.clear();
        if (BaseMiscUtil.isValid(list)) {
            this.mAllMediaSha1s.addAll(list);
        }
    }

    public synchronized List<String> getSelectedMediaSha1s() {
        return this.mSelectedMediaSha1s == null ? null : new ArrayList(this.mSelectedMediaSha1s);
    }

    public synchronized void setSelectedMediaSha1s(List<String> list, String str) {
        if (this.mSelectedMediaSha1s == null) {
            this.mSelectedMediaSha1s = new ArrayList();
        }
        this.mSelectedMediaSha1s.clear();
        if (BaseMiscUtil.isValid(list)) {
            this.mSelectedMediaSha1s.addAll(list);
        } else {
            HashMap hashMap = new HashMap(1);
            hashMap.put("from", str);
            StatHelper.recordCountEvent("assistant", "assistant_card_remove_all_image", hashMap);
            DefaultLogger.d("Card", Log.getStackTraceString(new Throwable()));
        }
    }

    public final void putExtra(String str, String str2) {
        if (this.mExtras == null) {
            this.mExtras = new HashMap<>();
        }
        this.mExtras.put(str, str2);
    }

    public final String getExtra(String str) {
        HashMap<String, String> hashMap = this.mExtras;
        if (hashMap != null) {
            return hashMap.get(str);
        }
        return null;
    }

    public int getScenarioId() {
        return this.mScenarioId;
    }

    public void setScenarioId(int i) {
        this.mScenarioId = i;
    }

    public String getServerId() {
        return this.mServerId;
    }

    public void setServerId(String str) {
        this.mServerId = str;
    }

    public long getServerTag() {
        return this.mServerTag;
    }

    public void setServerTag(long j) {
        this.mServerTag = j;
    }

    public int getLocalFlag() {
        return this.mLocalFlag;
    }

    public void setLocalFlag(int i) {
        this.mLocalFlag = i;
    }

    public long getUpdateTime() {
        return this.mUpdateTime;
    }

    public void setUpdateTime(long j) {
        this.mUpdateTime = j;
    }

    public int getCreateBy() {
        return this.mCreateBy;
    }

    public void setCreateBy(int i) {
        this.mCreateBy = i;
    }

    public boolean isIgnored() {
        return this.mIsIgnored;
    }

    public boolean isSyncable() {
        return this.mIsSyncable;
    }

    public long getValidStartTime() {
        return this.mValidStartTime;
    }

    public long getValidEndTime() {
        return this.mValidEndTime;
    }

    public boolean isTop() {
        return this.mIsTop;
    }

    public boolean isShowVideo() {
        return this.mIsShowVideo;
    }

    public String getDisplayStatus() {
        return this.mDisplayStatus;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public double getScore() {
        return this.mScore;
    }

    public CardExtraInfo getCardExtraInfo() {
        return new CardExtraInfo(this.mUniqueKey, this.mIsIgnored, this.mDisplayStatus, this.mPriority, this.mScore);
    }

    public void setCardExtraInfo(CardExtraInfo cardExtraInfo) {
        if (cardExtraInfo != null) {
            this.mUniqueKey = cardExtraInfo.uniqueKey;
            this.mIsIgnored = cardExtraInfo.isIgnored;
            String str = "hidden";
            if (!str.equals(cardExtraInfo.displayStatus)) {
                str = "show";
            }
            this.mDisplayStatus = str;
        }
    }

    public boolean isValid() {
        int i = this.mLocalFlag;
        return i == 0 || i == 2 || i == 3;
    }

    public boolean isOutOfDate() {
        long currentTimeMillis = System.currentTimeMillis();
        return this.mValidStartTime > currentTimeMillis || this.mValidEndTime < currentTimeMillis;
    }

    public int getTimeType() {
        return (this.mScenarioId / 100) * 100;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public List<TableColumn> getTableColumns() {
        ArrayList arrayList = new ArrayList();
        Entity.addColumn(arrayList, "cardId", "INTEGER");
        Entity.addColumn(arrayList, "title", "TEXT");
        Entity.addColumn(arrayList, "description", "TEXT");
        Entity.addColumn(arrayList, "actionText", "TEXT");
        Entity.addColumn(arrayList, "actionUrl", "TEXT");
        Entity.addColumn(arrayList, "detailUrl", "TEXT");
        Entity.addColumn(arrayList, "imageUri", "TEXT");
        Entity.addColumn(arrayList, "createTime", "INTEGER");
        Entity.addColumn(arrayList, "deletable", "INTEGER");
        Entity.addColumn(arrayList, nexExportFormat.TAG_FORMAT_TYPE, "INTEGER");
        Entity.addColumn(arrayList, "styles", "TEXT");
        Entity.addColumn(arrayList, "extras", "TEXT");
        Entity.addColumn(arrayList, "scenarioId", "INTEGER");
        Entity.addColumn(arrayList, "serverId", "TEXT");
        Entity.addColumn(arrayList, "serverTag", "INTEGER");
        Entity.addColumn(arrayList, "localFlag", "INTEGER");
        Entity.addColumn(arrayList, "updateTime", "INTEGER");
        Entity.addColumn(arrayList, "createdBy", "INTEGER");
        Entity.addColumn(arrayList, "ignored", "INTEGER");
        Entity.addColumn(arrayList, "syncable", "INTEGER", String.valueOf(1));
        Entity.addColumn(arrayList, "validStart", "INTEGER", String.valueOf(0));
        Entity.addColumn(arrayList, "validEnd", "INTEGER", String.valueOf(Long.MAX_VALUE));
        Entity.addColumn(arrayList, "isTop", "INTEGER", String.valueOf(0));
        Entity.addColumn(arrayList, "isVideo", "INTEGER", String.valueOf(0));
        Entity.addColumn(arrayList, "displayStatus", "TEXT");
        Entity.addColumn(arrayList, j.k, "INTEGER");
        Entity.addColumn(arrayList, MiStat.Param.SCORE, "DOUBLE");
        return arrayList;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onInitFromCursor(Cursor cursor) {
        this.mTitle = Entity.getString(cursor, "title");
        this.mDescription = Entity.getString(cursor, "description");
        this.mDetailUrl = Entity.getString(cursor, "detailUrl");
        this.mActionUrl = Entity.getString(cursor, "actionUrl");
        String string = Entity.getString(cursor, "imageUri");
        if (string != null) {
            this.mImageUri = Uri.parse(string);
        }
        this.mCreateTime = Entity.getLong(cursor, "createTime");
        boolean z = false;
        this.mIsDeletable = Entity.getInt(cursor, "deletable") == 1;
        setType(Entity.getInt(cursor, nexExportFormat.TAG_FORMAT_TYPE));
        parseStyles(Entity.getString(cursor, "styles"));
        this.mExtras = stringToMap(Entity.getString(cursor, "extras"));
        this.mUniqueKey = (Record.UniqueKey) GsonUtils.fromJson(getExtra("unique_key"), (Class<Object>) Record.UniqueKey.class);
        this.mOperationInfo = (CardInfo.OperationInfo) GsonUtils.fromJson(getExtra("operation_info"), (Class<Object>) CardInfo.OperationInfo.class);
        try {
            this.mAllMediaSha1s = GsonUtils.getArray(getExtra("all_images"), String.class);
            this.mSelectedMediaSha1s = GsonUtils.getArray(getExtra("selected_images"), String.class);
            this.mCoverMediaFeatureItems = GsonUtils.getArray(getExtra("covers"), MediaFeatureItem.class);
        } catch (JSONException e) {
            DefaultLogger.e("Card", "Create card from cursor error:" + e);
        }
        int i = Entity.getInt(cursor, "scenarioId");
        this.mScenarioId = i;
        if (i <= 0) {
            Record.UniqueKey uniqueKey = this.mUniqueKey;
            this.mScenarioId = uniqueKey != null ? uniqueKey.getScenarioId() : 0;
        }
        this.mServerId = Entity.getString(cursor, "serverId");
        this.mServerTag = Entity.getLong(cursor, "serverTag");
        this.mLocalFlag = Entity.getInt(cursor, "localFlag");
        this.mUpdateTime = Entity.getLong(cursor, "updateTime");
        this.mCreateBy = Entity.getInt(cursor, "createdBy");
        this.mIsIgnored = Entity.getInt(cursor, "ignored") == 1;
        this.mIsSyncable = Entity.getInt(cursor, "syncable") == 1;
        this.mValidStartTime = Entity.getLong(cursor, "validStart");
        this.mValidEndTime = Entity.getLong(cursor, "validEnd");
        this.mIsTop = Entity.getInt(cursor, "isTop") == 1;
        if (Entity.getInt(cursor, "isVideo") == 1) {
            z = true;
        }
        this.mIsShowVideo = z;
        this.mDisplayStatus = Entity.getString(cursor, "displayStatus");
        this.mPriority = Entity.getInt(cursor, j.k);
        this.mScore = Entity.getDouble(cursor, MiStat.Param.SCORE);
    }

    @Override // com.miui.gallery.dao.base.Entity
    public synchronized void onConvertToContents(ContentValues contentValues) {
        contentValues.put("title", this.mTitle);
        contentValues.put("description", this.mDescription);
        contentValues.putNull("actionText");
        contentValues.put("actionUrl", this.mActionUrl);
        contentValues.put("detailUrl", this.mDetailUrl);
        Uri uri = this.mImageUri;
        if (uri == null) {
            contentValues.putNull("imageUri");
        } else {
            contentValues.put("imageUri", uri.toString());
        }
        contentValues.put("createTime", Long.valueOf(this.mCreateTime));
        int i = 1;
        contentValues.put("deletable", Integer.valueOf(this.mIsDeletable ? 1 : 0));
        contentValues.put(nexExportFormat.TAG_FORMAT_TYPE, Integer.valueOf(getType()));
        contentValues.put("styles", wrapStyles());
        putExtra("unique_key", GsonUtils.toString(this.mUniqueKey));
        putExtra("operation_info", GsonUtils.toString(this.mOperationInfo));
        putExtra("all_images", GsonUtils.toString(this.mAllMediaSha1s));
        putExtra("selected_images", GsonUtils.toString(this.mSelectedMediaSha1s));
        putExtra("covers", GsonUtils.toString(this.mCoverMediaFeatureItems));
        contentValues.put("extras", mapToString(this.mExtras));
        contentValues.put("scenarioId", Integer.valueOf(this.mScenarioId));
        contentValues.put("serverId", this.mServerId);
        contentValues.put("serverTag", Long.valueOf(this.mServerTag));
        contentValues.put("localFlag", Integer.valueOf(this.mLocalFlag));
        contentValues.put("updateTime", Long.valueOf(this.mUpdateTime));
        contentValues.put("createdBy", Integer.valueOf(this.mCreateBy));
        contentValues.put("ignored", Integer.valueOf(this.mIsIgnored ? 1 : 0));
        contentValues.put("syncable", Integer.valueOf(this.mIsSyncable ? 1 : 0));
        contentValues.put("validStart", Long.valueOf(this.mValidStartTime));
        contentValues.put("validEnd", Long.valueOf(this.mValidEndTime));
        contentValues.put("isTop", Integer.valueOf(this.mIsTop ? 1 : 0));
        if (!this.mIsShowVideo) {
            i = 0;
        }
        contentValues.put("isVideo", Integer.valueOf(i));
        contentValues.put("displayStatus", this.mDisplayStatus);
        contentValues.put(j.k, Integer.valueOf(this.mPriority));
        contentValues.put(MiStat.Param.SCORE, Double.valueOf(this.mScore));
    }

    public synchronized void copyFrom(Card card) {
        if (card != null) {
            this.mTitle = card.getTitle();
            this.mDescription = card.getDescription();
            this.mActionUrl = card.getActionUrl();
            this.mDetailUrl = card.getDetailUrl();
            this.mImageUri = card.getImageUri();
            this.mCreateTime = card.getCreateTime();
            this.mIsDeletable = card.isDeletable();
            this.mType = card.getType();
            this.mUniqueKey = card.getUniqueKey();
            this.mOperationInfo = card.getOperationInfo();
            this.mAllMediaSha1s = card.getAllMediaSha1s();
            this.mSelectedMediaSha1s = card.getSelectedMediaSha1s();
            this.mCoverMediaFeatureItems = card.getCoverMediaFeatureItems();
            this.mImageResId = card.getImageResId(GalleryApp.sGetAndroidContext());
            this.mBaseColor = card.getBaseColor();
            this.mScenarioId = card.getScenarioId();
            this.mServerId = card.getServerId();
            this.mServerTag = card.getServerTag();
            this.mLocalFlag = card.getLocalFlag();
            this.mUpdateTime = card.getUpdateTime();
            this.mCreateBy = card.getCreateBy();
            this.mIsIgnored = card.isIgnored();
            this.mIsSyncable = card.isSyncable();
            this.mValidStartTime = card.getValidStartTime();
            this.mValidEndTime = card.getValidEndTime();
            this.mIsTop = card.isTop();
            this.mIsShowVideo = card.isShowVideo();
            this.mDisplayStatus = card.getDisplayStatus();
            this.mPriority = card.getPriority();
            this.mScore = card.getScore();
        }
    }

    public String toString() {
        return "Card{mTitle='" + this.mTitle + CoreConstants.SINGLE_QUOTE_CHAR + ", mDescription='" + this.mDescription + CoreConstants.SINGLE_QUOTE_CHAR + ", mCreateTime=" + this.mCreateTime + ", mIsDeletable=" + this.mIsDeletable + ", mType=" + this.mType + ", mScenarioId=" + this.mScenarioId + ", mServerId='" + this.mServerId + CoreConstants.SINGLE_QUOTE_CHAR + ", mServerTag=" + this.mServerTag + ", mLocalFlag=" + this.mLocalFlag + ", mUpdateTime=" + this.mUpdateTime + ", mCreateBy=" + this.mCreateBy + ", mIsIgnored=" + this.mIsIgnored + ", mIsSyncable=" + this.mIsSyncable + ", mValidStartTime=" + this.mValidStartTime + ", mValidEndTime=" + this.mValidEndTime + ", mIsTop=" + this.mIsTop + ", mIsShowVideo=" + this.mIsShowVideo + ", mDisplayStatus='" + this.mDisplayStatus + CoreConstants.SINGLE_QUOTE_CHAR + ", mPriority=" + this.mPriority + ", mScore=" + this.mScore + ", mUniqueKey=" + this.mUniqueKey + '}';
    }

    @Override // com.miui.gallery.dao.base.Entity
    public String[] getUniqueConstraints() {
        return new String[]{j.c};
    }

    public int getType() {
        return this.mType;
    }

    public synchronized boolean isEmpty() {
        boolean z;
        if (this.mType != 2) {
            if (!BaseMiscUtil.isValid(this.mSelectedMediaSha1s)) {
                z = true;
            }
        }
        z = false;
        return z;
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0063, code lost:
        if (r6.mSelectedMediaSha1s.size() > r6.mCoverMediaFeatureItems.size()) goto L39;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized boolean isCoversNeedRefresh() {
        /*
            r6 = this;
            monitor-enter(r6)
            java.util.List<java.lang.String> r0 = r6.mSelectedMediaSha1s     // Catch: java.lang.Throwable -> L6c
            r1 = 0
            if (r0 == 0) goto L6a
            java.util.List<com.miui.gallery.assistant.model.MediaFeatureItem> r0 = r6.mCoverMediaFeatureItems     // Catch: java.lang.Throwable -> L6c
            r2 = 1
            if (r0 == 0) goto L68
            java.util.HashSet r0 = new java.util.HashSet     // Catch: java.lang.Throwable -> L6c
            r0.<init>()     // Catch: java.lang.Throwable -> L6c
            java.util.List<com.miui.gallery.assistant.model.MediaFeatureItem> r3 = r6.mCoverMediaFeatureItems     // Catch: java.lang.Throwable -> L6c
            java.util.Iterator r3 = r3.iterator()     // Catch: java.lang.Throwable -> L6c
        L16:
            boolean r4 = r3.hasNext()     // Catch: java.lang.Throwable -> L6c
            if (r4 == 0) goto L2c
            java.lang.Object r4 = r3.next()     // Catch: java.lang.Throwable -> L6c
            com.miui.gallery.assistant.model.MediaFeatureItem r4 = (com.miui.gallery.assistant.model.MediaFeatureItem) r4     // Catch: java.lang.Throwable -> L6c
            if (r4 == 0) goto L16
            java.lang.String r4 = r4.getSha1()     // Catch: java.lang.Throwable -> L6c
            r0.add(r4)     // Catch: java.lang.Throwable -> L6c
            goto L16
        L2c:
            java.util.List<java.lang.String> r3 = r6.mSelectedMediaSha1s     // Catch: java.lang.Throwable -> L6c
            java.util.Iterator r3 = r3.iterator()     // Catch: java.lang.Throwable -> L6c
        L32:
            boolean r4 = r3.hasNext()     // Catch: java.lang.Throwable -> L6c
            if (r4 == 0) goto L48
            java.lang.Object r4 = r3.next()     // Catch: java.lang.Throwable -> L6c
            java.lang.String r4 = (java.lang.String) r4     // Catch: java.lang.Throwable -> L6c
            boolean r5 = r0.contains(r4)     // Catch: java.lang.Throwable -> L6c
            if (r5 == 0) goto L32
            r0.remove(r4)     // Catch: java.lang.Throwable -> L6c
            goto L32
        L48:
            boolean r0 = r0.isEmpty()     // Catch: java.lang.Throwable -> L6c
            if (r0 == 0) goto L65
            java.util.List<com.miui.gallery.assistant.model.MediaFeatureItem> r0 = r6.mCoverMediaFeatureItems     // Catch: java.lang.Throwable -> L6c
            int r0 = r0.size()     // Catch: java.lang.Throwable -> L6c
            r3 = 5
            if (r0 >= r3) goto L66
            java.util.List<java.lang.String> r0 = r6.mSelectedMediaSha1s     // Catch: java.lang.Throwable -> L6c
            int r0 = r0.size()     // Catch: java.lang.Throwable -> L6c
            java.util.List<com.miui.gallery.assistant.model.MediaFeatureItem> r3 = r6.mCoverMediaFeatureItems     // Catch: java.lang.Throwable -> L6c
            int r3 = r3.size()     // Catch: java.lang.Throwable -> L6c
            if (r0 <= r3) goto L66
        L65:
            r1 = r2
        L66:
            monitor-exit(r6)
            return r1
        L68:
            monitor-exit(r6)
            return r2
        L6a:
            monitor-exit(r6)
            return r1
        L6c:
            r0 = move-exception
            monitor-exit(r6)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.card.Card.isCoversNeedRefresh():boolean");
    }

    public final void setType(int i) {
        this.mType = i;
    }

    public final void parseStyles(String str) {
        HashMap<String, String> stringToMap = stringToMap(str);
        if (stringToMap == null) {
            return;
        }
        this.mBaseColor = stringToInt(stringToMap.get("baseColor"));
    }

    public final String wrapStyles() {
        HashMap hashMap = new HashMap();
        hashMap.put("baseColor", String.valueOf(this.mBaseColor));
        return mapToString(hashMap);
    }

    public boolean isLocalDeleted() {
        return this.mLocalFlag == 1;
    }

    public String generateDuplicateKey() {
        if (getUniqueKey() != null && getUniqueKey().getStartTime() > 0) {
            return this.mScenarioId + "_" + getUniqueKey().getStartTime() + "_" + getUniqueKey().getTargetTime() + "_" + getUniqueKey().getLocation();
        }
        return this.mScenarioId + "_" + this.mCreateTime;
    }

    @Override // java.lang.Comparable
    public int compareTo(Card card) {
        int compare;
        if (isBackup()) {
            int i = -Integer.compare(this.mPriority, card.mPriority);
            if (i != 0) {
                return i;
            }
            compare = Double.compare(this.mScore, card.mScore);
        } else {
            int i2 = -Boolean.compare(this.mIsTop, card.mIsTop);
            if (i2 != 0) {
                return i2;
            }
            compare = Long.compare(this.mCreateTime, card.mCreateTime);
        }
        return -compare;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof Card) && this.mId == ((Card) obj).mId;
    }

    public int hashCode() {
        return (int) this.mId;
    }

    public synchronized boolean removeImages(List<String> list) {
        boolean z;
        int i;
        z = false;
        if (!BaseMiscUtil.isValid(this.mSelectedMediaSha1s) || !BaseMiscUtil.isValid(this.mAllMediaSha1s) || !BaseMiscUtil.isValid(list)) {
            i = 0;
        } else {
            i = 0;
            for (String str : list) {
                if (this.mSelectedMediaSha1s.contains(str) && this.mSelectedMediaSha1s.remove(str)) {
                    i++;
                }
                if (this.mAllMediaSha1s.contains(str)) {
                    this.mAllMediaSha1s.remove(str);
                }
            }
        }
        if (i > 0) {
            DefaultLogger.d("Card", "Delete %d images from Card %d", Integer.valueOf(i), Long.valueOf(this.mId));
        }
        if (i > 0) {
            z = true;
        }
        return z;
    }

    public long getRecordStartTime() {
        if (getUniqueKey() != null) {
            return getUniqueKey().getStartTime();
        }
        return -1L;
    }

    /* loaded from: classes.dex */
    public static class Builder {
        public List<String> mAllMediaSha1s;
        public int mBaseColor;
        public final Context mContext;
        public List<MediaFeatureItem> mCoverMediaFeatureItems;
        public int mCreateBy;
        public long mCreateTime;
        public String mDescription;
        public String mDetailUrl;
        public String mDisplayStatus;
        public int mImageResId;
        public Uri mImageUri;
        public CardInfo.OperationInfo mOperationInfo;
        public int mPriority;
        public int mScenarioId;
        public double mScore;
        public List<String> mSelectedMediaSha1s;
        public String mServerId;
        public long mServerTag;
        public String mTitle;
        public Record.UniqueKey mUniqueKey;
        public long mUpdateTime;
        public boolean mDeletable = true;
        public int mType = 0;
        public boolean mIsIgnored = false;
        public boolean mIsSyncable = true;
        public long mValidStartTime = 0;
        public long mValidEndTime = Long.MAX_VALUE;
        public boolean mIsTop = false;
        public boolean mIsShowVideo = false;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setTitle(String str) {
            this.mTitle = str;
            return this;
        }

        public Builder setDescription(String str) {
            this.mDescription = str;
            return this;
        }

        public Builder setDetailUrl(String str) {
            this.mDetailUrl = str;
            return this;
        }

        public Builder setImageUri(Uri uri) {
            this.mImageUri = uri;
            return this;
        }

        public Builder setDeletable(boolean z) {
            this.mDeletable = z;
            return this;
        }

        public Builder setType(int i) {
            this.mType = i;
            return this;
        }

        public Builder setUniqueKey(Record.UniqueKey uniqueKey) {
            this.mUniqueKey = uniqueKey;
            return this;
        }

        public Builder setAllMediaSha1s(List<String> list) {
            this.mAllMediaSha1s = list;
            return this;
        }

        public Builder setSelectedMediaSha1s(List<String> list) {
            this.mSelectedMediaSha1s = list;
            return this;
        }

        public Builder setCoverMediaFeatureItems(List<MediaFeatureItem> list) {
            this.mCoverMediaFeatureItems = list;
            return this;
        }

        public Builder setScenarioId(int i) {
            this.mScenarioId = i;
            return this;
        }

        public Builder setServerId(String str) {
            this.mServerId = str;
            return this;
        }

        public Builder setServerTag(long j) {
            this.mServerTag = j;
            return this;
        }

        public Builder setCreateBy(int i) {
            this.mCreateBy = i;
            return this;
        }

        public Builder setCreateTime(long j) {
            this.mCreateTime = j;
            return this;
        }

        public Builder setUpdateTime(long j) {
            this.mUpdateTime = j;
            return this;
        }

        public Builder setIsIgnored(boolean z) {
            this.mIsIgnored = z;
            return this;
        }

        public Builder setSyncable(boolean z) {
            this.mIsSyncable = z;
            return this;
        }

        public Builder setOperationInfo(CardInfo.OperationInfo operationInfo) {
            this.mOperationInfo = operationInfo;
            return this;
        }

        public Builder setValidStartTime(long j) {
            this.mValidStartTime = j;
            return this;
        }

        public Builder setValidEndTime(long j) {
            if (j <= 0) {
                j = Long.MAX_VALUE;
            }
            this.mValidEndTime = j;
            return this;
        }

        public Builder setTop(boolean z) {
            this.mIsTop = z;
            return this;
        }

        public Builder setShowVideo(boolean z) {
            this.mIsShowVideo = z;
            return this;
        }

        public Builder setDisplayStatus(String str) {
            String str2 = "hidden";
            if (!Objects.equals(str, str2)) {
                str2 = "show";
            }
            this.mDisplayStatus = str2;
            return this;
        }

        public Builder setPriority(int i) {
            this.mPriority = i;
            return this;
        }

        public Builder setScore(double d) {
            this.mScore = d;
            return this;
        }

        public Card build() {
            if (TextUtils.isEmpty(this.mTitle)) {
                DefaultLogger.e("Card", "the title must not be empty.");
            }
            Uri uri = null;
            Card card = new Card();
            card.mTitle = this.mTitle;
            card.mDescription = this.mDescription;
            card.mDetailUrl = this.mDetailUrl;
            card.mIsDeletable = this.mDeletable;
            card.mBaseColor = this.mBaseColor;
            card.mUniqueKey = this.mUniqueKey;
            card.mOperationInfo = this.mOperationInfo;
            CardInfo.OperationInfo operationInfo = this.mOperationInfo;
            card.mActionUrl = operationInfo != null ? operationInfo.getUrl() : "";
            Uri uri2 = this.mImageUri;
            if (uri2 != null) {
                uri = Card.decodeUri(uri2);
            } else {
                CardInfo.OperationInfo operationInfo2 = this.mOperationInfo;
                if (operationInfo2 != null) {
                    uri = Uri.parse(operationInfo2.getBackgroundUrl());
                }
            }
            card.mImageUri = uri;
            card.mAllMediaSha1s = this.mAllMediaSha1s;
            card.mSelectedMediaSha1s = this.mSelectedMediaSha1s;
            card.mCoverMediaFeatureItems = this.mCoverMediaFeatureItems;
            card.mScenarioId = this.mScenarioId;
            card.mServerId = this.mServerId;
            card.mServerTag = this.mServerTag;
            card.mCreateBy = this.mCreateBy;
            card.mCreateTime = this.mCreateTime;
            card.mUpdateTime = this.mUpdateTime;
            card.mIsIgnored = this.mIsIgnored;
            card.mIsSyncable = this.mIsSyncable;
            card.mValidStartTime = this.mValidStartTime;
            card.mValidEndTime = this.mValidEndTime;
            card.mIsTop = this.mIsTop;
            card.mIsShowVideo = this.mIsShowVideo;
            card.mDisplayStatus = this.mDisplayStatus;
            card.mPriority = this.mPriority;
            card.mScore = this.mScore;
            int i = this.mImageResId;
            if (i > 0) {
                card.mImageUri = Card.convertResIdToUri(this.mContext, i);
            }
            card.mType = this.mType;
            return card;
        }
    }

    public static Uri decodeUri(Uri uri) {
        return uri == null ? uri : Uri.parse(Uri.decode(uri.toString()));
    }

    public static Uri convertResIdToUri(Context context, int i) {
        return Uri.parse(String.format("%s://%s", context.getResources().getResourceTypeName(i), context.getResources().getResourceEntryName(i)));
    }

    public static int convertUriToResId(Context context, Uri uri) {
        String scheme = uri.getScheme();
        String authority = uri.getAuthority();
        return context.getResources().getIdentifier(String.format("%s/%s", scheme, authority), null, context.getPackageName());
    }

    public static String mapToString(HashMap<String, String> hashMap) {
        if (hashMap == null) {
            return null;
        }
        try {
            return new Gson().toJson(hashMap);
        } catch (Exception e) {
            DefaultLogger.e("Card", "mapToString occur error.\n", e);
            return null;
        }
    }

    public static HashMap<String, String> stringToMap(String str) {
        if (str == null) {
            return null;
        }
        try {
            return (HashMap) new Gson().fromJson(str, new TypeToken<HashMap<String, String>>() { // from class: com.miui.gallery.card.Card.1
            }.getType());
        } catch (Exception e) {
            DefaultLogger.e("Card", "stringToMap occur error.\n", e);
            return null;
        }
    }

    public static int stringToInt(String str) {
        if (str == null) {
            return 0;
        }
        try {
            return Integer.valueOf(str).intValue();
        } catch (Exception e) {
            DefaultLogger.e("Card", "stringToInt occur error.\n", e);
            return 0;
        }
    }

    /* loaded from: classes.dex */
    public static class CardExtraInfo {
        public final String displayStatus;
        public final boolean isIgnored;
        public final int priority;
        public final double score;
        public final Record.UniqueKey uniqueKey;

        public CardExtraInfo(Record.UniqueKey uniqueKey, boolean z, String str, int i, double d) {
            this.uniqueKey = uniqueKey;
            this.isIgnored = z;
            this.displayStatus = str;
            this.priority = i;
            this.score = d;
        }
    }
}
