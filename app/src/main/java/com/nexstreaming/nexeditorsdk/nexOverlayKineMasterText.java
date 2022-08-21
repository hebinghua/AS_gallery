package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import java.io.File;
import java.text.Bidi;

@Deprecated
/* loaded from: classes3.dex */
public final class nexOverlayKineMasterText extends nexOverlayImage {
    private static final int DEFAULT_BACKGROUND_COLOR = -2013265920;
    private static final String LOG_TAG = "nexOverlayKineMasterText";
    private static final int MAX_TEX_SIZE = 2000;
    private static final int TEXT_WRAP_WIDTH = 1280;
    private static int number = 1;
    private int TextId;
    private float actualScale;
    private boolean bUserFontMode;
    private int backgroundColor;
    private TextPaint cachedTextPaint;
    private boolean enableBackground;
    private boolean enableGlow;
    private boolean enableGradient;
    private boolean enableOutline;
    private boolean enableShadow;
    private boolean extendBackground;
    private String fontId;
    private int glowColor;
    private float glowRadius;
    private int glowType;
    private int[] gradientColors;
    private transient int height;
    private String layerText;
    private Context mContext;
    private int outlineColor;
    private float outlineWidth;
    public final boolean serialKMText;
    private int shadowColor;
    private float shadowDx;
    private float shadowDy;
    private float shadowRadius;
    private Layout.Alignment textAlign;
    private int textColor;
    private transient StaticLayout textLayout;
    private float textSize;
    private transient boolean validDimensions;
    private transient int width;

    @Deprecated
    public nexOverlayKineMasterText(Context context) {
        super("nexOverlayStandardText");
        this.layerText = "";
        this.textSize = 50.0f;
        this.textColor = -1;
        this.enableBackground = false;
        this.extendBackground = false;
        this.backgroundColor = DEFAULT_BACKGROUND_COLOR;
        this.enableShadow = true;
        this.shadowColor = -16777216;
        this.shadowRadius = 5.0f;
        this.shadowDx = 3.0f;
        this.shadowDy = 3.0f;
        this.enableGlow = false;
        this.glowColor = -1426063446;
        this.glowRadius = 8.0f;
        this.glowType = 0;
        this.enableOutline = false;
        this.outlineColor = -3355444;
        this.outlineWidth = 1.0f;
        this.enableGradient = false;
        this.actualScale = 1.0f;
        this.textAlign = Layout.Alignment.ALIGN_CENTER;
        this.TextId = 0;
        this.serialKMText = true;
        this.mContext = context;
        int i = number;
        this.TextId = i;
        number = i + 1;
    }

    @Deprecated
    public nexOverlayKineMasterText(Context context, String str, int i) {
        super("nexOverlayStandardText");
        this.layerText = "";
        this.textSize = 50.0f;
        this.textColor = -1;
        this.enableBackground = false;
        this.extendBackground = false;
        this.backgroundColor = DEFAULT_BACKGROUND_COLOR;
        this.enableShadow = true;
        this.shadowColor = -16777216;
        this.shadowRadius = 5.0f;
        this.shadowDx = 3.0f;
        this.shadowDy = 3.0f;
        this.enableGlow = false;
        this.glowColor = -1426063446;
        this.glowRadius = 8.0f;
        this.glowType = 0;
        this.enableOutline = false;
        this.outlineColor = -3355444;
        this.outlineWidth = 1.0f;
        this.enableGradient = false;
        this.actualScale = 1.0f;
        this.textAlign = Layout.Alignment.ALIGN_CENTER;
        this.TextId = 0;
        this.serialKMText = true;
        this.mContext = context;
        this.layerText = str;
        this.textSize = i;
        int i2 = number;
        this.TextId = i2;
        number = i2 + 1;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexOverlayImage
    public int getWidth() {
        calcDimensions();
        return this.width;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexOverlayImage
    public int getHeight() {
        calcDimensions();
        return this.height;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexOverlayImage
    public Bitmap getUserBitmap() {
        return makeTextBitmap();
    }

    @Override // com.nexstreaming.nexeditorsdk.nexOverlayImage
    public String getUserBitmapID() {
        return "KineMasterText-" + this.TextId;
    }

    @Deprecated
    public String getText() {
        String str = this.layerText;
        return str == null ? "" : str;
    }

    @Deprecated
    public void setText(String str) {
        this.layerText = str;
        this.validDimensions = false;
        this.mUpdate = true;
    }

    @Deprecated
    public void setTextSize(float f) {
        this.textSize = f;
        this.validDimensions = false;
        this.mUpdate = true;
    }

    @Deprecated
    public void setTextColor(int i) {
        if (this.textColor != i) {
            this.textColor = i;
            this.mUpdate = true;
        }
    }

    @Deprecated
    public float getTextSize() {
        return this.textSize;
    }

    @Deprecated
    public int getTextColor() {
        return this.textColor;
    }

    @Deprecated
    public int getGlowColor() {
        return getGlowColor(true);
    }

    public int getGlowColor(boolean z) {
        if (!z) {
            if (!this.enableGlow) {
                return 0;
            }
            return this.glowColor;
        }
        return this.glowColor;
    }

    @Deprecated
    public void setGlowColor(int i) {
        if (this.glowColor != i) {
            this.glowColor = i;
            if (!this.enableGlow) {
                return;
            }
            this.mUpdate = true;
        }
    }

    @Deprecated
    public int getOutlineColor() {
        return getOutlineColor(true);
    }

    public int getOutlineColor(boolean z) {
        if (!z) {
            if (!this.enableOutline) {
                return 0;
            }
            return this.outlineColor;
        }
        return this.outlineColor;
    }

    @Deprecated
    public void setOutlineColor(int i) {
        if (this.outlineColor != i) {
            this.outlineColor = i;
            if (!this.enableOutline) {
                return;
            }
            this.mUpdate = true;
        }
    }

    @Deprecated
    public int getShadowColor() {
        return getShadowColor(true);
    }

    public int getShadowColor(boolean z) {
        if (!z) {
            if (!this.enableShadow) {
                return 0;
            }
            return this.shadowColor;
        }
        return this.shadowColor;
    }

    @Deprecated
    public void setShadowColor(int i) {
        if (this.shadowColor != i) {
            this.shadowColor = i;
            if (!this.enableShadow) {
                return;
            }
            this.mUpdate = true;
        }
    }

    @Deprecated
    public boolean isEnableShadow() {
        return this.enableShadow;
    }

    @Deprecated
    public void EnableShadow(boolean z) {
        if (this.enableShadow != z) {
            this.enableShadow = z;
            this.mUpdate = true;
        }
    }

    @Deprecated
    public boolean isEnableGlow() {
        return this.enableGlow;
    }

    @Deprecated
    public void EnableGlow(boolean z) {
        if (this.enableGlow != z) {
            this.enableGlow = z;
            this.mUpdate = true;
        }
    }

    @Deprecated
    public boolean isEnableOutline() {
        return this.enableOutline;
    }

    @Deprecated
    public void EnableOutline(boolean z) {
        if (this.enableOutline != z) {
            this.enableOutline = z;
            this.mUpdate = true;
        }
    }

    @Deprecated
    public void setFontId(String str) {
        if (str == null) {
            this.bUserFontMode = false;
            this.fontId = null;
            this.validDimensions = false;
            this.mUpdate = true;
            calcDimensions();
        } else if (this.fontId == str) {
        } else {
            if (str.charAt(0) == '/') {
                int lastIndexOf = str.lastIndexOf(46);
                if (lastIndexOf == -1 || str.substring(lastIndexOf).compareToIgnoreCase(".ttf") != 0 || !new File(str).isFile()) {
                    return;
                }
                this.bUserFontMode = true;
                this.fontId = str;
                this.validDimensions = false;
                this.mUpdate = true;
                calcDimensions();
            } else if (!nexFont.isLoadedFont(str)) {
            } else {
                this.bUserFontMode = false;
                this.fontId = str;
                this.validDimensions = false;
                this.mUpdate = true;
                calcDimensions();
            }
        }
    }

    @Deprecated
    public String getFontId() {
        return getFontIdInternal(true);
    }

    public String getFontIdInternal(boolean z) {
        if (z) {
            return this.fontId;
        }
        if (this.bUserFontMode) {
            return null;
        }
        return this.fontId;
    }

    private synchronized Bitmap makeTextBitmap() {
        calcDimensions();
        int shadowPadding = getShadowPadding();
        float f = this.actualScale;
        int i = (int) (this.width * f);
        int i2 = (int) (this.height * f);
        if (i > 0 && i2 > 0) {
            Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            float f2 = this.actualScale;
            canvas.scale(f2, f2);
            float f3 = shadowPadding;
            canvas.translate(f3, f3);
            TextPaint paint = this.textLayout.getPaint();
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setStyle(Paint.Style.FILL);
            if (this.enableShadow) {
                paint.setMaskFilter(new BlurMaskFilter(this.shadowRadius / this.actualScale, BlurMaskFilter.Blur.NORMAL));
                paint.setColor(this.shadowColor);
                canvas.save();
                canvas.translate(this.shadowDx, this.shadowDy);
                this.textLayout.draw(canvas);
                canvas.restore();
            }
            if (this.enableGlow) {
                paint.setMaskFilter(new BlurMaskFilter(this.glowRadius / this.actualScale, BlurMaskFilter.Blur.OUTER));
                paint.setColor(this.glowColor);
                this.textLayout.draw(canvas);
            }
            paint.setMaskFilter(null);
            paint.setColor(this.textColor);
            this.textLayout.draw(canvas);
            if (this.enableOutline) {
                this.textLayout.getPaint().setStyle(Paint.Style.STROKE);
                paint.setColor(this.outlineColor);
                paint.setStrokeWidth(this.outlineWidth / this.actualScale);
                this.textLayout.draw(canvas);
            }
            return createBitmap;
        }
        return null;
    }

    private TextPaint getPaint() {
        TextPaint textPaint = this.cachedTextPaint;
        if (textPaint == null) {
            this.cachedTextPaint = new TextPaint();
        } else {
            textPaint.reset();
        }
        TextPaint textPaint2 = this.cachedTextPaint;
        textPaint2.setTextSize(this.textSize);
        textPaint2.setAntiAlias(true);
        textPaint2.setColor(-1);
        textPaint2.setStrokeWidth(this.outlineWidth);
        textPaint2.setStyle(Paint.Style.FILL_AND_STROKE);
        Typeface typeface = null;
        String str = this.fontId;
        if (str != null) {
            if (this.bUserFontMode) {
                typeface = Typeface.createFromFile(str);
            } else {
                typeface = nexFont.getTypeface(this.mContext, str);
            }
        }
        if (typeface != null) {
            textPaint2.setTypeface(typeface);
        }
        return textPaint2;
    }

    private int getShadowPadding() {
        return (int) Math.ceil(Math.max(this.outlineWidth, Math.max(this.glowRadius, this.shadowRadius + Math.max(Math.abs(this.shadowDx), Math.abs(this.shadowDy)))));
    }

    private synchronized void calcDimensions() {
        if (!this.validDimensions || this.textLayout == null) {
            TextPaint paint = getPaint();
            String text = getText();
            int min = (int) Math.min(nexApplicationConfig.getAspectProfile().getWidth(), StaticLayout.getDesiredWidth(text, paint) + 1.0f);
            Bidi bidi = new Bidi(text, -2);
            if (!this.textAlign.equals(Layout.Alignment.ALIGN_CENTER) && !bidi.baseIsLeftToRight()) {
                if (!bidi.baseIsLeftToRight() && this.textAlign.equals(Layout.Alignment.ALIGN_NORMAL)) {
                    this.textLayout = new StaticLayout(text, 0, this.layerText.length(), paint, min, Layout.Alignment.ALIGN_OPPOSITE, 1.0f, 0.0f, true);
                } else if (!bidi.baseIsLeftToRight() && this.textAlign.equals(Layout.Alignment.ALIGN_OPPOSITE)) {
                    this.textLayout = new StaticLayout(text, 0, this.layerText.length(), paint, min, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
                }
                int shadowPadding = getShadowPadding() * 2;
                this.width = Math.max(1, this.textLayout.getWidth()) + shadowPadding;
                this.height = Math.max(1, this.textLayout.getHeight()) + shadowPadding;
                this.validDimensions = true;
            }
            this.textLayout = new StaticLayout(text, 0, this.layerText.length(), paint, min, this.textAlign, 1.0f, 0.0f, true);
            int shadowPadding2 = getShadowPadding() * 2;
            this.width = Math.max(1, this.textLayout.getWidth()) + shadowPadding2;
            this.height = Math.max(1, this.textLayout.getHeight()) + shadowPadding2;
            this.validDimensions = true;
        }
    }
}
