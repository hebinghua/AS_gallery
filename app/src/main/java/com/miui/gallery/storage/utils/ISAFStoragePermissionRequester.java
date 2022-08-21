package com.miui.gallery.storage.utils;

import android.content.Intent;
import android.net.Uri;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import java.util.Map;

/* loaded from: classes2.dex */
public interface ISAFStoragePermissionRequester extends IStorageFunction {
    void onHandleRequestPermissionResult(Fragment fragment, Uri uri);

    void onHandleRequestPermissionResult(FragmentActivity fragmentActivity, int i, int i2, Intent intent);

    void onHandleRequestPermissionResult(FragmentActivity fragmentActivity, Uri uri);

    void requestPermission(FragmentActivity fragmentActivity, String str, Map<String, Object> map, IStoragePermissionStrategy.Permission... permissionArr);
}
