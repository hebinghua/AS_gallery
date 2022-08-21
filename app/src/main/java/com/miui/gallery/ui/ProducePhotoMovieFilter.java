package com.miui.gallery.ui;

import android.content.Context;
import com.miui.gallery.R;
import com.miui.gallery.adapter.CheckableAdapter;
import com.miui.gallery.util.ToastUtils;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public class ProducePhotoMovieFilter extends BaseProduceFilter {
    public static final int[] PRODUCE_RANGE = ProduceRangeContract.PRODUCE_PHOTO_MOVIE_RANGE;
    public static final List<String> PRODUCE_UNSUPPORTED_MIME_TYPE = Arrays.asList("video/*", "image/x-adobe-dng");

    public ProducePhotoMovieFilter(List<CheckableAdapter.CheckedItem> list) {
        super(list, PRODUCE_RANGE, PRODUCE_UNSUPPORTED_MIME_TYPE);
    }

    @Override // com.miui.gallery.ui.BaseProduceFilter
    public void showUnsupportedToast() {
        Context context = this.mContext;
        ToastUtils.makeText(context, context.getString(R.string.unsupport_raw_and_video, context.getString(R.string.home_menu_photo_movie)));
    }

    @Override // com.miui.gallery.ui.BaseProduceFilter
    public void showOutOfRangeToast() {
        Context context = this.mContext;
        ToastUtils.makeText(context, context.getString(R.string.photo_movie_select_image_range, Integer.valueOf(this.mSupportMimeRange[0]), Integer.valueOf(this.mSupportMimeRange[1])));
    }
}
