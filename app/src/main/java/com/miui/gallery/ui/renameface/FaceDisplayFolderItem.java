package com.miui.gallery.ui.renameface;

import com.miui.gallery.model.DisplayFolderItem;
import com.miui.gallery.util.face.FaceRegionRectF;

/* compiled from: FaceAlbumHandlerBase.java */
/* loaded from: classes2.dex */
public class FaceDisplayFolderItem extends DisplayFolderItem {
    public boolean isTip;
    public FaceRegionRectF mFacePosRelative;

    public FaceDisplayFolderItem(String str, String str2, String str3, FaceRegionRectF faceRegionRectF) {
        super(str, str2, str3);
        this.mFacePosRelative = faceRegionRectF;
    }

    public FaceDisplayFolderItem(boolean z) {
        super("", null, 0, "", "");
        this.isTip = z;
    }
}
