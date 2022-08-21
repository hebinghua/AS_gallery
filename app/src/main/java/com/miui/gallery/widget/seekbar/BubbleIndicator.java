package com.miui.gallery.widget.seekbar;

import android.view.View;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import com.miui.gallery.baseui.R$style;
import com.miui.gallery.widget.seekbar.BiDirectionSeekBar;

/* loaded from: classes3.dex */
public class BubbleIndicator<V extends View> implements SeekBar.OnSeekBarChangeListener, BiDirectionSeekBar.OnSeekBarProgressListener {
    public Callback<V> mCallback;
    public V mContentView;
    public SeekBar.OnSeekBarChangeListener mDelegate;
    public int[] mLocation;
    public int mOffsetY;
    public PopupWindow mPopup;
    public BiDirectionSeekBar.OnSeekBarProgressListener mProgressListener;

    /* loaded from: classes3.dex */
    public interface Callback<V extends View> {
        void updateProgress(V v, int i);
    }

    public void onProgressUpdate(V v, int i) {
    }

    public BubbleIndicator(V v, int i, Callback<V> callback, SeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
        this(v, i, callback, onSeekBarChangeListener, null);
    }

    public BubbleIndicator(V v, int i, Callback<V> callback, SeekBar.OnSeekBarChangeListener onSeekBarChangeListener, BiDirectionSeekBar.OnSeekBarProgressListener onSeekBarProgressListener) {
        this.mLocation = new int[2];
        this.mContentView = v;
        this.mOffsetY = i;
        this.mCallback = callback;
        this.mDelegate = onSeekBarChangeListener;
        this.mProgressListener = onSeekBarProgressListener;
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        if (z) {
            updateProgress(this.mContentView, i);
            PopupWindow popupWindow = this.mPopup;
            if (popupWindow != null) {
                popupWindow.update(computeX(seekBar), computeY(seekBar), -1, -1);
            }
        }
        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = this.mDelegate;
        if (onSeekBarChangeListener != null) {
            onSeekBarChangeListener.onProgressChanged(seekBar, i, z);
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(final SeekBar seekBar) {
        if (this.mPopup == null) {
            this.mContentView.setVisibility(4);
            PopupWindow popupWindow = new PopupWindow((View) this.mContentView, -2, -2, false);
            this.mPopup = popupWindow;
            popupWindow.setTouchable(false);
            this.mContentView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.miui.gallery.widget.seekbar.BubbleIndicator.1
                @Override // android.view.View.OnLayoutChangeListener
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                    BubbleIndicator.this.mPopup.update(BubbleIndicator.this.computeX(seekBar), BubbleIndicator.this.computeY(seekBar), -1, -1);
                    view.setVisibility(0);
                    view.removeOnLayoutChangeListener(this);
                }
            });
        }
        seekBar.getLocationInWindow(this.mLocation);
        this.mPopup.setAnimationStyle(R$style.Gallery_BubbleIndicatorAnim);
        this.mPopup.showAtLocation(seekBar, 0, computeX(seekBar), computeY(seekBar));
        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = this.mDelegate;
        if (onSeekBarChangeListener != null) {
            onSeekBarChangeListener.onStartTrackingTouch(seekBar);
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seekBar) {
        this.mPopup.dismiss();
        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = this.mDelegate;
        if (onSeekBarChangeListener != null) {
            onSeekBarChangeListener.onStopTrackingTouch(seekBar);
        }
    }

    public boolean isShowing() {
        PopupWindow popupWindow = this.mPopup;
        return popupWindow != null && popupWindow.isShowing();
    }

    public void dismiss() {
        this.mPopup.dismiss();
    }

    public final void updateProgress(V v, int i) {
        onProgressUpdate(v, i);
        Callback<V> callback = this.mCallback;
        if (callback != null) {
            callback.updateProgress(this.mContentView, i);
        }
    }

    @Override // com.miui.gallery.widget.seekbar.BiDirectionSeekBar.OnSeekBarProgressListener
    public void onProgressChanged(float f) {
        BiDirectionSeekBar.OnSeekBarProgressListener onSeekBarProgressListener = this.mProgressListener;
        if (onSeekBarProgressListener != null) {
            onSeekBarProgressListener.onProgressChanged(f);
        }
    }

    public final int computeX(SeekBar seekBar) {
        return (((this.mLocation[0] + seekBar.getPaddingLeft()) - seekBar.getThumbOffset()) + seekBar.getThumb().getBounds().centerX()) - (this.mContentView.getWidth() / 2);
    }

    public final int computeY(SeekBar seekBar) {
        return ((this.mLocation[1] + (seekBar.getHeight() / 2)) - this.mOffsetY) - (this.mContentView.getHeight() / 2);
    }
}
