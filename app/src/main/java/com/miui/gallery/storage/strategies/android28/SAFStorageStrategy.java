package com.miui.gallery.storage.strategies.android28;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.system.ErrnoException;
import android.system.Os;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import androidx.documentfile.provider.GalleryRawDocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.PreferenceHelper;
import com.miui.gallery.storage.strategies.BaseStorageStrategy;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StrategyType;
import com.miui.gallery.storage.utils.ISAFStoragePermissionRequester;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@StrategyType(type = 2)
/* loaded from: classes2.dex */
public class SAFStorageStrategy extends BaseStorageStrategy {
    public final Context mApplicationContext;
    public final TreeDocumentFileProvider mDocumentFileProvider = mo1412createDocumentFileProvider();
    public final ISAFStoragePermissionRequester mSAFStoragePermissionRequester;

    public SAFStorageStrategy(Context context, ISAFStoragePermissionRequester iSAFStoragePermissionRequester) {
        this.mApplicationContext = context;
        this.mSAFStoragePermissionRequester = iSAFStoragePermissionRequester;
    }

    /* renamed from: createDocumentFileProvider */
    public TreeDocumentFileProvider mo1412createDocumentFileProvider() {
        return new TreeDocumentFileProvider(this.mApplicationContext);
    }

    public DocumentFile createDirectory(String str) {
        DocumentFile createDirectory;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        DocumentFile documentFile = this.mDocumentFileProvider.get(str);
        if (documentFile != null && documentFile.exists()) {
            return documentFile;
        }
        String parentFolderPath = BaseFileUtils.getParentFolderPath(str);
        String fileName = BaseFileUtils.getFileName(str);
        DocumentFile documentFile2 = this.mDocumentFileProvider.get(parentFolderPath);
        if (documentFile2 == null || !documentFile2.exists()) {
            documentFile2 = createDirectory(parentFolderPath);
        } else if (!documentFile2.isDirectory()) {
            return null;
        }
        if (documentFile2 == null || !documentFile2.exists()) {
            return null;
        }
        DocumentFile findFile = documentFile2.findFile(fileName);
        if (findFile == null) {
            createDirectory = documentFile2.createDirectory(fileName);
        } else if (!findFile.exists() || !findFile.isFile()) {
            return findFile;
        } else {
            if (!findFile.delete()) {
                return null;
            }
            DefaultLogger.w("SAFStorageStrategy", "[createDirectory] delete [%s] since it has the same name as the dest folder.", findFile);
            createDirectory = documentFile2.createDirectory(fileName);
        }
        if (createDirectory == null) {
            return null;
        }
        try {
            Os.chmod(str, 511);
            Os.chown(str, -1, -1);
        } catch (ErrnoException e) {
            DefaultLogger.d("SAFStorageStrategy", "[createDirectory] error in chmod or chown for [%s], reason [%s]", str, e.getMessage());
        }
        return createDirectory;
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategy
    public DocumentFile getDocumentFile(String str, IStoragePermissionStrategy.Permission permission, Bundle bundle) {
        if (!checkPermission(str, permission).granted) {
            return null;
        }
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[permission.ordinal()];
        if (i == 1 || i == 2) {
            DocumentFile createDirectory = createDirectory(BaseFileUtils.getParentFolderPath(str));
            if (createDirectory != null) {
                return createDirectory.createFile(BaseFileMimeUtil.getMimeType(str), BaseFileUtils.getFileName(str));
            }
            return null;
        } else if (i == 3) {
            return createDirectory(str);
        } else {
            if (i == 4) {
                return new GalleryRawDocumentFile(null, new File(str));
            }
            return this.mDocumentFileProvider.generate(str);
        }
    }

    /* renamed from: com.miui.gallery.storage.strategies.android28.SAFStorageStrategy$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission;

        static {
            int[] iArr = new int[IStoragePermissionStrategy.Permission.values().length];
            $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission = iArr;
            try {
                iArr[IStoragePermissionStrategy.Permission.APPEND.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.INSERT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.INSERT_DIRECTORY.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.QUERY_DIRECTORY.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.UPDATE_DIRECTORY.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.DELETE_DIRECTORY.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.UPDATE.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.QUERY.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.DELETE.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    @Override // com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public IStoragePermissionStrategy.PermissionResult checkPermission(String str, IStoragePermissionStrategy.Permission permission) {
        IStoragePermissionStrategy.PermissionResult permissionResult = new IStoragePermissionStrategy.PermissionResult(str, permission);
        if (!StorageUtils.isInSecondaryStorage(str)) {
            return permissionResult;
        }
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[permission.ordinal()];
        if (i == 3 || i == 4) {
            permissionResult.granted = true;
            return permissionResult;
        }
        if (i != 5 && i != 6) {
            permissionResult.granted = !TextUtils.isEmpty(BaseGalleryPreferences.BaseDocumentProvider.getExternalSDCardUri());
            permissionResult.applicable = true;
        }
        return permissionResult;
    }

    @Override // com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void requestPermission(FragmentActivity fragmentActivity, String str, Map<String, Object> map, IStoragePermissionStrategy.Permission... permissionArr) {
        ISAFStoragePermissionRequester iSAFStoragePermissionRequester = this.mSAFStoragePermissionRequester;
        if (iSAFStoragePermissionRequester == null) {
            return;
        }
        iSAFStoragePermissionRequester.requestPermission(fragmentActivity, str, map, permissionArr);
    }

    @Override // com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void onHandleRequestPermissionResult(FragmentActivity fragmentActivity, int i, int i2, Intent intent) {
        ISAFStoragePermissionRequester iSAFStoragePermissionRequester = this.mSAFStoragePermissionRequester;
        if (iSAFStoragePermissionRequester == null) {
            return;
        }
        iSAFStoragePermissionRequester.onHandleRequestPermissionResult(fragmentActivity, i, i2, intent);
    }

    @Override // com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void onHandleRequestPermissionResult(FragmentActivity fragmentActivity, Uri uri) {
        ISAFStoragePermissionRequester iSAFStoragePermissionRequester = this.mSAFStoragePermissionRequester;
        if (iSAFStoragePermissionRequester == null) {
            return;
        }
        iSAFStoragePermissionRequester.onHandleRequestPermissionResult(fragmentActivity, uri);
    }

    @Override // com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void onHandleRequestPermissionResult(Fragment fragment, Uri uri) {
        ISAFStoragePermissionRequester iSAFStoragePermissionRequester = this.mSAFStoragePermissionRequester;
        if (iSAFStoragePermissionRequester == null) {
            return;
        }
        iSAFStoragePermissionRequester.onHandleRequestPermissionResult(fragment, uri);
    }

    @Override // com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void registerPermissionObserver(ContentObserver contentObserver) {
        this.mDocumentFileProvider.registerObserver(contentObserver);
    }

    /* loaded from: classes2.dex */
    public static class TreeDocumentFileProvider implements SharedPreferences.OnSharedPreferenceChangeListener {
        public Map<String, DocumentFile> mCache;
        public Context mContext;
        public List<ContentObserver> mObservers;

        public TreeDocumentFileProvider(Context context) {
            this.mContext = context;
            init();
        }

        public void init() {
            this.mCache = new Hashtable();
            this.mObservers = new LinkedList();
            PreferenceHelper.registerOnSharedPreferenceChangeListener(this);
            String secondaryStoragePath = StorageUtils.getSecondaryStoragePath();
            if (TextUtils.isEmpty(secondaryStoragePath)) {
                return;
            }
            String externalSDCardUri = BaseGalleryPreferences.BaseDocumentProvider.getExternalSDCardUri();
            if (TextUtils.isEmpty(externalSDCardUri)) {
                return;
            }
            this.mCache.put(secondaryStoragePath, DocumentFile.fromTreeUri(this.mContext, Uri.parse(externalSDCardUri)));
        }

        public DocumentFile get(String str) {
            String[] split;
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            for (String str2 = str; !TextUtils.isEmpty(str2); str2 = BaseFileUtils.getParentFolderPath(str2)) {
                DocumentFile documentFile = this.mCache.get(str2);
                if (documentFile != null) {
                    String replace = str.replace(str2, "");
                    if (TextUtils.isEmpty(replace)) {
                        return documentFile;
                    }
                    for (String str3 : replace.split(File.separator)) {
                        if (!TextUtils.isEmpty(str3) && (documentFile = documentFile.findFile(str3)) == null) {
                            return null;
                        }
                    }
                    this.mCache.put(str, documentFile);
                    return documentFile;
                }
            }
            return null;
        }

        public DocumentFile generate(String str) {
            String[] split;
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            String str2 = File.separator;
            StringBuilder sb = new StringBuilder(str2);
            StringBuilder sb2 = new StringBuilder(str2);
            DocumentFile documentFile = null;
            for (String str3 : str.split(str2)) {
                if (!TextUtils.isEmpty(str3)) {
                    if (documentFile == null) {
                        sb.append(str3);
                        documentFile = this.mCache.get(sb.toString());
                        if (documentFile == null) {
                            sb.append(File.separator);
                            documentFile = this.mCache.get(sb.toString());
                        }
                    } else {
                        sb2.append(str3);
                        sb2.append(File.separator);
                    }
                }
            }
            if (documentFile == null) {
                return null;
            }
            sb2.deleteCharAt(sb2.length() - 1);
            return DocumentFile.fromTreeUri(this.mContext, DocumentsContract.buildDocumentUriUsingTree(documentFile.getUri(), DocumentsContract.getTreeDocumentId(documentFile.getUri()) + ((Object) sb2)));
        }

        @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
            DefaultLogger.v("TreeDocumentFileProvider", "onSharedPreferenceChanged [%s]", str);
            if (TextUtils.isEmpty(str)) {
                return;
            }
            String replace = str.replace(BaseGalleryPreferences.PrefKeys.DOCUMENT_PROVIDER_URI_PREFIX, "");
            String uri = BaseGalleryPreferences.BaseDocumentProvider.getUri(replace);
            if (TextUtils.isEmpty(uri)) {
                return;
            }
            storeUri(replace, uri);
        }

        public void storeUri(String str, String str2) {
            this.mCache.put(str, DocumentFile.fromTreeUri(this.mContext, Uri.parse(str2)));
            for (ContentObserver contentObserver : this.mObservers) {
                contentObserver.onChange(false);
            }
        }

        public void registerObserver(ContentObserver contentObserver) {
            this.mObservers.add(contentObserver);
        }
    }
}
