package com.miui.gallery.provider.deprecated;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.cloud.baby.BabyInfo;
import com.miui.gallery.cloud.operation.create.CreateGroupItem;
import com.miui.gallery.cloud.peopleface.FaceDataManager;
import com.miui.gallery.cloud.peopleface.PeopleFace;
import com.miui.gallery.model.PeopleContactInfo;
import com.miui.gallery.model.SendToCloudFolderItem;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.FaceManager;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.stat.StatHelper;
import com.miui.gallery.ui.renameface.FaceAlbumHandlerBase;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.baby.CopyFaceAlbumItemsToBabyAlbumTask;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.mirror.synergy.CallMethod;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

/* loaded from: classes2.dex */
public class NormalPeopleFaceMediaSet implements Parcelable {
    public static final Parcelable.Creator<NormalPeopleFaceMediaSet> CREATOR = new Parcelable.Creator<NormalPeopleFaceMediaSet>() { // from class: com.miui.gallery.provider.deprecated.NormalPeopleFaceMediaSet.7
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public NormalPeopleFaceMediaSet mo1239createFromParcel(Parcel parcel) {
            return new NormalPeopleFaceMediaSet(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public NormalPeopleFaceMediaSet[] mo1240newArray(int i) {
            return new NormalPeopleFaceMediaSet[i];
        }
    };
    public long mId;
    public String mName;
    public String mServerId;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public NormalPeopleFaceMediaSet(long j, String str, String str2) {
        this.mId = j;
        this.mServerId = str;
        this.mName = str2;
    }

    public NormalPeopleFaceMediaSet(Parcel parcel) {
        this.mId = parcel.readLong();
        this.mServerId = parcel.readString();
        this.mName = parcel.readString();
    }

    public String getServerId() {
        return this.mServerId;
    }

    public long getBucketId() {
        return this.mId;
    }

    public String getName() {
        return this.mName;
    }

    public boolean hasName() {
        return !TextUtils.isEmpty(this.mName);
    }

    public final void setName(String str) {
        this.mName = str;
    }

    public static void merge(Context context, ArrayList<NormalPeopleFaceMediaSet> arrayList, String str) {
        NormalPeopleFaceMediaSet next;
        if (arrayList.isEmpty() || TextUtils.isEmpty(str)) {
            return;
        }
        synchronized (FaceDataManager.PEOPLE_FACE_URI) {
            if (arrayList.size() == 1) {
                if (!str.equalsIgnoreCase(arrayList.get(0).getName())) {
                    arrayList.get(0).rename(context, str, (PeopleContactInfo) null);
                }
            } else {
                PeopleFace groupByPeopleName = FaceDataManager.getGroupByPeopleName(context, str);
                if (groupByPeopleName == null || TextUtils.isEmpty(groupByPeopleName.serverId)) {
                    Iterator<NormalPeopleFaceMediaSet> it = arrayList.iterator();
                    while (it.hasNext()) {
                        next = it.next();
                        if (!TextUtils.isEmpty(next.getServerId())) {
                            next.rename(context, str, (PeopleContactInfo) null);
                            break;
                        }
                    }
                }
                next = null;
                if (next == null) {
                    if (groupByPeopleName != null) {
                        next = new NormalPeopleFaceMediaSet(Long.parseLong(groupByPeopleName._id), groupByPeopleName.serverId, str);
                    } else {
                        next = arrayList.get(0);
                        next.rename(context, str, (PeopleContactInfo) null);
                    }
                }
                arrayList.remove(next);
                next.mergeToThis(arrayList);
            }
        }
    }

    public final void mergeToThis(ArrayList<NormalPeopleFaceMediaSet> arrayList) {
        String concatAll = GalleryUtils.concatAll(arrayList, ",", new GalleryUtils.ConcatConverter<NormalPeopleFaceMediaSet>() { // from class: com.miui.gallery.provider.deprecated.NormalPeopleFaceMediaSet.2
            @Override // com.miui.gallery.util.GalleryUtils.ConcatConverter
            public String convertToString(NormalPeopleFaceMediaSet normalPeopleFaceMediaSet) {
                String serverId = normalPeopleFaceMediaSet.getServerId();
                if (TextUtils.isEmpty(serverId) || TextUtils.equals(serverId, NormalPeopleFaceMediaSet.this.getServerId())) {
                    return "";
                }
                return "'" + normalPeopleFaceMediaSet.getBucketId() + "'";
            }
        });
        String concatAll2 = GalleryUtils.concatAll(arrayList, ",", new GalleryUtils.ConcatConverter<NormalPeopleFaceMediaSet>() { // from class: com.miui.gallery.provider.deprecated.NormalPeopleFaceMediaSet.3
            @Override // com.miui.gallery.util.GalleryUtils.ConcatConverter
            public String convertToString(NormalPeopleFaceMediaSet normalPeopleFaceMediaSet) {
                return TextUtils.isEmpty(normalPeopleFaceMediaSet.getServerId()) ? String.valueOf(normalPeopleFaceMediaSet.getBucketId()) : "";
            }
        });
        long mergeGroups = mergeGroups(concatAll, GalleryUtils.concatAll(arrayList, ",", new GalleryUtils.ConcatConverter<NormalPeopleFaceMediaSet>() { // from class: com.miui.gallery.provider.deprecated.NormalPeopleFaceMediaSet.4
            @Override // com.miui.gallery.util.GalleryUtils.ConcatConverter
            public String convertToString(NormalPeopleFaceMediaSet normalPeopleFaceMediaSet) {
                String serverId = normalPeopleFaceMediaSet.getServerId();
                if (TextUtils.isEmpty(serverId) || TextUtils.equals(serverId, NormalPeopleFaceMediaSet.this.getServerId())) {
                    return "";
                }
                return "'" + serverId + "'";
            }
        }), String.valueOf(getBucketId()), getServerId());
        long mergeGroups2 = mergeGroups(concatAll2, "", String.valueOf(getBucketId()), getServerId());
        Log.i("PeopleFaceMediaSet", "merge result: rowsAffectedSynced=" + mergeGroups + ", rowsAffectedNotSynced=" + mergeGroups2);
    }

    public boolean rename(Context context, String str, PeopleContactInfo peopleContactInfo) {
        return rename(context, str, false, peopleContactInfo);
    }

    public boolean rename(Context context, String str, boolean z) {
        return rename(context, str, z, null);
    }

    public final boolean rename(Context context, String str, boolean z, PeopleContactInfo peopleContactInfo) {
        PeopleFace groupByPeopleName;
        long mergeGroupA2GroupB;
        synchronized (FaceDataManager.PEOPLE_FACE_URI) {
            PeopleFace groupByPeopleName2 = FaceDataManager.getGroupByPeopleName(context, str, getBucketId());
            if (!TextUtils.isEmpty(getServerId())) {
                groupByPeopleName = FaceDataManager.getGroupByServerId(context, getServerId());
            } else {
                groupByPeopleName = FaceDataManager.getGroupByPeopleName(context, getName(), groupByPeopleName2 != null ? Long.parseLong(groupByPeopleName2._id) : -1L);
            }
            boolean z2 = false;
            if (groupByPeopleName2 == null) {
                if (-1 != renameOldGroup(groupByPeopleName, str, peopleContactInfo)) {
                    setName(str);
                }
                return false;
            }
            if (TextUtils.isEmpty(groupByPeopleName.serverId)) {
                moveFaceFromLocalA2B(groupByPeopleName._id, groupByPeopleName2._id);
                deleteLocalGroups(groupByPeopleName._id);
                mergeGroupA2GroupB = -1;
            } else if (TextUtils.isEmpty(groupByPeopleName2.serverId)) {
                moveFaceFromLocalA2B(groupByPeopleName2._id, groupByPeopleName._id);
                deleteLocalGroups(groupByPeopleName2._id);
                mergeGroupA2GroupB = renameOldGroup(groupByPeopleName, str);
            } else {
                if (GalleryPreferences.Face.isFaceRecommendGroupHidden(groupByPeopleName.serverId) && GalleryPreferences.Face.isFaceRecommendGroupHidden(groupByPeopleName2.serverId)) {
                    z2 = true;
                }
                if (z) {
                    mergeGroupA2GroupB = mergeGroupA2GroupB(groupByPeopleName2, groupByPeopleName);
                    GalleryPreferences.Face.setFaceRecommendGroupHidden(groupByPeopleName.serverId, z2);
                    renameOldGroup(groupByPeopleName, str);
                } else {
                    mergeGroupA2GroupB = mergeGroupA2GroupB(groupByPeopleName, groupByPeopleName2);
                    GalleryPreferences.Face.setFaceRecommendGroupHidden(groupByPeopleName2.serverId, z2);
                }
            }
            if (-1 != mergeGroupA2GroupB) {
                setName(str);
            }
            return true;
        }
    }

    public final long renameOldGroup(PeopleFace peopleFace, String str) {
        return renameOldGroup(peopleFace, str, null);
    }

    public final long renameOldGroup(PeopleFace peopleFace, String str, PeopleContactInfo peopleContactInfo) {
        ContentValues contentValues = new ContentValues();
        if (peopleFace != null) {
            contentValues.put("peopleName", str);
            if (peopleContactInfo != null) {
                String formatContactJson = peopleContactInfo.formatContactJson();
                if (!TextUtils.isEmpty(formatContactJson)) {
                    contentValues.put("peopleContactJsonInfo", formatContactJson);
                }
                if (!TextUtils.isEmpty(peopleContactInfo.relationWithMe)) {
                    int relationType = PeopleContactInfo.getRelationType(peopleContactInfo.relationWithMe);
                    if (relationType >= 0) {
                        contentValues.put("relationType", Integer.valueOf(relationType));
                    }
                    if (PeopleContactInfo.isUserDefineRelation(relationType)) {
                        contentValues.put("relationText", peopleContactInfo.relationWithMeText);
                    }
                }
            }
            if (peopleFace.localFlag == 0) {
                contentValues.put("localFlag", (Integer) 16);
            }
            return FaceDataManager.safeUpdateFace(contentValues, "_id = ? ", new String[]{peopleFace._id});
        }
        return -1L;
    }

    public final void deleteLocalGroups(String str) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("localFlag", (Integer) 2);
        FaceDataManager.safeUpdateFace(contentValues, "_id = ? ", new String[]{str});
    }

    public final void moveFaceFromLocalA2B(String str, String str2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("localFlag", (Integer) 5);
        contentValues.put("localGroupId", str2);
        FaceDataManager.safeUpdateFace(contentValues, "localGroupId = ? and localFlag != ? ", new String[]{str, String.valueOf(2)});
    }

    public final long mergeGroupA2GroupB(PeopleFace peopleFace, PeopleFace peopleFace2) {
        String str;
        String str2 = peopleFace._id;
        if (TextUtils.isEmpty(peopleFace.serverId)) {
            str = "";
        } else {
            str = "'" + peopleFace.serverId + "'";
        }
        return mergeGroups(str2, str, peopleFace2._id, peopleFace2.serverId);
    }

    public final long mergeGroups(String str, String str2, String str3, String str4) {
        long safeUpdateFace;
        ContentValues contentValues = new ContentValues();
        if (TextUtils.isEmpty(str2)) {
            contentValues.put("localFlag", (Integer) 2);
            safeUpdateFace = FaceDataManager.safeUpdateFace(contentValues, String.format(Locale.US, "%s in (%s)", j.c, str), null);
        } else {
            contentValues.put("localFlag", (Integer) 12);
            contentValues.put("localGroupId", str3);
            contentValues.put("groupId", str4);
            safeUpdateFace = FaceDataManager.safeUpdateFace(contentValues, String.format(Locale.US, "%s in (%s) and %s != %s and %s != %d", j.c, str, j.c, str3, "localFlag", 2), null);
        }
        FaceDataManager.moveChildrenToAnotherGroup(str, str2, str3, str4);
        return safeUpdateFace;
    }

    public void mergeAnAlbumToThis(String str) {
        if (TextUtils.equals(str, getServerId())) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("localFlag", (Integer) 12);
        contentValues.put("groupId", getServerId());
        FaceDataManager.safeUpdateFace(contentValues, "serverId = ? and localFlag != ? ", new String[]{str, String.valueOf(2)});
        FaceDataManager.changeChildrenOfPeopleA2PeopleB(str, getServerId());
    }

    public String createBabyAlbumAndAddItems(String str, BabyInfo babyInfo, final Activity activity) {
        if (AlbumDataHelper.getAlbumByFileName(activity, str) != null) {
            str = str + activity.getString(R.string.baby_suffix);
            ToastUtils.makeTextLong(activity, activity.getString(R.string.same_name_album_exist_and_toast, new Object[]{str}));
        }
        String str2 = str;
        String localCreateBabyGroup = CreateGroupItem.localCreateBabyGroup(GalleryApp.sGetAndroidContext(), str2);
        final SendToCloudFolderItem sendToCloudFolderItem = new SendToCloudFolderItem(0, localCreateBabyGroup, false, str2, babyInfo);
        final Cursor allImagesData = getAllImagesData();
        ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.provider.deprecated.NormalPeopleFaceMediaSet.5
            @Override // java.lang.Runnable
            public void run() {
                Activity activity2 = activity;
                if (activity2 == null || activity2.isDestroyed()) {
                    return;
                }
                CopyFaceAlbumItemsToBabyAlbumTask.instance(activity, allImagesData, sendToCloudFolderItem, 0, R.string.creating_baby_album).execute(new Void[0]);
            }
        });
        return localCreateBabyGroup;
    }

    public Cursor getAllImagesData() {
        return FaceManager.queryAllImagesOfOnePerson(getServerId());
    }

    public static void doMoveFacesToAnother(FaceAlbumHandlerBase.FaceFolderItem faceFolderItem, long[] jArr) {
        long j;
        if (faceFolderItem instanceof FaceAlbumHandlerBase.FaceNewFolerItem) {
            initialFaceNewFolerItem(faceFolderItem);
        }
        if (faceFolderItem == null) {
            ignoreFaces(jArr);
            return;
        }
        try {
            j = Long.parseLong(faceFolderItem.getId());
        } catch (NumberFormatException unused) {
            j = -1;
        }
        if (j == -1) {
            return;
        }
        move2AnohterAlbum(jArr, j);
    }

    public static void initialFaceNewFolerItem(Object obj) {
        FaceAlbumHandlerBase.FaceNewFolerItem faceNewFolerItem = (FaceAlbumHandlerBase.FaceNewFolerItem) obj;
        if (!faceNewFolerItem.isCreatedInDb()) {
            PeopleFace groupByPeopleName = FaceDataManager.getGroupByPeopleName(GalleryApp.sGetAndroidContext(), faceNewFolerItem.getName());
            if (groupByPeopleName != null) {
                ContentValues contentValues = new ContentValues();
                String[] strArr = {groupByPeopleName._id};
                contentValues.put("visibilityType", (Integer) 1);
                FaceDataManager.safeUpdateFace(contentValues, "_id = ? ", strArr);
                faceNewFolerItem.setId(groupByPeopleName._id);
            } else {
                faceNewFolerItem.setId(String.valueOf(FaceDataManager.insertOneGroupWithName2DB(faceNewFolerItem.getName(), faceNewFolerItem.getContactjson())));
            }
            faceNewFolerItem.setCreatedInDb();
        }
    }

    public static void move2AnohterAlbum(long[] jArr, long j) {
        String formatSelectionIn = formatSelectionIn(jArr);
        if (!TextUtils.isEmpty(formatSelectionIn)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("localFlag", (Integer) 5);
            contentValues.put("groupId", "");
            contentValues.put("localGroupId", Long.valueOf(j));
            FaceDataManager.safeUpdateFace(contentValues, String.format(Locale.US, "%s IN (%s)", j.c, formatSelectionIn), null);
        }
    }

    public static void ignoreFaces(long[] jArr) {
        String formatSelectionIn = formatSelectionIn(jArr);
        if (!TextUtils.isEmpty(formatSelectionIn)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("localFlag", (Integer) 2);
            FaceDataManager.safeUpdateFace(contentValues, String.format(Locale.US, "%s IN (%s)", j.c, formatSelectionIn), null);
        }
    }

    public static String formatSelectionIn(long[] jArr) {
        StringBuffer stringBuffer = new StringBuffer();
        int length = jArr.length;
        for (int i = 0; i < length; i++) {
            stringBuffer.append(jArr[i]);
            if (i < length - 1) {
                stringBuffer.append(CoreConstants.COMMA_CHAR);
            }
        }
        return stringBuffer.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00aa  */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v23 */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v4, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r2v7, types: [java.lang.CharSequence] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean moveFaceToMyselfGroup(android.content.Context r12, long r13) {
        /*
            java.lang.String r0 = "PeopleFaceMediaSet"
            com.miui.gallery.model.PeopleContactInfo$Relation r1 = com.miui.gallery.model.PeopleContactInfo.Relation.myself
            int r2 = r1.getRelationType()
            java.util.List r2 = com.miui.gallery.provider.FaceManager.queryPeopleIdOfRelation(r12, r2)
            r3 = 0
            r4 = 1
            if (r2 == 0) goto L22
            java.lang.Long r5 = java.lang.Long.valueOf(r13)
            boolean r5 = r2.contains(r5)
            if (r5 == 0) goto L22
            java.lang.Long r1 = java.lang.Long.valueOf(r13)
            r2.remove(r1)
            goto L2d
        L22:
            java.lang.String r1 = com.miui.gallery.model.PeopleContactInfo.getRelationValue(r1)
            long[] r5 = new long[r4]
            r5[r3] = r13
            doMoveToRelation(r5, r1, r1)
        L2d:
            if (r2 == 0) goto L42
            boolean r1 = r2.isEmpty()
            if (r1 != 0) goto L42
            com.miui.gallery.model.PeopleContactInfo$Relation r1 = com.miui.gallery.model.PeopleContactInfo.Relation.unknown
            java.lang.String r1 = com.miui.gallery.model.PeopleContactInfo.getRelationValue(r1)
            long[] r2 = com.miui.gallery.util.MiscUtil.listToArray(r2)
            doMoveToRelation(r2, r1, r1)
        L42:
            java.lang.String r1 = com.miui.gallery.model.PeopleContactInfo.getDefaultNameForMyself(r12)     // Catch: java.lang.Exception -> Lc4
            r2 = 0
            android.content.ContentResolver r5 = r12.getContentResolver()     // Catch: java.lang.Throwable -> L90 java.lang.Exception -> L92
            android.net.Uri r6 = com.miui.gallery.provider.GalleryContract.PeopleFace.PERSONS_URI     // Catch: java.lang.Throwable -> L90 java.lang.Exception -> L92
            java.lang.String[] r7 = com.miui.gallery.util.face.PeopleCursorHelper.PROJECTION     // Catch: java.lang.Throwable -> L90 java.lang.Exception -> L92
            r8 = 0
            r9 = 0
            r10 = 0
            android.database.Cursor r12 = r5.query(r6, r7, r8, r9, r10)     // Catch: java.lang.Throwable -> L90 java.lang.Exception -> L92
            if (r12 == 0) goto L8c
            boolean r5 = r12.moveToFirst()     // Catch: java.lang.Throwable -> L85 java.lang.Exception -> L88
            if (r5 == 0) goto L8c
        L5e:
            java.lang.String r5 = com.miui.gallery.util.face.PeopleCursorHelper.getPeopleLocalId(r12)     // Catch: java.lang.Throwable -> L85 java.lang.Exception -> L88
            java.lang.Long r5 = java.lang.Long.valueOf(r5)     // Catch: java.lang.Throwable -> L85 java.lang.Exception -> L88
            long r5 = r5.longValue()     // Catch: java.lang.Throwable -> L85 java.lang.Exception -> L88
            int r5 = (r5 > r13 ? 1 : (r5 == r13 ? 0 : -1))
            if (r5 != 0) goto L73
            java.lang.String r2 = com.miui.gallery.util.face.PeopleCursorHelper.getPeopleName(r12)     // Catch: java.lang.Throwable -> L85 java.lang.Exception -> L88
            goto L7e
        L73:
            java.lang.String r5 = com.miui.gallery.util.face.PeopleCursorHelper.getPeopleName(r12)     // Catch: java.lang.Throwable -> L85 java.lang.Exception -> L88
            boolean r5 = r1.equalsIgnoreCase(r5)     // Catch: java.lang.Throwable -> L85 java.lang.Exception -> L88
            if (r5 == 0) goto L7e
            r3 = r4
        L7e:
            boolean r5 = r12.moveToNext()     // Catch: java.lang.Throwable -> L85 java.lang.Exception -> L88
            if (r5 != 0) goto L5e
            goto L8c
        L85:
            r13 = move-exception
            r2 = r12
            goto Lc0
        L88:
            r11 = r2
            r2 = r12
            r12 = r11
            goto L93
        L8c:
            com.miui.gallery.util.BaseMiscUtil.closeSilently(r12)     // Catch: java.lang.Exception -> Lc4
            goto L9c
        L90:
            r13 = move-exception
            goto Lc0
        L92:
            r12 = r2
        L93:
            java.lang.String r13 = "Failed to get people when moving face to myself group"
            android.util.Log.e(r0, r13)     // Catch: java.lang.Throwable -> L90
            com.miui.gallery.util.BaseMiscUtil.closeSilently(r2)     // Catch: java.lang.Exception -> Lc4
            r2 = r12
        L9c:
            java.util.HashMap r12 = new java.util.HashMap     // Catch: java.lang.Exception -> Lc4
            r13 = 2
            r12.<init>(r13)     // Catch: java.lang.Exception -> Lc4
            java.lang.String r13 = "from"
            boolean r14 = android.text.TextUtils.isEmpty(r2)     // Catch: java.lang.Exception -> Lc4
            if (r14 == 0) goto Lac
            java.lang.String r2 = "NoName"
        Lac:
            r12.put(r13, r2)     // Catch: java.lang.Exception -> Lc4
            java.lang.String r13 = "extra"
            java.lang.String r14 = java.lang.String.valueOf(r3)     // Catch: java.lang.Exception -> Lc4
            r12.put(r13, r14)     // Catch: java.lang.Exception -> Lc4
            java.lang.String r13 = "people_mark"
            java.lang.String r14 = "mark_people_to_myself"
            com.miui.gallery.stat.SamplingStatHelper.recordCountEvent(r13, r14, r12)     // Catch: java.lang.Exception -> Lc4
            goto Lc9
        Lc0:
            com.miui.gallery.util.BaseMiscUtil.closeSilently(r2)     // Catch: java.lang.Exception -> Lc4
            throw r13     // Catch: java.lang.Exception -> Lc4
        Lc4:
            java.lang.String r12 = "Error occurred when log event"
            android.util.Log.e(r0, r12)
        Lc9:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.deprecated.NormalPeopleFaceMediaSet.moveFaceToMyselfGroup(android.content.Context, long):boolean");
    }

    public static boolean moveFaceToRelationGroup(long[] jArr, String str, String str2) {
        if (PeopleContactInfo.getRelationType(str) == PeopleContactInfo.Relation.myself.getRelationType()) {
            if (jArr.length > 1) {
                Log.e("PeopleFaceMediaSet", "Moving more than one person to 'myself' is not allowed!");
                return false;
            }
            return moveFaceToMyselfGroup(GalleryApp.sGetAndroidContext(), jArr[0]);
        }
        return doMoveToRelation(jArr, str, str2);
    }

    public static boolean doMoveToRelation(long[] jArr, String str, String str2) {
        ArrayList<ContentProviderOperation> arrayList = new ArrayList<>();
        for (long j : jArr) {
            PeopleContactInfo queryContactInfoOfOnePerson = FaceManager.queryContactInfoOfOnePerson(j);
            if (queryContactInfoOfOnePerson == null) {
                queryContactInfoOfOnePerson = new PeopleContactInfo();
            } else if (PeopleContactInfo.getRelationValue(PeopleContactInfo.Relation.myself).equals(queryContactInfoOfOnePerson.relationWithMe)) {
                HashMap hashMap = new HashMap(1);
                hashMap.put("to", str);
                hashMap.put(CallMethod.ARG_EXTRA_STRING, str2);
                StatHelper.recordCountEvent("people_mark", "move_people_from_myself", hashMap);
            }
            queryContactInfoOfOnePerson.relationWithMe = str;
            queryContactInfoOfOnePerson.relationWithMeText = str2;
            ContentValues contentValues = new ContentValues();
            String formatContactJson = queryContactInfoOfOnePerson.formatContactJson();
            if (!TextUtils.isEmpty(formatContactJson)) {
                contentValues.put("peopleContactJsonInfo", formatContactJson);
            }
            int relationType = PeopleContactInfo.getRelationType(queryContactInfoOfOnePerson.relationWithMe);
            if (PeopleContactInfo.isUserDefineRelation(relationType)) {
                contentValues.put("relationText", str2);
            }
            contentValues.put("relationType", Integer.valueOf(relationType));
            if (FaceManager.getPeopleLocalFlagByLocalID(String.valueOf(j)) == 0) {
                contentValues.put("localFlag", (Integer) 16);
            }
            arrayList.add(ContentProviderOperation.newUpdate(FaceDataManager.PEOPLE_FACE_URI).withValues(contentValues).withSelection("_id = ? ", new String[]{String.valueOf(j)}).build());
        }
        if (!arrayList.isEmpty()) {
            try {
                GalleryApp.sGetAndroidContext().getContentResolver().applyBatch("com.miui.gallery.cloud.provider", arrayList);
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            } catch (RemoteException e2) {
                e2.printStackTrace();
            }
        }
        HashMap hashMap2 = new HashMap();
        hashMap2.put(nexExportFormat.TAG_FORMAT_TYPE, str);
        SamplingStatHelper.recordCountEvent("people", "people_set_relation", hashMap2);
        return true;
    }

    public static boolean setPeopleCover(long j, String str) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("selectCoverId", str);
        if (FaceManager.getPeopleLocalFlagByLocalID(String.valueOf(j)) == 0) {
            contentValues.put("localFlag", (Integer) 16);
        }
        return FaceDataManager.safeUpdateFace(contentValues, "_id = ? ", new String[]{String.valueOf(j)}) > 0;
    }

    public static ArrayList<PeopleFace> getBrothers(String str) {
        long currentTimeMillis = System.currentTimeMillis();
        StringBuffer stringBuffer = new StringBuffer();
        Cursor safeQueryFace2ImageByServerId = FaceDataManager.safeQueryFace2ImageByServerId(str);
        if (safeQueryFace2ImageByServerId == null || safeQueryFace2ImageByServerId.getCount() == 0) {
            closeCursor(safeQueryFace2ImageByServerId);
            return null;
        }
        while (safeQueryFace2ImageByServerId.moveToNext()) {
            FaceDataManager.appendId(stringBuffer, safeQueryFace2ImageByServerId.getString(1));
        }
        closeCursor(safeQueryFace2ImageByServerId);
        ArrayList<PeopleFace> arrayList = new ArrayList<>();
        Cursor faceInFaceIds = FaceDataManager.getFaceInFaceIds(stringBuffer.toString(), "");
        if (faceInFaceIds != null && faceInFaceIds.getCount() > 0) {
            ArrayList arrayList2 = new ArrayList();
            while (faceInFaceIds.moveToNext()) {
                arrayList2.add(new PeopleFace(faceInFaceIds));
            }
            closeCursor(faceInFaceIds);
            arrayList = FaceDataManager.fillInPeopleInfo(new LinkedList(arrayList2));
        } else {
            closeCursor(faceInFaceIds);
        }
        Log.i("PeopleFaceMediaSet", "get brothers cost:" + (System.currentTimeMillis() - currentTimeMillis));
        return arrayList;
    }

    public static void closeCursor(Cursor cursor) {
        BaseMiscUtil.closeSilently(cursor);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.mId);
        parcel.writeString(this.mServerId);
        parcel.writeString(this.mName);
    }
}
