package com.miui.gallery.editor.photo.app.filter;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.FilterData;
import com.miui.gallery.editor.photo.core.imports.filter.FilterItem;
import com.miui.gallery.editor.ui.view.DownloadView;
import com.miui.gallery.editor.ui.view.RoundImageView;
import com.miui.gallery.util.anim.FolmeUtil;

/* loaded from: classes2.dex */
public class FilterMenuItem extends RecyclerView.ViewHolder {
    public LottieAnimationView mConfigIndicator;
    public FilterData mData;
    public DownloadView mDownloadView;
    public RoundImageView mPreviewView;
    public boolean mSelected;
    public RoundImageView mSelector;
    public TextView mTitleView;
    public TextView mValueIndicator;

    public FilterMenuItem(View view) {
        super(view);
        FolmeUtil.setDefaultTouchAnim(view, null, true);
        this.mPreviewView = (RoundImageView) view.findViewById(R.id.preview);
        this.mTitleView = (TextView) view.findViewById(R.id.title);
        this.mValueIndicator = (TextView) view.findViewById(R.id.value_indicator);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) view.findViewById(R.id.configable_indicator);
        this.mConfigIndicator = lottieAnimationView;
        lottieAnimationView.setAnimation(R.raw.lottie_filter_configable_indicator);
        this.mConfigIndicator.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.miui.gallery.editor.photo.app.filter.FilterMenuItem.1
            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View view2) {
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View view2) {
                FilterMenuItem.this.mConfigIndicator.setProgress(1.0f);
                FilterMenuItem.this.mConfigIndicator.cancelAnimation();
            }
        });
        this.mSelector = (RoundImageView) view.findViewById(R.id.selector);
        this.mDownloadView = (DownloadView) view.findViewById(R.id.item_download);
        this.mDownloadView.setBackground(view.getResources().getDrawable(R.drawable.filter_menu_item_download_bg));
        float dimensionPixelSize = view.getResources().getDimensionPixelSize(R.dimen.filter_menu_item_corner);
        this.mSelector.setCorner(dimensionPixelSize);
        this.mPreviewView.setCorner(dimensionPixelSize);
    }

    public void bindData(FilterData filterData) {
        this.mData = filterData;
        this.mPreviewView.setImageResource(filterData.icon);
        this.mPreviewView.setContentDescription(filterData.name);
        this.mTitleView.setText(filterData.name);
        if (filterData instanceof FilterItem) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) this.itemView.getLayoutParams();
            int dimensionPixelSize = this.itemView.getResources().getDimensionPixelSize(R.dimen.editor_menu_filter_category_gap) - this.itemView.getResources().getDimensionPixelSize(R.dimen.editor_menu_filter_item_gap);
            if (!((FilterItem) filterData).getLast()) {
                dimensionPixelSize = 0;
            }
            layoutParams.setMarginEnd(dimensionPixelSize);
            this.itemView.setLayoutParams(layoutParams);
        }
        if (filterData.isNone() || filterData.isBuiltIn()) {
            this.mDownloadView.setVisibility(8);
            return;
        }
        this.mDownloadView.setStateImage(filterData.state);
        if (filterData.state != 0) {
            return;
        }
        filterData.state = 17;
    }

    public void setDownloadViewState(int i) {
        this.mDownloadView.setStateImage(i);
    }

    public void setConfigIndicator() {
        this.mConfigIndicator.post(new Runnable() { // from class: com.miui.gallery.editor.photo.app.filter.FilterMenuItem.2
            @Override // java.lang.Runnable
            public void run() {
                FilterMenuItem.this.mConfigIndicator.playAnimation();
            }
        });
    }

    public void setState(boolean z, boolean z2, boolean z3) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mSelector.getLayoutParams();
        this.mSelected = z;
        if (z) {
            this.mSelector.setVisibility(0);
        } else {
            this.mSelector.setVisibility(8);
        }
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
            this.mConfigIndicator.setVisibility(8);
        } else if (!z3) {
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

    public void updateIndicator(int i) {
        this.mValueIndicator.setText(String.valueOf(i));
    }
}
