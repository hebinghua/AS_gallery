package com.miui.gallery.scanner.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.scanner.provider.resolver.ExternalScanResolver;
import com.miui.gallery.scanner.provider.resolver.GalleryScanEventResolver;
import com.miui.gallery.scanner.provider.resolver.IScanMethodResolver;
import com.miui.gallery.scanner.provider.resolver.InternalScanResolver;
import com.miui.gallery.scanner.provider.resolver.MiMoverEventResolver;
import com.miui.gallery.scanner.provider.resolver.MiMoverScanResolver;
import com.miui.gallery.scanner.provider.resolver.QueryExternalSupportedVersionResolver;
import com.miui.gallery.util.NoMediaUtil;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class GalleryMediaScannerProvider extends ContentProvider {
    public static final List<IScanMethodResolver> SCAN_METHOD_RESOLVER_LIST;
    public static final UriMatcher URI_MATCHER;

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        return true;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }

    static {
        UriMatcher uriMatcher = new UriMatcher(-1);
        URI_MATCHER = uriMatcher;
        uriMatcher.addURI("com.miui.gallery.scanner", "nomedia_record", 1);
        LinkedList linkedList = new LinkedList();
        SCAN_METHOD_RESOLVER_LIST = linkedList;
        linkedList.add(new InternalScanResolver());
        linkedList.add(new ExternalScanResolver());
        linkedList.add(new GalleryScanEventResolver());
        linkedList.add(new MiMoverEventResolver());
        linkedList.add(new MiMoverScanResolver());
        linkedList.add(new QueryExternalSupportedVersionResolver());
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        if (URI_MATCHER.match(uri) != 1) {
            return null;
        }
        boolean isManualHideAlbum = NoMediaUtil.isManualHideAlbum(uri.getQueryParameter("folder_path"));
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"match_record"}, 1);
        matrixCursor.addRow(new Object[]{Boolean.valueOf(isManualHideAlbum)});
        return matrixCursor;
    }

    @Override // android.content.ContentProvider
    public Bundle call(String str, String str2, Bundle bundle) {
        if (!setupExtras(bundle)) {
            return null;
        }
        for (IScanMethodResolver iScanMethodResolver : SCAN_METHOD_RESOLVER_LIST) {
            if (iScanMethodResolver.handles(str)) {
                return iScanMethodResolver.resolve(getContext(), bundle);
            }
        }
        return null;
    }

    public final boolean setupExtras(Bundle bundle) {
        String callingPackage = getCallingPackage();
        if (TextUtils.isEmpty(callingPackage)) {
            return false;
        }
        bundle.putString("param_internal_calling_package_name", callingPackage);
        return true;
    }
}
