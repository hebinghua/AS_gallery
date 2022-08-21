package com.miui.gallery.widget.seekbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.SeekBar;
import com.miui.gallery.baseui.R$id;

/* loaded from: classes3.dex */
public class BasicSeekBar extends SeekBar {
    public SeekBarChangeDelegate mDelegate;
    public LayerDrawable mThumb;

    public BasicSeekBar(Context context) {
        this(context, null);
    }

    public BasicSeekBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16842875);
    }

    public BasicSeekBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        SeekBarChangeDelegate seekBarChangeDelegate = new SeekBarChangeDelegate();
        this.mDelegate = seekBarChangeDelegate;
        super.setOnSeekBarChangeListener(seekBarChangeDelegate);
        updateThumb(getProgress());
    }

    @Override // android.widget.AbsSeekBar
    public void setThumb(Drawable drawable) {
        if (drawable instanceof LayerDrawable) {
            LayerDrawable layerDrawable = (LayerDrawable) drawable;
            if (layerDrawable.findDrawableByLayerId(R$id.seekbar_thumb_active_state) != null && layerDrawable.findDrawableByLayerId(R$id.seekbar_thumb_reset_state) != null) {
                this.mThumb = layerDrawable;
                updateThumb(getProgress());
                return;
            }
        }
        super.setThumb(drawable);
    }

    @Override // android.widget.SeekBar
    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
        this.mDelegate.mDelegated = onSeekBarChangeListener;
    }

    public void updateThumb(int i) {
        LayerDrawable layerDrawable = this.mThumb;
        if (layerDrawable == null) {
            return;
        }
        if (i == 0) {
            super.setThumb(layerDrawable.findDrawableByLayerId(R$id.seekbar_thumb_reset_state));
        } else {
            super.setThumb(layerDrawable.findDrawableByLayerId(R$id.seekbar_thumb_active_state));
        }
    }

    /* loaded from: classes3.dex */
    public class SeekBarChangeDelegate implements SeekBar.OnSeekBarChangeListener {
        public SeekBar.OnSeekBarChangeListener mDelegated;

        public SeekBarChangeDelegate() {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = this.mDelegated;
            if (onSeekBarChangeListener != null) {
                onSeekBarChangeListener.onProgressChanged(seekBar, i, z);
            }
            BasicSeekBar.this.updateThumb(i);
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar) {
            SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = this.mDelegated;
            if (onSeekBarChangeListener != null) {
                onSeekBarChangeListener.onStartTrackingTouch(seekBar);
            }
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar) {
            SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = this.mDelegated;
            if (onSeekBarChangeListener != null) {
                onSeekBarChangeListener.onStopTrackingTouch(seekBar);
            }
        }
    }
}
