package com.miui.gallery.editor.photo.app;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Property;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.RenderFragment;
import com.miui.gallery.editor.photo.core.SdkManager;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.editor.utils.LayoutOrientationTracker;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;
import miuix.appcompat.app.ProgressDialog;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public abstract class MenuFragment<F extends RenderFragment, P extends SdkProvider<?, F>> extends EditorFragment implements LayoutOrientationTracker.OnLayoutOrientationChangeListener {
    public static int sAnimAppearDelay;
    public static int sAnimAppearDuration;
    public static int sAnimDisappearDuration;
    public Callbacks mCallbacks;
    public View mDiscardBtn;
    public Effect<?> mEffect;
    public Fragment mGestureFragment;
    public HostAbility mHostAbility;
    public long mLastClickTime;
    public View.OnClickListener mOnClickListener = new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.app.MenuFragment.1
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (!MenuFragment.this.isDoubleClick()) {
                if (view.getId() == R.id.ok) {
                    MenuFragment.this.notifySave();
                } else if (view.getId() != R.id.cancel) {
                } else {
                    MenuFragment.this.notifyDiscard();
                    if (MenuFragment.this.mRenderFragment == null) {
                        return;
                    }
                    MenuFragment.this.mRenderFragment.onDiscard();
                }
            }
        }
    };
    public RenderFragment.Callbacks mRenderCallbacks = new RenderFragment.Callbacks() { // from class: com.miui.gallery.editor.photo.app.MenuFragment.3
        @Override // com.miui.gallery.editor.photo.core.RenderFragment.Callbacks
        public void onSelected(int i) {
        }
    };
    public F mRenderFragment;
    public View mSaveBtn;
    public final P mSdkProvider;
    public boolean mSingleEffectMode;
    public static final Property<View, Float> VIEW_RELATIVE_Y = new Property<View, Float>(Float.class, "relative_y") { // from class: com.miui.gallery.editor.photo.app.MenuFragment.4
        @Override // android.util.Property
        public Float get(View view) {
            return null;
        }

        @Override // android.util.Property
        public void set(View view, Float f) {
            view.setY((((View) view.getParent()).getHeight() - view.getHeight()) + f.floatValue());
        }
    };
    public static final Property<View, Float> VIEW_RELATIVE_X = new Property<View, Float>(Float.class, "relative_y") { // from class: com.miui.gallery.editor.photo.app.MenuFragment.5
        @Override // android.util.Property
        public Float get(View view) {
            return null;
        }

        @Override // android.util.Property
        public void set(View view, Float f) {
            view.setX((((View) view.getParent()).getWidth() - view.getWidth()) + f.floatValue());
        }
    };

    /* loaded from: classes2.dex */
    public interface Callbacks {
        void onDiscard(MenuFragment menuFragment);

        Bitmap onLoadOrigin();

        Bitmap onLoadPreview();

        List<RenderData> onLoadRenderData();

        void onSave(MenuFragment menuFragment);
    }

    public Fragment createGestureFragment() {
        return null;
    }

    public void hideProcessDialog() {
    }

    @Override // com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
    public void onLayoutOrientationChange() {
    }

    public void showProcessDialog() {
    }

    public HostAbility getHostAbility() {
        return this.mHostAbility;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public MenuFragment(Effect<P> effect) {
        this.mEffect = effect;
        this.mSdkProvider = (P) SdkManager.INSTANCE.getProvider(effect);
    }

    @Override // com.miui.gallery.editor.photo.app.EditorFragment, androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof HostAbility) {
            this.mHostAbility = (HostAbility) activity;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public Animator onCreateAnimator(int i, boolean z, int i2) {
        if (this.mSingleEffectMode) {
            return null;
        }
        ObjectAnimator objectAnimator = new ObjectAnimator();
        if (z) {
            PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 150.0f, 0.0f);
            PropertyValuesHolder ofFloat2 = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 150.0f, 0.0f);
            PropertyValuesHolder ofFloat3 = PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f, 1.0f);
            PropertyValuesHolder[] propertyValuesHolderArr = new PropertyValuesHolder[2];
            if (!isLayoutPortrait()) {
                ofFloat = ofFloat2;
            }
            propertyValuesHolderArr[0] = ofFloat;
            propertyValuesHolderArr[1] = ofFloat3;
            objectAnimator.setValues(propertyValuesHolderArr);
            objectAnimator.setDuration(sAnimAppearDuration);
            objectAnimator.setStartDelay(sAnimAppearDelay);
        } else {
            PropertyValuesHolder ofFloat4 = PropertyValuesHolder.ofFloat(VIEW_RELATIVE_Y, 0.0f, 150.0f);
            PropertyValuesHolder ofFloat5 = PropertyValuesHolder.ofFloat(VIEW_RELATIVE_X, 0.0f, 150.0f);
            PropertyValuesHolder ofFloat6 = PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f, 0.0f);
            PropertyValuesHolder[] propertyValuesHolderArr2 = new PropertyValuesHolder[2];
            if (!isLayoutPortrait()) {
                ofFloat4 = ofFloat5;
            }
            propertyValuesHolderArr2[0] = ofFloat4;
            propertyValuesHolderArr2[1] = ofFloat6;
            objectAnimator.setValues(propertyValuesHolderArr2);
            objectAnimator.setDuration(sAnimDisappearDuration);
        }
        objectAnimator.setInterpolator(new CubicEaseOutInterpolator());
        return objectAnimator;
    }

    @Override // com.miui.gallery.editor.photo.app.EditorFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        DefaultLogger.d("MenuFragment", "MenuFragment onCreate");
        this.mGestureFragment = getFragmentManager().getFragment(getArguments(), "MenuFragment:gesture_fragment");
        if (getArguments() != null) {
            this.mSingleEffectMode = getArguments().getBoolean("single_effect_mode", false);
        }
        if (!this.mSingleEffectMode) {
            this.mRenderFragment = (F) getFragmentManager().getFragment(getArguments(), "MenuFragment:display_fragment");
        }
        if (sAnimAppearDuration == 0) {
            sAnimAppearDuration = getActivity().getResources().getInteger(R.integer.photo_editor_sub_editor_sub_menu_appear_duration);
        }
        if (sAnimDisappearDuration == 0) {
            sAnimDisappearDuration = getActivity().getResources().getInteger(R.integer.photo_editor_sub_editor_sub_menu_disappear_duration);
        }
        if (sAnimAppearDelay == 0) {
            sAnimAppearDelay = getActivity().getResources().getInteger(R.integer.photo_editor_sub_editor_sub_menu_appear_delay);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        View findViewById = view.findViewById(R.id.ok);
        this.mSaveBtn = findViewById;
        if (findViewById != null) {
            findViewById.setOnClickListener(this.mOnClickListener);
        }
        View findViewById2 = view.findViewById(R.id.cancel);
        this.mDiscardBtn = findViewById2;
        if (findViewById2 != null) {
            findViewById2.setOnClickListener(this.mOnClickListener);
        }
        AnimParams build = new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build();
        FolmeUtil.setCustomTouchAnim(this.mSaveBtn, build, null, null, null, true);
        FolmeUtil.setCustomTouchAnim(this.mDiscardBtn, build, null, null, null, true);
        if (!this.mSingleEffectMode) {
            view.setAlpha(0.0f);
            attachRenderFragment();
            return;
        }
        this.mSaveBtn.setEnabled(false);
    }

    public boolean isLayoutPortrait() {
        return EditorOrientationHelper.isLayoutPortrait(getContext());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void setRenderFragmentInSingleMode(RenderFragment renderFragment) {
        this.mRenderFragment = renderFragment;
        attachRenderFragment();
    }

    public final void attachRenderFragment() {
        Bitmap preview = getPreview();
        int i = -1;
        Integer valueOf = Integer.valueOf(preview == null ? -1 : preview.getWidth());
        if (preview != null) {
            i = preview.getHeight();
        }
        DefaultLogger.d("MenuFragment", "MenuFragment onViewCreated and preview bitmap : %s width : %d height : %d", preview, valueOf, Integer.valueOf(i));
        this.mRenderFragment.setBitmap(preview);
        FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
        beginTransaction.attach(this.mRenderFragment);
        Fragment fragment = this.mGestureFragment;
        if (fragment != null) {
            beginTransaction.attach(fragment);
        }
        beginTransaction.commit();
    }

    public void setSaveEnable(boolean z) {
        this.mSaveBtn.setEnabled(z);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        if (getFragmentManager().isDestroyed()) {
            return;
        }
        FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
        beginTransaction.detach(this.mRenderFragment);
        Fragment fragment = this.mGestureFragment;
        if (fragment != null) {
            beginTransaction.detach(fragment);
        }
        beginTransaction.commitAllowingStateLoss();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.mRenderFragment = null;
    }

    public F createRenderFragment(int i) {
        F f = (F) this.mSdkProvider.createFragment();
        Bundle arguments = f.getArguments();
        if (arguments == null) {
            arguments = new Bundle();
        }
        arguments.putInt("RenderFragment:title_res_id", i);
        f.setArguments(arguments);
        return f;
    }

    public final Bitmap getPreview() {
        return this.mCallbacks.onLoadPreview();
    }

    public final Bitmap decodeOrigin() {
        return this.mCallbacks.onLoadOrigin();
    }

    public final List<RenderData> getPreRenderData() {
        return this.mCallbacks.onLoadRenderData();
    }

    public final F getRenderFragment() {
        return this.mRenderFragment;
    }

    public final Fragment getGestureFragment() {
        return this.mGestureFragment;
    }

    public void notifyDiscard() {
        this.mCallbacks.onDiscard(this);
    }

    public void notifySave() {
        new Handler().postDelayed(new Runnable() { // from class: com.miui.gallery.editor.photo.app.MenuFragment.2
            @Override // java.lang.Runnable
            public void run() {
                if (MenuFragment.this.isAdded()) {
                    MenuFragment.this.showProcessDialog();
                }
            }
        }, 1000L);
        this.mCallbacks.onSave(this);
    }

    public final ProgressDialog genProgressDialog(String str) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(str);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        return progressDialog;
    }

    public boolean isDoubleClick() {
        if (System.currentTimeMillis() - this.mLastClickTime < 400) {
            return true;
        }
        this.mLastClickTime = System.currentTimeMillis();
        return false;
    }
}
