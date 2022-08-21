package com.miui.gallery.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Pair;
import com.miui.gallery.model.CursorDataSet;
import com.miui.gallery.model.MergedCursorDataSetWrapper;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.logger.TimingTracing;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/* loaded from: classes2.dex */
public class MergedCursorSetLoaderWrapper extends CursorSetLoader {
    public final List<CursorSetLoader> mLoaders;

    @Override // com.miui.gallery.loader.CursorSetLoader
    public String getOrder() {
        return null;
    }

    @Override // com.miui.gallery.loader.CursorSetLoader
    public String[] getProjection() {
        return new String[0];
    }

    @Override // com.miui.gallery.loader.CursorSetLoader
    public String getSelection() {
        return null;
    }

    @Override // com.miui.gallery.loader.CursorSetLoader
    public String[] getSelectionArgs() {
        return new String[0];
    }

    @Override // com.miui.gallery.loader.CursorSetLoader
    public String getTAG() {
        return "MergedCursorSetLoaderWrapper";
    }

    @Override // com.miui.gallery.loader.CursorSetLoader
    public Uri getUri() {
        return null;
    }

    @Override // com.miui.gallery.loader.CursorSetLoader
    public CursorDataSet wrapDataSet(Cursor cursor) {
        return null;
    }

    public MergedCursorSetLoaderWrapper(Context context, CursorSetLoader... cursorSetLoaderArr) {
        super(context);
        Objects.requireNonNull(cursorSetLoaderArr);
        this.mLoaders = Arrays.asList(cursorSetLoaderArr);
    }

    @Override // com.miui.gallery.loader.CursorSetLoader, androidx.loader.content.AsyncTaskLoader
    /* renamed from: loadInBackground */
    public CursorDataSet mo1444loadInBackground() {
        TimingTracing.beginTracing("MergedCursorSetLoaderWrapper_load", "loadInBackground");
        LinkedList linkedList = new LinkedList();
        int i = 0;
        Pair pair = null;
        for (CursorSetLoader cursorSetLoader : this.mLoaders) {
            CursorDataSet mo1444loadInBackground = cursorSetLoader.mo1444loadInBackground();
            if (mo1444loadInBackground != null) {
                if (mo1444loadInBackground.getInitPosition() >= 0 && (pair == null || mo1444loadInBackground.hasSetInitPosition())) {
                    pair = new Pair(Integer.valueOf(i), Integer.valueOf(mo1444loadInBackground.getInitPosition()));
                }
                i++;
                DefaultLogger.d("MergedCursorSetLoaderWrapper", "DateResource:%s,data size:%d", cursorSetLoader.getClass().getSimpleName(), Integer.valueOf(mo1444loadInBackground.getCount()));
            }
            linkedList.add(mo1444loadInBackground);
            TimingTracing.addSplit(String.format("loadInBackground-[%s]", cursorSetLoader.getClass().getSimpleName()));
        }
        try {
            return new MergedCursorDataSetWrapper(linkedList, pair, new Comparator(this.mLoaders.get(0).getOrder()));
        } finally {
            TimingTracing.addSplit("merge");
            long stopTracing = TimingTracing.stopTracing(null);
            if (stopTracing > 500) {
                HashMap hashMap = new HashMap();
                hashMap.put("cost_time", getSelection() + "_" + stopTracing);
                SamplingStatHelper.recordCountEvent("load_performance", getClass().getSimpleName(), hashMap);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class Comparator implements java.util.Comparator<Cursor> {
        public Map<String, Integer> mCachedPosition;
        public Map<String, Integer> mCachedType;
        public final List<Pair<String, Boolean>> mSubComparators = new LinkedList();

        public Comparator(String str) {
            List<String> list;
            if (TextUtils.isEmpty(str)) {
                list = Collections.emptyList();
            } else {
                list = (List) Arrays.stream(str.split(",")).map(MergedCursorSetLoaderWrapper$Comparator$$ExternalSyntheticLambda0.INSTANCE).filter(MergedCursorSetLoaderWrapper$Comparator$$ExternalSyntheticLambda2.INSTANCE).collect(Collectors.toList());
            }
            for (String str2 : list) {
                String[] strArr = (String[]) Arrays.stream(str2.split(" ")).map(MergedCursorSetLoaderWrapper$Comparator$$ExternalSyntheticLambda0.INSTANCE).filter(MergedCursorSetLoaderWrapper$Comparator$$ExternalSyntheticLambda3.INSTANCE).toArray(MergedCursorSetLoaderWrapper$Comparator$$ExternalSyntheticLambda1.INSTANCE);
                strArr = strArr.length == 1 ? new String[]{strArr[0], "ASC"} : strArr;
                if (strArr.length == 2 && (strArr[1].equalsIgnoreCase("ASC") || strArr[1].equalsIgnoreCase("DESC"))) {
                    this.mSubComparators.add(new Pair<>(strArr[0], Boolean.valueOf(!strArr[1].equalsIgnoreCase("DESC"))));
                }
            }
        }

        public static /* synthetic */ boolean lambda$new$0(String str) {
            return !TextUtils.isEmpty(str);
        }

        public static /* synthetic */ boolean lambda$new$1(String str) {
            return !TextUtils.isEmpty(str);
        }

        public static /* synthetic */ String[] lambda$new$2(int i) {
            return new String[i];
        }

        @Override // java.util.Comparator
        public int compare(Cursor cursor, Cursor cursor2) {
            if (cursor.isAfterLast()) {
                return 1;
            }
            if (cursor2.isAfterLast()) {
                return -1;
            }
            if (this.mCachedPosition == null) {
                this.mCachedPosition = new HashMap();
                this.mCachedType = new HashMap();
                for (Pair<String, Boolean> pair : this.mSubComparators) {
                    String str = (String) pair.first;
                    int columnIndex = cursor.getColumnIndex(str);
                    this.mCachedPosition.put(str, Integer.valueOf(columnIndex));
                    this.mCachedType.put(str, Integer.valueOf(cursor.getType(columnIndex)));
                }
            }
            for (Pair<String, Boolean> pair2 : this.mSubComparators) {
                String str2 = (String) pair2.first;
                Boolean bool = (Boolean) pair2.second;
                Integer num = this.mCachedType.get(str2);
                Integer num2 = this.mCachedPosition.get(str2);
                if (num != null && num2 != null) {
                    int intValue = num.intValue();
                    if (intValue == 1) {
                        long j = cursor.getLong(num2.intValue());
                        long j2 = cursor2.getLong(num2.intValue());
                        boolean booleanValue = bool.booleanValue();
                        int compare = Long.compare(j, j2);
                        return booleanValue ? compare : -compare;
                    } else if (intValue == 2) {
                        float f = cursor.getFloat(num2.intValue());
                        float f2 = cursor2.getFloat(num2.intValue());
                        boolean booleanValue2 = bool.booleanValue();
                        int compare2 = Float.compare(f, f2);
                        return booleanValue2 ? compare2 : -compare2;
                    }
                }
            }
            return 0;
        }
    }
}
