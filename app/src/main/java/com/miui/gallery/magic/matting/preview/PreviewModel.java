package com.miui.gallery.magic.matting.preview;

import android.graphics.Bitmap;
import android.net.Uri;
import com.miui.gallery.magic.base.BaseModel;
import com.miui.gallery.magic.matting.bean.BackgroundItem;
import com.miui.gallery.magic.util.MagicFileUtil;
import java.io.IOException;

/* loaded from: classes2.dex */
public class PreviewModel extends BaseModel<PreviewPresenter, IPreview$M> {
    public Uri mUri;

    public PreviewModel(PreviewPresenter previewPresenter) {
        super(previewPresenter);
    }

    @Override // com.miui.gallery.magic.base.SuperBase
    /* renamed from: initContract */
    public IPreview$M mo1070initContract() {
        return new IPreview$M() { // from class: com.miui.gallery.magic.matting.preview.PreviewModel.1
            @Override // com.miui.gallery.magic.matting.preview.IPreview$M
            public Bitmap decodeOrigin(Uri uri) {
                if (uri == null) {
                    return null;
                }
                PreviewModel.this.mUri = uri;
                return PreviewModel.this.getBitmap(uri, 1200);
            }

            @Override // com.miui.gallery.magic.matting.preview.IPreview$M
            public Bitmap decodeOrigin(BackgroundItem backgroundItem) {
                Uri originUri = backgroundItem.getBackgroundIndex() == 1 ? backgroundItem.getOriginUri() : null;
                if (originUri == null) {
                    originUri = PreviewModel.this.mUri;
                }
                backgroundItem.setOriginUri(originUri);
                return PreviewModel.this.getBitmap(originUri, 5000);
            }
        };
    }

    public final Bitmap getBitmap(Uri uri, int i) {
        try {
            return MagicFileUtil.getBitmap(((PreviewPresenter) this.mPresenter).getActivity(), uri, i);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
