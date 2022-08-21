package com.miui.gallery.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseIntArray;
import com.miui.gallery.loader.CloudSetLoader;
import com.miui.gallery.model.CursorDataSet;
import com.miui.gallery.provider.GalleryContract;
import com.xiaomi.stat.a.j;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public class SearchResultSetLoader extends CloudSetLoader {
    public String[] mIds;

    @Override // com.miui.gallery.loader.CloudSetLoader, com.miui.gallery.loader.CursorSetLoader
    public String getOrder() {
        return null;
    }

    @Override // com.miui.gallery.loader.CloudSetLoader, com.miui.gallery.loader.CursorSetLoader
    public String[] getSelectionArgs() {
        return null;
    }

    @Override // com.miui.gallery.loader.CloudSetLoader, com.miui.gallery.loader.CursorSetLoader
    public String getTAG() {
        return "SearchResultSetLoader";
    }

    public SearchResultSetLoader(Context context, Uri uri, Bundle bundle) {
        super(context, GalleryContract.Media.URI, bundle);
        if (bundle != null) {
            this.mIds = bundle.getStringArray("photo_selection_args");
        }
        this.mUnfoldBurst = true;
    }

    @Override // com.miui.gallery.loader.CloudSetLoader, com.miui.gallery.loader.CursorSetLoader
    public String getSelection() {
        Object[] objArr = new Object[4];
        objArr[0] = j.c;
        String[] strArr = this.mIds;
        objArr[1] = (strArr == null || strArr.length <= 0) ? "" : TextUtils.join(",", strArr);
        objArr[2] = "localGroupId";
        objArr[3] = -1000L;
        return String.format("%s in (%s) AND %s != %s", objArr);
    }

    @Override // com.miui.gallery.loader.CloudSetLoader, com.miui.gallery.loader.CursorSetLoader
    public CursorDataSet wrapDataSet(Cursor cursor) {
        return new SearchResultDataSet(this.mIds, cursor, this.mInitPos);
    }

    /* loaded from: classes2.dex */
    public static class SearchResultDataSet extends CloudSetLoader.UnfoldBurstCloudDataSet {
        public List<String> mIds;
        public SparseIntArray mPositionMap;

        @Override // com.miui.gallery.loader.CloudSetLoader.UnfoldBurstCloudDataSet, com.miui.gallery.loader.CloudSetLoader.CloudDataSet, com.miui.gallery.model.BaseDataSet
        public boolean foldBurst() {
            return false;
        }

        public SearchResultDataSet(String[] strArr, Cursor cursor, int i) {
            super(cursor, i, -1L, "");
            if (strArr != null) {
                this.mIds = Arrays.asList(strArr);
                this.mPositionMap = new SparseIntArray(this.mIds.size());
                if (!isValidate()) {
                    return;
                }
                for (int i2 = 0; i2 < cursor.getCount(); i2++) {
                    cursor.moveToPosition(i2);
                    this.mPositionMap.put(this.mIds.indexOf(cursor.getString(0)), i2);
                }
            }
        }

        @Override // com.miui.gallery.model.CursorDataSet
        public boolean moveToPosition(int i) {
            SparseIntArray sparseIntArray = this.mPositionMap;
            return sparseIntArray != null && i >= 0 && i < sparseIntArray.size() && isValidate(this.mPositionMap.valueAt(i)) && this.mCursor.moveToPosition(this.mPositionMap.valueAt(i));
        }
    }
}
