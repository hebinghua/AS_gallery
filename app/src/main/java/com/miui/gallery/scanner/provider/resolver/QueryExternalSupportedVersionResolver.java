package com.miui.gallery.scanner.provider.resolver;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.storage.constants.AndroidStorageConstants;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class QueryExternalSupportedVersionResolver extends IScanMethodResolver {
    public static final Map<String, IExternalCallerApplication> EXTERNAL_CALLER_APPLICATIONS;

    /* loaded from: classes2.dex */
    public interface IExternalCallerApplication {
        int getSupportedVersion();
    }

    static {
        HashMap hashMap = new HashMap();
        EXTERNAL_CALLER_APPLICATIONS = hashMap;
        hashMap.put(AndroidStorageConstants.PACKAGE_NAME_MEDIA_STORE, new MediaStoreCaller());
        hashMap.put("com.android.camera", new CameraCaller());
    }

    @Override // com.miui.gallery.scanner.provider.resolver.IScanMethodResolver
    public boolean handles(String str) {
        return TextUtils.equals(str, "query_external_supported_version");
    }

    @Override // com.miui.gallery.scanner.provider.resolver.IScanMethodResolver
    public Bundle resolve(Context context, Bundle bundle) {
        IExternalCallerApplication iExternalCallerApplication = EXTERNAL_CALLER_APPLICATIONS.get(bundle.getString("param_internal_calling_package_name"));
        int supportedVersion = iExternalCallerApplication == null ? 0 : iExternalCallerApplication.getSupportedVersion();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("supported_version", supportedVersion);
        return bundle2;
    }

    /* loaded from: classes2.dex */
    public static class MediaStoreCaller implements IExternalCallerApplication {
        @Override // com.miui.gallery.scanner.provider.resolver.QueryExternalSupportedVersionResolver.IExternalCallerApplication
        public int getSupportedVersion() {
            return 1;
        }

        public MediaStoreCaller() {
        }
    }

    /* loaded from: classes2.dex */
    public static class CameraCaller implements IExternalCallerApplication {
        @Override // com.miui.gallery.scanner.provider.resolver.QueryExternalSupportedVersionResolver.IExternalCallerApplication
        public int getSupportedVersion() {
            return 3;
        }

        public CameraCaller() {
        }
    }
}
