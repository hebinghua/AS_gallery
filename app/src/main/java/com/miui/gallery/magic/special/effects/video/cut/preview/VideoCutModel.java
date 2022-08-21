package com.miui.gallery.magic.special.effects.video.cut.preview;

import android.net.Uri;
import com.miui.gallery.magic.base.BaseModel;
import com.miui.gallery.magic.util.GetPathFromUri;

/* loaded from: classes2.dex */
public class VideoCutModel extends BaseModel<VideoCutPresenter, ICut$M> {
    public VideoCutModel(VideoCutPresenter videoCutPresenter) {
        super(videoCutPresenter);
    }

    @Override // com.miui.gallery.magic.base.SuperBase
    /* renamed from: initContract */
    public ICut$M mo1070initContract() {
        return new ICut$M() { // from class: com.miui.gallery.magic.special.effects.video.cut.preview.VideoCutModel.1
            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$M
            public String decode(Uri uri) {
                return uri == null ? "" : GetPathFromUri.getPath(((VideoCutPresenter) VideoCutModel.this.mPresenter).getActivity(), uri);
            }
        };
    }
}
