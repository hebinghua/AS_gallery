package com.miui.gallery.loader;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import androidx.loader.content.Loader;
import com.miui.gallery.model.BaseDataSet;
import com.miui.gallery.model.CursorDataSet;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.UriUtil;
import com.miui.gallery.util.logger.TimingTracing;
import java.util.HashMap;

/* loaded from: classes2.dex */
public abstract class CursorSetLoader extends BaseLoader {
    public Loader<BaseDataSet>.ForceLoadContentObserver mObserver;

    public String getLimit() {
        return null;
    }

    public abstract String getOrder();

    public abstract String[] getProjection();

    public abstract String getSelection();

    public abstract String[] getSelectionArgs();

    public abstract String getTAG();

    public abstract Uri getUri();

    public abstract CursorDataSet wrapDataSet(Cursor cursor);

    public CursorSetLoader(Context context) {
        super(context);
        this.mObserver = new Loader.ForceLoadContentObserver();
    }

    @Override // androidx.loader.content.AsyncTaskLoader
    /* renamed from: loadInBackground */
    public CursorDataSet mo1444loadInBackground() {
        TimingTracing.beginTracing("CursorSetLoader_load", "loadInBackground");
        try {
            Cursor query = query(getUri(), getProjection(), getSelection(), getSelectionArgs(), getOrder(), getLimit());
            if (query != null) {
                TimingTracing.addSplit("query");
                try {
                    int count = query.getCount();
                    TimingTracing.addSplit("getCount: " + count);
                } catch (RuntimeException e) {
                    query.close();
                    throw e;
                }
            }
            CursorDataSet wrapDataSet = wrapDataSet(query);
            TimingTracing.addSplit("wrapDataSet");
            return wrapDataSet;
        } finally {
            long stopTracing = TimingTracing.stopTracing(null);
            if (stopTracing > 500) {
                HashMap hashMap = new HashMap();
                hashMap.put("cost_time", getSelection() + "_" + stopTracing);
                SamplingStatHelper.recordCountEvent("load_performance", getTAG(), hashMap);
            }
        }
    }

    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2, String str3) {
        ContentResolver contentResolver = getContext().getContentResolver();
        if (str3 != null) {
            uri = UriUtil.appendLimit(uri, Integer.parseInt(str3));
        }
        return contentResolver.query(uri, strArr, str, strArr2, str2, null);
    }
}
