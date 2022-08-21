package com.meicam.themehelper;

import android.graphics.RectF;

/* loaded from: classes.dex */
public class NvsImageFileDesc {
    public boolean hasFace = false;
    public RectF faceRect = null;
    public String filePath = null;
    public boolean isCover = false;
    public boolean show = false;
    public boolean alternative = false;
    public float score = 0.0f;
    public float imgRatio = 0.0f;
    public boolean isLargeImg = false;
    public long fileLastTime = -1;

    public NvsImageFileDesc copy() {
        NvsImageFileDesc nvsImageFileDesc = new NvsImageFileDesc();
        nvsImageFileDesc.hasFace = this.hasFace;
        nvsImageFileDesc.isCover = this.isCover;
        nvsImageFileDesc.show = this.show;
        nvsImageFileDesc.alternative = this.alternative;
        nvsImageFileDesc.score = this.score;
        nvsImageFileDesc.fileLastTime = this.fileLastTime;
        nvsImageFileDesc.filePath = this.filePath;
        nvsImageFileDesc.imgRatio = this.imgRatio;
        nvsImageFileDesc.isLargeImg = this.isLargeImg;
        if (this.faceRect != null) {
            RectF rectF = new RectF();
            nvsImageFileDesc.faceRect = rectF;
            RectF rectF2 = this.faceRect;
            rectF.left = rectF2.left;
            rectF.top = rectF2.top;
            rectF.right = rectF2.right;
            rectF.bottom = rectF2.bottom;
        }
        return nvsImageFileDesc;
    }
}
