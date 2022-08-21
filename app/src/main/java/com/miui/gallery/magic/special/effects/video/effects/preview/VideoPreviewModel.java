package com.miui.gallery.magic.special.effects.video.effects.preview;

import android.net.Uri;
import com.miui.gallery.magic.base.BaseModel;
import com.miui.gallery.magic.util.GetPathFromUri;

/* loaded from: classes2.dex */
public class VideoPreviewModel extends BaseModel<VideoPreviewPresenter, IPreview$M> {
    public VideoPreviewModel(VideoPreviewPresenter videoPreviewPresenter) {
        super(videoPreviewPresenter);
    }

    @Override // com.miui.gallery.magic.base.SuperBase
    /* renamed from: initContract */
    public IPreview$M mo1070initContract() {
        return new IPreview$M() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewModel.1
            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$M
            public String decode(Uri uri) {
                return uri == null ? "" : GetPathFromUri.getPath(((VideoPreviewPresenter) VideoPreviewModel.this.mPresenter).getActivity(), uri);
            }
        };
    }
}
