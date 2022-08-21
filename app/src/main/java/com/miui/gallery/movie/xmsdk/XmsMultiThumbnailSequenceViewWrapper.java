package com.miui.gallery.movie.xmsdk;

import android.content.Context;
import android.view.View;
import com.miui.gallery.movie.ui.view.IMultiThumbnailSequenceView;
import com.miui.gallery.xmstreaming.XmsMultiThumbnailSequenceView;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class XmsMultiThumbnailSequenceViewWrapper implements IMultiThumbnailSequenceView<ArrayList<XmsMultiThumbnailSequenceView.ThumbnailSequenceDesc>> {
    public XmsMultiThumbnailSequenceView mImpl;

    public static /* synthetic */ void $r8$lambda$H2TJRgWdOq7k_HLJo3c8iNzkE64(IMultiThumbnailSequenceView.OnScrollChangeListener onScrollChangeListener, XmsMultiThumbnailSequenceView xmsMultiThumbnailSequenceView, int i, int i2) {
        onScrollChangeListener.onScrollChanged(i, i2);
    }

    public XmsMultiThumbnailSequenceViewWrapper(Context context) {
        this.mImpl = new XmsMultiThumbnailSequenceViewImpl(context);
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
    public void setThumbnailSequenceDescArray(ArrayList<XmsMultiThumbnailSequenceView.ThumbnailSequenceDesc> arrayList) {
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
        this.mImpl.setOnScrollChangeListenser(new XmsMultiThumbnailSequenceView.OnScrollChangeListener() { // from class: com.miui.gallery.movie.xmsdk.XmsMultiThumbnailSequenceViewWrapper$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.xmstreaming.XmsMultiThumbnailSequenceView.OnScrollChangeListener
            public final void onScrollChanged(XmsMultiThumbnailSequenceView xmsMultiThumbnailSequenceView, int i, int i2) {
                XmsMultiThumbnailSequenceViewWrapper.$r8$lambda$H2TJRgWdOq7k_HLJo3c8iNzkE64(IMultiThumbnailSequenceView.OnScrollChangeListener.this, xmsMultiThumbnailSequenceView, i, i2);
            }
        });
    }
}
