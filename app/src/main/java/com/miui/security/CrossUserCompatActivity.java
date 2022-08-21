package com.miui.security;

import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import com.miui.core.SdkHelper;
import com.miui.internal.CrossUserHelper;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes3.dex */
public class CrossUserCompatActivity extends AppCompatActivity {
    public volatile ContentResolver mCrossUserContentResolver;
    public volatile ContextWrapper mCrossUserContextWrapper;
    public final Object mLockObject = new Object();

    @Override // android.content.ContextWrapper, android.content.Context
    public ContentResolver getContentResolver() {
        if (isCrossUserPick()) {
            if (this.mCrossUserContentResolver == null) {
                synchronized (this.mLockObject) {
                    if (this.mCrossUserContentResolver == null) {
                        this.mCrossUserContentResolver = CrossUserHelper.getContentResolver(this, validateCrossUser());
                    }
                }
            }
            Log.d("CrossUserPickerActivity", "getContentResolver: CrossUserContentResolver");
            return this.mCrossUserContentResolver;
        }
        Log.d("CrossUserPickerActivity", "getContentResolver: NormalContentResolver");
        return super.getContentResolver();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Context getApplicationContext() {
        if (isCrossUserPick()) {
            if (this.mCrossUserContextWrapper == null) {
                synchronized (this.mLockObject) {
                    if (this.mCrossUserContextWrapper == null) {
                        this.mCrossUserContextWrapper = new CrossUserContextWrapper(super.getApplicationContext(), validateCrossUser());
                    }
                }
            }
            Log.d("CrossUserPickerActivity", "getApplicationContext: WrapperedApplication");
            return this.mCrossUserContextWrapper;
        }
        Log.d("CrossUserPickerActivity", "getApplicationContext: NormalApplication");
        return super.getApplicationContext();
    }

    @Override // android.app.Activity, android.content.ContextWrapper, android.content.Context
    public void startActivity(Intent intent) {
        if (isCrossUserPick()) {
            intent.putExtra("android.intent.extra.picked_user_id", validateCrossUser());
        }
        super.startActivity(intent);
    }

    @Override // android.app.Activity, android.content.ContextWrapper, android.content.Context
    public void startActivity(Intent intent, Bundle bundle) {
        if (isCrossUserPick()) {
            intent.putExtra("android.intent.extra.picked_user_id", validateCrossUser());
        }
        super.startActivity(intent, bundle);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void startActivityForResult(Intent intent, int i) {
        if (isCrossUserPick()) {
            intent.putExtra("android.intent.extra.picked_user_id", validateCrossUser());
        }
        super.startActivityForResult(intent, i);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void startActivityForResult(Intent intent, int i, Bundle bundle) {
        if (isCrossUserPick()) {
            intent.putExtra("android.intent.extra.picked_user_id", validateCrossUser());
        }
        super.startActivityForResult(intent, i, bundle);
    }

    @Override // androidx.fragment.app.FragmentActivity
    public void startActivityFromFragment(Fragment fragment, Intent intent, int i, Bundle bundle) {
        if (isCrossUserPick()) {
            intent.putExtra("android.intent.extra.picked_user_id", validateCrossUser());
        }
        super.startActivityFromFragment(fragment, intent, i, bundle);
    }

    public final int validateCrossUser() {
        if (getIntent() == null) {
            return -1;
        }
        int intExtra = getIntent().getIntExtra("android.intent.extra.picked_user_id", -1);
        if (!validateCallingPackage()) {
            return -1;
        }
        return intExtra;
    }

    public final boolean validateCallingPackage() {
        return getPackageName().equals(getCallingPackage()) || CrossUserHelper.checkUidPermission(this, getCallingPackage());
    }

    public boolean isCrossUserPick() {
        return SdkHelper.IS_MIUI && CrossUserHelper.support() && validateCrossUser() != -1;
    }

    /* loaded from: classes3.dex */
    public class CrossUserContextWrapper extends ContextWrapper {
        public Context mBase;
        public int mCrossUserId;

        public CrossUserContextWrapper(Context context, int i) {
            super(context);
            this.mBase = context;
            this.mCrossUserId = i;
        }

        @Override // android.content.ContextWrapper, android.content.Context
        public ContentResolver getContentResolver() {
            return CrossUserHelper.getContentResolver(this.mBase, this.mCrossUserId);
        }
    }
}
