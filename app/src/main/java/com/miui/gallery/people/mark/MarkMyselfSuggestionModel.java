package com.miui.gallery.people.mark;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import com.miui.gallery.R;
import com.miui.gallery.model.PeopleContactInfo;
import com.miui.gallery.people.mark.MarkPeopleSuggestionContract;
import com.miui.gallery.people.model.People;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.deprecated.NormalPeopleFaceMediaSet;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.face.PeopleCursorHelper;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class MarkMyselfSuggestionModel implements MarkPeopleSuggestionContract.Model {
    @Override // com.miui.gallery.people.mark.MarkPeopleSuggestionContract.Model
    public boolean needMark(Bundle bundle) {
        return isMarkingMyself(bundle) && GalleryPreferences.Face.getMarkMyselfTriggeredCount() <= 0 && GalleryPreferences.Face.isFirstSyncCompleted();
    }

    @Override // com.miui.gallery.people.mark.MarkPeopleSuggestionContract.Model
    public void loadMarkSuggestion(Context context, Bundle bundle, MarkPeopleSuggestionContract.LoadMarkSuggestionCallback loadMarkSuggestionCallback) {
        if (isMarkingMyself(bundle)) {
            new LoadPeopleTask(context, bundle, loadMarkSuggestionCallback).execute(new Void[0]);
        } else {
            loadMarkSuggestionCallback.onPeopleSuggestionLoadFailed();
        }
    }

    @Override // com.miui.gallery.people.mark.MarkPeopleSuggestionContract.Model
    public void markPeople(Context context, People people, Bundle bundle, MarkPeopleSuggestionContract.MarkPeopleCallback markPeopleCallback) {
        if (isMarkingMyself(bundle)) {
            new MarkPeopleTask(context, people, markPeopleCallback).execute(bundle);
        } else {
            markPeopleCallback.onMarkPeopleFailed(null);
        }
    }

    @Override // com.miui.gallery.people.mark.MarkPeopleSuggestionContract.Model
    public void onMarkSuggestionTriggered(Bundle bundle) {
        if (isMarkingMyself(bundle)) {
            GalleryPreferences.Face.onMarkMyselfTriggered();
        }
    }

    @Override // com.miui.gallery.people.mark.MarkPeopleSuggestionContract.Model
    public void setMarkResult(Bundle bundle, boolean z) {
        if (isMarkingMyself(bundle)) {
            GalleryPreferences.Face.setMarkMyselfResult(z);
        }
    }

    public final boolean isMarkingMyself(Bundle bundle) {
        String string = bundle.getString("option_mark_relation");
        return !TextUtils.isEmpty(string) && PeopleContactInfo.getRelationType(string) == PeopleContactInfo.Relation.myself.getRelationType();
    }

    /* loaded from: classes2.dex */
    public static class LoadPeopleTask extends AsyncTask<Void, Void, Pair<Integer, ArrayList<People>>> {
        public final int RT_INVALID = 0;
        public final int RT_NORMAL = 1;
        public final int RT_NO_NEED_MARK = 2;
        public MarkPeopleSuggestionContract.LoadMarkSuggestionCallback mCallback;
        public WeakReference<Context> mContextRef;
        public Bundle mMarkInfo;

        public LoadPeopleTask(Context context, Bundle bundle, MarkPeopleSuggestionContract.LoadMarkSuggestionCallback loadMarkSuggestionCallback) {
            this.mContextRef = new WeakReference<>(context);
            this.mMarkInfo = bundle;
            this.mCallback = loadMarkSuggestionCallback;
        }

        @Override // android.os.AsyncTask
        public Pair<Integer, ArrayList<People>> doInBackground(Void... voidArr) {
            final Context context = this.mContextRef.get();
            if (context == null) {
                return null;
            }
            return (Pair) SafeDBUtil.safeQuery(context, GalleryContract.PeopleFace.PERSONS_URI, PeopleCursorHelper.PROJECTION, (String) null, (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Pair<Integer, ArrayList<People>>>() { // from class: com.miui.gallery.people.mark.MarkMyselfSuggestionModel.LoadPeopleTask.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle */
                public Pair<Integer, ArrayList<People>> mo1808handle(Cursor cursor) {
                    ArrayList arrayList;
                    HashMap hashMap = new HashMap();
                    int i = 1;
                    if (cursor != null && cursor.moveToFirst()) {
                        final String defaultNameForMyself = PeopleContactInfo.getDefaultNameForMyself(context);
                        arrayList = new ArrayList(cursor.getCount());
                        int i2 = 0;
                        int i3 = 0;
                        do {
                            int relationType = PeopleCursorHelper.getRelationType(cursor);
                            if (relationType != PeopleContactInfo.Relation.child.getRelationType()) {
                                People people = PeopleCursorHelper.toPeople(cursor);
                                arrayList.add(people);
                                if (relationType == PeopleContactInfo.Relation.myself.getRelationType()) {
                                    i2++;
                                }
                                if (defaultNameForMyself.equalsIgnoreCase(people.getName())) {
                                    i3++;
                                }
                            }
                        } while (cursor.moveToNext());
                        if (i2 == 1) {
                            i = 2;
                            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "No need mark");
                        } else if (arrayList.size() > 0) {
                            Collections.sort(arrayList, new Comparator<People>() { // from class: com.miui.gallery.people.mark.MarkMyselfSuggestionModel.LoadPeopleTask.1.1
                                @Override // java.util.Comparator
                                public int compare(People people2, People people3) {
                                    int peopleTypePriorityForMyself = LoadPeopleTask.this.getPeopleTypePriorityForMyself(people2, defaultNameForMyself);
                                    int peopleTypePriorityForMyself2 = LoadPeopleTask.this.getPeopleTypePriorityForMyself(people3, defaultNameForMyself);
                                    return peopleTypePriorityForMyself == peopleTypePriorityForMyself2 ? people3.getFaceCount() - people2.getFaceCount() : peopleTypePriorityForMyself - peopleTypePriorityForMyself2;
                                }
                            });
                            int i4 = LoadPeopleTask.this.mMarkInfo.getInt("option_suggestion_limit", -1);
                            if (i4 > 0 && arrayList.size() > i4) {
                                arrayList = new ArrayList(arrayList.subList(0, i4));
                            }
                            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "Normal");
                            hashMap.put(MiStat.Param.COUNT, String.valueOf(i2));
                            hashMap.put("count_extra", String.valueOf(i3));
                        }
                        if (arrayList != null || arrayList.isEmpty()) {
                            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "No people");
                        }
                        SamplingStatHelper.recordCountEvent("people_mark", "mark_myself_triggered", hashMap);
                        return new Pair<>(Integer.valueOf(i), arrayList);
                    }
                    arrayList = null;
                    i = 0;
                    if (arrayList != null) {
                    }
                    hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "No people");
                    SamplingStatHelper.recordCountEvent("people_mark", "mark_myself_triggered", hashMap);
                    return new Pair<>(Integer.valueOf(i), arrayList);
                }
            });
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Pair<Integer, ArrayList<People>> pair) {
            Object obj;
            if (this.mCallback != null) {
                if (((Integer) pair.first).intValue() == 2) {
                    this.mCallback.onNoNeedMark();
                } else if (((Integer) pair.first).intValue() == 0 || (obj = pair.second) == null) {
                    this.mCallback.onPeopleSuggestionLoadFailed();
                } else {
                    this.mCallback.onPeopleSuggestionLoaded((ArrayList) obj);
                }
            }
        }

        public final int getPeopleTypePriorityForMyself(People people, String str) {
            if (PeopleContactInfo.Relation.myself == PeopleContactInfo.getRelation(people.getRelationType())) {
                return 0;
            }
            return (str == null || !str.equalsIgnoreCase(people.getName())) ? 2 : 1;
        }
    }

    /* loaded from: classes2.dex */
    public static class MarkPeopleTask extends AsyncTask<Bundle, Void, Boolean> {
        public MarkPeopleSuggestionContract.MarkPeopleCallback mCallback;
        public WeakReference<Context> mContextRef;
        public People mMarkPeople;

        public MarkPeopleTask(Context context, People people, MarkPeopleSuggestionContract.MarkPeopleCallback markPeopleCallback) {
            this.mContextRef = new WeakReference<>(context);
            this.mMarkPeople = people;
            this.mCallback = markPeopleCallback;
        }

        @Override // android.os.AsyncTask
        public Boolean doInBackground(Bundle... bundleArr) {
            Context context = this.mContextRef.get();
            return Boolean.valueOf(context != null && NormalPeopleFaceMediaSet.moveFaceToMyselfGroup(context, this.mMarkPeople.getId()));
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Boolean bool) {
            Context context = this.mContextRef.get();
            if (context == null || this.mCallback == null) {
                return;
            }
            if (bool == null || !bool.booleanValue()) {
                this.mCallback.onMarkPeopleFailed(context.getString(R.string.mark_operation_failed));
            } else {
                this.mCallback.onMarkPeopleSucceeded();
            }
        }
    }
}
