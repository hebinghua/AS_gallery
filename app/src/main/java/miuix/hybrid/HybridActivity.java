package miuix.hybrid;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import java.util.HashSet;
import java.util.Set;
import miuix.appcompat.app.AppCompatActivity;
import miuix.internal.hybrid.HybridManager;

/* loaded from: classes3.dex */
public class HybridActivity extends AppCompatActivity {
    public static final String EXTRA_URL = "com.miui.sdk.hybrid.extra.URL";
    private Set<HybridView> mHybridViews = new HashSet();

    public boolean autoLoadExtraUrlFromIntent() {
        return true;
    }

    public int getConfigResId() {
        return 0;
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(getContentView());
        View findViewById = findViewById(R.id.hybrid_view);
        if (findViewById == null || !(findViewById instanceof HybridView)) {
            return;
        }
        if (bundle != null) {
            registerHybridView(findViewById);
            for (HybridView hybridView : this.mHybridViews) {
                hybridView.restoreState(bundle);
            }
        } else if (autoLoadExtraUrlFromIntent()) {
            Intent intent = getIntent();
            String str = null;
            if (intent != null) {
                str = intent.getStringExtra(EXTRA_URL);
            }
            registerHybridView(findViewById, getConfigResId(), str);
        } else {
            registerHybridView(findViewById);
        }
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        for (HybridView hybridView : this.mHybridViews) {
            hybridView.saveState(bundle);
        }
    }

    public View getContentView() {
        return getLayoutInflater().inflate(R.layout.hybrid_main, (ViewGroup) null);
    }

    public final void registerHybridView(View view) {
        registerHybridView(view, getConfigResId());
    }

    public final void registerHybridView(View view, int i) {
        registerHybridView(view, i, null);
    }

    public final void registerHybridView(View view, int i, String str) {
        if (!(view instanceof HybridView)) {
            throw new IllegalArgumentException("view being registered is not a hybrid view");
        }
        HybridView hybridView = (HybridView) view;
        HybridManager hybridManager = new HybridManager(this, hybridView);
        hybridView.setHybridManager(hybridManager);
        this.mHybridViews.add(hybridView);
        hybridManager.init(i, str);
    }

    public final void unregisterHybridView(View view) {
        if (!(view instanceof HybridView)) {
            throw new IllegalArgumentException("view being unregistered is not a hybrid view");
        }
        this.mHybridViews.remove(view);
    }

    private void destroyHybridView() {
        for (HybridView hybridView : this.mHybridViews) {
            if (hybridView != null) {
                if (hybridView.getParent() != null) {
                    ((ViewGroup) hybridView.getParent()).removeView(hybridView);
                }
                hybridView.destroy();
            }
        }
        this.mHybridViews.clear();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        for (HybridView hybridView : this.mHybridViews) {
            hybridView.getHybridManager().onStart();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        for (HybridView hybridView : this.mHybridViews) {
            hybridView.getHybridManager().onResume();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        for (HybridView hybridView : this.mHybridViews) {
            hybridView.getHybridManager().onPause();
        }
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        for (HybridView hybridView : this.mHybridViews) {
            hybridView.getHybridManager().onStop();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        for (HybridView hybridView : this.mHybridViews) {
            hybridView.getHybridManager().onDestroy();
        }
        destroyHybridView();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        for (HybridView hybridView : this.mHybridViews) {
            hybridView.getHybridManager().onActivityResult(i, i2, intent);
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            for (HybridView hybridView : this.mHybridViews) {
                if (hybridView.canGoBack() && !hybridView.getHybridManager().isDetached()) {
                    hybridView.goBack();
                    return true;
                }
            }
        }
        return super.onKeyDown(i, keyEvent);
    }
}
