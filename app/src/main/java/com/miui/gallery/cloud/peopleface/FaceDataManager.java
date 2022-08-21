package com.miui.gallery.cloud.peopleface;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.google.common.collect.Lists;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.model.PeopleContactInfo;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.UriUtil;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

/* loaded from: classes.dex */
public class FaceDataManager {
    public static final Uri BASE_URI;
    public static final Uri FACE_TO_IMAGES_URI;
    public static final String IS_VALID_GROUP;
    public static final Uri PEOPLE_FACE_DELAY_NOTIFY_URI;
    public static final Uri PEOPLE_FACE_JOIN_FACE_TO_IMAGES_JOIN_CLOUD_URI;
    public static final Uri PEOPLE_FACE_JOIN_FACE_TO_IMAGES_URI;
    public static final Uri PEOPLE_FACE_URI;
    public static final Uri PEOPLE_RECOMMOND_URI;
    public static final String[] PROJECTION_BASIC_PEOPLE_INFO;
    public static final String itemIsGroup;
    public static final String itemIsNotGroup;

    static {
        Uri parse = Uri.parse("content://com.miui.gallery.cloud.provider");
        BASE_URI = parse;
        PEOPLE_FACE_URI = parse.buildUpon().appendPath("peopleFace").build();
        FACE_TO_IMAGES_URI = parse.buildUpon().appendPath("faceToImages").build();
        PEOPLE_FACE_JOIN_FACE_TO_IMAGES_JOIN_CLOUD_URI = parse.buildUpon().appendPath("peopleFaceJoinFaceToImagesJoinCloud").build();
        PEOPLE_RECOMMOND_URI = parse.buildUpon().appendPath("peopleRecommend").build();
        PEOPLE_FACE_JOIN_FACE_TO_IMAGES_URI = parse.buildUpon().appendPath("peopleFaceJoinFaceToImages").build();
        PEOPLE_FACE_DELAY_NOTIFY_URI = parse.buildUpon().appendPath("peopleFace").appendQueryParameter("delay_notify", "true").build();
        Locale locale = Locale.US;
        itemIsNotGroup = String.format(locale, "(%s.%s = '%s')", "peopleFace", nexExportFormat.TAG_FORMAT_TYPE, "FACE");
        itemIsGroup = String.format(locale, "(%s = '%s')", nexExportFormat.TAG_FORMAT_TYPE, "PEOPLE");
        IS_VALID_GROUP = String.format(locale, "(%s = '%s' AND (%s = %d OR %s = %d OR %s = %d OR %s = %d OR (%s = %d AND %s = '%s')) AND ( %s is null OR %s = '' OR %s = %s) AND (%s IS NULL OR %s != %d))", nexExportFormat.TAG_FORMAT_TYPE, "PEOPLE", "localFlag", 8, "localFlag", 10, "localFlag", 16, "localFlag", 14, "localFlag", 0, "serverStatus", "normal", "groupId", "groupId", "groupId", "serverId", "visibilityType", "visibilityType", 2);
        PROJECTION_BASIC_PEOPLE_INFO = new String[]{"serverId", nexExportFormat.TAG_FORMAT_TYPE, "groupId", "eTag"};
    }

    public static Cursor queryFaceTableToCursor(String[] strArr, String str, String[] strArr2, String str2) {
        try {
            return GalleryDBHelper.getInstance().getReadableDatabase().query(SupportSQLiteQueryBuilder.builder("peopleFace").columns(strArr).selection(str, strArr2).orderBy(str2).create());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Cursor queryPeopleRecommondTableToCursor(String[] strArr, String str, String[] strArr2, String str2) {
        try {
            return GalleryDBHelper.getInstance().getReadableDatabase().query(SupportSQLiteQueryBuilder.builder("peopleRecommend").columns(strArr).selection(str, strArr2).orderBy(str2).create());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Cursor queryJoinTable(String[] strArr, String str, String[] strArr2, String str2, String str3) {
        try {
            return GalleryUtils.queryToCursor(UriUtil.appendGroupBy(PEOPLE_FACE_JOIN_FACE_TO_IMAGES_JOIN_CLOUD_URI, str3, null), strArr, str, strArr2, str2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Uri safeInsertFace(ContentValues contentValues) {
        return safeInsertFace(contentValues, false);
    }

    public static Uri safeInsertFace(ContentValues contentValues, boolean z) {
        try {
            return GalleryUtils.safeInsert(z ? PEOPLE_FACE_DELAY_NOTIFY_URI : PEOPLE_FACE_URI, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Uri safeInsertPeopleRecommend(ContentValues contentValues) {
        try {
            return GalleryUtils.safeInsert(PEOPLE_RECOMMOND_URI, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Cursor safeQueryPeopleRecommend(String[] strArr, String str, String[] strArr2) {
        try {
            return GalleryDBHelper.getInstance().getReadableDatabase().query(SupportSQLiteQueryBuilder.builder("peopleRecommend").columns(strArr).selection(str, strArr2).create());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long safeUpdatePeopleRecommend(ContentValues contentValues, String str, String[] strArr) {
        try {
            return GalleryUtils.safeUpdate(PEOPLE_RECOMMOND_URI, contentValues, str, strArr);
        } catch (Exception e) {
            e.printStackTrace();
            return -1L;
        }
    }

    public static long safeUpdateFace(ContentValues contentValues, String str, String[] strArr) {
        return safeUpdateFace(contentValues, str, strArr, false);
    }

    public static long safeUpdateFace(ContentValues contentValues, String str, String[] strArr, boolean z) {
        try {
            return GalleryUtils.safeUpdate(z ? PEOPLE_FACE_DELAY_NOTIFY_URI : PEOPLE_FACE_URI, contentValues, str, strArr);
        } catch (Exception e) {
            e.printStackTrace();
            return -1L;
        }
    }

    public static Uri safeInsertFace2Image(ContentValues contentValues) {
        try {
            return GalleryUtils.safeInsert(FACE_TO_IMAGES_URI, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Cursor safeQueryFace2ImageByServerId(String str) {
        try {
            return GalleryDBHelper.getInstance().getReadableDatabase().query(SupportSQLiteQueryBuilder.builder("faceToImages").columns(CloudUtils.getProjectionAll()).selection("imageServerId = ? ", new String[]{str}).create());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void safeIgnorePeopleByIds(ArrayList<Long> arrayList) {
        Uri uri = PEOPLE_FACE_URI;
        synchronized (uri) {
            ArrayList<ContentProviderOperation> arrayList2 = new ArrayList<>();
            ContentValues contentValues = new ContentValues();
            contentValues.put("visibilityType", (Integer) 2);
            String format = String.format("%s in ( %s", j.c, GalleryUtils.concatAll(arrayList, ","));
            arrayList2.add(ContentProviderOperation.newUpdate(uri).withValues(contentValues).withSelection(format + ") AND localFlag = 8", null).build());
            contentValues.put("localFlag", (Integer) 13);
            arrayList2.add(ContentProviderOperation.newUpdate(uri).withValues(contentValues).withSelection(format + ") AND localFlag != 8", null).build());
            try {
                GalleryApp.sGetAndroidContext().getContentResolver().applyBatch("com.miui.gallery.cloud.provider", arrayList2);
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            } catch (RemoteException e2) {
                e2.printStackTrace();
            }
        }
    }

    public static Cursor getFaceInFaceIds(String str, String str2) {
        try {
            return GalleryDBHelper.getInstance().getReadableDatabase().query(SupportSQLiteQueryBuilder.builder("peopleFace").columns(CloudUtils.getProjectionAll()).selection(getPeopleFaceSelection(str, str2, false), null).create());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getPeopleFaceSelection(String str, String str2, boolean z) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("localFlag");
        stringBuffer.append("!=");
        stringBuffer.append(2);
        stringBuffer.append(" AND ");
        stringBuffer.append("serverStatus");
        stringBuffer.append(" = '");
        stringBuffer.append("normal");
        stringBuffer.append("' AND (");
        stringBuffer.append("serverId");
        stringBuffer.append(" IN (");
        stringBuffer.append(str);
        stringBuffer.append(") OR ");
        stringBuffer.append(j.c);
        stringBuffer.append(" IN (");
        stringBuffer.append(str2);
        stringBuffer.append(" )) AND ");
        stringBuffer.append(z ? itemIsGroup : itemIsNotGroup);
        return stringBuffer.toString();
    }

    public static String getVisiblePeopleFaceSelection(String str, String str2, boolean z) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getPeopleFaceSelection(str, str2, z));
        stringBuffer.append(" AND (");
        stringBuffer.append("visibilityType");
        stringBuffer.append(" IS NULL OR ");
        stringBuffer.append("visibilityType");
        stringBuffer.append("!=");
        stringBuffer.append(2);
        stringBuffer.append(" )");
        return stringBuffer.toString();
    }

    public static ArrayList<PeopleFace> fillInPeopleInfo(LinkedList<PeopleFace> linkedList) {
        return fillInPeopleInfo(linkedList, 0);
    }

    public static ArrayList<PeopleFace> fillInPeopleInfo(final LinkedList<PeopleFace> linkedList, int i) {
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        for (int i2 = 0; i2 < linkedList.size(); i2++) {
            appendId(stringBuffer, linkedList.get(i2).groupId);
            appendId(stringBuffer2, linkedList.get(i2).localGroupId);
        }
        final ArrayList<PeopleFace> newArrayList = Lists.newArrayList();
        if (((Boolean) GalleryUtils.safeQuery("peopleFace", CloudUtils.getProjectionAll(), getVisiblePeopleFaceSelection(stringBuffer.toString(), stringBuffer2.toString(), true), (String[]) null, (String) null, new GalleryUtils.QueryHandler<Boolean>() { // from class: com.miui.gallery.cloud.peopleface.FaceDataManager.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.util.GalleryUtils.QueryHandler
            /* renamed from: handle */
            public Boolean mo1712handle(Cursor cursor) {
                boolean z = false;
                if (cursor != null) {
                    boolean z2 = false;
                    while (cursor.moveToNext()) {
                        Iterator it = linkedList.iterator();
                        while (it.hasNext()) {
                            PeopleFace peopleFace = (PeopleFace) it.next();
                            if (FaceDataManager.isFaceInGroup(peopleFace, cursor)) {
                                if (FaceDataManager.isPeopleMerged(cursor)) {
                                    peopleFace.localGroupId = cursor.getString(4);
                                    peopleFace.groupId = cursor.getString(3);
                                    peopleFace.relationType = cursor.getInt(19);
                                } else {
                                    peopleFace.peopleName = cursor.getString(15);
                                    peopleFace.localGroupId = cursor.getString(0);
                                    peopleFace.groupId = cursor.getString(1);
                                    peopleFace.relationType = cursor.getInt(19);
                                    it.remove();
                                    newArrayList.add(peopleFace);
                                }
                                z2 = true;
                            }
                        }
                    }
                    z = z2;
                }
                return Boolean.valueOf(z);
            }
        })).booleanValue() && !linkedList.isEmpty() && i < 4) {
            newArrayList.addAll(fillInPeopleInfo(linkedList, i + 1));
        }
        return newArrayList;
    }

    public static boolean isPeopleMerged(Cursor cursor) {
        String string = cursor.getString(0);
        String string2 = cursor.getString(1);
        String string3 = cursor.getString(4);
        String string4 = cursor.getString(3);
        return (!TextUtils.isEmpty(string3) && !TextUtils.equals(string, string3)) || (!TextUtils.isEmpty(string4) && !TextUtils.equals(string2, string4));
    }

    public static boolean isFaceInGroup(PeopleFace peopleFace, Cursor cursor) {
        return (!TextUtils.isEmpty(peopleFace.groupId) && peopleFace.groupId.equalsIgnoreCase(cursor.getString(1))) || (!TextUtils.isEmpty(peopleFace.localGroupId) && peopleFace.localGroupId.equalsIgnoreCase(cursor.getString(0)));
    }

    public static void appendId(StringBuffer stringBuffer, String str) {
        if (!TextUtils.isEmpty(str)) {
            if (stringBuffer.length() > 0) {
                stringBuffer.append(",");
            }
            stringBuffer.append("'");
            stringBuffer.append(str);
            stringBuffer.append("'");
        }
    }

    public static void moveChildrenToAnotherGroup(String str, String str2, String str3, String str4) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("groupId", str4);
        contentValues.put("localGroupId", str3);
        safeUpdateFace(contentValues, String.format(Locale.US, " ( %s in (%s) or %s in (%s) ) and %s != %d", "groupId", str2, "localGroupId", str, "localFlag", 2), null);
    }

    public static void changeChildrenOfPeopleA2PeopleB(String str, String str2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("groupId", str2);
        safeUpdateFace(contentValues, "groupId = ?   and localFlag != ? ", new String[]{str, String.valueOf(2)});
    }

    public static Uri getPeopleFaceLimitUri(int i) {
        return UriUtil.appendLimit(PEOPLE_FACE_URI, i);
    }

    public static PeopleFace getGroupByPeopleName(Context context, String str) {
        return getGroupByPeopleName(context, str, -1L);
    }

    public static PeopleFace getGroupByPeopleName(Context context, String str, long j) {
        Cursor cursor = null;
        if (str == null) {
            return null;
        }
        try {
            ContentResolver contentResolver = context.getContentResolver();
            Uri peopleFaceLimitUri = getPeopleFaceLimitUri(1);
            String[] projectionAll = CloudUtils.getProjectionAll();
            Cursor query = contentResolver.query(peopleFaceLimitUri, projectionAll, String.format("%s = ? AND %s != ? AND " + IS_VALID_GROUP, "peopleName", j.c), new String[]{str, Long.toString(j)}, null);
            if (query != null) {
                try {
                    if (query.moveToNext()) {
                        PeopleFace peopleFace = new PeopleFace(query);
                        query.close();
                        return peopleFace;
                    }
                } catch (Throwable th) {
                    th = th;
                    cursor = query;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
            if (query != null) {
                query.close();
            }
            return null;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static boolean ifHaveBabyType(Context context) {
        return ((Boolean) SafeDBUtil.safeQuery(context, getPeopleFaceLimitUri(1), CloudUtils.getProjectionAll(), "not(peopleType is null ) ", (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Boolean>() { // from class: com.miui.gallery.cloud.peopleface.FaceDataManager.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public Boolean mo1808handle(Cursor cursor) {
                if (cursor != null && cursor.getCount() > 0) {
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            }
        })).booleanValue();
    }

    public static PeopleFace getGroupByServerId(Context context, String str) {
        Cursor cursor = null;
        try {
            ContentResolver contentResolver = context.getContentResolver();
            Uri peopleFaceLimitUri = getPeopleFaceLimitUri(1);
            String[] projectionAll = CloudUtils.getProjectionAll();
            Cursor query = contentResolver.query(peopleFaceLimitUri, projectionAll, "serverId = ? AND " + IS_VALID_GROUP, new String[]{str}, null);
            if (query != null) {
                try {
                    if (query.moveToNext()) {
                        PeopleFace peopleFace = new PeopleFace(query);
                        query.close();
                        return peopleFace;
                    }
                } catch (Throwable th) {
                    th = th;
                    cursor = query;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
            if (query != null) {
                query.close();
            }
            return null;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static PeopleFace getItem(String str) {
        Throwable th;
        Cursor cursor;
        PeopleFace peopleFace = null;
        try {
            cursor = queryFaceTableToCursor(PROJECTION_BASIC_PEOPLE_INFO, "serverId = '" + str + "'", null, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToNext()) {
                        peopleFace = new PeopleFace(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getLong(3));
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            return peopleFace;
        } catch (Throwable th3) {
            th = th3;
            cursor = null;
        }
    }

    public static long insertOneGroupWithName2DB(String str, String str2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("localFlag", (Integer) 8);
        contentValues.put("peopleName", str);
        if (!TextUtils.isEmpty(str2)) {
            contentValues.put("peopleContactJsonInfo", str2);
            PeopleContactInfo fromJson = PeopleContactInfo.fromJson(str2);
            if (!TextUtils.isEmpty(fromJson.relationWithMe)) {
                int relationType = PeopleContactInfo.getRelationType(fromJson.relationWithMe);
                if (relationType >= 0) {
                    contentValues.put("relationType", Integer.valueOf(relationType));
                }
                if (PeopleContactInfo.isUserDefineRelation(relationType)) {
                    contentValues.put("relationText", fromJson.relationWithMeText);
                }
            }
        }
        contentValues.put(nexExportFormat.TAG_FORMAT_TYPE, "PEOPLE");
        contentValues.put("visibilityType", (Integer) 1);
        return Long.parseLong(safeInsertFace(contentValues).getLastPathSegment());
    }
}
