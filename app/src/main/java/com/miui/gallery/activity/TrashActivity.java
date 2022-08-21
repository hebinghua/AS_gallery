package com.miui.gallery.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import com.miui.gallery.R;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.trash.TrashUtils;
import com.miui.gallery.ui.TrashFragment;
import com.miui.gallery.util.concurrent.ThreadManager;
import java.util.List;

/* loaded from: classes.dex */
public class TrashActivity extends BaseActivity {
    public TrashFragment mFragment;
    public Future mPullDeleteListFuture;

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.trash_activity);
        this.mFragment = (TrashFragment) getSupportFragmentManager().findFragmentById(R.id.trash_fragment);
        startPullDeleteList();
    }

    public final void startPullDeleteList() {
        this.mPullDeleteListFuture = ThreadManager.getMiscPool().submit(new ThreadPool.Job<Object>() { // from class: com.miui.gallery.activity.TrashActivity.1
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public Object mo1807run(ThreadPool.JobContext jobContext) {
                TrashUtils.pullDeleteListFromServer();
                return null;
            }
        });
    }

    @Override // com.miui.gallery.activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        Future future = this.mPullDeleteListFuture;
        if (future != null) {
            future.cancel();
            this.mPullDeleteListFuture = null;
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
