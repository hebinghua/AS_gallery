package com.miui.gallery.magic.idphoto.preview;

import android.graphics.Bitmap;
import android.graphics.Rect;
import com.miui.gallery.magic.idphoto.bean.PhotoStyle;
import com.miui.gallery.magic.widget.idphoto.IDPhotoView;

/* loaded from: classes2.dex */
public interface IPreview$VP {
    IDPhotoView getIDPhotoView();

    Bitmap getPhotoPaper(Bitmap bitmap);

    void initBlending(PhotoStyle photoStyle);

    void initFaceInvoker();

    void initIdpData(Bitmap bitmap);

    void loadPhotoPaper(Bitmap bitmap, PhotoStyle photoStyle);

    void loadPreview(Bitmap bitmap, PhotoStyle photoStyle, Rect rect);

    void loadProcessBitmap();

    void saveImage(int i);

    void setPhotoPaper();

    void sizeChange(PhotoStyle photoStyle);
}
