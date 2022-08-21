package com.miui.gallery.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.R$styleable;
import com.miui.gallery.util.MamlUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.DispatchRelativeLayout;
import com.xiaomi.stat.b.h;
import java.io.File;

/* loaded from: classes2.dex */
public class EmptyPageWithoutSBL extends DispatchRelativeLayout {
    public static String TAG = "EmptyPage";
    public boolean isIgnoreParentPadding;
    public Button mActionButton;
    public TextView mBigTitle;
    public Context mContext;
    public TextView mDescription;
    public GalleryMamlView mGalleryMamlView;
    public ImageView mIcon;
    public FrameLayout mIconFl;
    public TextView mTitle;
    public View mTopDividerLine;

    public EmptyPageWithoutSBL(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public EmptyPageWithoutSBL(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mContext = context;
        init(attributeSet);
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        updateConfiguration(configuration);
    }

    public final void updateConfiguration(Configuration configuration) {
        if (this.mIcon == null) {
            return;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mActionButton.getLayoutParams();
        int i = 0;
        if (configuration.orientation == 2) {
            boolean z = configuration.screenHeightDp >= 600;
            TextView textView = this.mTitle;
            if (!z) {
                i = 8;
            }
            textView.setVisibility(i);
            if (this.mActionButton.getVisibility() == 4) {
                this.mActionButton.setVisibility(8);
            }
            marginLayoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.empty_page_landscape_margin_bottom);
        } else {
            if (!TextUtils.isEmpty(this.mTitle.getText())) {
                this.mTitle.setVisibility(0);
            }
            if (this.mActionButton.getVisibility() == 8) {
                this.mActionButton.setVisibility(4);
            }
            marginLayoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.empty_page_margin_bottom);
        }
        this.mActionButton.setLayoutParams(marginLayoutParams);
    }

    public final void init(AttributeSet attributeSet) {
        if (isInEditMode()) {
            return;
        }
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.EmptyPage);
        try {
            String string = obtainStyledAttributes.getString(2);
            Drawable drawable = obtainStyledAttributes.getDrawable(4);
            CharSequence text = obtainStyledAttributes.getText(5);
            CharSequence text2 = obtainStyledAttributes.getText(3);
            Drawable drawable2 = obtainStyledAttributes.getDrawable(0);
            CharSequence text3 = obtainStyledAttributes.getText(1);
            boolean z = obtainStyledAttributes.getBoolean(7, true);
            boolean z2 = obtainStyledAttributes.getBoolean(8, false);
            this.isIgnoreParentPadding = obtainStyledAttributes.getBoolean(6, false);
            obtainStyledAttributes.recycle();
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.empty_page_with_mamlview_without_sbl, this);
            this.mIconFl = (FrameLayout) inflate.findViewById(R.id.icon_fl_anim);
            this.mIcon = (ImageView) inflate.findViewById(R.id.icon);
            this.mTitle = (TextView) inflate.findViewById(R.id.title);
            this.mBigTitle = (TextView) inflate.findViewById(R.id.big_title);
            this.mDescription = (TextView) inflate.findViewById(R.id.description);
            if (Build.VERSION.SDK_INT >= 21) {
                this.mBigTitle.setLetterSpacing(0.025f);
                this.mDescription.setLetterSpacing(0.025f);
            }
            this.mActionButton = (Button) inflate.findViewById(R.id.action_button);
            this.mTopDividerLine = inflate.findViewById(R.id.top_divider_line);
            if (MamlUtil.isSupportMaml() && !TextUtils.isEmpty(string)) {
                if (!new File(this.mContext.getFilesDir().getAbsolutePath() + "/maml_resources" + h.g + string).exists()) {
                    String str = TAG;
                    DefaultLogger.e(str, string + " is illegal,zip resource does not find");
                    this.mIconFl.setVisibility(8);
                    this.mIcon.setVisibility(0);
                    if (drawable != null) {
                        setIcon(drawable);
                    }
                    setCanClick(false);
                } else {
                    this.mIconFl.setVisibility(0);
                    this.mIcon.setVisibility(8);
                    this.mGalleryMamlView = new GalleryMamlView(getContext(), this.mIconFl, string);
                    setParentStateListener(new DispatchRelativeLayout.ParentStateListener() { // from class: com.miui.gallery.widget.EmptyPageWithoutSBL.1
                        @Override // com.miui.gallery.widget.DispatchRelativeLayout.ParentStateListener
                        public void onClickView(View view) {
                            EmptyPageWithoutSBL.this.mGalleryMamlView.switchActive();
                        }
                    });
                    this.mGalleryMamlView.unActive();
                    setCanClick(true);
                }
            } else {
                this.mIconFl.setVisibility(8);
                this.mIcon.setVisibility(0);
                if (drawable != null) {
                    setIcon(drawable);
                }
                setCanClick(false);
            }
            if (text2 != null) {
                setSingleLineTextMode(false);
                setDescription(text2);
                if (text != null) {
                    setBigTitle(text);
                }
            } else {
                setSingleLineTextMode(true);
                if (text != null) {
                    setTitle(text);
                }
            }
            if (drawable2 != null) {
                setActionButtonBackground(drawable2);
            }
            if (text3 != null) {
                setActionButtonText(text3);
            }
            setActionButtonVisible(z);
            setTopDividerLineVisible(z2);
            updateConfiguration(this.mContext.getResources().getConfiguration());
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    private void setSingleLineTextMode(boolean z) {
        if (z) {
            this.mTitle.setVisibility(0);
            this.mBigTitle.setVisibility(8);
            this.mDescription.setVisibility(8);
            return;
        }
        this.mTitle.setVisibility(8);
        this.mBigTitle.setVisibility(0);
        this.mDescription.setVisibility(0);
    }

    @Override // android.widget.RelativeLayout, android.view.View
    public void onMeasure(int i, int i2) {
        if (this.isIgnoreParentPadding && (getParent() instanceof ViewGroup)) {
            ViewGroup viewGroup = (ViewGroup) getParent();
            int paddingLeft = viewGroup.getPaddingLeft();
            int paddingRight = viewGroup.getPaddingRight();
            int paddingTop = viewGroup.getPaddingTop();
            int paddingBottom = viewGroup.getPaddingBottom();
            int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(paddingLeft + paddingRight + View.MeasureSpec.getSize(i), 1073741824);
            i2 = View.MeasureSpec.makeMeasureSpec(paddingTop + paddingBottom + View.MeasureSpec.getSize(i2), 1073741824);
            i = makeMeasureSpec;
        }
        super.onMeasure(i, i2);
    }

    public void setOnActionButtonClickListener(View.OnClickListener onClickListener) {
        this.mActionButton.setOnClickListener(onClickListener);
    }

    public void resumeMaml() {
        GalleryMamlView galleryMamlView = this.mGalleryMamlView;
        if (galleryMamlView != null) {
            galleryMamlView.resume();
        }
    }

    public void pauseMaml() {
        GalleryMamlView galleryMamlView = this.mGalleryMamlView;
        if (galleryMamlView != null) {
            galleryMamlView.pause();
        }
    }

    public void destroyMaml() {
        GalleryMamlView galleryMamlView = this.mGalleryMamlView;
        if (galleryMamlView != null) {
            galleryMamlView.destroy();
        }
    }

    public void setIcon(int i) {
        this.mIcon.setImageResource(i);
    }

    public void setIcon(Drawable drawable) {
        this.mIcon.setImageDrawable(drawable);
    }

    public void setTitle(CharSequence charSequence) {
        this.mTitle.setText(charSequence);
    }

    public void setTitle(int i) {
        this.mTitle.setText(i);
    }

    public void setBigTitle(CharSequence charSequence) {
        this.mBigTitle.setText(charSequence);
    }

    public void setBigTitle(int i) {
        this.mBigTitle.setText(i);
    }

    public void setDescription(CharSequence charSequence) {
        this.mDescription.setText(charSequence);
    }

    public void setDescription(int i) {
        this.mDescription.setText(i);
    }

    public void setActionButtonText(CharSequence charSequence) {
        this.mActionButton.setText(charSequence);
    }

    public void setActionButtonText(int i) {
        this.mActionButton.setText(i);
    }

    public void setActionButtonBackground(int i) {
        this.mActionButton.setBackgroundResource(i);
    }

    public void setActionButtonBackground(Drawable drawable) {
        this.mActionButton.setBackground(drawable);
    }

    public void setActionButtonVisible(boolean z) {
        this.mActionButton.setVisibility(z ? 0 : 8);
    }

    public void setTopDividerLineVisible(boolean z) {
        this.mTopDividerLine.setVisibility(z ? 0 : 4);
    }

    public void setActionButtonClickable(boolean z) {
        this.mActionButton.setClickable(z);
    }

    /* loaded from: classes2.dex */
    public static class EmptyConfig {
        public boolean isActionButtonClickable;
        public boolean isShowActionButton;
        public Drawable mActionButtonBackgroundDrawable;
        public View.OnClickListener mActionButtonClickListener;
        public CharSequence mActionButtonTitle;
        public int mBackgroundId;
        public CharSequence mBigTitle;
        public int mBigTitleResId;
        public CharSequence mDescriptionText;
        public int mDescriptionTextResId;
        public Drawable mIconDrawable;
        public int mIconResId;
        public int mId;
        public CharSequence mTitle;
        public int mTitleResId;

        public EmptyConfig setTitle(int i) {
            this.mTitleResId = i;
            return this;
        }

        public EmptyConfig setIcon(int i) {
            this.mIconResId = i;
            return this;
        }

        public EmptyConfig disableActionButton() {
            this.isShowActionButton = false;
            return this;
        }

        public EmptyConfig setActionButtonVisible(boolean z) {
            this.isShowActionButton = z;
            return this;
        }

        public EmptyConfig setBackground(int i) {
            this.mBackgroundId = i;
            return this;
        }
    }

    public void apply(EmptyConfig emptyConfig) {
        if (emptyConfig.mIconDrawable != null) {
            setIcon(emptyConfig.mIconDrawable);
        } else if (emptyConfig.mIconResId != 0) {
            setIcon(emptyConfig.mIconResId);
        }
        if (emptyConfig.mDescriptionTextResId == 0) {
            if (emptyConfig.mDescriptionText != null) {
                setSingleLineTextMode(false);
                setDescription(emptyConfig.mDescriptionText);
            } else {
                setSingleLineTextMode(true);
            }
        } else {
            setSingleLineTextMode(false);
            setDescription(emptyConfig.mDescriptionTextResId);
        }
        if (!TextUtils.isEmpty(emptyConfig.mBigTitle)) {
            setBigTitle(emptyConfig.mBigTitle);
        } else if (emptyConfig.mBigTitleResId != 0) {
            setBigTitle(emptyConfig.mBigTitleResId);
        } else if (!TextUtils.isEmpty(emptyConfig.mTitle)) {
            setTitle(emptyConfig.mTitle);
        } else if (emptyConfig.mTitleResId != 0) {
            setTitle(emptyConfig.mTitleResId);
        }
        if (emptyConfig.mActionButtonBackgroundDrawable != null) {
            setActionButtonBackground(emptyConfig.mActionButtonBackgroundDrawable);
        }
        if (!TextUtils.isEmpty(emptyConfig.mActionButtonTitle)) {
            setActionButtonText(emptyConfig.mActionButtonTitle);
        }
        setActionButtonVisible(emptyConfig.isShowActionButton);
        setTopDividerLineVisible(emptyConfig.isActionButtonClickable);
        if (emptyConfig.isShowActionButton && emptyConfig.mActionButtonClickListener != null) {
            setOnActionButtonClickListener(emptyConfig.mActionButtonClickListener);
            setActionButtonClickable(emptyConfig.isActionButtonClickable);
        }
        if (emptyConfig.mId != 0) {
            setId(emptyConfig.mId);
        }
    }
}
