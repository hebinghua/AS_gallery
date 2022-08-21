package com.miui.gallery.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import com.miui.gallery.R;
import com.miui.gallery.ui.CleanerPhotoPickFragment;
import com.miui.gallery.ui.RawPhotoPickFragment;
import com.miui.gallery.ui.ScreenshotPhotoPickFragment;
import com.miui.gallery.ui.SimilarPhotoPickFragment;
import com.miui.gallery.ui.SlimPhotoPickFragment;
import java.util.List;
import miui.gallery.support.actionbar.ActionBarCompat;

/* loaded from: classes.dex */
public class CleanerPhotoPickActivity extends BaseActivity {
    public CleanerPhotoPickFragment mFragment;
    public Button mMoreButton;
    public Button mSelectAllButton;

    @Override // com.miui.gallery.activity.BaseActivity
    public int getFragmentContainerId() {
        return 16908290;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        startFragmentByType(getIntent().getIntExtra("extra_cleaner_photo_pick_type", -1));
        View customEndViewOnly = ActionBarCompat.setCustomEndViewOnly(this, R.layout.cleaner_photo_pick_action_layout);
        this.mSelectAllButton = (Button) customEndViewOnly.findViewById(R.id.do_select);
        this.mMoreButton = (Button) customEndViewOnly.findViewById(R.id.more);
    }

    public Button getSelectAllButton() {
        return this.mSelectAllButton;
    }

    public final void startFragmentByType(int i) {
        String str = i != 0 ? i != 1 ? i != 3 ? i != 4 ? null : "RawPhotoPickFragment" : "SimilarPhotoPickFragment" : "ScreenshotPhotoPickFragment" : "SlimPhotoPickFragment";
        if (str != null) {
            this.mFragment = (CleanerPhotoPickFragment) startFragment(CleanerPhotoPickActivity$$ExternalSyntheticLambda0.INSTANCE, str, false, true);
        }
    }

    public static /* synthetic */ Fragment lambda$startFragmentByType$0(String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1664156229:
                if (str.equals("RawPhotoPickFragment")) {
                    c = 0;
                    break;
                }
                break;
            case -930854915:
                if (str.equals("ScreenshotPhotoPickFragment")) {
                    c = 1;
                    break;
                }
                break;
            case -521425320:
                if (str.equals("SimilarPhotoPickFragment")) {
                    c = 2;
                    break;
                }
                break;
            case -126446330:
                if (str.equals("SlimPhotoPickFragment")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return new RawPhotoPickFragment();
            case 1:
                return new ScreenshotPhotoPickFragment();
            case 2:
                return new SimilarPhotoPickFragment();
            case 3:
                return new SlimPhotoPickFragment();
            default:
                return null;
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
        this.mFragment.onProvideKeyboardShortcuts(list, menu, i);
        super.onProvideKeyboardShortcuts(list, menu, i);
    }

    @Override // android.app.Activity
    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        if (this.mFragment.onKeyShortcut(i, keyEvent)) {
            return true;
        }
        return super.onKeyShortcut(i, keyEvent);
    }
}
