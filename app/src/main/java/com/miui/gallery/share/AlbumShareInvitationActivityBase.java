package com.miui.gallery.share;

import android.content.DialogInterface;
import android.os.Bundle;
import com.miui.gallery.app.activity.AndroidActivity;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class AlbumShareInvitationActivityBase extends AndroidActivity {
    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setBackgroundDrawableResource(17170445);
    }

    /* loaded from: classes2.dex */
    public static class OnBlockMessageCancelled implements DialogInterface.OnCancelListener {
        public final WeakReference<AndroidActivity> mActivityRef;

        public OnBlockMessageCancelled(AndroidActivity androidActivity) {
            this.mActivityRef = new WeakReference<>(androidActivity);
        }

        @Override // android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface dialogInterface) {
            AndroidActivity androidActivity = this.mActivityRef.get();
            if (androidActivity != null) {
                androidActivity.finish();
            }
        }
    }
}
