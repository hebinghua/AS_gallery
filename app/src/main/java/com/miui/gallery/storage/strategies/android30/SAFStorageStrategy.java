package com.miui.gallery.storage.strategies.android30;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.android28.SAFStorageStrategy;
import com.miui.gallery.storage.strategies.android30.preference.RSAFPreferences;
import com.miui.gallery.storage.strategies.base.StrategyType;
import com.miui.gallery.storage.utils.ISAFStoragePermissionRequester;
import com.miui.gallery.storage.utils.Utils;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;

@StrategyType(type = 2)
/* loaded from: classes2.dex */
public class SAFStorageStrategy extends com.miui.gallery.storage.strategies.android28.SAFStorageStrategy {
    public SAFStorageStrategy(Context context, ISAFStoragePermissionRequester iSAFStoragePermissionRequester) {
        super(context, iSAFStoragePermissionRequester);
    }

    @Override // com.miui.gallery.storage.strategies.android28.SAFStorageStrategy
    /* renamed from: createDocumentFileProvider  reason: collision with other method in class */
    public TreeDocumentFileProvider mo1412createDocumentFileProvider() {
        return new TreeDocumentFileProvider(this.mApplicationContext);
    }

    /* renamed from: com.miui.gallery.storage.strategies.android30.SAFStorageStrategy$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission;

        static {
            int[] iArr = new int[IStoragePermissionStrategy.Permission.values().length];
            $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission = iArr;
            try {
                iArr[IStoragePermissionStrategy.Permission.INSERT_DIRECTORY.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.QUERY_DIRECTORY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.UPDATE_DIRECTORY.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.DELETE_DIRECTORY.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.QUERY.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.APPEND.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.DELETE.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.INSERT.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.UPDATE.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    @Override // com.miui.gallery.storage.strategies.android28.SAFStorageStrategy, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public IStoragePermissionStrategy.PermissionResult checkPermission(String str, IStoragePermissionStrategy.Permission permission) {
        IStoragePermissionStrategy.PermissionResult permissionResult = new IStoragePermissionStrategy.PermissionResult(str, permission);
        int[] iArr = AnonymousClass1.$SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission;
        int i = iArr[permission.ordinal()];
        if (i == 1) {
            String str2 = File.separator;
            if (!str.endsWith(str2)) {
                str = str + str2;
            }
            if (new File(BaseFileUtils.getAbsoluteRootParentFolderPath(this.mApplicationContext, str)).exists()) {
                permissionResult.granted = true;
                return permissionResult;
            } else if (!Utils.isUnderStandardCollection(StorageUtils.getRelativePath(this.mApplicationContext, str))) {
                permissionResult.applicable = true;
                return permissionResult;
            } else {
                permissionResult.granted = true;
                return permissionResult;
            }
        } else if (i == 2) {
            permissionResult.granted = true;
            return permissionResult;
        } else {
            if (i != 3 && i != 4) {
                if (this.mDocumentFileProvider.generate(str) == null) {
                    if (Utils.isUnderOtherAppSpecificDirectory(this.mApplicationContext, str) || TextUtils.isEmpty(StorageUtils.getRelativePath(this.mApplicationContext, BaseFileUtils.getParentFolderPath(str)))) {
                        return permissionResult;
                    }
                    permissionResult.applicable = true;
                    return permissionResult;
                }
                switch (iArr[permission.ordinal()]) {
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                        permissionResult.granted = true;
                        break;
                    default:
                        return permissionResult;
                }
            }
            return permissionResult;
        }
    }

    /* loaded from: classes2.dex */
    public static class TreeDocumentFileProvider extends SAFStorageStrategy.TreeDocumentFileProvider {
        public TreeDocumentFileProvider(Context context) {
            super(context);
        }

        /* JADX WARN: Removed duplicated region for block: B:8:0x003f A[Catch: Exception -> 0x009c, TRY_LEAVE, TryCatch #0 {Exception -> 0x009c, blocks: (B:3:0x0011, B:5:0x0021, B:6:0x0039, B:8:0x003f, B:11:0x0052, B:12:0x0058, B:14:0x0079, B:18:0x008f, B:15:0x007e), top: B:31:0x0011 }] */
        @Override // com.miui.gallery.storage.strategies.android28.SAFStorageStrategy.TreeDocumentFileProvider
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void init() {
            /*
                r7 = this;
                java.util.Hashtable r0 = new java.util.Hashtable
                r0.<init>()
                r7.mCache = r0
                java.util.LinkedList r0 = new java.util.LinkedList
                r0.<init>()
                r7.mObservers = r0
                com.miui.gallery.storage.strategies.android30.preference.RSAFSharedPreferenceHelper.registerOnSharedPreferenceChangeListener(r7)
                android.content.Context r0 = r7.mContext     // Catch: java.lang.Exception -> L9c
                android.content.ContentResolver r0 = r0.getContentResolver()     // Catch: java.lang.Exception -> L9c
                java.util.List r0 = r0.getPersistedUriPermissions()     // Catch: java.lang.Exception -> L9c
                boolean r1 = com.miui.gallery.util.BaseMiscUtil.isValid(r0)     // Catch: java.lang.Exception -> L9c
                if (r1 == 0) goto Lde
                java.util.stream.Stream r0 = r0.stream()     // Catch: java.lang.Exception -> L9c
                com.miui.gallery.storage.strategies.android30.SAFStorageStrategy$TreeDocumentFileProvider$$ExternalSyntheticLambda0 r1 = com.miui.gallery.storage.strategies.android30.SAFStorageStrategy$TreeDocumentFileProvider$$ExternalSyntheticLambda0.INSTANCE     // Catch: java.lang.Exception -> L9c
                java.util.stream.Stream r0 = r0.map(r1)     // Catch: java.lang.Exception -> L9c
                java.util.stream.Collector r1 = java.util.stream.Collectors.toList()     // Catch: java.lang.Exception -> L9c
                java.lang.Object r0 = r0.collect(r1)     // Catch: java.lang.Exception -> L9c
                java.util.List r0 = (java.util.List) r0     // Catch: java.lang.Exception -> L9c
                java.util.Iterator r0 = r0.iterator()     // Catch: java.lang.Exception -> L9c
            L39:
                boolean r1 = r0.hasNext()     // Catch: java.lang.Exception -> L9c
                if (r1 == 0) goto Lde
                java.lang.Object r1 = r0.next()     // Catch: java.lang.Exception -> L9c
                android.net.Uri r1 = (android.net.Uri) r1     // Catch: java.lang.Exception -> L9c
                java.lang.String r2 = r1.getAuthority()     // Catch: java.lang.Exception -> L9c
                java.lang.String r3 = "com.android.externalstorage.documents"
                boolean r2 = android.text.TextUtils.equals(r2, r3)     // Catch: java.lang.Exception -> L9c
                if (r2 != 0) goto L52
                goto L39
            L52:
                android.content.Context r2 = r7.mContext     // Catch: java.lang.IllegalArgumentException -> L39 java.lang.Exception -> L9c
                androidx.documentfile.provider.DocumentFile r2 = androidx.documentfile.provider.DocumentFile.fromTreeUri(r2, r1)     // Catch: java.lang.IllegalArgumentException -> L39 java.lang.Exception -> L9c
                java.lang.String r3 = r1.getLastPathSegment()     // Catch: java.lang.Exception -> L9c
                r4 = 0
                r5 = 58
                int r6 = r3.indexOf(r5)     // Catch: java.lang.Exception -> L9c
                java.lang.String r4 = r3.substring(r4, r6)     // Catch: java.lang.Exception -> L9c
                int r5 = r3.indexOf(r5)     // Catch: java.lang.Exception -> L9c
                r6 = 1
                int r5 = r5 + r6
                java.lang.String r3 = r3.substring(r5)     // Catch: java.lang.Exception -> L9c
                java.lang.String r5 = "primary"
                boolean r5 = android.text.TextUtils.equals(r4, r5)     // Catch: java.lang.Exception -> L9c
                if (r5 == 0) goto L7e
                java.lang.String r3 = com.miui.gallery.util.StorageUtils.getPathInPrimaryStorage(r3)     // Catch: java.lang.Exception -> L9c
                goto L8f
            L7e:
                java.lang.String r3 = com.miui.gallery.util.StorageUtils.getPathInSecondaryStorage(r3)     // Catch: java.lang.Exception -> L9c
                android.content.Context r5 = r7.mContext     // Catch: java.lang.Exception -> L9c
                java.lang.String r5 = com.miui.gallery.util.StorageUtils.getVolumeName(r5, r3, r6)     // Catch: java.lang.Exception -> L9c
                boolean r4 = r4.equalsIgnoreCase(r5)     // Catch: java.lang.Exception -> L9c
                if (r4 != 0) goto L8f
                goto L39
            L8f:
                java.util.Map<java.lang.String, androidx.documentfile.provider.DocumentFile> r4 = r7.mCache     // Catch: java.lang.Exception -> L9c
                r4.put(r3, r2)     // Catch: java.lang.Exception -> L9c
                java.lang.String r1 = r1.toString()     // Catch: java.lang.Exception -> L9c
                com.miui.gallery.storage.strategies.android30.preference.RSAFSharedPreferenceHelper.putString(r3, r1)     // Catch: java.lang.Exception -> L9c
                goto L39
            L9c:
                java.util.Map r0 = com.miui.gallery.storage.strategies.android30.preference.RSAFSharedPreferenceHelper.getAll()
                java.util.Set r0 = r0.entrySet()
                java.util.Iterator r0 = r0.iterator()
            La8:
                boolean r1 = r0.hasNext()
                if (r1 == 0) goto Lde
                java.lang.Object r1 = r0.next()
                java.util.Map$Entry r1 = (java.util.Map.Entry) r1
                java.lang.Object r2 = r1.getKey()
                java.lang.String r2 = (java.lang.String) r2
                java.lang.Object r1 = r1.getValue()
                java.lang.String r1 = java.lang.String.valueOf(r1)
                android.content.Context r3 = r7.mContext
                android.net.Uri r1 = android.net.Uri.parse(r1)
                androidx.documentfile.provider.DocumentFile r1 = androidx.documentfile.provider.DocumentFile.fromTreeUri(r3, r1)
                if (r1 == 0) goto Lda
                boolean r3 = r1.exists()
                if (r3 == 0) goto Lda
                java.util.Map<java.lang.String, androidx.documentfile.provider.DocumentFile> r3 = r7.mCache
                r3.put(r2, r1)
                goto La8
            Lda:
                com.miui.gallery.storage.strategies.android30.preference.RSAFSharedPreferenceHelper.remove(r2)
                goto La8
            Lde:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.storage.strategies.android30.SAFStorageStrategy.TreeDocumentFileProvider.init():void");
        }

        @Override // com.miui.gallery.storage.strategies.android28.SAFStorageStrategy.TreeDocumentFileProvider, android.content.SharedPreferences.OnSharedPreferenceChangeListener
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
            DefaultLogger.v("TreeDocumentFileProvider", "onSharedPreferenceChanged [%s]", str);
            String uri = RSAFPreferences.getUri(str);
            if (TextUtils.isEmpty(uri)) {
                return;
            }
            storeUri(str, uri);
        }
    }
}
