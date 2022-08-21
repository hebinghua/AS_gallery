package com.miui.gallery.editor.photo.core;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.miui.gallery.R;
import com.miui.gallery.app.fragment.AndroidFragment;
import com.miui.gallery.editor.photo.app.HostAbility;
import com.miui.gallery.editor.photo.utils.Attachable;
import com.miui.gallery.editor.utils.FolmeUtilsEditor;
import com.miui.gallery.editor.utils.LayoutOrientationTracker;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class RenderFragment extends AndroidFragment implements LayoutOrientationTracker.OnLayoutOrientationChangeListener {
    public Bitmap mBitmap;
    public Callbacks mCallbacks;
    public Button mCompareBtn;
    public Effect<?> mEffect;
    public HostAbility mHostAbility;
    public TextView mTitleTV;

    /* loaded from: classes2.dex */
    public interface Callbacks {
        void onSelected(int i);
    }

    public abstract void clear();

    public void enterImmersive() {
    }

    public void exitImmersive() {
    }

    public int getUnSupportStringRes() {
        return 0;
    }

    public abstract boolean isEmpty();

    public boolean isSupportAnimation() {
        return false;
    }

    public boolean isSupportBitmap(Bitmap bitmap) {
        return true;
    }

    public void onDiscard() {
    }

    public abstract RenderData onExport();

    @Override // com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
    public void onLayoutOrientationChange() {
    }

    public abstract List<String> onSample();

    public HostAbility getHostAbility() {
        return this.mHostAbility;
    }

    @Override // androidx.fragment.app.Fragment
    public void setArguments(Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments == null || arguments.isEmpty()) {
            super.setArguments(bundle);
        } else {
            arguments.putAll(bundle);
        }
    }

    @Override // com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        DefaultLogger.d("RenderFragment", "RenderFragment onCreate");
        if (this.mEffect == null) {
            this.mEffect = Effect.values()[getArguments().getInt("RenderFragment:effect")];
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        DefaultLogger.d("RenderFragment", "RenderFragment onViewCreated");
        this.mTitleTV = (TextView) view.findViewById(R.id.effect_title);
        int i = getArguments().getInt("RenderFragment:title_res_id", -1);
        TextView textView = this.mTitleTV;
        if (textView != null && i != -1) {
            textView.setText(i);
            this.mTitleTV.requestFocus();
        }
        Button button = (Button) view.findViewById(R.id.compare_btn);
        this.mCompareBtn = button;
        FolmeUtilsEditor.animButton(button);
    }

    public void showCompareButton() {
        Button button = this.mCompareBtn;
        if (button == null) {
            return;
        }
        button.setVisibility(0);
    }

    public void setCompareTouchListener(View.OnTouchListener onTouchListener) {
        Button button = this.mCompareBtn;
        if (button == null) {
            return;
        }
        button.setOnTouchListener(onTouchListener);
    }

    public void hideCompareButton() {
        Button button = this.mCompareBtn;
        if (button == null) {
            return;
        }
        button.setVisibility(8);
    }

    public TextView getTitleView() {
        return this.mTitleTV;
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof Attachable) {
            ((Attachable) parentFragment).attach(this);
        }
        if (activity instanceof HostAbility) {
            this.mHostAbility = (HostAbility) activity;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        this.mBitmap = null;
        release();
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public final RenderData export() {
        if (isEmpty()) {
            return null;
        }
        RenderData onExport = onExport();
        onExport.mType = this.mEffect;
        return onExport;
    }

    public final List<String> sample() {
        return onSample();
    }

    public final void setCallbacks(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    public final void onSelected(int i) {
        Callbacks callbacks = this.mCallbacks;
        if (callbacks != null) {
            callbacks.onSelected(i);
        }
    }

    public void release() {
        clear();
    }
}
