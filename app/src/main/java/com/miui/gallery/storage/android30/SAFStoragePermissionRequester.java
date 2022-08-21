package com.miui.gallery.storage.android30;

import android.content.Context;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.android.internal.storage.StorageInfo;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.android28.SAFStoragePermissionRequester;
import com.miui.gallery.storage.constants.GalleryStorageConstants;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.StorageUtils;
import java.io.File;
import java.util.Map;

/* loaded from: classes2.dex */
public class SAFStoragePermissionRequester extends com.miui.gallery.storage.android28.SAFStoragePermissionRequester {
    @Override // com.miui.gallery.storage.android28.SAFStoragePermissionRequester, com.miui.gallery.storage.utils.IStorageFunction
    public boolean handles(Context context, int i, int i2) {
        switch (i) {
            case 26:
            case 27:
            case 28:
                return false;
            case 29:
                return i2 > 29;
            default:
                return true;
        }
    }

    @Override // com.miui.gallery.storage.android28.SAFStoragePermissionRequester
    /* renamed from: createRequestProcessor  reason: collision with other method in class */
    public RequestProcessor mo1404createRequestProcessor() {
        return new RequestProcessor();
    }

    /* loaded from: classes2.dex */
    public static class PermissionRequest extends SAFStoragePermissionRequester.PermissionRequest {
        public PermissionRequest(RequestProcessor requestProcessor, FragmentActivity fragmentActivity, Fragment fragment, String str, Map<String, Object> map, IStoragePermissionStrategy.Permission permission) {
            super(requestProcessor, fragmentActivity, fragment, str, map, permission);
        }

        @Override // com.miui.gallery.storage.android28.SAFStoragePermissionRequester.PermissionRequest
        public SAFStoragePermissionRequester.PermissionRequest prepare() {
            String str;
            SAFStoragePermissionRequester.PermissionRequest translate2InsertDirectoryRequest;
            StorageInfo storageInfo = StorageUtils.getStorageInfo(getContext(), getPath());
            if (storageInfo == null || storageInfo.isXspace()) {
                return SAFStoragePermissionRequester.PermissionRequest.INVALID_REQUEST;
            }
            if (StorageSolutionProvider.get().checkPermission(getPath(), getType()).granted) {
                return SAFStoragePermissionRequester.PermissionRequest.INVALID_REQUEST;
            }
            String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("SAFStoragePermissionRequester", "prepare");
            StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
            String path = getPath();
            IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.QUERY_DIRECTORY;
            DocumentFile documentFile = storageStrategyManager.getDocumentFile(path, permission, appendInvokerTag);
            if (documentFile != null && documentFile.exists()) {
                return !documentFile.isDirectory() ? getRequestProcessor().mo1405createPermissionRequest(getHostActivity(), getHostFragment(), BaseFileUtils.getAbsoluteRootParentFolderPath(getHostActivity(), getPath()), getParams(), getType()) : this;
            }
            String path2 = getPath();
            String str2 = File.separator;
            if (path2.endsWith(str2)) {
                SAFStoragePermissionRequester.PermissionRequest translate2InsertDirectoryRequest2 = SAFStoragePermissionRequester.translate2InsertDirectoryRequest(getRequestProcessor(), getHostActivity(), getHostFragment(), getPath(), getParams());
                return translate2InsertDirectoryRequest2 != null ? translate2InsertDirectoryRequest2 : this;
            }
            String parentFolderPath = BaseFileUtils.getParentFolderPath(getPath());
            String str3 = GalleryStorageConstants.KEY_FOR_EMPTY_RELATIVE_PATH;
            if (TextUtils.equals(str3, StorageUtils.getRelativePath(getContext(), parentFolderPath))) {
                str = BaseFileUtils.concat(parentFolderPath, str3);
            } else {
                str = parentFolderPath + str2;
            }
            DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(str, permission, appendInvokerTag);
            if ((documentFile2 == null || !documentFile2.exists()) && (translate2InsertDirectoryRequest = SAFStoragePermissionRequester.translate2InsertDirectoryRequest(getRequestProcessor(), getHostActivity(), getHostFragment(), str, getParams())) != null) {
                getRequestProcessor().submit(getHostActivity(), getHostFragment(), getPath(), getParams(), getType());
                return translate2InsertDirectoryRequest;
            }
            return getRequestProcessor().mo1405createPermissionRequest(getHostActivity(), getHostFragment(), BaseFileUtils.getAbsoluteRootParentFolderPath(getContext(), str), getParams(), getType());
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0034, code lost:
        return com.miui.gallery.storage.android28.SAFStoragePermissionRequester.PermissionRequest.INVALID_REQUEST;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.miui.gallery.storage.android28.SAFStoragePermissionRequester.PermissionRequest translate2InsertDirectoryRequest(com.miui.gallery.storage.android28.SAFStoragePermissionRequester.RequestProcessor r6, androidx.fragment.app.FragmentActivity r7, androidx.fragment.app.Fragment r8, java.lang.String r9, java.util.Map<java.lang.String, java.lang.Object> r10) {
        /*
            java.lang.String r0 = "SAFStoragePermissionRequester"
            java.lang.String r1 = "translate2InsertDirectoryRequest"
            java.lang.String r0 = com.miui.gallery.util.FileHandleRecordHelper.appendInvokerTag(r0, r1)
            com.miui.gallery.storage.strategies.base.StorageStrategyManager r1 = com.miui.gallery.storage.StorageSolutionProvider.get()
            com.miui.gallery.storage.strategies.IStoragePermissionStrategy$Permission r2 = com.miui.gallery.storage.strategies.IStoragePermissionStrategy.Permission.INSERT_DIRECTORY
            androidx.documentfile.provider.DocumentFile r0 = r1.getDocumentFile(r9, r2, r0)
            if (r0 != 0) goto L44
            java.io.File r0 = new java.io.File
            r0.<init>(r9)
        L19:
            if (r0 == 0) goto L30
            java.io.File r9 = r0.getParentFile()
            if (r9 == 0) goto L30
            java.io.File r9 = r0.getParentFile()
            boolean r9 = r9.exists()
            if (r9 != 0) goto L30
            java.io.File r0 = r0.getParentFile()
            goto L19
        L30:
            if (r0 != 0) goto L35
            com.miui.gallery.storage.android28.SAFStoragePermissionRequester$PermissionRequest r6 = com.miui.gallery.storage.android28.SAFStoragePermissionRequester.PermissionRequest.INVALID_REQUEST
            return r6
        L35:
            java.lang.String r3 = r0.getAbsolutePath()
            com.miui.gallery.storage.strategies.IStoragePermissionStrategy$Permission r5 = com.miui.gallery.storage.strategies.IStoragePermissionStrategy.Permission.INSERT_DIRECTORY
            r0 = r6
            r1 = r7
            r2 = r8
            r4 = r10
            com.miui.gallery.storage.android28.SAFStoragePermissionRequester$PermissionRequest r6 = r0.mo1405createPermissionRequest(r1, r2, r3, r4, r5)
            return r6
        L44:
            r6 = 0
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.storage.android30.SAFStoragePermissionRequester.translate2InsertDirectoryRequest(com.miui.gallery.storage.android28.SAFStoragePermissionRequester$RequestProcessor, androidx.fragment.app.FragmentActivity, androidx.fragment.app.Fragment, java.lang.String, java.util.Map):com.miui.gallery.storage.android28.SAFStoragePermissionRequester$PermissionRequest");
    }

    /* loaded from: classes2.dex */
    public static class RequestProcessor extends SAFStoragePermissionRequester.RequestProcessor {
        @Override // com.miui.gallery.storage.android28.SAFStoragePermissionRequester.RequestProcessor
        /* renamed from: createPermissionRequest */
        public /* bridge */ /* synthetic */ SAFStoragePermissionRequester.PermissionRequest mo1405createPermissionRequest(FragmentActivity fragmentActivity, Fragment fragment, String str, Map map, IStoragePermissionStrategy.Permission permission) {
            return mo1405createPermissionRequest(fragmentActivity, fragment, str, (Map<String, Object>) map, permission);
        }

        @Override // com.miui.gallery.storage.android28.SAFStoragePermissionRequester.RequestProcessor
        /* renamed from: createPermissionRequest  reason: collision with other method in class */
        public PermissionRequest mo1405createPermissionRequest(FragmentActivity fragmentActivity, Fragment fragment, String str, Map<String, Object> map, IStoragePermissionStrategy.Permission permission) {
            return new PermissionRequest(this, fragmentActivity, fragment, str, map, permission);
        }
    }
}
