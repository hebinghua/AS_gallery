package com.miui.gallery.editor.photo.app.miuibeautify;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.view.View;
import com.miui.filtersdk.beauty.BeautyParameterType;
import com.miui.gallery.R;
import com.miui.gallery.app.fragment.AndroidFragment;
import com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautyEffect;
import com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautyRenderFragment;
import java.util.HashMap;
import java.util.Map;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public class ChildMenuFragment extends AndroidFragment implements MiuiBeautyRenderFragment.OnBeautyProcessListener {
    public static int sAnimAppearDelay;
    public static int sAnimAppearDuration;
    public static int sAnimDisappearDuration;
    public static int sAnimOffset;
    public Callbacks mCallbacks;
    public Map<BeautyParameterType, Float> mExtraTable = new HashMap();

    /* loaded from: classes2.dex */
    public interface Callbacks {
        void changeTitle(String str);

        void onBeautyParameterChanged(ChildMenuFragment childMenuFragment);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautyRenderFragment.OnBeautyProcessListener
    public void onBeautyProcessEnd() {
    }

    @Override // com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautyRenderFragment.OnBeautyProcessListener
    public void onBeautyProcessStart() {
    }

    @Override // androidx.fragment.app.Fragment
    public Animator onCreateAnimator(int i, boolean z, int i2) {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        if (z) {
            objectAnimator.setValues(PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, sAnimOffset, 0.0f), PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f, 1.0f));
            objectAnimator.setDuration(sAnimAppearDuration);
            objectAnimator.setStartDelay(sAnimAppearDelay);
        } else {
            objectAnimator.setValues(PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0.0f, sAnimOffset), PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f, 0.0f));
            objectAnimator.setDuration(sAnimDisappearDuration);
        }
        objectAnimator.setInterpolator(new CubicEaseOutInterpolator());
        return objectAnimator;
    }

    @Override // com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (sAnimOffset == 0) {
            sAnimOffset = getActivity().getResources().getDimensionPixelSize(R.dimen.photo_editor_enter_sub_editor_sub_menu_offset);
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
        MiuiBeautyEffect miuiBeautyEffect;
        super.onViewCreated(view, bundle);
        view.setAlpha(0.0f);
        Bundle arguments = getArguments();
        if (arguments == null || (miuiBeautyEffect = (MiuiBeautyEffect) arguments.getParcelable("BEAUTY_EFFECT")) == null) {
            return;
        }
        this.mCallbacks.changeTitle(miuiBeautyEffect.mName);
    }

    public void setCallback(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    public final void notifyBeautyParameterChanged() {
        this.mCallbacks.onBeautyParameterChanged(this);
    }

    public void setBeautyParameterTable(BeautyParameterType beautyParameterType, float f) {
        if (beautyParameterType == BeautyParameterType.BRIGHT_EYE_RATIO) {
            this.mExtraTable.put(BeautyParameterType.IRIS_SHINE_RATIO, Float.valueOf(f));
        }
        this.mExtraTable.put(beautyParameterType, Float.valueOf(f));
    }

    public void setBeautyParameterTable(Map<BeautyParameterType, Float> map) {
        this.mExtraTable = map;
    }

    public Map<BeautyParameterType, Float> getBeautyParameterTable() {
        return this.mExtraTable;
    }
}
