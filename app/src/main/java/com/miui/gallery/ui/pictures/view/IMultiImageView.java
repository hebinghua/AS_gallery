package com.miui.gallery.ui.pictures.view;

import android.view.View;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.ui.pictures.view.ImageCell;
import com.miui.gallery.widget.recyclerview.transition.TransitionalAnchor;
import java.util.List;

/* loaded from: classes2.dex */
public interface IMultiImageView extends TransitionalAnchor, ImageCell.RequestDrawingCallback {
    void bindData(List<ImageCellData> list);

    default void optionalInvalidateAfterResumed() {
    }

    void recycle();

    void setPreviewOptions(GlideOptions glideOptions);

    void setRecycledCellPoll(RecycledCellPoll recycledCellPoll);

    void setRequestOptions(GlideOptions glideOptions);

    void setSpacing(int i);

    void setSpanCount(int i);

    default View asView() {
        return (View) this;
    }
}
