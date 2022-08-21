package com.miui.gallery.cleaner;

import android.content.Context;
import com.miui.gallery.R;
import com.miui.gallery.util.FormatUtil;
import com.miui.gallery.util.RichTextUtil;

/* loaded from: classes.dex */
public class ScanResult {
    public int mAction;
    public int mCount;
    public int mCountText;
    public int mDescription;
    public ResultImage[] mImages;
    public OnScanResultClickListener mOnClickListener;
    public long mSize;
    public int mTitle;
    public int mType;

    /* loaded from: classes.dex */
    public interface OnScanResultClickListener {
        void onClick(Context context);
    }

    public ScanResult() {
    }

    public int getType() {
        return this.mType;
    }

    public long getSize() {
        return this.mSize;
    }

    public ResultImage[] getImages() {
        return this.mImages;
    }

    public int getCount() {
        return this.mCount;
    }

    public int getCountText() {
        return this.mCountText;
    }

    public int getTitle() {
        return this.mTitle;
    }

    public CharSequence getMergedSubTitle(Context context) {
        return getCount() > 0 ? RichTextUtil.splitTextWithDrawable(context, context.getResources().getQuantityString(getCountText(), getCount(), Integer.valueOf(getCount())), context.getResources().getDimensionPixelSize(R.dimen.home_page_grid_header_front_text), FormatUtil.formatFileSize(context, getSize()), context.getResources().getDimensionPixelSize(R.dimen.timeline_time_text_size), (int) R.drawable.cleaner_title_divider) : "";
    }

    public int getDescription() {
        return this.mDescription;
    }

    public int getAction() {
        return this.mAction;
    }

    public void setDescription(int i) {
        this.mDescription = i;
    }

    public void setAction(int i) {
        this.mAction = i;
    }

    public void onClick(Context context) {
        OnScanResultClickListener onScanResultClickListener = this.mOnClickListener;
        if (onScanResultClickListener != null) {
            onScanResultClickListener.onClick(context);
        }
    }

    /* loaded from: classes.dex */
    public static class Builder {
        public int mCount;
        public ResultImage[] mImages;
        public OnScanResultClickListener mOnClickListener;
        public long mSize;
        public int mType = -1;
        public int mCountText = R.plurals.photo_count_format;

        public Builder setType(int i) {
            this.mType = i;
            return this;
        }

        public Builder setSize(long j) {
            this.mSize = j;
            return this;
        }

        public Builder setImages(ResultImage[] resultImageArr) {
            this.mImages = resultImageArr;
            return this;
        }

        public Builder setCount(int i) {
            this.mCount = i;
            return this;
        }

        public Builder setOnScanResultClickListener(OnScanResultClickListener onScanResultClickListener) {
            this.mOnClickListener = onScanResultClickListener;
            return this;
        }

        public ScanResult build() {
            ScanResult scanResult = new ScanResult();
            int i = this.mType;
            if (i != -1) {
                if (i == 0) {
                    scanResult.mTitle = R.string.slim_preference_title;
                    scanResult.mDescription = R.string.slim_preference_summery;
                    scanResult.mAction = R.string.slim_action;
                } else if (i == 1) {
                    scanResult.mTitle = R.string.cleaner_screen_shot_title;
                    scanResult.mDescription = R.string.cleaner_screen_shot_description;
                    scanResult.mAction = R.string.cleaner_screen_shot_action;
                } else if (i == 3) {
                    scanResult.mTitle = R.string.cleaner_similar_title;
                    scanResult.mDescription = R.string.cleaner_similar_description;
                    scanResult.mAction = R.string.cleaner_similar_action;
                } else if (i == 4) {
                    scanResult.mTitle = R.string.cleaner_raw_title;
                    scanResult.mDescription = R.string.cleaner_raw_description;
                    scanResult.mAction = R.string.cleaner_raw_action;
                }
                if (scanResult.mTitle != 0) {
                    if (scanResult.mAction != 0) {
                        scanResult.mType = this.mType;
                        scanResult.mSize = this.mSize;
                        scanResult.mImages = this.mImages;
                        scanResult.mCount = this.mCount;
                        scanResult.mCountText = this.mCountText;
                        scanResult.mOnClickListener = this.mOnClickListener;
                        return scanResult;
                    }
                    throw new RuntimeException("the action must not be empty.");
                }
                throw new RuntimeException("the title must not be empty.");
            }
            throw new RuntimeException("the type must set.");
        }
    }

    /* loaded from: classes.dex */
    public static class ResultImage {
        public long mId;
        public String mPath;

        public ResultImage(long j, String str) {
            this.mId = j;
            this.mPath = str;
        }
    }
}
