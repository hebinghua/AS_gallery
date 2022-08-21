package com.miui.gallery.activity;

import android.content.Intent;
import android.os.Bundle;

/* loaded from: classes.dex */
public class TranslucentExternalPhotoPageActivity extends BaseExternalPhotoPageActivity {
    @Override // com.miui.gallery.activity.BaseExternalPhotoPageActivity
    public boolean isTranslucent() {
        return true;
    }

    @Override // com.miui.gallery.activity.BaseExternalPhotoPageActivity
    public void doIfFromCamera(Intent intent, Bundle bundle) {
        overridePendingTransition(0, 0);
        intent.putExtra("photo_enter_transit", true);
    }
}
