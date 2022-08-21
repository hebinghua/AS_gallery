package com.miui.gallery.movie.nvsdk;

import android.content.Context;
import android.view.View;
import com.meicam.sdk.NvsMultiThumbnailSequenceView;
import com.miui.gallery.movie.ui.view.IMultiThumbnailSequenceView;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class NvsMultiThumbnailSequenceViewWrapper implements IMultiThumbnailSequenceView<ArrayList<NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc>> {
    public NvsMultiThumbnailSequenceView mImpl;

    public NvsMultiThumbnailSequenceViewWrapper(Context context) {
        this.mImpl = new NvsMultiThumbnailSequenceView(context);
    }

    @Override // com.miui.gallery.movie.ui.view.IMultiThumbnailSequenceView
    public void setThumbnailAspectRatio(float f) {
        this.mImpl.setThumbnailAspectRatio(f);
    }

    @Override // com.miui.gallery.movie.ui.view.IMultiThumbnailSequenceView
    public void setThumbnailImageFillMode(int i) {
        this.mImpl.setThumbnailImageFillMode(i);
    }

    @Override // com.miui.gallery.movie.ui.view.IMultiThumbnailSequenceView
    public double getPixelPerMicrosecond() {
        return this.mImpl.getPixelPerMicrosecond();
    }

    @Override // com.miui.gallery.movie.ui.view.IMultiThumbnailSequenceView
    public void setPixelPerMicrosecond(double d) {
        this.mImpl.setPixelPerMicrosecond(d);
    }

    @Override // com.miui.gallery.movie.ui.view.IMultiThumbnailSequenceView
    public void setThumbnailSequenceDescArray(ArrayList<NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc> arrayList) {
        this.mImpl.setThumbnailSequenceDescArray(arrayList);
    }

    @Override // com.miui.gallery.movie.ui.view.IMultiThumbnailSequenceView
    public void setStartPadding(int i) {
        this.mImpl.setStartPadding(i);
    }

    @Override // com.miui.gallery.movie.ui.view.IMultiThumbnailSequenceView
    public void setEndPadding(int i) {
        this.mImpl.setEndPadding(i);
    }

    @Override // com.miui.gallery.movie.ui.view.IMultiThumbnailSequenceView
    public int getSequenceWidth(int i, double d) {
        return (int) Math.floor((i * d * 1000.0d) + 0.5d);
    }

    @Override // com.miui.gallery.movie.ui.view.IMultiThumbnailSequenceView
    public View getRealView() {
        return this.mImpl;
    }

    @Override // com.miui.gallery.movie.ui.view.IMultiThumbnailSequenceView
    public void scrollTo(int i, int i2) {
        this.mImpl.scrollTo(i, i2);
    }

    @Override // com.miui.gallery.movie.ui.view.IMultiThumbnailSequenceView
    public void fullScroll(int i) {
        this.mImpl.fullScroll(i);
    }

    @Override // com.miui.gallery.movie.ui.view.IMultiThumbnailSequenceView
    public void setOnScrollChangeListener(final IMultiThumbnailSequenceView.OnScrollChangeListener onScrollChangeListener) {
        this.mImpl.setOnScrollChangeListenser(new NvsMultiThumbnailSequenceView.OnScrollChangeListener() { // from class: com.miui.gallery.movie.nvsdk.NvsMultiThumbnailSequenceViewWrapper$$ExternalSyntheticLambda0
            @Override // com.meicam.sdk.NvsMultiThumbnailSequenceView.OnScrollChangeListener
            public final void onScrollChanged(NvsMultiThumbnailSequenceView nvsMultiThumbnailSequenceView, int i, int i2) {
                IMultiThumbnailSequenceView.OnScrollChangeListener.this.onScrollChanged(i, i2);
            }
        });
    }
}
