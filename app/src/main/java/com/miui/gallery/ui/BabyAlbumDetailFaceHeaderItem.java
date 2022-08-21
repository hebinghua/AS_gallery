package com.miui.gallery.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.R;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.RegionConfig;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.util.face.FaceRegionRectF;
import com.miui.gallery.util.glide.BindImageHelper;
import com.miui.gallery.util.glide.GlideRequestManagerHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Objects;

/* loaded from: classes2.dex */
public class BabyAlbumDetailFaceHeaderItem extends LinearLayout {
    public TextView mAgeCurrent;
    public TextView mAlbumName;
    public ImageView mBackground;
    public String mCurrentBgPhotoPath;
    public GlideOptions mDefaultRequestOptions;
    public ImageView mFace;
    public String mFacePath;

    public BabyAlbumDetailFaceHeaderItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mBackground = (ImageView) findViewById(R.id.background);
        this.mFace = (ImageView) findViewById(R.id.face);
        this.mAlbumName = (TextView) findViewById(R.id.album_name);
        this.mAgeCurrent = (TextView) findViewById(R.id.age_current);
        if (this.mDefaultRequestOptions == null) {
            this.mDefaultRequestOptions = GlideOptions.microThumbOf().mo972placeholder(R.drawable.baby_empty_default).autoClone();
        }
    }

    public void setOnFaceClickListener(View.OnClickListener onClickListener) {
        this.mFace.setOnClickListener(onClickListener);
    }

    public void setOnBackgroundClickListener(View.OnClickListener onClickListener) {
        this.mBackground.setOnClickListener(onClickListener);
    }

    public void bindHeaderBackgroundPic(String str, Uri uri, RequestOptions requestOptions) {
        String str2 = this.mCurrentBgPhotoPath;
        if (str2 == null || !Objects.equals(str2, str)) {
            BindImageHelper.bindImage(str, uri, DownloadType.THUMBNAIL, this.mBackground, requestOptions);
            this.mCurrentBgPhotoPath = str;
        }
    }

    public void bindHeadFacePic(String str, FaceRegionRectF faceRegionRectF) {
        this.mFacePath = str;
        RequestManager safeGet = GlideRequestManagerHelper.safeGet(this.mFace);
        if (safeGet != null) {
            safeGet.mo985asBitmap().mo962load(GalleryModel.of(str)).mo946apply((BaseRequestOptions<?>) this.mDefaultRequestOptions.decodeRegion(RegionConfig.ofFace(faceRegionRectF, 2.0f))).into(this.mFace);
        } else {
            DefaultLogger.w("BabyAlbumDetailFaceHeaderItem", "bindHeadFacePic failed, maybe caused by page destroy");
        }
    }

    public void bindHeadFacePicFromNet(String str, FaceRegionRectF faceRegionRectF) {
        this.mFacePath = str;
        RequestManager safeGet = GlideRequestManagerHelper.safeGet(this.mFace);
        if (safeGet != null) {
            safeGet.mo985asBitmap().mo962load(GalleryModel.of(str)).mo946apply((BaseRequestOptions<?>) this.mDefaultRequestOptions.decodeRegion(RegionConfig.ofFace(faceRegionRectF, 2.0f))).into(this.mFace);
        } else {
            DefaultLogger.w("BabyAlbumDetailFaceHeaderItem", "bindHeadFacePicFromNet failed, maybe caused by page destroy");
        }
    }

    public String getFacePath() {
        return this.mFacePath;
    }

    public Bitmap getHeadFacePic() {
        Bitmap bitmap;
        Drawable drawable = this.mFace.getDrawable();
        if (!(drawable instanceof BitmapDrawable) || (bitmap = ((BitmapDrawable) drawable).getBitmap()) == null) {
            return null;
        }
        return bitmap.copy(Bitmap.Config.ARGB_8888, true);
    }

    public void setAlbumName(String str) {
        this.mAlbumName.setText(str);
    }

    public void setAge(String str) {
        this.mAgeCurrent.setText(str);
    }
}
