package com.miui.gallery.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.miui.gallery.app.activity.GalleryActivity;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.market.MacaronInstaller;
import com.miui.settings.Settings;
import java.util.HashMap;

/* loaded from: classes.dex */
public class MacaronsActivity extends GalleryActivity {
    @Override // com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        boolean isPackageInstalled = BaseMiscUtil.isPackageInstalled(MacaronInstaller.getInstance().getPackageName());
        if (isPackageInstalled) {
            Uri data = intent.getData();
            Intent jumpIntent = MacaronInstaller.getInstance().getJumpIntent();
            jumpIntent.putExtra("DARK_MODE", BaseMiscUtil.isNightMode(this));
            jumpIntent.putExtra("OUTPUT_PATH", StorageUtils.getPathInPrimaryStorage(StorageConstants.RELATIVE_DIRECTORY_CREATIVE));
            jumpIntent.putExtra("COUNTRY_CODE", Settings.getRegion());
            jumpIntent.setData(data);
            jumpIntent.setFlags(1);
            startActivityForResult(jumpIntent, 56);
        } else {
            finishPage();
        }
        HashMap hashMap = new HashMap();
        hashMap.put("state", "isInstall_" + isPackageInstalled);
        SamplingStatHelper.recordCountEvent("macaron", "start_app", hashMap);
    }

    @Override // com.miui.gallery.app.activity.MiuiActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 56 && intent != null) {
            IntentUtil.gotoPhotoPage(this, intent.getStringExtra("RESULT_PATH"));
        }
        setResult(i2);
        finishPage();
    }

    public void finishPage() {
        finish();
    }
}
