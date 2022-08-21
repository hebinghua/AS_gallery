package com.miui.gallery.editor.photo.core.imports.mosaic;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import com.miui.gallery.editor.photo.core.imports.mosaic.MosaicGLEntity;
import com.miui.gallery.editor.photo.core.imports.mosaic.shader.GLTextureSizeShader;
import com.miui.gallery.editor.photo.widgets.glview.GLFBOManager;
import com.miui.gallery.editor.photo.widgets.glview.shader.GLShaderGroup;
import com.miui.gallery.editor.photo.widgets.glview.shader.GLTextureShader;
import com.miui.gallery.util.Bitmaps;
import com.miui.gallery.util.Scheme;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.StaticContext;

/* loaded from: classes2.dex */
public class MosaicEffectProcessor {
    public final int mOriginTextureHeight;
    public final int mOriginTextureWidth;
    public float[] mTextureCood = new float[8];
    public int mViewHeight;
    public int mViewWidth;

    public final float[] getGLPosition(float f) {
        float f2 = -f;
        return new float[]{-1.0f, f2, 1.0f, f2, -1.0f, f, 1.0f, f};
    }

    public MosaicEffectProcessor(int i, int i2, int i3, int i4) {
        this.mOriginTextureWidth = i;
        this.mOriginTextureHeight = i2;
        this.mViewWidth = i3;
        this.mViewHeight = i4;
    }

    public void updateViewPort(int i, int i2) {
        this.mViewWidth = i;
        this.mViewHeight = i2;
    }

    public void draw(GLFBOManager gLFBOManager, MosaicGLEntity mosaicGLEntity, int i, int i2, GLTextureShader gLTextureShader) {
        draw(gLFBOManager, mosaicGLEntity, i, i2, gLTextureShader, 1.0f, 1.0f, 1.0f);
    }

    /* renamed from: com.miui.gallery.editor.photo.core.imports.mosaic.MosaicEffectProcessor$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$editor$photo$core$imports$mosaic$MosaicGLEntity$TYPE;

        static {
            int[] iArr = new int[MosaicGLEntity.TYPE.values().length];
            $SwitchMap$com$miui$gallery$editor$photo$core$imports$mosaic$MosaicGLEntity$TYPE = iArr;
            try {
                iArr[MosaicGLEntity.TYPE.ORIGIN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$mosaic$MosaicGLEntity$TYPE[MosaicGLEntity.TYPE.RESOURCE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$mosaic$MosaicGLEntity$TYPE[MosaicGLEntity.TYPE.EFFECT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public void draw(GLFBOManager gLFBOManager, MosaicGLEntity mosaicGLEntity, int i, int i2, GLTextureShader gLTextureShader, float f, float f2, float f3) {
        if (mosaicGLEntity == null) {
            return;
        }
        int i3 = AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$core$imports$mosaic$MosaicGLEntity$TYPE[mosaicGLEntity.type.ordinal()];
        if (i3 == 1) {
            gLFBOManager.bind();
            GLES20.glClear(16640);
            drawOrigin(gLTextureShader, i2);
            gLFBOManager.unBind();
            return;
        }
        if (i3 == 2) {
            gLFBOManager.bind();
            GLES20.glClear(16640);
            drawResource((MosaicGLResourceEntity) mosaicGLEntity, gLTextureShader, f, f2, f3);
            gLFBOManager.unBind();
        } else if (i3 == 3) {
            drawEffect(gLFBOManager, (MosaicGLEffectEntity) mosaicGLEntity, gLTextureShader, i, f, f3);
        }
    }

    public final void drawOrigin(GLTextureShader gLTextureShader, int i) {
        gLTextureShader.draw(i);
    }

    public final void drawEffect(GLFBOManager gLFBOManager, MosaicGLEffectEntity mosaicGLEffectEntity, GLTextureShader gLTextureShader, int i, float f, float f2) {
        GLTextureSizeShader generateSpecificShader = mosaicGLEffectEntity.generateSpecificShader(this.mOriginTextureWidth, this.mOriginTextureHeight, this.mViewWidth, this.mViewHeight);
        if (generateSpecificShader != null) {
            generateSpecificShader.setScale(f);
            generateSpecificShader.setMosaicScale(f2);
            if (generateSpecificShader instanceof GLShaderGroup) {
                int effectedTexture = ((GLShaderGroup) generateSpecificShader).getEffectedTexture(i);
                gLFBOManager.bind();
                GLES20.glClear(16640);
                gLTextureShader.draw(effectedTexture);
                gLFBOManager.unBind();
            } else {
                gLFBOManager.bind();
                GLES20.glClear(16640);
                generateSpecificShader.drawFBO(i);
                gLFBOManager.unBind();
            }
            generateSpecificShader.destroy();
        }
    }

    public final void drawResource(MosaicGLResourceEntity mosaicGLResourceEntity, GLTextureShader gLTextureShader, float f, float f2, float f3) {
        String str = mosaicGLResourceEntity.mTileMode;
        String crop = Scheme.ASSETS.crop(mosaicGLResourceEntity.mResourceAssetPath);
        int i = 10497;
        if (str.equals("EDGE")) {
            i = 33071;
        } else {
            str.equals("REPEAT");
        }
        Bitmap decodeAsset = Bitmaps.decodeAsset(StaticContext.sGetAndroidContext(), crop, null);
        if (decodeAsset == null || decodeAsset.getWidth() <= 0 || decodeAsset.getHeight() <= 0) {
            return;
        }
        Bitmap changeBitmapSize = changeBitmapSize(decodeAsset);
        getTextureCood(this.mTextureCood, (this.mOriginTextureWidth / changeBitmapSize.getWidth()) / f, (((this.mOriginTextureHeight / changeBitmapSize.getHeight()) / f) * f2) / f3);
        int loadTexture = loadTexture(changeBitmapSize, i);
        gLTextureShader.draw(loadTexture, getGLPosition(f2), this.mTextureCood);
        GLES20.glDeleteTextures(1, new int[]{loadTexture}, 0);
    }

    public final Bitmap changeBitmapSize(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        float screenWidth = this.mOriginTextureWidth / ScreenUtils.getScreenWidth();
        if (screenWidth < 1.0f) {
            if (bitmap.getWidth() * screenWidth < 1.0f || bitmap.getHeight() * screenWidth < 1.0f) {
                return Bitmap.createBitmap(bitmap, 0, 0, 1, 1, matrix, false);
            }
            matrix.postScale(screenWidth, screenWidth);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        }
        return bitmap;
    }

    public static void getTextureCood(float[] fArr, float f, float f2) {
        fArr[0] = 0.0f;
        fArr[1] = f2;
        fArr[2] = f;
        fArr[3] = f2;
        fArr[4] = 0.0f;
        fArr[5] = 0.0f;
        fArr[6] = f;
        fArr[7] = 0.0f;
    }

    public static int loadTexture(Bitmap bitmap, int i) {
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        GLES20.glBindTexture(3553, iArr[0]);
        GLES20.glTexParameterf(3553, 10240, 9729.0f);
        GLES20.glTexParameterf(3553, 10241, 9729.0f);
        float f = i;
        GLES20.glTexParameterf(3553, 10242, f);
        GLES20.glTexParameterf(3553, 10243, f);
        GLUtils.texImage2D(3553, 0, bitmap, 0);
        bitmap.recycle();
        return iArr[0];
    }
}
