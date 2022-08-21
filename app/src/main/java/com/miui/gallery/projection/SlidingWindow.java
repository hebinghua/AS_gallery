package com.miui.gallery.projection;

import android.text.TextUtils;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.projection.IConnectController;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Arrays;

/* compiled from: ConnectController.java */
/* loaded from: classes2.dex */
public class SlidingWindow {
    public int mEndIndex;
    public IConnectController.DataSet mMediaSet;
    public int mStartIndex;
    public int mTotalCount;
    public String[] mFiles = new String[151];
    public int mCurrentIndex = 0;
    public boolean mCurrentIndexChanged = true;

    public synchronized void setMediaSet(IConnectController.DataSet dataSet) {
        IConnectController.DataSet dataSet2 = this.mMediaSet;
        if (dataSet2 != dataSet || dataSet != null) {
            if (dataSet == null || dataSet2 != dataSet) {
                reset();
            }
            this.mMediaSet = dataSet;
        }
    }

    public final void reset() {
        this.mStartIndex = 0;
        this.mEndIndex = 0;
        Arrays.fill(this.mFiles, (Object) null);
        this.mCurrentIndex = 0;
        this.mCurrentIndexChanged = true;
    }

    public synchronized String getPrevious(String str, boolean z) {
        if (this.mCurrentIndexChanged) {
            this.mCurrentIndexChanged = false;
            slideWindowTo(this.mCurrentIndex);
        }
        if (this.mTotalCount > 0 && !TextUtils.isEmpty(str)) {
            int indexOf = indexOf(str);
            if (indexOf == 0) {
                slideWindowTo(this.mStartIndex);
                indexOf = indexOf(str);
            }
            if (indexOf == -1) {
                indexOf = this.mCurrentIndex - this.mStartIndex;
            }
            int i = indexOf - 1;
            if (z) {
                if (i < 0) {
                    i = this.mFiles.length - 1;
                }
            } else if (i < 0) {
                return null;
            }
            DefaultLogger.d("SlidingWindow", "getPrevious: pre=" + this.mFiles[i] + ", index=" + i);
            return this.mFiles[i];
        }
        return null;
    }

    public synchronized String getNext(String str, boolean z) {
        int i = 0;
        if (this.mCurrentIndexChanged) {
            this.mCurrentIndexChanged = false;
            slideWindowTo(this.mCurrentIndex);
        }
        if (this.mTotalCount > 0 && !TextUtils.isEmpty(str)) {
            int indexOf = indexOf(str);
            if (indexOf == -1) {
                indexOf = this.mCurrentIndex - this.mStartIndex;
            } else {
                int i2 = this.mEndIndex;
                if (indexOf == (i2 - this.mStartIndex) - 1) {
                    if (i2 < this.mTotalCount) {
                        slideWindowTo(i2 - 1);
                        indexOf = indexOf(str);
                    } else if (!z) {
                        return null;
                    } else {
                        slideWindowTo(0);
                        indexOf = -1;
                    }
                }
            }
            int i3 = indexOf + 1;
            if (z) {
                if (i3 >= this.mFiles.length) {
                    DefaultLogger.d("SlidingWindow", "getNext: next=" + this.mFiles[i] + ", index=" + i);
                    return this.mFiles[i];
                }
            } else if (i3 >= this.mFiles.length) {
                return null;
            }
            i = i3;
            DefaultLogger.d("SlidingWindow", "getNext: next=" + this.mFiles[i] + ", index=" + i);
            return this.mFiles[i];
        }
        return null;
    }

    public final int indexOf(String str) {
        String[] strArr;
        if (str != null) {
            int i = 0;
            for (String str2 : this.mFiles) {
                if (str2 != null && str2.equals(str)) {
                    return i;
                }
                i++;
            }
            return -1;
        }
        return -1;
    }

    public final void doSlideWindow(int i) {
        IConnectController.DataSet dataSet = this.mMediaSet;
        if (dataSet == null) {
            reset();
            return;
        }
        int count = dataSet.getCount();
        this.mTotalCount = count;
        if (count <= 0) {
            reset();
            return;
        }
        int i2 = 0;
        this.mStartIndex = Math.max(i - 75, 0);
        this.mEndIndex = Math.min(i + 75 + 1, this.mTotalCount);
        for (int i3 = this.mStartIndex; i3 < this.mEndIndex; i3++) {
            BaseDataItem item = this.mMediaSet.getItem(null, i3);
            if (item != null) {
                String pathDisplayBetter = item.getPathDisplayBetter();
                if (!TextUtils.isEmpty(pathDisplayBetter)) {
                    this.mFiles[i2] = pathDisplayBetter;
                    i2++;
                }
            }
        }
        DefaultLogger.d("SlidingWindow", "slideWindow, mStartIndex=" + this.mStartIndex + ", mEndIndex=" + this.mEndIndex);
    }

    public final void slideWindowTo(int i) {
        int i2;
        IConnectController.DataSet dataSet = this.mMediaSet;
        if (dataSet == null) {
            reset();
            return;
        }
        int count = dataSet.getCount();
        if (count <= 0) {
            reset();
            return;
        }
        if (i < 0) {
            i = 0;
        } else if (i >= count) {
            i = count - 1;
        }
        int i3 = this.mStartIndex;
        int i4 = this.mEndIndex;
        if (i3 != i4 && count == (i2 = this.mTotalCount) && (i2 <= i4 - i3 || !isWindowDirty(i))) {
            return;
        }
        doSlideWindow(i);
    }

    public final boolean isWindowDirty(int i) {
        int i2;
        int i3 = this.mStartIndex;
        return (i3 > 0 && i - i3 < 50) || ((i2 = this.mEndIndex) < this.mTotalCount && i2 - i < 50);
    }

    public synchronized void onCurrentIndexChanged(int i) {
        this.mCurrentIndexChanged |= this.mCurrentIndex != i;
        this.mCurrentIndex = i;
    }

    public void releaseData() {
        if (this.mMediaSet != null) {
            this.mMediaSet = null;
        }
    }
}
