package com.miui.gallery.storage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.core.util.Pair;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.storage.android28.SAFStoragePermissionRequester;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.android30.preference.RSAFSharedPreferenceHelper;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.StringUtils;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class DocumentProviderUtils {
    public static void requestPermission(FragmentActivity fragmentActivity, String str, IStoragePermissionStrategy.Permission permission) {
        Intent intent;
        DefaultLogger.d("DocumentProviderUtils", "[%s] request [%s] permission for [%s]", fragmentActivity.getClass().getSimpleName(), permission, str);
        if (fragmentActivity instanceof IDocumentUILauncherOwner) {
            if (permission == IStoragePermissionStrategy.Permission.INSERT_DIRECTORY) {
                ((IDocumentUILauncherOwner) fragmentActivity).getCreateDocumentDirLauncher().launch(Pair.create(BaseFileUtils.getFileTitle(BaseFileUtils.getFileName(str)), buildInitialUriForCreateDir(fragmentActivity, str)));
                return;
            } else {
                ((IDocumentUILauncherOwner) fragmentActivity).getOpenDocumentTreeLauncher().launch(buildInitialUri(fragmentActivity, str));
                return;
            }
        }
        if (permission == IStoragePermissionStrategy.Permission.INSERT_DIRECTORY) {
            intent = new Intent("android.intent.action.CREATE_DOCUMENT");
            intent.addCategory("android.intent.category.OPENABLE");
            intent.setType("vnd.android.document/directory");
            intent.putExtra("android.intent.extra.TITLE", BaseFileUtils.getFileTitle(BaseFileUtils.getFileName(str)));
            intent.putExtra("android.provider.extra.INITIAL_URI", buildInitialUriForCreateDir(fragmentActivity, str));
        } else {
            intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
            intent.putExtra("android.provider.extra.INITIAL_URI", buildInitialUri(fragmentActivity, str));
        }
        fragmentActivity.startActivityForResult(intent, 63);
    }

    public static void requestPermission(Fragment fragment, String str, IStoragePermissionStrategy.Permission permission) {
        DefaultLogger.d("DocumentProviderUtils", "[%s] request permission for [%s]", fragment.getClass().getSimpleName(), str);
        if (fragment instanceof IDocumentUILauncherOwner) {
            Context context = fragment.getContext();
            if (permission == IStoragePermissionStrategy.Permission.INSERT_DIRECTORY) {
                ((IDocumentUILauncherOwner) fragment).getCreateDocumentDirLauncher().launch(Pair.create(BaseFileUtils.getFileTitle(BaseFileUtils.getFileName(str)), buildInitialUriForCreateDir(context, str)));
                return;
            } else {
                ((IDocumentUILauncherOwner) fragment).getOpenDocumentTreeLauncher().launch(buildInitialUri(context, str));
                return;
            }
        }
        FragmentActivity activity = fragment.getActivity();
        if (activity != null) {
            requestPermission(activity, str, permission);
        } else {
            DefaultLogger.e("DocumentProviderUtils", "Activity of [%s] is null", fragment.getClass().getSimpleName());
        }
    }

    public static Uri buildInitialUri(Context context, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("content://com.android.externalstorage.documents/document/");
        sb.append(StorageUtils.isInPrimaryStorage(str) ? "primary" : StorageUtils.getVolumeName(context, str, false));
        sb.append(Uri.encode(":" + StorageUtils.getRelativePath(context, str, false)));
        return Uri.parse(sb.toString());
    }

    public static Uri buildInitialUriForCreateDir(Context context, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("content://com.android.externalstorage.documents/document/");
        sb.append(StorageUtils.isInPrimaryStorage(str) ? "primary" : StorageUtils.getVolumeName(context, str, false));
        sb.append(Uri.encode(":" + StorageUtils.getRelativePath(context, BaseFileUtils.getParentFolderPath(str), false)));
        return Uri.parse(sb.toString());
    }

    public static void onHandleRequestPermissionResult(Context context, Intent intent, SAFStoragePermissionRequester.PermissionRequest permissionRequest) {
        Uri data;
        if (intent == null || permissionRequest == null || (data = intent.getData()) == null) {
            return;
        }
        try {
            if (DocumentFile.fromTreeUri(context, data) == null) {
                return;
            }
            persistDocumentProviderUri(context, data);
        } catch (IllegalArgumentException e) {
            DefaultLogger.e("DocumentProviderUtils", e);
        }
    }

    public static void onHandleRequestPermissionResult(Context context, Uri uri) {
        if (uri == null) {
            return;
        }
        try {
            if (DocumentFile.fromTreeUri(context, uri) == null) {
                return;
            }
            persistDocumentProviderUri(context, uri);
        } catch (IllegalArgumentException e) {
            DefaultLogger.e("DocumentProviderUtils", e);
        }
    }

    public static void persistDocumentProviderUri(Context context, Uri uri) {
        String pathInSecondaryStorage;
        String pathInSecondaryStorage2;
        int i = context.getApplicationInfo().targetSdkVersion;
        switch (Build.VERSION.SDK_INT) {
            case 26:
            case 27:
            case 28:
            case 29:
                try {
                    context.getContentResolver().takePersistableUriPermission(uri, 3);
                    BaseGalleryPreferences.BaseDocumentProvider.setExternalSDCardUri(uri.toString());
                    return;
                } catch (Exception e) {
                    DefaultLogger.e("DocumentProviderUtils", e);
                    return;
                }
            case 30:
                if (i == 29) {
                    try {
                        context.getContentResolver().takePersistableUriPermission(uri, 3);
                        BaseGalleryPreferences.BaseDocumentProvider.setExternalSDCardUri(uri.toString());
                        return;
                    } catch (Exception e2) {
                        DefaultLogger.e("DocumentProviderUtils", e2);
                        return;
                    }
                }
                String lastPathSegment = uri.getLastPathSegment();
                String substring = lastPathSegment.substring(0, lastPathSegment.indexOf(58));
                String substring2 = lastPathSegment.substring(lastPathSegment.indexOf(58) + 1);
                if (StringUtils.equalsIgnoreCase(substring, "primary")) {
                    pathInSecondaryStorage = StorageUtils.getPathInPrimaryStorage(substring2);
                } else {
                    pathInSecondaryStorage = StorageUtils.getPathInSecondaryStorage(substring2);
                }
                try {
                    context.getContentResolver().takePersistableUriPermission(uri, 3);
                    RSAFSharedPreferenceHelper.putString(pathInSecondaryStorage, uri.toString());
                    return;
                } catch (Exception e3) {
                    DefaultLogger.e("DocumentProviderUtils", e3);
                    return;
                }
            default:
                String lastPathSegment2 = uri.getLastPathSegment();
                String substring3 = lastPathSegment2.substring(0, lastPathSegment2.indexOf(58));
                String substring4 = lastPathSegment2.substring(lastPathSegment2.indexOf(58) + 1);
                if (StringUtils.equalsIgnoreCase(substring3, "primary")) {
                    pathInSecondaryStorage2 = StorageUtils.getPathInPrimaryStorage(substring4);
                } else {
                    pathInSecondaryStorage2 = StorageUtils.getPathInSecondaryStorage(substring4);
                }
                try {
                    context.getContentResolver().takePersistableUriPermission(uri, 3);
                    RSAFSharedPreferenceHelper.putString(pathInSecondaryStorage2, uri.toString());
                    return;
                } catch (Exception e4) {
                    DefaultLogger.e("DocumentProviderUtils", e4);
                    return;
                }
        }
    }
}
