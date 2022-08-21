package com.miui.gallery.editor.ui.view.switchview;

/* loaded from: classes2.dex */
public class MoreSlideSwitchConfig {
    public int mNormalTextColor;
    public int mSelectedColor;
    public int mSelectedHeight;
    public int mSelectedTextColor;
    public int mSelectedWidth;
    public int mSelectedXRadius;
    public int mSelectedYRadius;
    public int mTextSize;
    public String[] mTitles;

    public String[] getTitles() {
        return this.mTitles;
    }

    public int getSelectedWidth() {
        return this.mSelectedWidth;
    }

    public void setSelectedWidth(int i) {
        this.mSelectedWidth = i;
    }

    public int getSelectedHeight() {
        return this.mSelectedHeight;
    }

    public void setSelectedHeight(int i) {
        this.mSelectedHeight = i;
    }

    public int getSelectedColor() {
        return this.mSelectedColor;
    }

    public void setSelectedColor(int i) {
        this.mSelectedColor = i;
    }

    public int getSelectedXRadius() {
        return this.mSelectedXRadius;
    }

    public void setSelectedXRadius(int i) {
        this.mSelectedXRadius = i;
    }

    public int getSelectedYRadius() {
        return this.mSelectedYRadius;
    }

    public void setSelectedYRadius(int i) {
        this.mSelectedYRadius = i;
    }

    public int getNormalTextColor() {
        return this.mNormalTextColor;
    }

    public void setNormalTextColor(int i) {
        this.mNormalTextColor = i;
    }

    public int getSelectedTextColor() {
        return this.mSelectedTextColor;
    }

    public void setSelectedTextColor(int i) {
        this.mSelectedTextColor = i;
    }

    public int getTextSize() {
        return this.mTextSize;
    }

    public void setTextSize(int i) {
        this.mTextSize = i;
    }

    /* loaded from: classes2.dex */
    public static final class Builder {
        public int mNormalTextColor;
        public int mSelectedColor;
        public int mSelectedHeight;
        public int mSelectedTextColor;
        public int mSelectedWidth;
        public int mSelectedXRadius;
        public int mSelectedYRadius;
        public int mTextSize;
        public String[] mTitles;

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder withTitles(String[] strArr) {
            this.mTitles = strArr;
            return this;
        }

        public Builder withSelectedWidth(int i) {
            this.mSelectedWidth = i;
            return this;
        }

        public MoreSlideSwitchConfig build() {
            MoreSlideSwitchConfig moreSlideSwitchConfig = new MoreSlideSwitchConfig();
            moreSlideSwitchConfig.mSelectedWidth = this.mSelectedWidth;
            moreSlideSwitchConfig.mNormalTextColor = this.mNormalTextColor;
            moreSlideSwitchConfig.mSelectedColor = this.mSelectedColor;
            moreSlideSwitchConfig.mSelectedTextColor = this.mSelectedTextColor;
            moreSlideSwitchConfig.mSelectedHeight = this.mSelectedHeight;
            moreSlideSwitchConfig.mSelectedYRadius = this.mSelectedYRadius;
            moreSlideSwitchConfig.mTitles = this.mTitles;
            moreSlideSwitchConfig.mSelectedXRadius = this.mSelectedXRadius;
            moreSlideSwitchConfig.mTextSize = this.mTextSize;
            return moreSlideSwitchConfig;
        }
    }
}
