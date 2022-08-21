package com.miui.gallery.activity.facebaby;

import android.os.Bundle;
import android.view.KeyEvent;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.ui.RecommendFacePageFragment;

/* loaded from: classes.dex */
public class RecommendFacePageActivity extends BaseActivity {
    public RecommendFacePageFragment mRecommendFaceFragment;

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.recommend_face_page_activity);
        this.mRecommendFaceFragment = (RecommendFacePageFragment) getSupportFragmentManager().findFragmentById(R.id.recommend_face_fragment);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, android.app.Activity
    public void finish() {
        this.mRecommendFaceFragment.onActivityFinish();
        super.finish();
    }

    @Override // com.miui.gallery.activity.BaseActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            finish();
            return true;
        }
        return false;
    }
}
