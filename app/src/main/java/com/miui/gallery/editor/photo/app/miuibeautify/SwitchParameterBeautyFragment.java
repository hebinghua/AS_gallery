package com.miui.gallery.editor.photo.app.miuibeautify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.miui.filtersdk.beauty.BeautyProcessorManager;
import com.miui.filtersdk.beauty.IntelligentBeautyProcessor;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautyEffect;
import miuix.slidingwidget.widget.SlidingButton;

/* loaded from: classes2.dex */
public class SwitchParameterBeautyFragment extends ChildMenuFragment {
    public SlidingButton mSlidingButton;

    @Override // com.miui.gallery.editor.photo.app.miuibeautify.ChildMenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            ((TextView) view.findViewById(R.id.switch_param_title)).setText(getActivity().getResources().getString(R.string.photo_editor_miui_beauty_menu_eraser_blemish_title));
            this.mSlidingButton = (SlidingButton) view.findViewById(R.id.check_box);
            final MiuiBeautyEffect miuiBeautyEffect = (MiuiBeautyEffect) arguments.getParcelable("BEAUTY_EFFECT");
            this.mSlidingButton.setOnPerformCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.miui.gallery.editor.photo.app.miuibeautify.SwitchParameterBeautyFragment.1
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    float[] supportedParamRange;
                    IntelligentBeautyProcessor beautyProcessor = BeautyProcessorManager.INSTANCE.getBeautyProcessor();
                    MiuiBeautyEffect miuiBeautyEffect2 = miuiBeautyEffect;
                    if (miuiBeautyEffect2 == null || (supportedParamRange = beautyProcessor.getSupportedParamRange(miuiBeautyEffect2.mBeautyType)) == null || supportedParamRange.length < 2) {
                        return;
                    }
                    SwitchParameterBeautyFragment.this.setBeautyParameterTable(miuiBeautyEffect.mBeautyType, z ? 1.0f : 0.0f);
                    SwitchParameterBeautyFragment.this.notifyBeautyParameterChanged();
                }
            });
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.miuibeauty_switch_param_fragment, viewGroup, false);
    }
}
