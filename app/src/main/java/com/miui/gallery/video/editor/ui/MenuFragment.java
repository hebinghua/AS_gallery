package com.miui.gallery.video.editor.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import com.miui.gallery.R;
import com.miui.gallery.app.fragment.AndroidFragment;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.video.editor.VideoEditor;
import com.miui.gallery.video.editor.factory.VideoEditorModuleFactory;
import com.miui.gallery.video.editor.interfaces.IVideoEditorListener$IVideoEditorFragmentCallback;
import com.miui.gallery.video.editor.manager.SmartVideoJudgeManager;
import com.xiaomi.mirror.synergy.CallMethod;
import java.util.HashMap;
import java.util.List;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public abstract class MenuFragment extends AndroidFragment {
    public static int sAnimAppearDelay;
    public static int sAnimAppearDuration;
    public static int sAnimDisappearDuration;
    public static int sAnimOffset;
    public IVideoEditorListener$IVideoEditorFragmentCallback mCallback;
    public Context mContext;
    public boolean mHasLoadData = false;
    public VideoEditorModuleFactory mModuleFactory;
    public VideoEditor mVideoEditor;

    public abstract boolean doCancel();

    public abstract List<String> getCurrentEffect();

    public abstract int getEffectId();

    public final String mapIdToStatCate(int i) {
        switch (i) {
            case R.id.video_editor_audio /* 2131363728 */:
                return "video_editor_audio";
            case R.id.video_editor_filter /* 2131363733 */:
                return "video_editor_filter";
            case R.id.video_editor_smart_effect /* 2131363740 */:
                return "video_editor_smart_effect";
            case R.id.video_editor_trim /* 2131363746 */:
                return "video_editor_clip";
            case R.id.video_editor_water_mark /* 2131363749 */:
                return "video_editor_text";
            default:
                return "video_editor_unknown";
        }
    }

    public void onPlayButtonClicked() {
    }

    public void onVideoLoadCompleted() {
    }

    public void updateLastFragment(MenuFragment menuFragment) {
    }

    public void updateVoiceState(boolean z) {
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override // com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initAnimatorData();
    }

    private void initAnimatorData() {
        if (sAnimOffset == 0) {
            sAnimOffset = getActivity().getResources().getDimensionPixelSize(R.dimen.video_editor_enter_sub_editor_main_menu_offset);
        }
        if (sAnimAppearDuration == 0) {
            sAnimAppearDuration = getActivity().getResources().getInteger(R.integer.video_editor_sub_editor_sub_menu_appear_duration);
        }
        if (sAnimDisappearDuration == 0) {
            sAnimDisappearDuration = getActivity().getResources().getInteger(R.integer.video_editor_sub_editor_sub_menu_disappear_duration);
        }
        if (sAnimAppearDelay == 0) {
            sAnimAppearDelay = getActivity().getResources().getInteger(R.integer.video_editor_sub_editor_sub_menu_appear_delay);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        updatePlayBtnView();
    }

    @Override // androidx.fragment.app.Fragment
    public Animator onCreateAnimator(int i, boolean z, int i2) {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        if (z) {
            objectAnimator.setValues(PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, sAnimOffset, 0.0f), PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f, 1.0f));
            objectAnimator.setInterpolator(new CubicEaseOutInterpolator());
            if (getView() != null) {
                getView().setAlpha(0.0f);
            }
            objectAnimator.setStartDelay(sAnimAppearDelay);
            objectAnimator.setDuration(sAnimAppearDuration);
        } else {
            PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f, 0.0f);
            objectAnimator.setInterpolator(new CubicEaseOutInterpolator());
            objectAnimator.setValues(ofFloat);
            objectAnimator.setDuration(sAnimDisappearDuration);
        }
        return objectAnimator;
    }

    public void recordEventWithEffectChanged() {
        SamplingStatHelper.recordCountEvent(mapIdToStatCate(getEffectId()), "enter");
    }

    public void onExitMode() {
        IVideoEditorListener$IVideoEditorFragmentCallback iVideoEditorListener$IVideoEditorFragmentCallback = this.mCallback;
        if (iVideoEditorListener$IVideoEditorFragmentCallback != null) {
            iVideoEditorListener$IVideoEditorFragmentCallback.showNavEditMenu();
        }
    }

    public void setCallBack(IVideoEditorListener$IVideoEditorFragmentCallback iVideoEditorListener$IVideoEditorFragmentCallback) {
        this.mCallback = iVideoEditorListener$IVideoEditorFragmentCallback;
        this.mVideoEditor = iVideoEditorListener$IVideoEditorFragmentCallback.getVideoEditor();
    }

    public void recordEventWithApply() {
        HashMap<String, String> hashMap = new HashMap<>();
        List<String> currentEffect = getCurrentEffect();
        if (currentEffect == null || currentEffect.isEmpty()) {
            hashMap.put("effect", "none");
            processStatParams(hashMap);
            SamplingStatHelper.recordCountEvent(mapIdToStatCate(getEffectId()), "save_detail", hashMap);
        } else {
            for (String str : currentEffect) {
                hashMap.put("effect", str);
                processStatParams(hashMap);
                SamplingStatHelper.recordCountEvent(mapIdToStatCate(getEffectId()), "save_detail", hashMap);
            }
        }
        SamplingStatHelper.recordCountEvent(mapIdToStatCate(getEffectId()), "save");
    }

    public void recordEventWithCancel() {
        HashMap<String, String> hashMap = new HashMap<>();
        List<String> currentEffect = getCurrentEffect();
        if (currentEffect == null || currentEffect.isEmpty()) {
            hashMap.put("effect", "none");
            processStatParams(hashMap);
            SamplingStatHelper.recordCountEvent(mapIdToStatCate(getEffectId()), "discard_detail", hashMap);
        } else {
            for (String str : currentEffect) {
                hashMap.put("effect", str);
                processStatParams(hashMap);
                SamplingStatHelper.recordCountEvent(mapIdToStatCate(getEffectId()), "discard_detail", hashMap);
            }
        }
        SamplingStatHelper.recordCountEvent(mapIdToStatCate(getEffectId()), "discard");
    }

    public void updatePlayBtnView() {
        IVideoEditorListener$IVideoEditorFragmentCallback iVideoEditorListener$IVideoEditorFragmentCallback = this.mCallback;
        if (iVideoEditorListener$IVideoEditorFragmentCallback != null) {
            iVideoEditorListener$IVideoEditorFragmentCallback.updatePlayBtnView();
        }
    }

    public final void processStatParams(HashMap<String, String> hashMap) {
        if (SmartVideoJudgeManager.isAvailable()) {
            hashMap.put(CallMethod.ARG_EXTRA_STRING, "smart_effect");
        } else {
            hashMap.put(CallMethod.ARG_EXTRA_STRING, "not_smart_effect");
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.mCallback = null;
    }
}
