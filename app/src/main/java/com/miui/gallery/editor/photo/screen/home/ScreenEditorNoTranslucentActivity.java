package com.miui.gallery.editor.photo.screen.home;

import android.os.Bundle;

/* loaded from: classes2.dex */
public class ScreenEditorNoTranslucentActivity extends ScreenEditorActivity {
    @Override // com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        this.mIsNoTranslucentActivity = true;
        super.onCreate(bundle);
    }
}
