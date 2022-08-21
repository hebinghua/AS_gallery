package com.miui.gallery.provider;

import android.content.ContentUris;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Pair;
import com.miui.gallery.model.SecretInfo;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.cloudmanager.method.album.DoReplaceAlbumCoverMethod;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.Arrays;

/* loaded from: classes2.dex */
public class CloudUtils {
    public static final UriMatcher sUriMatcher;

    public static int getDeleteOptions(int i) {
        return i != 1 ? 0 : 1;
    }

    static {
        UriMatcher uriMatcher = new UriMatcher(-1);
        sUriMatcher = uriMatcher;
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "gallery_cloud/#", 0);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "share_image/#", 1);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "media/#", 2);
    }

    public static Bundle create(Context context, String str, Bundle bundle) {
        return context.getContentResolver().call(GalleryContract.AUTHORITY_URI, "create_album", str, bundle);
    }

    public static Bundle renameAlbum(Context context, long j, String str) {
        Bundle bundle = new Bundle();
        bundle.putLong("album_id", j);
        return context.getContentResolver().call(GalleryContract.AUTHORITY_URI, "rename_album", str, bundle);
    }

    public static long[] move(Context context, long j, boolean z, long... jArr) throws StoragePermissionMissingException {
        return addToAlbum(context, 1, j, z, jArr);
    }

    public static long[] copy(Context context, long j, long... jArr) throws StoragePermissionMissingException {
        return addToAlbum(context, 0, j, false, jArr);
    }

    public static long[] move(Context context, long j, ArrayList<Uri> arrayList) throws StoragePermissionMissingException {
        return addToAlbum(context, 1, j, arrayList);
    }

    public static long[] copy(Context context, long j, ArrayList<Uri> arrayList) throws StoragePermissionMissingException {
        return addToAlbum(context, 0, j, arrayList);
    }

    public static long[] addToAlbum(Context context, int i, long j, boolean z, long... jArr) throws StoragePermissionMissingException {
        Bundle bundle;
        Bundle bundle2 = new Bundle();
        bundle2.putInt("extra_type", i);
        bundle2.putInt("extra_src_type", 0);
        bundle2.putLongArray("extra_src_media_ids", jArr);
        bundle2.putBoolean("should_operate_sync", z);
        try {
            bundle = context.getContentResolver().call(GalleryContract.AUTHORITY_URI, "add_to_album", String.valueOf(j), bundle2);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof StoragePermissionMissingException) {
                throw ((StoragePermissionMissingException) e.getCause());
            }
            DefaultLogger.e("CloudUtils", e);
            bundle = null;
        }
        if (bundle == null) {
            return null;
        }
        return bundle.getLongArray("ids");
    }

    public static long[] addToAlbum(Context context, int i, long j, ArrayList<Uri> arrayList) throws StoragePermissionMissingException {
        Bundle bundle;
        Bundle bundle2 = new Bundle();
        bundle2.putInt("extra_type", i);
        bundle2.putInt("extra_src_type", 1);
        bundle2.putParcelableArrayList("extra_src_uris", arrayList);
        try {
            bundle = context.getContentResolver().call(GalleryContract.AUTHORITY_URI, "add_to_album", String.valueOf(j), bundle2);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof StoragePermissionMissingException) {
                throw ((StoragePermissionMissingException) e.getCause());
            }
            DefaultLogger.e("CloudUtils", e);
            bundle = null;
        }
        if (bundle == null) {
            return null;
        }
        return bundle.getLongArray("ids");
    }

    public static long[] deleteById(Context context, int i, long... jArr) throws StoragePermissionMissingException {
        return deleteById(context, 0, i, jArr);
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0056  */
    /* JADX WARN: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static long[] deleteById(android.content.Context r4, int r5, int r6, long... r7) throws com.miui.gallery.storage.exceptions.StoragePermissionMissingException {
        /*
            java.lang.String r0 = "CloudUtils"
            android.os.Bundle r1 = new android.os.Bundle
            r1.<init>()
            java.lang.String r2 = "delete_by"
            r3 = 0
            r1.putInt(r2, r3)
            java.lang.String r2 = "extra_ids"
            r1.putLongArray(r2, r7)
            int r5 = getDeleteOptions(r5)
            java.lang.String r2 = "extra_delete_options"
            r1.putInt(r2, r5)
            java.lang.String r5 = "extra_delete_reason"
            r1.putInt(r5, r6)
            r5 = 0
            android.content.ContentResolver r4 = r4.getContentResolver()     // Catch: java.lang.RuntimeException -> L46
            android.net.Uri r2 = com.miui.gallery.provider.GalleryContract.AUTHORITY_URI     // Catch: java.lang.RuntimeException -> L46
            java.lang.String r3 = "delete"
            android.os.Bundle r4 = r4.call(r2, r3, r5, r1)     // Catch: java.lang.RuntimeException -> L46
            java.lang.String r1 = "[%s] => reques deleteById success, deleteReason = [%s], result = [%s]"
            java.lang.String r7 = java.util.Arrays.toString(r7)     // Catch: java.lang.RuntimeException -> L44
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch: java.lang.RuntimeException -> L44
            if (r4 != 0) goto L3c
            java.lang.String r2 = "null"
            goto L40
        L3c:
            java.lang.String r2 = r4.toString()     // Catch: java.lang.RuntimeException -> L44
        L40:
            com.miui.gallery.util.logger.DefaultLogger.d(r0, r1, r7, r6, r2)     // Catch: java.lang.RuntimeException -> L44
            goto L53
        L44:
            r6 = move-exception
            goto L48
        L46:
            r6 = move-exception
            r4 = r5
        L48:
            java.lang.Throwable r7 = r6.getCause()
            boolean r7 = r7 instanceof com.miui.gallery.storage.exceptions.StoragePermissionMissingException
            if (r7 != 0) goto L5d
            com.miui.gallery.util.logger.DefaultLogger.e(r0, r6)
        L53:
            if (r4 != 0) goto L56
            goto L5c
        L56:
            java.lang.String r5 = "ids"
            long[] r5 = r4.getLongArray(r5)
        L5c:
            return r5
        L5d:
            java.lang.Throwable r4 = r6.getCause()
            com.miui.gallery.storage.exceptions.StoragePermissionMissingException r4 = (com.miui.gallery.storage.exceptions.StoragePermissionMissingException) r4
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.CloudUtils.deleteById(android.content.Context, int, int, long[]):long[]");
    }

    public static long[] deleteByPath(Context context, int i, String... strArr) throws StoragePermissionMissingException {
        return deleteByPath(context, 0, i, strArr);
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0056  */
    /* JADX WARN: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static long[] deleteByPath(android.content.Context r4, int r5, int r6, java.lang.String... r7) throws com.miui.gallery.storage.exceptions.StoragePermissionMissingException {
        /*
            java.lang.String r0 = "CloudUtils"
            android.os.Bundle r1 = new android.os.Bundle
            r1.<init>()
            java.lang.String r2 = "delete_by"
            r3 = 1
            r1.putInt(r2, r3)
            java.lang.String r2 = "extra_paths"
            r1.putStringArray(r2, r7)
            int r5 = getDeleteOptions(r5)
            java.lang.String r2 = "extra_delete_options"
            r1.putInt(r2, r5)
            java.lang.String r5 = "extra_delete_reason"
            r1.putInt(r5, r6)
            r5 = 0
            android.content.ContentResolver r4 = r4.getContentResolver()     // Catch: java.lang.RuntimeException -> L46
            android.net.Uri r2 = com.miui.gallery.provider.GalleryContract.AUTHORITY_URI     // Catch: java.lang.RuntimeException -> L46
            java.lang.String r3 = "delete"
            android.os.Bundle r4 = r4.call(r2, r3, r5, r1)     // Catch: java.lang.RuntimeException -> L46
            java.lang.String r1 = "[%s] => reques deleteByPath success, deleteReason = [%s], result = [%s]"
            java.lang.String r7 = java.util.Arrays.toString(r7)     // Catch: java.lang.RuntimeException -> L44
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch: java.lang.RuntimeException -> L44
            if (r4 != 0) goto L3c
            java.lang.String r2 = "null"
            goto L40
        L3c:
            java.lang.String r2 = r4.toString()     // Catch: java.lang.RuntimeException -> L44
        L40:
            com.miui.gallery.util.logger.DefaultLogger.d(r0, r1, r7, r6, r2)     // Catch: java.lang.RuntimeException -> L44
            goto L53
        L44:
            r6 = move-exception
            goto L48
        L46:
            r6 = move-exception
            r4 = r5
        L48:
            java.lang.Throwable r7 = r6.getCause()
            boolean r7 = r7 instanceof com.miui.gallery.storage.exceptions.StoragePermissionMissingException
            if (r7 != 0) goto L5d
            com.miui.gallery.util.logger.DefaultLogger.e(r0, r6)
        L53:
            if (r4 != 0) goto L56
            goto L5c
        L56:
            java.lang.String r5 = "ids"
            long[] r5 = r4.getLongArray(r5)
        L5c:
            return r5
        L5d:
            java.lang.Throwable r4 = r6.getCause()
            com.miui.gallery.storage.exceptions.StoragePermissionMissingException r4 = (com.miui.gallery.storage.exceptions.StoragePermissionMissingException) r4
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.CloudUtils.deleteByPath(android.content.Context, int, int, java.lang.String[]):long[]");
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x004d  */
    /* JADX WARN: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static long[] deleteCloudByPath(android.content.Context r5, int r6, java.lang.String... r7) throws com.miui.gallery.storage.exceptions.StoragePermissionMissingException {
        /*
            java.lang.String r0 = "CloudUtils"
            android.os.Bundle r1 = new android.os.Bundle
            r1.<init>()
            java.lang.String r2 = "delete_by"
            r3 = 3
            r1.putInt(r2, r3)
            java.lang.String r2 = "extra_paths"
            r1.putStringArray(r2, r7)
            java.lang.String r2 = "extra_delete_reason"
            r1.putInt(r2, r6)
            r2 = 0
            android.content.ContentResolver r5 = r5.getContentResolver()     // Catch: java.lang.RuntimeException -> L3d
            android.net.Uri r3 = com.miui.gallery.provider.GalleryContract.AUTHORITY_URI     // Catch: java.lang.RuntimeException -> L3d
            java.lang.String r4 = "delete"
            android.os.Bundle r5 = r5.call(r3, r4, r2, r1)     // Catch: java.lang.RuntimeException -> L3d
            java.lang.String r1 = "[%s] => reques deleteCloudByPath success, deleteReason = [%s], result = [%s]"
            java.lang.String r7 = java.util.Arrays.toString(r7)     // Catch: java.lang.RuntimeException -> L3b
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch: java.lang.RuntimeException -> L3b
            if (r5 != 0) goto L33
            java.lang.String r3 = "null"
            goto L37
        L33:
            java.lang.String r3 = r5.toString()     // Catch: java.lang.RuntimeException -> L3b
        L37:
            com.miui.gallery.util.logger.DefaultLogger.d(r0, r1, r7, r6, r3)     // Catch: java.lang.RuntimeException -> L3b
            goto L4a
        L3b:
            r6 = move-exception
            goto L3f
        L3d:
            r6 = move-exception
            r5 = r2
        L3f:
            java.lang.Throwable r7 = r6.getCause()
            boolean r7 = r7 instanceof com.miui.gallery.storage.exceptions.StoragePermissionMissingException
            if (r7 != 0) goto L54
            com.miui.gallery.util.logger.DefaultLogger.e(r0, r6)
        L4a:
            if (r5 != 0) goto L4d
            goto L53
        L4d:
            java.lang.String r6 = "ids"
            long[] r2 = r5.getLongArray(r6)
        L53:
            return r2
        L54:
            java.lang.Throwable r5 = r6.getCause()
            com.miui.gallery.storage.exceptions.StoragePermissionMissingException r5 = (com.miui.gallery.storage.exceptions.StoragePermissionMissingException) r5
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.CloudUtils.deleteCloudByPath(android.content.Context, int, java.lang.String[]):long[]");
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0050  */
    /* JADX WARN: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static long[] deleteAlbum(android.content.Context r4, int r5, int r6, long... r7) throws com.miui.gallery.storage.exceptions.StoragePermissionMissingException {
        /*
            java.lang.String r0 = "CloudUtils"
            android.os.Bundle r1 = new android.os.Bundle
            r1.<init>()
            java.lang.String r2 = "extra_album_ids"
            r1.putLongArray(r2, r7)
            int r5 = getDeleteOptions(r5)
            java.lang.String r2 = "extra_delete_options"
            r1.putInt(r2, r5)
            java.lang.String r5 = "extra_delete_reason"
            r1.putInt(r5, r6)
            r5 = 0
            android.content.ContentResolver r4 = r4.getContentResolver()     // Catch: java.lang.RuntimeException -> L40
            android.net.Uri r2 = com.miui.gallery.provider.GalleryContract.AUTHORITY_URI     // Catch: java.lang.RuntimeException -> L40
            java.lang.String r3 = "delete_album"
            android.os.Bundle r4 = r4.call(r2, r3, r5, r1)     // Catch: java.lang.RuntimeException -> L40
            java.lang.String r1 = "[%s] => reques deleteAlbum success, deleteReason = [%s], result = [%s]"
            java.lang.String r7 = java.util.Arrays.toString(r7)     // Catch: java.lang.RuntimeException -> L3e
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch: java.lang.RuntimeException -> L3e
            if (r4 != 0) goto L36
            java.lang.String r2 = "null"
            goto L3a
        L36:
            java.lang.String r2 = r4.toString()     // Catch: java.lang.RuntimeException -> L3e
        L3a:
            com.miui.gallery.util.logger.DefaultLogger.d(r0, r1, r7, r6, r2)     // Catch: java.lang.RuntimeException -> L3e
            goto L4d
        L3e:
            r6 = move-exception
            goto L42
        L40:
            r6 = move-exception
            r4 = r5
        L42:
            java.lang.Throwable r7 = r6.getCause()
            boolean r7 = r7 instanceof com.miui.gallery.storage.exceptions.StoragePermissionMissingException
            if (r7 != 0) goto L57
            com.miui.gallery.util.logger.DefaultLogger.e(r0, r6)
        L4d:
            if (r4 != 0) goto L50
            goto L56
        L50:
            java.lang.String r5 = "ids"
            long[] r5 = r4.getLongArray(r5)
        L56:
            return r5
        L57:
            java.lang.Throwable r4 = r6.getCause()
            com.miui.gallery.storage.exceptions.StoragePermissionMissingException r4 = (com.miui.gallery.storage.exceptions.StoragePermissionMissingException) r4
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.CloudUtils.deleteAlbum(android.content.Context, int, int, long[]):long[]");
    }

    public static long[] addToFavoritesByPath(Context context, String... strArr) {
        Bundle bundle = new Bundle();
        bundle.putInt("operation_type", 1);
        bundle.putInt("add_remove_by", 2);
        bundle.putStringArray("extra_src_paths", strArr);
        return context.getContentResolver().call(GalleryContract.AUTHORITY_URI, "add_remove_favorite", (String) null, bundle).getLongArray("ids");
    }

    public static long[] removeFromFavoritesByPath(Context context, String... strArr) {
        Bundle bundle = new Bundle();
        bundle.putInt("operation_type", 2);
        bundle.putInt("add_remove_by", 2);
        bundle.putStringArray("extra_src_paths", strArr);
        return context.getContentResolver().call(GalleryContract.AUTHORITY_URI, "add_remove_favorite", (String) null, bundle).getLongArray("ids");
    }

    public static long[] addToFavoritesById(Context context, long... jArr) {
        Bundle bundle = new Bundle();
        bundle.putInt("operation_type", 1);
        bundle.putInt("add_remove_by", 1);
        bundle.putLongArray("extra_src_media_ids", jArr);
        return context.getContentResolver().call(GalleryContract.AUTHORITY_URI, "add_remove_favorite", (String) null, bundle).getLongArray("ids");
    }

    public static long[] addToFavoritesByUri(Context context, ArrayList<Uri> arrayList) {
        Bundle bundle = new Bundle();
        bundle.putInt("operation_type", 1);
        bundle.putInt("add_remove_by", 4);
        bundle.putParcelableArrayList("extra_src_uris", arrayList);
        return context.getContentResolver().call(GalleryContract.AUTHORITY_URI, "add_remove_favorite", (String) null, bundle).getLongArray("ids");
    }

    public static long[] removeFromFavoritesByUri(Context context, ArrayList<Uri> arrayList) {
        Bundle bundle = new Bundle();
        bundle.putInt("operation_type", 2);
        bundle.putInt("add_remove_by", 4);
        bundle.putParcelableArrayList("extra_src_uris", arrayList);
        return context.getContentResolver().call(GalleryContract.AUTHORITY_URI, "add_remove_favorite", (String) null, bundle).getLongArray("ids");
    }

    public static long[] removeFromFavoritesById(Context context, long... jArr) {
        Bundle bundle = new Bundle();
        bundle.putInt("operation_type", 2);
        bundle.putInt("add_remove_by", 1);
        bundle.putLongArray("extra_src_media_ids", jArr);
        return context.getContentResolver().call(GalleryContract.AUTHORITY_URI, "add_remove_favorite", (String) null, bundle).getLongArray("ids");
    }

    public static long[] replaceFavoritesById(Context context, long... jArr) {
        Bundle bundle = new Bundle();
        bundle.putInt("operation_type", 3);
        bundle.putInt("add_remove_by", 3);
        bundle.putLongArray("extra_src_media_ids", jArr);
        return context.getContentResolver().call(GalleryContract.AUTHORITY_URI, "add_remove_favorite", (String) null, bundle).getLongArray("ids");
    }

    public static boolean updateAlbumAttributes(Context context, Uri uri, long[] jArr, long j, boolean z, boolean z2, boolean z3) {
        Bundle bundle = new Bundle();
        bundle.putLongArray("album_id", jArr);
        bundle.putLong("attributes_bit", j);
        bundle.putBoolean("set", z);
        bundle.putBoolean("manual", z2);
        if (z3) {
            callMethodAsync(context, uri, "set_album_attributes", bundle);
            return true;
        }
        callMethodSync(context, uri, "set_album_attributes", bundle);
        return true;
    }

    public static long[] addToSecret(Context context, long... jArr) throws StoragePermissionMissingException {
        Bundle bundle;
        Bundle bundle2 = new Bundle();
        bundle2.putLongArray("extra_src_media_ids", jArr);
        try {
            bundle = context.getContentResolver().call(GalleryContract.AUTHORITY_URI, "add_secret", (String) null, bundle2);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof StoragePermissionMissingException) {
                throw ((StoragePermissionMissingException) e.getCause());
            }
            DefaultLogger.e("CloudUtils", e);
            bundle = null;
        }
        if (bundle == null) {
            return null;
        }
        return bundle.getLongArray("ids");
    }

    public static long[] addToSecret(Context context, ArrayList<Uri> arrayList) throws StoragePermissionMissingException {
        Bundle bundle;
        Bundle bundle2 = new Bundle();
        bundle2.putParcelableArrayList("extra_src_uris", arrayList);
        try {
            bundle = context.getContentResolver().call(GalleryContract.AUTHORITY_URI, "add_secret", (String) null, bundle2);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof StoragePermissionMissingException) {
                throw ((StoragePermissionMissingException) e.getCause());
            }
            DefaultLogger.e("CloudUtils", e);
            bundle = null;
        }
        if (bundle == null) {
            return null;
        }
        return bundle.getLongArray("ids");
    }

    public static SecretInfo getSecretInfo(Context context, long j, final SecretInfo secretInfo) {
        return (SecretInfo) SafeDBUtil.safeQuery(context, GalleryContract.Cloud.CLOUD_URI, new String[]{j.c, "localFile", "secretKey"}, "_id=?", new String[]{String.valueOf(j)}, (String) null, new SafeDBUtil.QueryHandler<SecretInfo>() { // from class: com.miui.gallery.provider.CloudUtils.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public SecretInfo mo1808handle(Cursor cursor) {
                if (cursor != null && cursor.moveToFirst()) {
                    SecretInfo.this.mSecretPath = cursor.getString(cursor.getColumnIndex("localFile"));
                    SecretInfo.this.mSecretKey = cursor.getBlob(cursor.getColumnIndex("secretKey"));
                }
                return SecretInfo.this;
            }
        });
    }

    public static long[] removeFromSecret(Context context, long j, long... jArr) throws StoragePermissionMissingException {
        Bundle bundle;
        Bundle bundle2 = new Bundle();
        bundle2.putLongArray("extra_src_media_ids", jArr);
        try {
            bundle = context.getContentResolver().call(GalleryContract.AUTHORITY_URI, "remove_secret", String.valueOf(j), bundle2);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof StoragePermissionMissingException) {
                throw ((StoragePermissionMissingException) e.getCause());
            }
            DefaultLogger.e("CloudUtils", e);
            bundle = null;
        }
        if (bundle == null) {
            return null;
        }
        return bundle.getLongArray("ids");
    }

    public static long renameByPath(Context context, String str, String str2) throws StoragePermissionMissingException {
        Bundle bundle;
        Bundle bundle2 = new Bundle();
        bundle2.putInt("operation_type", 1);
        bundle2.putString("extra_src_path", str);
        try {
            bundle = context.getContentResolver().call(GalleryContract.AUTHORITY_URI, "rename", str2, bundle2);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof StoragePermissionMissingException) {
                throw ((StoragePermissionMissingException) e.getCause());
            }
            DefaultLogger.e("CloudUtils", e);
            bundle = null;
        }
        if (bundle == null) {
            return 0L;
        }
        return bundle.getLong("id");
    }

    public static long renameById(Context context, long j, String str) throws StoragePermissionMissingException {
        Bundle bundle;
        Bundle bundle2 = new Bundle();
        bundle2.putInt("operation_type", 2);
        bundle2.putLong("src_cloud_id", j);
        try {
            bundle = context.getContentResolver().call(GalleryContract.AUTHORITY_URI, "rename", str, bundle2);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof StoragePermissionMissingException) {
                throw ((StoragePermissionMissingException) e.getCause());
            }
            DefaultLogger.e("CloudUtils", e);
            bundle = null;
        }
        if (bundle == null) {
            return 0L;
        }
        return bundle.getLong("id");
    }

    public static void callMethodAsync(final Context context, final Uri uri, final String str, final Bundle bundle) {
        new AsyncTask<Void, Void, Void>() { // from class: com.miui.gallery.provider.CloudUtils.2
            @Override // android.os.AsyncTask
            public Void doInBackground(Void... voidArr) {
                Context context2 = context;
                if (context2 != null) {
                    context2.getContentResolver().call(uri, str, (String) null, bundle);
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public static Bundle callMethodSync(Context context, Uri uri, String str, Bundle bundle) {
        if (context != null) {
            return context.getContentResolver().call(uri, str, (String) null, bundle);
        }
        return null;
    }

    public static Pair<String, Long[]> editPhotoTimeInfoBy(Context context, long j, long j2, String str, boolean z) throws StoragePermissionMissingException {
        Bundle bundle;
        Long[] lArr = null;
        if (!TextUtils.isEmpty(str) || j != 0) {
            Bundle bundle2 = new Bundle();
            bundle2.putLong("newtime", j2);
            bundle2.putString("photo_path", str);
            bundle2.putBoolean("is_favorites", z);
            bundle2.putLong("photo_id", j);
            int i = 1;
            if (j == 0) {
                i = 2;
            }
            bundle2.putInt("update_photo_by", i);
            try {
                bundle = context.getContentResolver().call(GalleryContract.AUTHORITY_URI, "update_photo_datetime", (String) null, bundle2);
            } catch (RuntimeException e) {
                if (e.getCause() instanceof StoragePermissionMissingException) {
                    throw ((StoragePermissionMissingException) e.getCause());
                }
                DefaultLogger.e("CloudUtils", e);
                bundle = null;
            }
            if (bundle == null) {
                return null;
            }
            if (bundle.containsKey("ids")) {
                lArr = (Long[]) Arrays.stream(bundle.getLongArray("ids")).boxed().toArray(CloudUtils$$ExternalSyntheticLambda0.INSTANCE);
            }
            return Pair.create(bundle.getString("photo_path"), lArr);
        }
        return null;
    }

    public static /* synthetic */ Long[] lambda$editPhotoTimeInfoBy$0(int i) {
        return new Long[i];
    }

    public static String queryAddressByCloudId(Context context, long j) {
        Cursor cursor = null;
        if (j == 0) {
            return null;
        }
        try {
            Cursor query = context.getContentResolver().query(ContentUris.withAppendedId(GalleryContract.Cloud.CLOUD_URI, j), new String[]{"address"}, null, null, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        String string = query.getString(0);
                        query.close();
                        return string;
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

    public static Pair<String, Byte[]> queryPhotoPathByUri(Context context, Uri uri) {
        Cursor query;
        Cursor cursor = null;
        Byte[] bArr = null;
        if (uri == null) {
            return null;
        }
        try {
            int match = sUriMatcher.match(uri);
            if (match == 0 || match == 1) {
                query = context.getContentResolver().query(uri, new String[]{InternalContract$Cloud.ALIAS_CLEAR_FIRST, "secretKey"}, null, null, null);
            } else {
                query = match != 2 ? null : context.getContentResolver().query(uri, new String[]{"alias_clear_thumbnail", "secretKey"}, null, null, null);
            }
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        byte[] blob = query.getBlob(1);
                        if (blob != null) {
                            bArr = new Byte[blob.length];
                            for (int i = 0; i < blob.length; i++) {
                                bArr[i] = Byte.valueOf(blob[i]);
                            }
                        }
                        Pair<String, Byte[]> create = Pair.create(query.getString(0), bArr);
                        query.close();
                        return create;
                    }
                } catch (Throwable th) {
                    cursor = query;
                    th = th;
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

    public static long queryAlbumAttributesByAlbumLocalPath(Context context, String str) {
        if (!TextUtils.isEmpty(str)) {
            Cursor albumByColumnnameAndValue = AlbumDataHelper.getAlbumByColumnnameAndValue(context, new String[]{"attributes"}, String.format("localPath COLLATE NOCASE IN (%s)", "\"" + str + "\""), null);
            if (albumByColumnnameAndValue != null && albumByColumnnameAndValue.moveToFirst()) {
                return albumByColumnnameAndValue.getLong(0);
            }
            return 0L;
        }
        return 0L;
    }

    public static Bundle updateAttributesIfIsRubbishTag(long j, long j2, boolean z) {
        Bundle bundle = new Bundle();
        if (Album.isRubbishAlbum(j)) {
            if (z) {
                bundle.putLong(" packageAttibuteBit", 128 | j2 | 8 | 2 | 32);
                bundle.putLong("attributeBitMask", 64 | 4 | j | 1 | 16);
            } else {
                bundle.putLong("attributeBitMask", 16 | j | 2048);
                bundle.putLong(" packageAttibuteBit", 128 | 64 | j2 | 32 | 4096);
            }
        }
        return bundle;
    }

    public static void updateAlbumSortPosition(Context context, Uri uri, long[] jArr, String[] strArr, boolean z) {
        Bundle bundle = new Bundle();
        bundle.putLongArray("param_album_id", jArr);
        bundle.putStringArray("param_sort_position", strArr);
        if (z) {
            callMethodAsync(context, uri, "change_album_sort_position", bundle);
        } else {
            callMethodSync(context, uri, "change_album_sort_position", bundle);
        }
    }

    public static ArrayList<DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult> replaceAlbumCover(Context context, Uri uri, long j, boolean z, long... jArr) {
        Bundle bundle = new Bundle();
        bundle.putLongArray("album_id", jArr);
        bundle.putLong("cover_id", j);
        if (z) {
            callMethodAsync(context, uri, "replace_album_cover", bundle);
            return null;
        }
        Bundle callMethodSync = callMethodSync(context, uri, "replace_album_cover", bundle);
        if (callMethodSync == null) {
            return null;
        }
        return callMethodSync.getParcelableArrayList("replace_album_cover_result");
    }

    public static final void parceNotifyUri(Bundle bundle, Uri... uriArr) {
        if (bundle == null || uriArr == null) {
            return;
        }
        bundle.putParcelableArray("should_notify_uri_list", uriArr);
    }

    public static final Uri[] extraNotififyUri(Bundle bundle) {
        Parcelable[] parcelableArray;
        if (bundle == null || (parcelableArray = bundle.getParcelableArray("should_notify_uri_list")) == null) {
            return null;
        }
        return (Uri[]) parcelableArray;
    }

    public static Cursor queryAlbumPhotos(Context context, Uri uri, String[] strArr, String str, String[] strArr2, String str2, int i) {
        if (i > 0) {
            try {
                uri = uri.buildUpon().appendQueryParameter("limit", String.valueOf(i)).build();
            } catch (Exception e) {
                DefaultLogger.d("CloudUtils", e.getMessage());
                return null;
            }
        }
        return context.getContentResolver().query(uri, strArr, str, strArr2, str2);
    }
}
