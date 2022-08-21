package com.miui.gallery.editor.photo.app.miuibeautify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import com.miui.filtersdk.beauty.BeautyProcessorManager;
import com.miui.filtersdk.beauty.IntelligentBeautyProcessor;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautyEffect;
import com.miui.gallery.widget.seekbar.BubbleIndicator;

/* loaded from: classes2.dex */
public class SingleParameterBeautyFragment extends ChildMenuFragment {
    public SeekBar mSeekBar;
    public BubbleIndicator.Callback<TextView> mIndicatorCallback = new BubbleIndicator.Callback<TextView>() { // from class: com.miui.gallery.editor.photo.app.miuibeautify.SingleParameterBeautyFragment.1
        @Override // com.miui.gallery.widget.seekbar.BubbleIndicator.Callback
        public void updateProgress(TextView textView, int i) {
            textView.setText(String.valueOf(i));
        }
    };
    public SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() { // from class: com.miui.gallery.editor.photo.app.miuibeautify.SingleParameterBeautyFragment.2
        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar) {
            MiuiBeautyEffect miuiBeautyEffect;
            float[] supportedParamRange;
            IntelligentBeautyProcessor beautyProcessor = BeautyProcessorManager.INSTANCE.getBeautyProcessor();
            Bundle arguments = SingleParameterBeautyFragment.this.getArguments();
            if (arguments == null || (miuiBeautyEffect = (MiuiBeautyEffect) arguments.getParcelable("BEAUTY_EFFECT")) == null || (supportedParamRange = beautyProcessor.getSupportedParamRange(miuiBeautyEffect.mBeautyType)) == null || supportedParamRange.length < 2) {
                return;
            }
            SingleParameterBeautyFragment.this.setBeautyParameterTable(miuiBeautyEffect.mBeautyType, (seekBar.getProgress() / 100.0f) * (supportedParamRange[1] - supportedParamRange[0]));
            SingleParameterBeautyFragment.this.notifyBeautyParameterChanged();
        }
    };

    @Override // com.miui.gallery.editor.photo.app.miuibeautify.ChildMenuFragment, com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.miuibeauty_single_param_fragment, viewGroup, false);
    }

    @Override // com.miui.gallery.editor.photo.app.miuibeautify.ChildMenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekbar);
        this.mSeekBar = seekBar;
        seekBar.setOnSeekBarChangeListener(this.mSeekBarChangeListener);
        this.mSeekBar.setOnSeekBarChangeListener(new BubbleIndicator((TextView) View.inflate(getActivity(), R.layout.seekbar_bubble_indicator_text, null), getActivity().getResources().getDimensionPixelSize(R.dimen.photo_editor_bubble_indicator_offset), this.mIndicatorCallback, this.mSeekBarChangeListener));
    }
}
