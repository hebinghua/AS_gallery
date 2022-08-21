package com.miui.gallery.editor.photo.app.sky;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.SkyData;
import com.miui.gallery.editor.photo.core.imports.sky.SkyDataImpl;
import com.miui.gallery.editor.ui.view.DownloadView;
import com.miui.gallery.editor.ui.view.RoundImageView;
import com.miui.gallery.util.anim.FolmeUtil;

/* loaded from: classes2.dex */
public class SkyHolder extends RecyclerView.ViewHolder {
    public LottieAnimationView mConfigIndicator;
    public SkyData mData;
    public DownloadView mDownloadView;
    public RoundImageView mPreviewView;
    public boolean mSelected;
    public RoundImageView mSelector;
    public TextView mTitleView;
    public TextView mValueIndicator;

    public SkyHolder(View view) {
        super(view);
        FolmeUtil.setDefaultTouchAnim(view, null, true);
        this.mPreviewView = (RoundImageView) view.findViewById(R.id.preview);
        this.mTitleView = (TextView) view.findViewById(R.id.title);
        this.mValueIndicator = (TextView) view.findViewById(R.id.value_indicator);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) view.findViewById(R.id.configable_indicator);
        this.mConfigIndicator = lottieAnimationView;
        lottieAnimationView.setAnimation(R.raw.lottie_filter_configable_indicator);
        this.mSelector = (RoundImageView) view.findViewById(R.id.selector);
        this.mDownloadView = (DownloadView) view.findViewById(R.id.item_download);
        this.mDownloadView.setBackground(view.getResources().getDrawable(R.drawable.filter_menu_item_download_bg));
        float dimensionPixelSize = view.getResources().getDimensionPixelSize(R.dimen.filter_menu_item_corner);
        this.mSelector.setCorner(dimensionPixelSize);
        this.mPreviewView.setCorner(dimensionPixelSize);
    }

    public void bindData(SkyData skyData, boolean z, boolean z2) {
        this.mData = skyData;
        this.mPreviewView.setImageResource(skyData.getIcon());
        this.mPreviewView.setContentDescription(skyData.name);
        this.mTitleView.setText(skyData.name);
        if (skyData instanceof SkyDataImpl) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) this.itemView.getLayoutParams();
            int dimensionPixelSize = this.itemView.getResources().getDimensionPixelSize(R.dimen.editor_menu_filter_category_gap) - this.itemView.getResources().getDimensionPixelSize(R.dimen.editor_menu_filter_item_gap);
            if (!((SkyDataImpl) skyData).getLast()) {
                dimensionPixelSize = 0;
            }
            layoutParams.setMarginEnd(dimensionPixelSize);
            this.itemView.setLayoutParams(layoutParams);
        }
        if (skyData.isNone()) {
            this.mDownloadView.setVisibility(8);
        } else {
            this.mDownloadView.setStateImage(skyData.getDownloadState());
            if (skyData.getDownloadState() == 0) {
                skyData.setDownloadState(17);
            }
        }
        setState(z, z2);
        updateIndicator(skyData.getProgress());
    }

    public void setConfigIndicator() {
        this.mConfigIndicator.post(new Runnable() { // from class: com.miui.gallery.editor.photo.app.sky.SkyHolder.1
            @Override // java.lang.Runnable
            public void run() {
                SkyHolder.this.mConfigIndicator.playAnimation();
            }
        });
    }

    public final void setState(boolean z, boolean z2) {
        if (z) {
            this.mSelector.setVisibility(0);
        } else {
            this.mSelector.setVisibility(8);
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mSelector.getLayoutParams();
        this.mSelected = z;
        int[] rules = layoutParams.getRules();
        if (!z) {
            this.mValueIndicator.setVisibility(8);
            this.mConfigIndicator.setVisibility(8);
            if (rules[6] == R.id.title) {
                return;
            }
            layoutParams.removeRule(10);
            layoutParams.addRule(6, R.id.title);
            return;
        }
        if (!z2) {
            this.mValueIndicator.setVisibility(8);
            if (!this.mData.isNone()) {
                this.mConfigIndicator.setVisibility(0);
            }
        } else {
            this.mValueIndicator.setVisibility(0);
            this.mConfigIndicator.setVisibility(8);
        }
        if (rules[10] == -1) {
            return;
        }
        layoutParams.addRule(10, -1);
        layoutParams.removeRule(6);
    }

    public final void updateIndicator(int i) {
        this.mValueIndicator.setText(String.valueOf(i));
    }
}
