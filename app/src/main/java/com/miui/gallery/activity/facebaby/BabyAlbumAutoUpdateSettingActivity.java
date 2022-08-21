package com.miui.gallery.activity.facebaby;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import com.miui.gallery.R;
import com.miui.gallery.app.activity.MiuiActivity;

/* loaded from: classes.dex */
public class BabyAlbumAutoUpdateSettingActivity extends MiuiActivity {
    public BabyAlbumAutoUpdateSettingsFragment mFragment;

    @Override // com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.preference_container);
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        BabyAlbumAutoUpdateSettingsFragment babyAlbumAutoUpdateSettingsFragment = new BabyAlbumAutoUpdateSettingsFragment();
        this.mFragment = babyAlbumAutoUpdateSettingsFragment;
        beginTransaction.replace(R.id.preference_container, babyAlbumAutoUpdateSettingsFragment).commit();
    }

    public final void setResult() {
        Intent intent = new Intent();
        intent.putExtra("associate_people_face_local_id", this.mFragment.getPeopleFaceLocalId());
        intent.putExtra("baby-info", this.mFragment.getBabyInfo());
        setResult(-1, intent);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        setResult();
        super.onBackPressed();
    }

    @Override // android.app.Activity
    public boolean onNavigateUp() {
        setResult();
        return super.onNavigateUp();
    }
}
