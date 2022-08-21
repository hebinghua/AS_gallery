package miuix.hybrid;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.HashSet;
import java.util.Set;
import miuix.appcompat.app.Fragment;
import miuix.internal.hybrid.HybridManager;

/* loaded from: classes3.dex */
public class HybridFragment extends Fragment {
    private Set<HybridView> mHybridViews = new HashSet();

    public int getConfigResId() {
        return 0;
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return getContentView();
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        Intent intent;
        super.onViewCreated(view, bundle);
        View findViewById = view.findViewById(R.id.hybrid_view);
        if (findViewById == null || !(findViewById instanceof HybridView)) {
            return;
        }
        String str = null;
        if (bundle != null) {
            str = bundle.getString(HybridActivity.EXTRA_URL);
        }
        if (str == null && (intent = getActivity().getIntent()) != null) {
            str = intent.getStringExtra(HybridActivity.EXTRA_URL);
        }
        registerHybridView((HybridView) findViewById, getConfigResId(), str);
    }

    public View getContentView() {
        return getActivity().getLayoutInflater().inflate(R.layout.hybrid_main, (ViewGroup) null);
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
        HybridManager hybridManager = new HybridManager(getActivity(), hybridView);
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

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        for (HybridView hybridView : this.mHybridViews) {
            hybridView.getHybridManager().onStart();
        }
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        for (HybridView hybridView : this.mHybridViews) {
            hybridView.getHybridManager().onResume();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        for (HybridView hybridView : this.mHybridViews) {
            hybridView.getHybridManager().onPause();
        }
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        for (HybridView hybridView : this.mHybridViews) {
            hybridView.getHybridManager().onStop();
        }
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        for (HybridView hybridView : this.mHybridViews) {
            hybridView.getHybridManager().onDestroy();
        }
        destroyHybridView();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        for (HybridView hybridView : this.mHybridViews) {
            hybridView.getHybridManager().onActivityResult(i, i2, intent);
        }
    }
}
